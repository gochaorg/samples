package xyz.cofe.win.wmi

import java.util.function.Consumer

import static org.codehaus.groovy.scriptom.tlb.wbemscripting.WbemFlagEnum.wbemFlagForwardOnly

class WMIImpl implements WMI {
    def services

    @Override
    void execQuery(String query, Consumer<Object> wmiObjectConsumer) {
        if( query==null )throw new IllegalArgumentException("query==null")
        if( wmiObjectConsumer==null )throw new IllegalArgumentException("wmiObjectConsumer==null");

        def srvc = services
        if( srvc==null ) throw new IllegalStateException("services property is null")

        for(wmiObj in services.ExecQuery(query, 'WQL', wbemFlagForwardOnly)){
            wmiObjectConsumer.accept(wmiObj)
        }
    }

    @Override
    Object getObject(String path) {
        if( path==null )throw new IllegalArgumentException("path==null");

        def srvc = services
        if( srvc==null ) throw new IllegalStateException("services property is null")

        return srvc.Get(path)
    }
}
