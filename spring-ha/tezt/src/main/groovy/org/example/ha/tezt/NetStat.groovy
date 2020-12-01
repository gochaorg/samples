package org.example.ha.tezt

import xyz.cofe.win.WinAPI
import xyz.cofe.win.wmi.MSFT_NetTCPConnection
import xyz.cofe.win.wmi.Win32_Process
import xyz.cofe.win.wmi.WmiNamespaces

import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * Получение информации о TCP сокетах
 */
class NetStat implements Iterable<SocketUseInfo> {
    public final Map<SocketAddr,SocketUseInfo> usedTcpSockets = new TreeMap<>()
    public static NetStat fetch(){
        NetStat inst = new NetStat()
        WinAPI.run { winapi ->
            WmiNamespaces.CIMV2.connect( winapi ) { wmi ->
                WmiNamespaces.StandardCimv2.connect(winapi) { swmi ->
                    MSFT_NetTCPConnection.list(swmi).each { socket ->
                        SocketAddr saLocal = new SocketAddr(host: socket.localAddress, port: socket.localPort)
                        SocketAddr saRemote = new SocketAddr(host: socket.remoteAddress, port: socket.remotePort)

                        Win32_Process win32_process = Win32_Process.get(wmi, socket.owningProcess)
                        OsProcess proc = new OsProcess(win32_process)

                        SocketUseInfo si = new SocketUseInfo(
                            local: saLocal,
                            remote: saRemote,
                            process: proc
                        )

                        inst.usedTcpSockets[saLocal] = si
                    }
                }
            }
        }
        return inst
    }

    void each( Consumer<SocketUseInfo> useInfo ){
        if( useInfo==null )throw new IllegalArgumentException("useInfo==null");
        usedTcpSockets.each {k,v -> useInfo.accept(v) }
    }

    int getSize(){ usedTcpSockets.size() }

    SocketUseInfo getAt( int idx ){
        if( idx<0 )return null
        int i = -1
        for( def e : usedTcpSockets.entrySet() ){
            i++
            if( i==idx ){
                return e.value
            }
        }
        return null
    }

    @Override
    Iterator<SocketUseInfo> iterator() {
        return usedTcpSockets.values().iterator()
    }
}
