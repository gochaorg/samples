package org.example.junit;

import org.junit.jupiter.api.*;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

public class DbTest {
    private static final Properties connectRequisites;
    static {
        connectRequisites = new Properties();
        URL u = DbTest.class.getResource("/connect.properties");
        if( u==null )throw new Error("resource /connect.properties not found");
        try {
            try(InputStream strm = u.openStream()){
                connectRequisites.load(strm);
            }
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    private static Connection connect(){
        try {
            String url = connectRequisites.getProperty("url");
            String username = connectRequisites.getProperty("username");
            String pswd = connectRequisites.getProperty("password");
            System.out.println("sql connect to\n  url="+url+"\n  username="+username);

            return DriverManager.getConnection(
                url,
                username,
                pswd
            );
        } catch (SQLException e) {
            throw new IOError(e);
        }
    }
    private static Connection connection;
    private static Sql.Executor sql;

    @BeforeAll
    public synchronized static void openConnection(){
        if( connection==null ) {
            connection = connect();
            sql = new Sql.Executor(connection);
        }
    }

    @AfterAll
    public synchronized static void closeConnection(){
        if( connection!=null ){
            try {
                System.out.println("close sql connection");
                connection.close();
                connection = null;
                sql = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @BeforeEach
    public void setupColumnsWidth(){
        Sql.defaultWidth = 15;
    }

    @Tag(T.MSSQL)
    @Test
    public void selectAvailableTables(){
        sql.println("select name, type_desc from sys.tables");
    }
}
