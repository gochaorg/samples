package xyz.cofe.win;

import org.junit.jupiter.api.Test;
import xyz.cofe.win.wmi.MSFT_NetTCPConnection;
import xyz.cofe.win.wmi.WMIPath;
import xyz.cofe.win.wmi.Win32_Process;
import xyz.cofe.win.wmi.WmiNamespaces;

public class WMITest {
    @Test
    public void listProcesses(){
        System.out.println("WMITest");
        WinAPI.run( winapi->{
            WmiNamespaces.CIMV2.connect( winapi, wmi -> {
                Win32_Process.list(wmi).forEach( proc -> {
                    System.out.println(
                        "proc "+proc.getProcessId()+
                        " cap="+proc.getCaption()+
                        " cmdLine="+proc.getCommandLine());

                    System.out.println("  path="+ WMIPath.pathOf(proc.getActiveX()));

//                    if( "notepad.exe".equalsIgnoreCase(proc.getCaption()) ){
//                        proc.terminate(1);
//                    }
                });
            });
        });
    }

    @Test
    public void getProcTest(){
        WinAPI.run(winApi -> {
            winApi.wmi( wmi -> {
                System.out.println(
                    wmi.getObject("\\\\L65195118\\root\\cimv2:Win32_Process.Handle=\"0\"")
                );
                System.out.println(
                    wmi.getObject("\\\\.\\root\\cimv2:Win32_Process.Handle=\"0\"")
                );
            });
        });
    }

    @Test
    public void listTcpSockets(){
        WinAPI.run( winAPI -> {
            WmiNamespaces.StandardCimv2.connect(winAPI, wmi -> {
                MSFT_NetTCPConnection.list(wmi).forEach( conn -> {
                    System.out.println(
                        "tcp: "+conn.getLocalAddress()+":"+conn.getLocalPort()+" -> "+conn.getRemoteAddress()+":"+conn.getRemotePort()
                    );
                    System.out.println("  proc="+conn.getOwningProcess());
                });
            });
        });
    }
}
