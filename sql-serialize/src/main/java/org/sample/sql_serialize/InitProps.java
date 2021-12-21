package org.sample.sql_serialize;

import xyz.cofe.io.fn.IOFun;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class InitProps {
    private static final Properties props;
    static {
        var propsUrl = InitProps.class.getResource("/init.properties");
        props = new Properties();
        if( propsUrl!=null ){
            try {
                props.load(new StringReader(IOFun.readText(propsUrl,"utf-8")));
            } catch (IOException e) {
                System.err.println("can't read resorce "+propsUrl);
                System.err.println(e);
            }
        }
    }

    private static String getSProp( String name, String defaultValue ){
        var envval = System.getenv("SQL_SER_"+name);
        if( envval!=null )return envval;

        var propval = props.get(name);
        if( propval!=null )return propval.toString();

        return defaultValue;
    }

    public static String dockerContainerId(){ return getSProp("docker.container", "mssql-ser-1"); }
    public static String saPasssword(){ return getSProp("mssql.sa.password", "mssql-ser-1"); }
    public static String mssqlHost(){ return getSProp("mssql.host", "mssql-ser-1"); }
    public static String mssqlDb(){ return getSProp("mssql.db", "mssql-ser-1"); }
    public static String usrName(){ return getSProp("mssql.usr.name", "mssql-ser-1"); }
    public static String usrPswd(){ return getSProp("mssql.usr.pswd", "mssql-ser-1"); }
}
