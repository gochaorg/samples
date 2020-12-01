package xyz.cofe.win.wmi

import org.codehaus.groovy.scriptom.Scriptom
import org.codehaus.groovy.scriptom.util.wbemscripting.WbemDateTime

class MSFT_NetTCPConnection extends MSFT_NetTransportConnection {
    static List<MSFT_NetTCPConnection> list(StandardCimv2WMI wmi ){
        if( wmi==null )throw new IllegalArgumentException("wmi==null");
        List<MSFT_NetTCPConnection> list = new ArrayList<>()
        wmi.execQuery( 'SELECT * FROM MSFT_NetTCPConnection' ) { ax ->
            list.add(new MSFT_NetTCPConnection(activeX: ax))
        }
        return list
    }

    String getRemoteAddress(){ Scriptom.isNull(activeX.RemoteAddress) ? null : activeX.RemoteAddress }
    Integer getRemotePort(){ Scriptom.isNull(activeX.RemotePort) ? null : activeX.RemotePort }

    /**
     * The state of the TCP connection.
     *
     * <table>
     *     <tr>
     * <td>Value 	</td><td>Meaning</td>
     * </tr><tr>
     * <td>1 Closed
     * </td>	<td>Represents no connection state at all.</td>
     * </tr><tr>
     *
     * <td>2 Listen
     * 	</td><td>Waiting for a connection request from any remote TCP and port.</td>
     * </tr><tr>
     *
     * <td>3 SynSent
     * 	</td><td>Waiting for a matching connection request after having sent a connection request (SYN packet).</td>
     *
     * </tr><tr>
     *
     * <td>4 SynReceived
     * 	</td><td>Waiting for a confirming connection request acknowledgment after having both received and sent a connection request (SYN packet).
     *</td>
     * </tr><tr>
     *
     * <td>5 Established
     * 	</td><td>Represents an open connection, data received can be delivered to the user. This is the normal state for the data transfer phase of the TCP connection.
     *</td>
     *
     * </tr><tr>
     * <td>6 FinWait1
     * 	</td><td>Waiting for a connection termination request from the remote TCP, or an acknowledgment of the connection termination request previously sent.
     *</td>
     *
     * </tr><tr>
     * <td>7 FinWait2
     * 	</td><td>Waiting for a connection termination request from the remote TCP.
     *</td>
     *
     * </tr><tr>
     * <td>8 CloseWait
     * 	</td><td>Waiting for a connection termination request from the local user.
     *</td>

     * </tr><tr>
     * <td>9 Closing
     * 	</td><td>Waiting for a connection termination request acknowledgment from the remote TCP.
     *</td>

     * </tr><tr>
     * <td>10 LastAck
     * 	</td><td>Waiting for an acknowledgment of the connection termination request previously sent to the remote TCP.
     *</td>

     * </tr><tr>
     * <td>11 TimeWait
     * </td><td>Waiting for enough time to pass to be sure the remote TCP received the acknowledgment of its connection termination request.
     *
     * </tr><tr>
     * <td>12 DeleteTCB</td><td></td>
     * </tr>
     * </table>
     * @return The state of the TCP connection.
     */
    Integer getState(){ Scriptom.isNull(activeX.State) ? null : activeX.State }
    Integer getAppliedSetting(){ Scriptom.isNull(activeX.AppliedSetting) ? null : activeX.AppliedSetting }
    Integer getOwningProcess(){ Scriptom.isNull(activeX.OwningProcess) ? null : activeX.OwningProcess }
    Date getCreationTime(){ Scriptom.isNull(activeX.CreationTime) ? null : WbemDateTime.toJavaDate(activeX.CreationTime) }

    /**
     * The connection offload state of the TCP connection.
     * <ul>
     * <li>InHost (0)
     * <li>Offloading (1)
     * <li>Offloaded (2)
     * <li>Uploading (3)
     * </ul>
     * @return offload state
     */
    Integer getOffloadState(){ Scriptom.isNull(activeX.OffloadState) ? null : activeX.OffloadState }
}
