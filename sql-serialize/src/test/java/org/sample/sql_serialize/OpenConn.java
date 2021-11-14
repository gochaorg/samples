package org.sample.sql_serialize;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OpenConn {
    public static Connection open_as_sa() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlserver://localhost:1433","sa", "qA%124zA_e!");
    }
    public static Connection open_as_usr1() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlserver://localhost:1433","usr1", "usr1");
    }
}
