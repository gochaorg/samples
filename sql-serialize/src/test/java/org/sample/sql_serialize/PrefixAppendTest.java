package org.sample.sql_serialize;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

public class PrefixAppendTest {
    @Test
    public void test01(){
        StringWriter sw = new StringWriter();
        PrefixAppendable pref = new PrefixAppendable(()->"[pref] ", sw);
        try {
            pref.append("hello\n");
            pref.append("world").append("\n");
            pref.append("gogo").append("\n\r");
            pref.append("dady").append(" now").append("\r\n");
            pref.append("bla");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(sw.toString());
    }
}
