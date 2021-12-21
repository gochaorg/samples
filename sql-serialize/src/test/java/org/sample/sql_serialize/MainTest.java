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
    /**
     * Запуск основного цикла тестирования
     * @param isolation режим изолирования
     * @param conn1 соединение 1
     * @param conn2 соединение 2
     */
    public void test(int isolation, String table, Connection conn1, Connection conn2) throws SQLException {
        if( conn1==null )throw new IllegalArgumentException( "conn1==null" );
        if( conn2==null )throw new IllegalArgumentException( "conn2==null" );
        if( table==null )throw new IllegalArgumentException( "table==null" );

        // Время начала теста
        long start_time = System.currentTimeMillis();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Подготавливаем первый поток
        // отключаем всякие auto commit
        conn1.setAutoCommit(false);
        conn1.setTransactionIsolation(isolation);

        Inserter insertThread = new Inserter();

        insertThread.start_time = start_time;
        insertThread.setName("inserter");
        insertThread.conn = conn1;
        insertThread.table = table;
        insertThread.delay_select = 300;
        insertThread.delay_insert = 300;
        insertThread.delay_commit = 1400;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Подготавливаем второй поток
        conn2.setAutoCommit(false);
        conn2.setTransactionIsolation(isolation);

        ParallelActions parallelThread = new ParallelActions();

        parallelThread.setName("parallel");
        parallelThread.setDaemon(true);
        parallelThread.start_time = start_time;
        parallelThread.conn = conn2;
        parallelThread.start();

        insertThread.onSelected = ev -> {
            parallelThread.push( ()->System.out.println("before insert parallel") );
            parallelThread.push( parallelThread.dumpLocks() );
            parallelThread.push( parallelThread.insertRow( table,-1,"a", true) );

            parallelThread.push( ()->System.out.println("after insert parallel") );
            parallelThread.push( parallelThread.dumpLocks() );
        };

        insertThread.onInserted = ev -> {
            parallelThread.push( ()->System.out.println("after insert") );
            parallelThread.push( parallelThread.dumpLocks() );
        };

        insertThread.onCommitted = ev -> {
            parallelThread.push( ()->System.out.println("after commit") );
            parallelThread.push( parallelThread.dumpLocks() );
        };

        ////////////////////////
        // Запускаем и ждем
        insertThread.start();
        try {
            insertThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        parallelThread.interrupt();
        try {
            parallelThread.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            parallelThread.stop();
        }
    }

    @Test
    public void test01() throws SQLException {
        try( var conn1 = OpenConn.open_as_usr1();
             var conn2  = OpenConn.open_as_usr1();
        ){
            int isolation = Connection.TRANSACTION_READ_COMMITTED;
            // int isolation = Connection.TRANSACTION_SERIALIZABLE;

            System.out.println("connected");

            var prepare = new PrepareDB(new SQL(conn1));
            prepare.dropTables();
            prepare.createTables();

            test(isolation, "t1", conn1, conn2);

            ////////////////////////////////////////////////
            System.out.println("=".repeat(80));
            isolation = Connection.TRANSACTION_SERIALIZABLE;
            test(isolation, "t2", conn1, conn2);

            ///////////////////////////
            System.out.println("");
            System.out.println("*".repeat(80));
            System.out.println("");
            new SQL(conn1).exec("select * from t1 order by id");

            System.out.println("");
            System.out.println("-".repeat(80));
            System.out.println("");
            new SQL(conn1).exec("select * from t2 order by id");
        }
    }

    //region всякие доп функции
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
        public default Runnable insertRow( String table, int n, String tag, boolean forceCommit ){
            if( table==null )throw new IllegalArgumentException( "table==null" );
            return ()->{
                var sql = new SQL(getConnection());
                sql.print( b -> b.out(getOutput()) );

                sql.exec("insert into "+table+" (n,t) values (?,?)", List.of(n,tag) );
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

    /**
     * Поток, который вычитывает задания из очереди и выполняет их.
     *
     * <p> Задания в очереди могут быть выполнены после определенного промежутка времени.
     */
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

        /**
         * Добавляет задание в очередь
         * @param r задание
         */
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
    //endregion

    //region notification events
    public static class InsEvent {
        public int iteration;
    }
    public static class InsEvent_Select extends InsEvent {
        public int rowsCount;
    }
    //endregion

    @SuppressWarnings("BusyWait")
    public static class Inserter extends ParallelActions {
        public Connection conn;
        public String tag = "a";
        public String table = "t1";

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
                log("auto commit "+(conn.getAutoCommit() ? "on" : "off"));

                //region log transact isolation
                int tr_isolation = conn.getTransactionIsolation();
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

                log("transact isolation "+isol_name);
                //endregion

                if( cnt>0 ){
                    for( int i=0; i<cnt; i++ ){
                        //region select rows count
                        log("select rows count");

                        //region delay before select
                        if( delay_select>0 ){
                            log("delay select "+delay_select);
                            Thread.sleep(delay_select);
                        }
                        //endregion
                        //region read rows count: select count(*) from table ...
                        var cnt_rows_0 = sql.rows(
                            "select count(*) as cnt from "+table+" where t = ?",List.of(tag)
                        );

                        var cnt_rows_1 = cnt_rows_0.size()>0 ? cnt_rows_0.get(0).get("cnt") : null;
                        var cnt_rows = cnt_rows_1 instanceof Number ? ((Number)cnt_rows_1).intValue() : -1;

                        log("selected rows count = "+cnt_rows);
                        //endregion
                        //region notify onSelected(event)
                        var onSel = onSelected;
                        if( onSel!=null ){
                            InsEvent_Select detail = new InsEvent_Select();
                            detail.iteration = i;
                            detail.rowsCount = cnt_rows;
                            onSel.accept(detail);
                        }
                        //endregion
                        //endregion
                        //region insert rows count
                        log("insert");
                        //region delay before insert
                        if( delay_insert>0 ){
                            log("delay insert "+delay_insert);
                            Thread.sleep(delay_insert);
                        }
                        //endregion

                        sql.exec("insert into "+table+" (n,t) values (?,?)", List.of(cnt_rows,tag) );
                        log("inserted");

                        //region notify onInserted
                        var onIns = onInserted;
                        if( onIns!=null ){
                            InsEvent ev = new InsEvent();
                            ev.iteration = i;
                            onIns.accept(ev);
                        }
                        //endregion
                        //endregion
                        //region commit changes
                        if( !conn.getAutoCommit() ) {
                            log("commit");
                            //region delay before commit
                            if (delay_commit > 0) {
                                log("commit delay " + delay_commit);
                                Thread.sleep(delay_commit);
                            }
                            //endregion

                            conn.commit();
                            log("committed");

                            //region notify onCommitted(event)
                            var onCmt = onCommitted;
                            if (onCmt != null) {
                                InsEvent ev = new InsEvent();
                                ev.iteration = i;
                                onCmt.accept(ev);
                            }
                            //endregion
                        }
                        //endregion
                    }
                }
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
