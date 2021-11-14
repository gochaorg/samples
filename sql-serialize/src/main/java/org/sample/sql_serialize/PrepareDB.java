package org.sample.sql_serialize;

/**
 * Подготовка СУБД
 */
public class PrepareDB {
    public final SQL sql;
    public PrepareDB(SQL sql){
        if( sql==null )throw new IllegalArgumentException( "sql==null" );
        this.sql = sql;
    }

    public static final String table1 = "t1";
    public static final String table2 = "t2";
    public static final String schema = "dbo";

    /**
     * Удаление ранее созданных таблиц
     */
    public void dropTables(){
        dropTable(table1);
        dropTable(table2);
    }

    /**
     * Удаление таблицы, если она существует
     * @param tableName имя таблицы
     */
    public void dropTable( String tableName ){
        if( tableName==null )throw new IllegalArgumentException( "tableName==null" );
        if( tableName.length()<1 )throw new IllegalArgumentException( "tableName.length()<1" );

        var t = schema+"."+tableName;
        sql.exec("if object_id('"+t+"', 'U') is not null\n" +
            "    drop table "+t);
    }

    /**
     * Создание рабочих таблиц
     */
    public void createTables(){
        createTestTable(table1);
        createTestTable(table2);
    }

    /**
     * Создание тестовой таблицы
     * @param tableName имя таблицы
     */
    public void createTestTable(String tableName){
        if( tableName==null )throw new IllegalArgumentException( "tableName==null" );
        if( tableName.length()<1 )throw new IllegalArgumentException( "tableName.length()<1" );

        var t = schema+"."+tableName;
        sql.exec(
            "create table "+t+" (\n" +
                "  id int identity(1,1),\n"+
                "  dt datetime2 default (getdate()),\n"+
                "  n int not null default (0),\n" +
                "  t nvarchar(200) null\n" +
                ")"
        );
    }


}
