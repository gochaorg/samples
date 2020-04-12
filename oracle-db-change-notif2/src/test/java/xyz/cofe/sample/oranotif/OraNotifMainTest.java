package xyz.cofe.sample.oranotif;

import org.junit.jupiter.api.Test;

public class OraNotifMainTest {
    @Test
    public void run1(){
        OraNotifMain.main(new String[]{
            "-user", "TESTNOTIFY", "-password", "TESTNOTIFY",
            "-host", "localhost", "-port", "1521", "-service", "ORCLCDB.localdomain",
            "-lq", "SELECT * FROM TABLE1",
            "-timeout", "60", "sec",
            "-cSI", "true"
        });
    }
}
