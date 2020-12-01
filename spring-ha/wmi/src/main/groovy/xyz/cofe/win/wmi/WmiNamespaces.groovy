package xyz.cofe.win.wmi

import xyz.cofe.win.WinAPI

import java.util.function.Consumer

class WmiNamespaces {
    //region Factory
    static abstract class Factory<API> {
        abstract String getName()
        abstract <API> API createApi(WMI wmi)
        void connect(WinAPI winapi, Consumer<API> client){
            if( winapi==null )throw new IllegalArgumentException("winapi==null");
            if( client==null )throw new IllegalArgumentException("client==null");
            winapi.wmi('.', getName() ) {w ->
                client.accept( createApi(w) )
            }
        }
        void connect(WinAPI winapi, String host, Consumer<API> client){
            if( winapi==null )throw new IllegalArgumentException("winapi==null");
            if( host==null )throw new IllegalArgumentException("host==null");
            if( client==null )throw new IllegalArgumentException("client==null");
            winapi.wmi(host, getName() ) {w ->
                client.accept( createApi(w) )
            }
        }
        void connect(WinAPI winapi, String host, String user, String password, Consumer<API> client){
            if( winapi==null )throw new IllegalArgumentException("winapi==null");
            if( host==null )throw new IllegalArgumentException("host==null");
            if( client==null )throw new IllegalArgumentException("client==null");
            if( user==null )throw new IllegalArgumentException("user==null");
            if( password==null )throw new IllegalArgumentException("password==null");
            winapi.wmi(host, getName(), user, password ) {w ->
                client.accept( createApi(w) )
            }
        }
        void connect(WinAPI winapi, String host, String user, String password, String locale, Consumer<API> client){
            if( winapi==null )throw new IllegalArgumentException("winapi==null");
            if( host==null )throw new IllegalArgumentException("host==null");
            if( client==null )throw new IllegalArgumentException("client==null");
            if( user==null )throw new IllegalArgumentException("user==null");
            if( password==null )throw new IllegalArgumentException("password==null");
            if( locale==null )throw new IllegalArgumentException("locale==null");
            winapi.wmi(host, getName(), user, password, locale ) {w ->
                client.accept( createApi(w) )
            }
        }
        void connect(WinAPI winapi, String host, String user, String password, String locale, String authority, Consumer<API> client){
            if( winapi==null )throw new IllegalArgumentException("winapi==null");
            if( host==null )throw new IllegalArgumentException("host==null");
            if( client==null )throw new IllegalArgumentException("client==null");
            if( user==null )throw new IllegalArgumentException("user==null");
            if( password==null )throw new IllegalArgumentException("password==null");
            if( locale==null )throw new IllegalArgumentException("locale==null");
            if( authority==null )throw new IllegalArgumentException("authority==null");
            winapi.wmi(host, getName(), user, password, locale, authority ) {w ->
                client.accept( createApi(w) )
            }
        }
    }
    static abstract class Namespace implements WMI {
        @SuppressWarnings('GrFinalVariableAccess')
        final WMI wmiInterface;

        private Namespace(){}

        Namespace(WMI wmi){
            if( wmi==null )throw new IllegalArgumentException("wmi==null");
            this.wmiInterface = wmi;
        }

        @Override
        void execQuery(String query, Consumer<Object> wmiObjectConsumer) {
            wmiInterface.execQuery(query,wmiObjectConsumer)
        }

        @Override
        Object getObject(String path) {
            wmiInterface.getObject(path)
        }
    }
    //endregion

    //region CIMV2
    public static class CIMV2Factory extends Factory<CIMV2WMI> {
        @Override String getName() { return 'root\\CIMV2' }
        @Override def <API> API createApi(WMI wmi) {
            return new CIMV2WMI(wmi) as API
        }
    }

    public static final CIMV2Factory CIMV2 = new CIMV2Factory()
    //endregion

    //region StandardCimv2
    public static class StandardCimv2Factory extends Factory<StandardCimv2WMI> {
        @Override String getName() { return 'ROOT\\StandardCimv2' }
        @Override def <API> API createApi(WMI wmi) {
            return new StandardCimv2WMI(wmi) as API
        }
    }

    public static final StandardCimv2Factory StandardCimv2 = new StandardCimv2Factory()
    //endregion
}
