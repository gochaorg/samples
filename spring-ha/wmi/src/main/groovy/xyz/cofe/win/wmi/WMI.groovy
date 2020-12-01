package xyz.cofe.win.wmi

import java.util.function.Consumer

interface WMI {
    void execQuery(String query, Consumer<Object> wmiObjectConsumer)
    Object getObject(String path)
}
