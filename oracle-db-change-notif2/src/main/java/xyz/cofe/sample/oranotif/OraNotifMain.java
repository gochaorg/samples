package xyz.cofe.sample.oranotif;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Пример использования Oracle Database Change Notification
 * <hr>
 * Ресурсы
 * <ul>
 *     <li>
 *         <a href="http://www.oraclebi.ru/files/presentations/imelnikov/ChangeNotification.pdf">
 *          Презентация ORACLE
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://docs.oracle.com/cd/E11882_01/java.112/e16548/dbchgnf.htm#JJDBC28815">
 *         Database JDBC Developer's Guide /
 *         Database Change Notification
 *         </a>
 *     </li>
 *     <li>
 *         <a href="http://www.dba-oracle.com/t_packages_dbms_change_notification.htm">
 *             dbms_change_notification Tips
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://docs.oracle.com/cd/B19306_01/appdev.102/b14258/d_chngnt.htm#BABEECBE">
 *             DBMS_CHANGE_NOTIFICATION
 *             </a>
 *     </li>
 *     <li>
 *         <a href="https://docs.oracle.com/cd/B19306_01/B14251_01/adfns_dcn.htm#ADFNS1020">
 *             Developing Applications with Database Change Notification
 *             , Best Practices
 *             , Troubleshooting
 *         </a>
 *     </li>
 * </ul>
 */
public class OraNotifMain {
    /** IP/Имя хоста с СУБД */
    private String oraHost;

    /** Порт СУБД (oracle listener port) */
    private int oraPort;

    /** Имя сервиса СУБД (service name / sid) */
    private String oraService;

    /** Имя пользователя ORACLE */
    private String oraUser;

    /** Пароль пользователя ORACLE */
    private String oraPassword;

    /**
     * Входная точка в приложение
     * @param commandLineArgs аргументы, см {@link #parseCommandLine(String[])}
     */
    public static void main(String[] commandLineArgs){
        if( commandLineArgs!=null && (
            commandLineArgs.length==1 &&
            commandLineArgs[0].matches("/\\?|\\-\\?|(?i)\\-\\-?help")
        ) || (commandLineArgs.length==0)
        ){
            showHelp();
            return;
        }

        OraNotifMain main = new OraNotifMain();
        main.parseCommandLine(commandLineArgs);
        main.run();
    }

    private static void showHelp(){
        URL url = OraNotifMain.class.getResource("help.md");
        if( url==null ){
            System.out.println("help not found");
            return;
        }

        try {
            try( InputStream strm = url.openStream() ){
                byte[] data = new byte[1024*64];
                int readed = strm.read(data);
                if( readed>0 ){
                    String str = new String(data,0,readed,"utf-8");
                    System.out.println(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Парсинг аргментов коммандной строки
     * @param commandLineArgs аргменты.
     *                        Передаются ввиде комбинаций ключ/значение:
     *                        <br>
     *
     *                        <ul>
     *                        <li>
     *                        <b>IP/Имя хоста с СУБД</b><br>
     *                        <code>-host</code>
     *                        <code>IP/Имя</code>
     *                        </li>
     *
     *                        <li>
     *                        <b>Порт СУБД (oracle listener port)</b><br>
     *                        <code>-port</code>
     *                        <code>Номер_порта</code>
     *                        </li>
     *
     *                        <li>
     *                        <b>Имя сервиса СУБД (service name / sid)</b><br>
     *                        <code>-service</code>
     *                        <code>service_name</code>
     *                        </li>
     *
     *                        <li>
     *                        <b>Имя пользователя ORACLE</b><br>
     *                        <code>-user</code>
     *                        <code>user_name</code>
     *                        </li>
     *
     *                        <li>
     *                        <b>Пароль пользователя ORACLE</b><br>
     *                        <code>-password</code>
     *                        <code>password</code>
     *                        </li>
     *
     *                        <li>
     *                        <b>Текст SQL-SELECT запроса для прослушивания изменений</b><br>
     *                        <code>-listenQuery</code>
     *                        <code>SQL_Запрос</code>
     *                        </li>
     *
     *                        <li>
     *                        <b>Время работы программы</b><br>
     *                        <code>-timeout</code>
     *                        <code>Время</code>
     *                        <code>Измерение_времени</code>
     *                        <br>
     *                            Измерение_времени - опционально,
     *                          возможные значения: <br>
     *                          ms, msec, msecond, mseconds, <br>
     *                          s, sec, second, seconds, <br>
     *                          m, min, minut, minutes   <br>
     *                        </li>
     *
     *                        <li>
     *                        <b>С какой переодичностью (мс) выводить эхо</b><br>
     *                        <code>-echo</code>
     *                        <code>Время</code>
     *                        <code>Измерение_времени</code>
     *                        <br>
     *                            Измерение_времени - опционально,
     *                          возможные значения: <br>
     *                          ms, msec, msecond, mseconds, <br>
     *                          s, sec, second, seconds, <br>
     *                          m, min, minut, minutes   <br>
     *                        </li>
     *
     *                        </ul>
     *
     */
    private void parseCommandLine(String[] commandLineArgs){
        List<String> args = new ArrayList<String>(Arrays.asList(commandLineArgs));

        Supplier<Long> timeUnitSuffix = ()->{
            Set<String> timeUnits = new LinkedHashSet<String>(){{
                add("ms");
                add("msec");
                add("msecond");
                add("mseconds");
                add("s");
                add("sec");
                add("second");
                add("seconds");
                add("m");
                add("min");
                add("minute");
                add("minutes");
            }};
            if( args.size()>0 && timeUnits.contains(args.get(0)) ){
                String tu = args.remove(0);
                long k = 1;
                switch( tu ){
                    case "ms":
                    case "msec":
                    case "msecond":
                    case "mseconds":
                        k = 1;
                        break;
                    case "s":
                    case "sec":
                    case "second":
                    case "seconds":
                        k = 1000;
                        break;
                    case "m":
                    case "min":
                    case "minute":
                    case "minutes":
                        k = 1000L * 60L;
                        break;
                }
                return k;
            }
            return 1L;
        };

        while( args.size()>0 ){
            String arg0 = args.remove(0);
            switch( arg0 ){
                case "-host":
                    oraHost = args.size()>0 ? args.remove(0) : null;
                    break;
                case "-port":
                    oraPort = args.size()>0 ? Integer.parseInt(args.remove(0)) : null;
                    break;
                case "-service":
                    oraService = args.size()>0 ? args.remove(0) : null;
                    break;
                case "-user":
                    oraUser = args.size()>0 ? args.remove(0) : null;
                    break;
                case "-password":
                    oraPassword = args.size()>0 ? args.remove(0) : null;
                    break;
                case "-listenQuery":
                case "-lq":
                    if( args.size()>0 ){
                        listenQueriesText.add( args.remove(0) );
                    }
                    break;
                case "-timeout":
                    if( args.size()>0 ){
                        listenTimeout = Long.parseLong(args.remove(0));
                        listenTimeout = timeUnitSuffix.get() * listenTimeout;
                    }
                    break;
                case "-closeStatementImmediatly":
                case "-cSI":
                    if( args.size()>0 ){
                        closeStatementImmediatly = Boolean.parseBoolean(args.remove(0));
                    }
                    break;
                case "-listenQueryInBG":
                    if( !args.isEmpty() ){
                        listenInBgThread = Boolean.parseBoolean(args.remove(0));
                    }
                    break;
                case "-echo":
                    if( !args.isEmpty() ){
                        sleepTimeout = Long.parseLong(args.remove(0));
                        sleepTimeout = sleepTimeout * timeUnitSuffix.get();
                    }
                    break;
                case "-useExecSrvc":
                    if( !args.isEmpty() ){
                        useExecutorService = Boolean.parseBoolean(args.remove(0));
                    }
                    break;
                default:
                    throw new IllegalArgumentException("can't parse: "+arg0);
            }
        }
    }

    /**
     * Соединение с СУБД
     */
    private volatile OracleConnection connection;

    /**
     * Установка соединения с СУБД или возвращение ранее созданного соединения
     * @return соединение с СУБД
     */
    private synchronized OracleConnection connect(){
        if( connection!=null )return connection;

        if( oraHost==null )throw new IllegalStateException("oraHost not defined");
        if( oraPort==0 )throw new IllegalStateException("oraPort not defined");
        if( oraService==null )throw new IllegalStateException("oraService not defined");
        if( oraUser==null )throw new IllegalStateException("oraUser not defined");
        if( oraPassword==null )throw new IllegalStateException("oraPassword not defined");

        String url = "jdbc:oracle:thin:@"+oraHost+":"+oraPort+"/"+oraService;

        OracleDriver oraDriver = new OracleDriver();

        Properties props = new Properties();
        props.setProperty("user", oraUser);
        props.setProperty("password", oraPassword);

        try{
            connection = (OracleConnection)oraDriver.connect(url,props);
            return connection;
        }catch( SQLException e ){
            System.out.println(e);
            throw new IOError(e);
        }
    }

    /**
     * Регистрация изменений в СУБД
     */
    private volatile DatabaseChangeRegistration dbChangeRegistration;

    /**
     * Создание или возвращение ранее созданного регистрации изменений СУБД
     * @return Регистрация изменений в СУБД
     */
    private synchronized DatabaseChangeRegistration dbChangeRegistration(){
        if( dbChangeRegistration!=null )return dbChangeRegistration;

        Properties prop = new Properties();

        prop.put(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
        prop.put(OracleConnection.DCN_QUERY_CHANGE_NOTIFICATION, "true");
        try{
            dbChangeRegistration = connect().registerDatabaseChangeNotification(prop);
            System.out.println("reg id "+dbChangeRegistration.getRegId());
            return dbChangeRegistration;
        }catch( SQLException e ){
            System.out.println(e);
            throw new IOError(e);
        }
    }

    /**
     * Завершение работы с СУБД
     */
    private synchronized void disconnect(){
        if( connection==null )return;
        for( OracleStatement st : listenQueries.keySet() ){
            try {
                if( !st.isClosed() ){
                    st.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if( dbChangeRegistration!=null ){
            dbChangeListeners.forEach( ls -> {
                try{
                    dbChangeRegistration.removeListener(ls);
                }catch( SQLException e ){
                    System.out.println(e);
                    e.printStackTrace();
                }
            });
            dbChangeListeners.clear();

            try{
                connection.unregisterDatabaseChangeNotification( dbChangeRegistration );
                dbChangeRegistration = null;
            }catch( SQLException e ){
                System.out.println(e);
                e.printStackTrace();
            }
        }
        try{
            if( !connection.isClosed() ){
                connection.close();
            }
            connection = null;
        }catch( SQLException e ){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    /**
     * Подписчики на события изменения данных в СУБД
     */
    private final Set<DatabaseChangeListener> dbChangeListeners = new HashSet<>();

    /**
     * Использовать пул потоков
     */
    private boolean useExecutorService = true;

    /**
     * Пул потоков
     */
    private Executor lsExec = Executors.newSingleThreadExecutor();

    private DatabaseChangeListener listenChanges( Consumer<DatabaseChangeEvent> eventConsumer ){
        if( eventConsumer==null )throw new IllegalArgumentException( "eventConsumer==null" );
        DatabaseChangeListener dbChangeListener = new DatabaseChangeListener() {
            @Override
            public void onDatabaseChangeNotification( DatabaseChangeEvent e) {
                if( e==null )return;
                eventConsumer.accept(e);
            }
        };
        try{
            if( useExecutorService ) {
                dbChangeRegistration().addListener(dbChangeListener, lsExec);
            }else {
                dbChangeRegistration().addListener(dbChangeListener);
            }
            dbChangeListeners.add(dbChangeListener);
            return dbChangeListener;
        }catch( SQLException e ){
            System.out.println(e);
            throw new IOError(e);
        }
    }

    /**
     * Подписчик на события изменения,
     * выводит поступившие события в stdout
     * @return подписчик
     */
    private Consumer<DatabaseChangeEvent> listenForStdout(){
        return e -> {
            StringBuilder sb = new StringBuilder();

            sb.append("DatabaseChangeEvent:").append(System.lineSeparator());
            sb.append(e.toString());
            System.out.println(sb);
        };
    }

    /**
     * Запросы которые ожижают изменения
     */
    private final WeakHashMap<OracleStatement, Long> listenQueries
        = new WeakHashMap<>();

    /**
     * Закрывать запрос сразу после выполнения
     */
    private boolean closeStatementImmediatly = true;

    /**
     * Список запросов ожидающих изменения
     * Данный список пополняется в {@link #parseCommandLine(String[])}
     */
    private List<String> listenQueriesText = new ArrayList<>();

    /**
     * Запускать запросы в фоновом потоке
     */
    private boolean listenInBgThread = false;

    /**
     * Выполнение запроса ожидания изменения
     * @param query SQL SELECT запрос
     * @see #parseCommandLine(String[])
     */
    private void listenQuery( String query ){
        if( query==null )throw new IllegalArgumentException( "query==null" );
        Runnable run = ()->{
            try{
                Statement st = connect().createStatement();
                OracleStatement ost = (OracleStatement)st;

                ost.setDatabaseChangeRegistration(dbChangeRegistration());
                listenQueries.put(ost,System.nanoTime());

                ResultSet rs = st.executeQuery(query);
                while( rs.next() ){}

                String[] tables = dbChangeRegistration().getTables();
                System.out.println("dbChangeRegistration tables");
                for( String t : tables ) System.out.println(t);

                rs.close();
                if( closeStatementImmediatly )st.close();
            }catch( SQLException e ){
                System.out.println(e);
                throw new IOError(e);
            }
        };
        if( listenInBgThread ){
            System.out.println("listen query "+query+" in background");
            Thread th = new Thread(run, "listen: "+query);

            th.setDaemon(true);
            th.start();
        }else{
            System.out.println("listen query "+query+" in foreground");
            run.run();
        }
    }

    /**
     * Время (мс) работы программы
     */
    private long listenTimeout=-1;

    /**
     * С какой переодичностью (мс) выводить эхо
     */
    private long sleepTimeout=1000;

    /**
     * Выполнение основного цикла работы программы
     */
    private void run(){
        System.out.println("try connect");
        connect();

        System.out.println("listen changes into stdout");
        listenChanges(listenForStdout());

        for( String query : listenQueriesText ){
            System.out.println("execure listen query: "+query);
            listenQuery(query);
        }

        if( listenTimeout>0 ){
            long started = System.currentTimeMillis();
            while( true ){
                long tdiff = Math.abs(started - System.currentTimeMillis());
                if( tdiff>listenTimeout ){
                    System.out.println("listen finish by timeout");
                    break;
                }
                try{
                    System.out.println("wait for DatabaseChangeEvent, wait time="+(tdiff/1000)+" seconds, max="+(listenTimeout/1000));
                    Thread.sleep(sleepTimeout);
                }catch( InterruptedException e ){
                    System.out.println("listen finish by InterruptedException");
                    break;
                }
            }
        }

        System.out.println("disconnect");
        disconnect();
    }
}
