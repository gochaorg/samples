package org.sample.sql_serialize;

import java.io.IOError;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import xyz.cofe.fn.Fn2;
import xyz.cofe.fn.Fn3;
import xyz.cofe.fn.Tuple2;
import xyz.cofe.text.table.CellFormat;
import xyz.cofe.text.table.TableFormat;
import xyz.cofe.text.table.TableFormats;

/**
 * Вывод выборки из SQL
 */
public class PrintResultSet {
    /**
     * Куда будем выводить содержание
     */
    public final Appendable out;

    /**
     * Формат таблицы
     */
    public static final TableFormat tableFormat = TableFormats.asciiTable();
    static {
        tableFormat.getCellFormatMap().put("request_status", true, new CellFormat().width(8));
        tableFormat.getCellFormatMap().put("resource_database_id", true, new CellFormat().width(8));
        tableFormat.getCellFormatMap().put("resource_description", true, new CellFormat().width(8));
        tableFormat.getCellFormatMap().put("resource_type", true, new CellFormat().width(8));
        tableFormat.getCellFormatMap().put("resource_subtype", true, new CellFormat().width(8));
        tableFormat.getCellFormatMap().put("request_reference_count", true, new CellFormat().width(8));
        tableFormat.getCellFormatMap().put("request_lifetime", true, new CellFormat().width(8));
        tableFormat.getCellFormatMap().put("request_session_id", true, new CellFormat().width(8));
        tableFormat.getCellFormatMap().put("resource_associated_entity_id", true, new CellFormat().width(10));
        tableFormat.getCellFormatMap().put("resource_lock_partition", true, new CellFormat().width(10));
        tableFormat.getCellFormatMap().put("request_mode", true, new CellFormat().width(8));
        tableFormat.getCellFormatMap().put("request_type", true, new CellFormat().width(7));
        tableFormat.getCellFormatMap().put("dt", true, new CellFormat().width(30));
    }
    private boolean trimValues = true;

    /**
     * Конструктор
     * @param out Куда будем выводить содержание
     */
    public PrintResultSet(Appendable out){
        if( out==null )throw new IllegalArgumentException( "out==null" );
        this.out = out;
    }

    /**
     * Вывод выборки на экран
     * @param rs выборка
     * @throws SQLException ошибка работы с SQL
     */
    public void print(ResultSet rs) throws SQLException {
        if( rs==null )throw new IllegalArgumentException( "rs==null" );
        var meta = rs.getMetaData();
        int cols_co = meta.getColumnCount();

        List<String> labels = new ArrayList<>();
        for( int i=0; i<cols_co; i++ ){
            int col_no = i+1;

            String label = meta.getColumnLabel(col_no);
            labels.add(label);
        }

        String[] labels_s_arr = labels.toArray(new String[0]);
        tableFormat.formatHeader(labels_s_arr, labels_s_arr).forEach( line -> {
            try {
                out.append(line);
                out.append(System.lineSeparator());
            } catch (IOException e) {
                throw new IOError(e);
            }
        });

        List<String> data = new ArrayList<>();
        List<String> cols = new ArrayList<>();

        Fn3<Integer,
            Tuple2<String[],String[]>,
            Tuple2<String[],String[]>,
            Tuple2<String[],String[]>
        > printer = (call_i, prev, cur) -> {
            Fn2<String[], String[], List<String>> fmt = tableFormat::formatFirstRow;

            if( prev==null && cur!=null ){ // first
            } else if( prev!=null && cur!=null ) { // middle
                Tuple2<String[],String[]> t = prev;
                if( call_i>1 ){
                    tableFormat.formatMiddleRow(t.a(), t.b()).forEach( line -> {
                        try {
                            out.append(line);
                            out.append(System.lineSeparator());
                        } catch (IOException e) {
                            throw new IOError(e);
                        }
                    });
                }else {
                    tableFormat.formatFirstRow(t.a(), t.b()).forEach( line -> {
                        try {
                            out.append(line);
                            out.append(System.lineSeparator());
                        } catch (IOException e) {
                            throw new IOError(e);
                        }
                    });
                }
            } else //noinspection ConstantConditions
                if( prev!=null && cur==null ) { // last
                Tuple2<String[],String[]> t = prev;
                tableFormat.formatLastRow(t.a(), t.b()).forEach( line -> {
                    try {
                        out.append(line);
                        out.append(System.lineSeparator());
                    } catch (IOException e) {
                        throw new IOError(e);
                    }
                });
            }

            return cur;
        };

        Tuple2<String[],String[]> s = null;

        int call_i = -1;
        while (rs.next()){
            call_i++;
            data.clear();
            cols.clear();

            for( int i=0; i<cols_co; i++ ){
                int col_no = i+1;
                String label = labels.get(i);
                Object value = rs.getObject(col_no);

                String str = value!=null ? value.toString() : "";
                if( trimValues )str = str.trim();
                data.add(str);
                cols.add(label);
            }

            var now = Tuple2.of( data.toArray(new String[0]), cols.toArray(new String[0]) );
            s = printer.apply(call_i,s,now);
        }

        call_i++;
        s = printer.apply(call_i, s,null);
    }
}
