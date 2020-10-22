package org.example.junit;

import java.io.IOError;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Sql {
    public static int defaultWidth = 10;
    public static final Map<String,Integer> width = new LinkedHashMap<>();
    private static String columnDelimiter = "|";

    public static String align( String str, int width ){
        if( str==null )throw new IllegalArgumentException("str==null");
        if( width<=0 )return "";

        if( str.length()==width ){
            return str;
        }else if( str.length()<width ){
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            for( int i=0; i<(width-str.length()); i++ ){
                sb.append(" ");
            }
            return sb.toString();
        }

        return str.substring(0,width);
    }

    public static void println(Appendable out, ResultSet rs) throws SQLException, IOException {
        if( out==null )throw new IllegalArgumentException("out==null");
        if( rs==null )throw new IllegalArgumentException("rs==null");

        ResultSetMetaData rsmeta = rs.getMetaData();
        int ccount = rsmeta.getColumnCount();

        List<String> colNames = new ArrayList<>();

        for( int i=1; i<=ccount; i++ ){
            String label = rsmeta.getColumnLabel(i);
            colNames.add(label==null ? "COL"+i : label);
        }

        int cidx = -1;
        for( String colName : colNames ){
            cidx++;
            int w = width.getOrDefault(colName,defaultWidth);
            if( cidx>0 && columnDelimiter!=null ){
                out.append(columnDelimiter);
            }
            out.append(align(colName,w));
        }
        out.append("\n");
        cidx = -1;
        for( String colName : colNames ){
            int w = width.getOrDefault(colName,defaultWidth);
            cidx++;
            if( cidx>0 && columnDelimiter!=null ){
                out.append(columnDelimiter);
            }
            for( int i=0; i<w; i++ )out.append("=");
        }
        out.append("\n");

        while (rs.next()){
            for( int i=1; i<=ccount; i++ ){
                Object value = rs.getObject(i);
                String colName = colNames.get(i-1);
                int w = width.getOrDefault(colName,defaultWidth);
                if( value==null ){
                    value = "null";
                }else {
                    value = value.toString();
                }

                if( i>1 && columnDelimiter!=null ){
                    out.append(columnDelimiter);
                }

                out.append(align(value.toString(),w));
            }
            out.append("\n");
        }
    }
    public static void println(Appendable out, Statement st) throws SQLException, IOException {
        if( out==null )throw new IllegalArgumentException("out==null");
        if( st==null )throw new IllegalArgumentException("st==null");

        int itr = -1;
        while (true) {
            itr++;

            ResultSet rs = st.getResultSet();
            if( rs!=null ){
                println(out,rs);
            }

            int updCnt = st.getUpdateCount();
            if( updCnt!=-1 ){
                out.append(Integer.toString(updCnt)).append(" rows affected");
            }else{
                //((stmt.getMoreResults() == false) && (stmt.getUpdateCount() == -1))
                boolean hasMore = st.getMoreResults();
                if( hasMore )continue;
                break;
            }
        }

        SQLWarning wrn = st.getWarnings();
        while ( wrn!=null ){
            out
                .append("[").append(wrn.getSQLState()).append("] ")
                .append(wrn.getMessage()).append("\n");

            wrn = wrn.getNextWarning();
        }
    }

    public static class Executor {
        public final Connection connection;
        public Executor(Connection connection) {
            if(connection==null)throw new IllegalArgumentException("connection==null");
            this.connection = connection;
        }

        public void println( String query ){
            if(query==null)throw new IllegalArgumentException("query==null");
            try {
                Statement st = connection.createStatement();
                st.execute(query);
                Sql.println(System.out,st);
                st.close();
            } catch (IOException|SQLException throwables) {
                throw new IOError(throwables);
            }
        }
    }
}
