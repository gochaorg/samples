package org.sample.sql_serialize;

import xyz.cofe.collection.ICaseStringMap;
import xyz.cofe.text.Text;

import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Упрощенная работы с SQL
 */
public class SQL {
    public final Connection connection;
    public SQL(Connection connection){
        this.connection = connection;
    }

    //region Логирование действий

    /**
     * Интерфейс логирования
     */
    public interface Print {
        void exec(String query);
        void rowsUpdated(long count);
        void resultSet(ResultSet rs);
        void warn(SQLWarning warn);
        static Print dummy(){
            return new Print() {
                @Override
                public void exec(String query) {
                }

                @Override
                public void rowsUpdated(long count) {
                }

                @Override
                public void resultSet(ResultSet rs) {
                }

                @Override
                public void warn(SQLWarning warn) {
                }
            };
        }
    }

    /**
     * Вывод в Stdio/Appendable
     */
    public static class Print2Std implements Print {
        public final Appendable out;

        public Print2Std(Appendable out){
            this.out = out;
        }

        @Override
        public void exec(String query) {
            if( query==null || query.trim().length()<1 )return;
            try {
                out.append("exec:").append(System.lineSeparator());
                out.append(Text.indent("  ",query)).append(System.lineSeparator());
            } catch (IOException e) {
                throw new IOError(e);
            }
        }

        @Override
        public void rowsUpdated(long count) {
            try {
                out.append("updated ").append(Long.toString(count)).append(" rows").append(System.lineSeparator());
            } catch (IOException e) {
                throw new IOError(e);
            }
        }

        @Override
        public void resultSet(ResultSet rs) {
            if( rs==null )return;
            try {
                new PrintResultSet(out).print(rs);
            } catch (SQLException e) {
                throw new IOError(e);
            }
        }

        @Override
        public void warn(SQLWarning warn) {
            if( warn==null )return;
            try {
                out
                    .append("[ ")
                    .append(Integer.toString(warn.getErrorCode()))
                    .append(" ")
                    .append(warn.getSQLState())
                    .append("] ")
                    .append(warn.getMessage())
                    .append(System.lineSeparator())
                ;
            } catch (IOException e) {
                throw new IOError(e);
            }
        }
    }

    private Print print = new Print2Std(System.out);

    /**
     * Настройка вывода/логирования
     * @param print интерфейс логирования
     * @return SELF ссылка
     */
    public SQL print(Print print){
        //noinspection ReplaceNullCheck
        if( print==null ){
            this.print = Print.dummy();
        }else {
            this.print = print;
        }
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class PrintBuilder {
        private Appendable out;
        public synchronized PrintBuilder out(Appendable out){
            this.out = out;
            return this;
        }

        private Supplier<String> prefix;
        public synchronized PrintBuilder prefix(Supplier<String> prefix){
            this.prefix = prefix;
            return this;
        }

        public synchronized Print build(){
            if( out==null )return Print.dummy();
            if( prefix==null ){
                return new Print2Std(out);
            }
            return new Print2Std(new PrefixAppendable(prefix, out));
        }
    }

    /**
     * Настройка вывода/логирования
     * @param builder конфигурация логирования
     * @return SELF ссылка
     */
    public SQL print(Consumer<PrintBuilder> builder){
        if( builder==null )throw new IllegalArgumentException( "builder==null" );
        PrintBuilder pb = new PrintBuilder();
        builder.accept(pb);
        return print(pb.build());
    }
    //endregion

    /**
     * Выполнение простого запроса SQL.
     * <p> Результат будет передан на экран ({@link #print(Print)}, {@link #print(Consumer)})
     * @param query запрос SQL
     */
    public void exec( String query ){
        if( query==null )throw new IllegalArgumentException( "query==null" );
        try {
            var st = connection.createStatement();
            print.exec(query);
            st.execute(query);

            printResult(st);
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }

    /**
     * Выполнение запроса SQL.
     * <p> Результат будет передан на экран ({@link #print(Print)}, {@link #print(Consumer)})
     * @param query запрос
     * @param params параметры запроса
     */
    public void exec( String query, List<?> params ){
        if( query==null )throw new IllegalArgumentException( "query==null" );
        if( params==null )throw new IllegalArgumentException( "params==null" );
        try {
            var ps = connection.prepareStatement(query);
            for( int i=0; i<params.size(); i++ ){
                ps.setObject(i+1,params.get(i));
            }

            ps.execute();
            printResult(ps);
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }

    private void printResult(Statement st) throws SQLException {
        while( true ) {
            int rowCount = st.getUpdateCount();
            if (rowCount != -1) {
                print.rowsUpdated(rowCount);
            }

            // команда DDL или 0 обновлений
            if (rowCount == 0) {
                st.getMoreResults();
                continue;
            }

            //  это счетчик обновлений
            if (rowCount > 0) {
                st.getMoreResults();
                continue;
            }

            ResultSet rs = st.getResultSet();
            if( rs==null ){
                break;
            }

            print.resultSet(rs);

            rs.close();
            st.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
        }

        var warns = st.getWarnings();
        while (warns!=null){
            print.warn(warns);
            warns = warns.getNextWarning();
        }
    }

    /**
     * Выполнение запроса к СУБД
     * @param query запрос
     * @param ctor конструктор строки - представления
     * @param rowConsumer приемник строки
     * @param <T> тип строки
     */
    public <T> void fetch( String query, Supplier<T> ctor, Consumer<T> rowConsumer ){
        if( query==null )throw new IllegalArgumentException( "query==null" );
        if( ctor==null )throw new IllegalArgumentException( "ctor==null" );
        if( rowConsumer==null )throw new IllegalArgumentException( "rowConsumer==null" );
        fetch(query,List.of(),ctor,rowConsumer);
    }

    /**
     * Выполнение запроса к СУБД
     * @param query запрос
     * @param params параметры запроса
     * @param ctor конструктор строки - представления
     * @param rowConsumer приемник строки
     * @param <T> тип строки
     */
    public <T> void fetch( String query, List<?> params, Supplier<T> ctor, Consumer<T> rowConsumer ){
        if( query==null )throw new IllegalArgumentException( "query==null" );
        if( params==null )throw new IllegalArgumentException( "params==null" );
        if( ctor==null )throw new IllegalArgumentException( "ctor==null" );
        if( rowConsumer==null )throw new IllegalArgumentException( "rowConsumer==null" );

        try {
            var ps = connection.prepareStatement(query);
            for( int i=0; i<params.size(); i++ ){
                ps.setObject(i+1,params.get(i));
            }
            fetch( ps.executeQuery(), ctor, rowConsumer );
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }


    /**
     * Отображение выборки на java объекты
     * @param rs выборка
     * @param ctor конструктор строки - представления
     * @param rowConsumer приемник строки
     * @param <T> тип строки
     */
    public <T> void fetch( ResultSet rs, Supplier<T> ctor, Consumer<T> rowConsumer ){
        if( rs==null )throw new IllegalArgumentException( "rs==null" );
        if( ctor==null )throw new IllegalArgumentException( "ctor==null" );
        if( rowConsumer==null )throw new IllegalArgumentException( "rowConsumer==null" );

        try {
            var meta = rs.getMetaData();
            //int ccnt = meta.getColumnCount();
            while (rs.next()){
                var row = ctor.get();
                if( row==null )throw new IllegalStateException("!!");

                var clzz = row.getClass();
                var flds = clzz.getFields();
                for( var fld : flds ){
                    if( (fld.getModifiers() & Modifier.STATIC)>0 )continue;

                    var value = rs.getObject(fld.getName());
                    if( value==null )continue;

                    var t_to = fld.getType();
                    var t_from = value.getClass();
                    if( t_to.isAssignableFrom(t_from) ){
                        try {
                            fld.set(row, value);
                        } catch (IllegalAccessException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }

                rowConsumer.accept(row);
            }
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }

    /**
     * Отображение выборки на {@link java.util.Map}
     * @param rs выборка
     * @param rowConsumer приемник строки
     */
    public void fetch( ResultSet rs, Consumer<? super Map<String,Object>> rowConsumer ){
        if( rs==null )throw new IllegalArgumentException( "rs==null" );
        if( rowConsumer==null )throw new IllegalArgumentException( "rowConsumer==null" );
        try {
            var meta = rs.getMetaData();
            while (rs.next()){
                ICaseStringMap<Object> map = new ICaseStringMap<>(true);
                int cc = meta.getColumnCount();
                for( int i=0;i<cc;i++ ){
                    String label = meta.getColumnLabel(i+1);
                    Object value = rs.getObject(i+1);
                    map.put(label, value);
                }
                rowConsumer.accept(map);
            }
        } catch (SQLException e){
            throw new IOError(e);
        }
    }

    /**
     * Выполнение запроса (SELECT like) к СУБД
     * @param query запрос
     * @param params параметры запроса
     * @return Результат запроса
     */
    public List<Map<String,Object>> rows( String query, List<?> params ){
        try {
            var ps = connection.prepareStatement(query);
            for( int i=0; i<params.size(); i++ ){
                ps.setObject(i+1,params.get(i));
            }

            List<Map<String,Object>> list = new ArrayList<>();
            fetch(ps.executeQuery(), list::add);
            return list;
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }
}
