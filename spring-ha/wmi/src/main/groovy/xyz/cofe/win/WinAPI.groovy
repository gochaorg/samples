package xyz.cofe.win

import org.codehaus.groovy.scriptom.ActiveXObject
import org.codehaus.groovy.scriptom.Scriptom
import xyz.cofe.jacob.JacobDll
import xyz.cofe.win.wmi.WMI
import xyz.cofe.win.wmi.WMIImpl

import java.util.function.Consumer

/**
 * Windows api
 */
class WinAPI {
    static {
        // Инициализация jacob dll
        JacobDll.init()
    }

    private static WinAPI instance = new WinAPI()

    /**
     * Выполнение кода в контексте wmi
     * @param client код
     */
    static void run( Consumer<WinAPI> client ){
        if( client==null )throw new IllegalArgumentException("client == null")
        Scriptom.inApartment {
            client.accept(instance)
        }
    }

    /**
     * Работа с wmi api
     * @param client клиентский код
     */
    @SuppressWarnings('GrMethodMayBeStatic')
    void wmi( Consumer<WMI> client ){
        if( client==null )throw new IllegalArgumentException("client==null");
        wmi0('.', null, null, null, null, null, client )
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    void wmi( String host, Consumer<WMI> client ){
        if( client==null )throw new IllegalArgumentException("client==null");
        if( host==null )throw new IllegalArgumentException("host==null");
        wmi0(host, null, null, null, null, null, client )
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    void wmi( String host, String namespace, Consumer<WMI> client ){
        if( client==null )throw new IllegalArgumentException("client==null");
        if( host==null )throw new IllegalArgumentException("host==null");
        if( namespace==null )throw new IllegalArgumentException("namespace==null");
        wmi0(host, namespace, null, null, null, null, client )
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    void wmi( String host, String namespace, String user, String password, Consumer<WMI> client ){
        if( client==null )throw new IllegalArgumentException("client==null");
        if( host==null )throw new IllegalArgumentException("host==null");
        if( namespace==null )throw new IllegalArgumentException("namespace==null");
        if( user==null )throw new IllegalArgumentException("user==null");
        if( password==null )throw new IllegalArgumentException("password==null");
        wmi0(host, namespace, user, password, null, null, client )
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    void wmi( String host, String namespace, String user, String password, String locale, Consumer<WMI> client ){
        if( client==null )throw new IllegalArgumentException("client==null");
        if( host==null )throw new IllegalArgumentException("host==null");
        if( namespace==null )throw new IllegalArgumentException("namespace==null");
        if( password==null )throw new IllegalArgumentException("password==null");
        if( user==null )throw new IllegalArgumentException("user==null");
        if( locale==null )throw new IllegalArgumentException("locale==null");
        wmi0(host, namespace, user, password, locale, null, client )
    }

    /**
     * Работа с wmi api
     * @param host хост (ip адрес или '.') <br>
     *     Computer name to which you are connecting.
     *     If the remote computer is in a different domain than the user account under which you log in,
     *     then use the fully qualified computer name. I
     *
     *     f you do not provide this parameter, the call defaults to the local computer.
     *
     *     Example: server1.network.fabrikam
     *
     *     You also can use an IP address in this parameter.
     *     If the IP address is in IPv6 format, the target computer must be running IPv6.
     *
     *     An address in IPv4 looks like 111.222.333.444
     *
     *     An IP address in IPv6 format looks like 2010:836B:4179::836B:4179
     *
     *     For more information on DNS and IPv4, see the Remarks section.
     *
     *
     * @param namespace простраство имен WMI
     * <ul>
     *     <li> 'ROOT\StandardCimv2' см <a href="https://docs.microsoft.com/en-us/previous-versions/windows/desktop/nettcpipprov/msft-nettcpconnection">MSFT_NetTCPConnection</a>
     *     <li> 'root\CIMV2' см <a href="https://docs.microsoft.com/en-us/windows/win32/cimwin32prov/win32-provider">Win32 Provider</a>
     * </ul>
     *
     * String that specifies the namespace to which you log on.
     * For example, to log on to the root\default namespace, use root\default.
     *
     * If you do not specify this parameter, it defaults to the namespace that is configured as the default namespace for scripting
     * @param user имя пользователя.
     *
     * User name to use to connect.
     * The string can be in the form of either a user name or a Domain\Username.
     *
     * Leave this parameter blank to use the current security context.
     * The strUser parameter should only be used with connections to remote WMI servers.
     *
     * If you attempt to specify strUser for a local WMI connection, the connection attempt fails.
     *
     * If Kerberos authentication is in use, then the username and password that is specified in strUser and strPassword cannot be intercepted on a network.
     *
     * You can use the UPN format to specify the strUser.
     *
     * Example: "DomainName\UserName"
     *
     *
     * @param password пароль.
     *
     * String that specifies the password to use when attempting to connect.
     * Leave the parameter blank to use the current security context.
     *
     * The strPassword parameter should only be used with connections to remote WMI servers.
     * If you attempt to specify strPassword for a local WMI connection, the connection attempt fails.
     *
     * If Kerberos authentication is in use then the username and password that is specified in strUser and strPassword cannot be intercepted on the network.
     *
     *
     * @param locale локаль.
     *
     * String that specifies the localization code.
     * If you want to use the current locale, leave it blank.
     *
     * If not blank, this parameter must be a string that indicates the desired locale where information must be retrieved.
     *
     * For Microsoft locale identifiers,
     * the format of the string is "MS_xxxx", where xxxx is a string in the hexadecimal form that indicates the LCID.
     *
     * For example, American English would appear as "MS_409".
     *
     *
     * @param authority Авторизация <br>
     *
     * <b>""</b> This parameter is optional. However, if it is specified, only Kerberos or NTLMDomain can be used. <br>
     * <b>"Kerberos:"</b> If the strAuthority parameter begins with the string "Kerberos:",
     * then Kerberos authentication is used and this parameter should contain a Kerberos principal name.
     *
     * The Kerberos principal name is specified as Kerberos:domain,
     * such as Kerberos:fabrikam where fabrikam is the server to which you are attempting to connect.<br>
     *
     * Example: "Kerberos:DOMAIN" <br>
     *
     * <b>"NTLMDomain:"</b>
     *
     * To use NT Lan Manager (NTLM) authentication, you must specify it as NTLMDomain:domain,
     * such as NTLMDomain:fabrikam where fabrikam is the name of the domain.
     *
     * <p> Example: "NTLMDomain:DOMAIN"
     *
     * <p> If you leave this parameter blank,
     * the operating system negotiates with COM to determine whether NTLM or Kerberos authentication is used.
     * This parameter should only be used with connections to remote WMI servers.
     *
     * If you attempt to set the authority for a local WMI connection, the connection attempt fails.
     *
     * @param client клиентский код
     */
    @SuppressWarnings('GrMethodMayBeStatic')
    void wmi( String host, String namespace, String user, String password, String locale, String authority, Consumer<WMI> client ){
        if( client==null )throw new IllegalArgumentException("client==null");
        if( host==null )throw new IllegalArgumentException("host==null");
        if( namespace==null )throw new IllegalArgumentException("namespace==null");
        if( password==null )throw new IllegalArgumentException("password==null");
        if( locale==null )throw new IllegalArgumentException("locale==null");
        if( authority==null )throw new IllegalArgumentException("authority==null");
        wmi0(host, namespace, user, password, locale, authority, client )
    }

    /**
     * Работа с wmi api
     * @param host хост, '.' - текущий хост
     * @param namespace пространство имен wmi:
     * <ul>
     *     <li> 'ROOT\StandardCimv2' см <a href="https://docs.microsoft.com/en-us/previous-versions/windows/desktop/nettcpipprov/msft-nettcpconnection">MSFT_NetTCPConnection</a>
     *     <li> 'root\CIMV2' см <a href="https://docs.microsoft.com/en-us/windows/win32/cimwin32prov/win32-provider">Win32 Provider</a>
     * </ul>
     * @param client клиентский код
     */
    @SuppressWarnings('GrMethodMayBeStatic')
    protected void wmi0( String host, String namespace, String user, String password, String locale, String authority, Consumer<WMI> client ){
        if( client==null )throw new IllegalArgumentException("client==null");

        def locator = new ActiveXObject('WbemScripting.SWbemLocator')
        def services;
        if( host!=null && namespace!=null && user!=null && password!=null && locale!=null && authority!=null ){
            services = locator.ConnectServer(host, namespace, user, password, locale, authority)
        }else if( host!=null && namespace!=null && user!=null && password!=null && locale!=null ){
            services = locator.ConnectServer(host, namespace, user, password, locale)
        }else if( host!=null && namespace!=null && user!=null && password!=null ){
            services = locator.ConnectServer(host, namespace, user, password)
        }else if( host!=null && namespace!=null && user!=null ){
            services = locator.ConnectServer(host, namespace, user)
        }else if( host!=null && namespace!=null ){
            services = locator.ConnectServer(host, namespace)
        }else if( host!=null ){
            services = locator.ConnectServer(host)
        }else{
            services = locator.ConnectServer()
        }

        WMIImpl wmiImpl = new WMIImpl(services: services)
        client.accept(wmiImpl)
    }
}
