package org.sample.sql_serialize;

import org.junit.jupiter.api.Test;
import xyz.cofe.fn.Consumer1;
import xyz.cofe.text.Text;

import java.io.IOError;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class MainTest {
    @Test
    public void test01() throws SQLException {
        long start_time = System.currentTimeMillis();

        try( var conn1 = OpenConn.open_as_usr1();
             var conn2  = OpenConn.open_as_usr1();
        ){
            int isolation = Connection.TRANSACTION_READ_COMMITTED;
            // int isolation = Connection.TRANSACTION_SERIALIZABLE;

            System.out.println("connected");

            var sql = new SQL(conn1);

            var prepare = new PrepareDB(sql);
            prepare.dropTables();
            prepare.createTables();
            /////////////////////////

            conn1.setAutoCommit(false);

            List<Thread> threads = new ArrayList<>();

            Inserter insTh = new Inserter();
            threads.add(insTh);

            insTh.start_time = start_time;
            insTh.conn = conn1;
            insTh.tr_isolation = isolation;
            insTh.delay_select = 0;
            insTh.delay_insert = 200;
            insTh.delay_commit = 1800;
            insTh.setName("inserter");

            /////////////////////
            conn2.setAutoCommit(false);
            conn2.setTransactionIsolation(isolation);

            ParallelActions parallelActions = new ParallelActions();
            parallelActions.setName("parallel");
            parallelActions.conn = conn2;
            parallelActions.start_time = start_time;
            parallelActions.setDaemon(true);
            parallelActions.start();

            insTh.onSelected = ev -> {
                parallelActions.push( parallelActions.dumpLocks() );
                parallelActions.push( parallelActions.insertRow(-1,"a", true) );
            };

            threads.forEach(Thread::start);
            threads.forEach(t -> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    t.stop();
                }
            });

            parallelActions.interrupt();
            try {
                parallelActions.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                parallelActions.stop();
            }
        }
    }

    public interface GetConnection {
        public Connection getConnection();
    }
    public interface GetPrefix {
        public Supplier<String> getPrefix();
    }
    public interface GetOutput {
        public Appendable getOutput();
    }

    private static final AtomicInteger dumpId = new AtomicInteger(0);
    public interface DumpLocks extends GetConnection, GetOutput, GetPrefix {
        default Runnable dumpLocks(){
            return ()->{
                var sql = new SQL(getConnection());
                sql.print( b -> b.out(getOutput()) );

                StringWriter sw = new StringWriter();
                //sql.print(new SQL.Print2Std(sw));
                sql.exec("select * from sys.dm_tran_locks");

                //String str = Text.indent("[locks#"+dumpId.incrementAndGet()+"] ",sw.toString());
                //System.out.println(str);
            };
        }
    }
    public interface InsertRow extends GetConnection, GetOutput, GetPrefix {
        public default Runnable insertRow( int n, String tag, boolean forceCommit ){
            return ()->{
                var sql = new SQL(getConnection());
                sql.print( b -> b.out(getOutput()) );

                sql.exec("insert into t1 (n,t) values (?,?)", List.of(n,tag) );
                if( forceCommit ){
                    var conn = getConnection();
                    try {
                        conn.commit();
                    } catch (SQLException e) {
                        throw new IOError(e);
                    }
                }
            };
        }
    }

    public static class ConditionalRunnable implements Runnable {
        public final Runnable runnable;
        public final Supplier<Boolean> ready;

        public ConditionalRunnable(Runnable runnable, Supplier<Boolean> ready) {
            if( ready==null )throw new IllegalArgumentException( "ready==null" );
            if( runnable==null )throw new IllegalArgumentException( "runnable==null" );

            this.runnable = runnable;
            this.ready = ready;
        }

        @Override
        public void run() {
            if(ready.get()) {
                runnable.run();
            }
        }
    }
    public static class ParallelActions extends Thread implements GetOutput, GetPrefix, DumpLocks, InsertRow {
        public Connection conn;
        public long start_time;
        private volatile Appendable output;

        @Override
        public Appendable getOutput() {
            if( output!=null )return output;
            synchronized (this) {
                output = new PrefixAppendable(getPrefix(), System.out);
                return output;
            }
        }

        @Override
        public Supplier<String> getPrefix() {
            return ()->{
                return "["+(System.currentTimeMillis() - start_time)+" "+getName()+":"+getId()+"] ";
            };
        }

        private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

        @Override
        public Connection getConnection() { return conn; }

        @Override
        public void run() {
            while ( !this.isInterrupted() ){
                Runnable r = tasks.poll();
                if( r==null ){
                    synchronized (tasks){
                        try {
                            tasks.wait(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }else {
                    if( r instanceof ConditionalRunnable ){
                        if( ((ConditionalRunnable) r).ready.get() ){
                            r.run();
                        } else {
                            tasks.add(r);
                        }
                    }else {
                        r.run();
                    }
                }
            }
        }
        public void push(Runnable r){
            synchronized (tasks){
                tasks.add(r);
                tasks.notifyAll();
            }
        }
        public void push(long runAt, Runnable r){
            long after = start_time + runAt;
            //noinspection ConstantConditions
            push(new ConditionalRunnable(r, ()->System.currentTimeMillis()>=after ));
        }
    }

    public static class InsEvent {
        public int iteration;
        public long durationNano;
    }
    public static class InsEvent_Select extends InsEvent {
        public int rowsCount;
    }

    public static class Inserter extends ParallelActions {
        public Connection conn;
        public int tr_isolation;
        public String tag = "a";

        public int cnt = 10;
        public long delay_insert = 0;
        public long delay_select = 0;
        public long delay_commit = 2000;

        public Consumer1<InsEvent_Select> onSelected;
        public Consumer1<InsEvent> onInserted;
        public Consumer1<InsEvent> onCommitted;

        private void log(String message) {
            try {
                getOutput().append(message).append(System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            var sql = new SQL(conn);
            sql.print( b -> b.out(getOutput()) );

            try {
                log("auto commit off");
                conn.setAutoCommit(false);

                String isol_name = tr_isolation==Connection.TRANSACTION_NONE
                    ? "None"
                    : tr_isolation==Connection.TRANSACTION_READ_UNCOMMITTED
                    ? "read uncommitted"
                    : tr_isolation==Connection.TRANSACTION_READ_COMMITTED
                    ? "read committed"
                    : tr_isolation==Connection.TRANSACTION_REPEATABLE_READ
                    ? "repeatable"
                    : tr_isolation==Connection.TRANSACTION_SERIALIZABLE
                    ? "serializable"
                    : "isolation "+tr_isolation;

                log("set transact isolation "+isol_name);
                conn.setTransactionIsolation(tr_isolation);

                if( cnt>0 ){
                    for( int i=0; i<cnt; i++ ){
                        log("select rows count");

                        if( delay_select>0 ){
                            log("delay select "+delay_select);
                            Thread.sleep(delay_select);
                        }

                        long t0_sel = System.nanoTime();
                        var cnt_rows_0 = sql.rows("select count(*) as cnt from t1 where t = ?",List.of(tag));
                        long t1_sel = System.nanoTime();

                        var cnt_rows_1 = cnt_rows_0.size()>0 ? cnt_rows_0.get(0).get("cnt") : null;
                        var cnt_rows = cnt_rows_1 instanceof Number ? ((Number)cnt_rows_1).intValue() : -1;

                        log("selected rows count = "+cnt_rows+", t.ms="+(t1_sel-t0_sel)/1_000_000.0);

                        var onSel = onSelected;
                        if( onSel!=null ){
                            InsEvent_Select detail = new InsEvent_Select();
                            detail.iteration = i;
                            detail.rowsCount = cnt_rows;
                            detail.durationNano = t1_sel - t0_sel;
                            onSel.accept(detail);
                        }

                        log("insert");

                        if( delay_insert>0 ){
                            log("delay insert "+delay_insert);
                            Thread.sleep(delay_insert);
                        }

                        long t0_ins = System.nanoTime();
                        sql.exec("insert into t1 (n,t) values (?,?)", List.of(cnt_rows,tag) );
                        long t1_ins = System.nanoTime();
                        log("inserted, t.ms="+(t1_ins-t0_ins)/1_000_000.0);

                        var onIns = onInserted;
                        if( onIns!=null ){
                            InsEvent ev = new InsEvent();
                            ev.durationNano = t1_ins - t0_ins;
                            ev.iteration = i;
                            onIns.accept(ev);
                        }

                        log("commit");
                        if( delay_commit>0 ){
                            log("commit delay "+delay_commit);
                            Thread.sleep(delay_commit);
                        }

                        long t0_cmt = System.nanoTime();
                        conn.commit();
                        long t1_cmt = System.nanoTime();

                        log("committed, t.ms="+(t1_cmt-t0_cmt)/1_000_000.0);

                        var onCmt = onCommitted;
                        if( onCmt!=null ){
                            InsEvent ev = new InsEvent();
                            ev.durationNano = t1_cmt - t0_cmt;
                            ev.iteration = i;
                            onCmt.accept(ev);
                        }
                    }
                }
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
