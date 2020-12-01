package xyz.cofe.win.wmi

import org.codehaus.groovy.scriptom.Scriptom
import org.codehaus.groovy.scriptom.util.wbemscripting.WbemDateTime
import xyz.cofe.win.WinAPI

class MSFT_NetTransportConnection {
//    static List<MSFT_NetTransportConnection> list( WinAPI winapi ){
//        if( winapi==null )throw new IllegalArgumentException("winapi==null");
//        winapi.wmi('.', WmiNamespaces.StandardCimv2.name, null, null, null, null ) {wmi ->
//            wmi.execQuery( '' )
//        }
//    }

    def activeX
    String getCaption(){ Scriptom.isNull(activeX.Caption) ? null : activeX.Caption }
    String getDescription(){ Scriptom.isNull(activeX.Description) ? null : activeX.Description }
    String getElementName(){ Scriptom.isNull(activeX.ElementName) ? null : activeX.ElementName }
    Date getInstallDate(){ Scriptom.isNull(activeX.InstallDate) ? null : WbemDateTime.toJavaDate(activeX.InstallDate) }
    Date getName(){ Scriptom.isNull(activeX.Name) ? null : activeX.Name }
    String getOperationalStatus(){ Scriptom.isNull(activeX.OperationalStatus) ? null : activeX.OperationalStatus.toString() }
    String getStatusDescriptions(){ Scriptom.isNull(activeX.StatusDescriptions) ? null : activeX.StatusDescriptions.toString() }
    String getStatus(){ Scriptom.isNull(activeX.Status) ? null : activeX.Status }
    Integer getHealthState(){ Scriptom.isNull(activeX.HealthState) ? null : activeX.HealthState }
    Integer getCommunicationStatus(){ Scriptom.isNull(activeX.CommunicationStatus) ? null : activeX.CommunicationStatus }
    Integer getDetailedStatus(){ Scriptom.isNull(activeX.DetailedStatus) ? null : activeX.DetailedStatus }
    Integer getOperatingStatus(){ Scriptom.isNull(activeX.OperatingStatus) ? null : activeX.OperatingStatus }
    Integer getPrimaryStatus(){ Scriptom.isNull(activeX.PrimaryStatus) ? null : activeX.PrimaryStatus }
    String getOtherEnabledState(){ Scriptom.isNull(activeX.OtherEnabledState) ? null : activeX.OtherEnabledState }
    Integer getEnabledDefault(){ Scriptom.isNull(activeX.OtherEnabledState) ? null : activeX.OtherEnabledState }
    Date getTimeOfLastStateChange(){ Scriptom.isNull(activeX.TimeOfLastStateChange) ? null : WbemDateTime.toJavaDate(activeX.TimeOfLastStateChange) }

    /**
     * TransitioningToState indicates the target state to which the instance is transitioning.
     *
     * <p>
     * A value of 5 "No Change" shall indicate that no transition is in progress.A value of 12 "Not Applicable" shall indicate the implementation does not support representing ongoing transitions.
     *
     * <p>
     * A value other than 5 or 12 shall identify the state to which the element is in the process of transitioning.
     * @return  target state to which the instance is transitioning
     */
    Integer getTransitioningToState(){ Scriptom.isNull(activeX.TransitioningToState) ? null : activeX.TransitioningToState }
    String getAvailableRequestedStates(){ Scriptom.isNull(activeX.AvailableRequestedStates) ? null : activeX.AvailableRequestedStates.toString() }
    String getInstanceID(){ Scriptom.isNull(activeX.InstanceID) ? null : activeX.InstanceID }
    Integer getEnabledState(){ Scriptom.isNull(activeX.EnabledState) ? null : activeX.EnabledState }

    /**
     * Read/write prop.
     *
     * RequestedState is an integer enumeration that indicates the last requested or desired state for the element,
     * irrespective of the mechanism through which it was requested.
     *
     * The actual state of the element is represented by EnabledState.
     *
     * This property is provided to compare the last requested and current enabled or disabled states.
     * Note that when EnabledState is set to 5 ("Not Applicable"), then this property has no meaning.
     * Refer to the EnabledState property description for explanations of the values in the RequestedState enumeration.
     *
     * <table>
     *     <tr>
     * <td>Value 	</td><td>Meaning</td>
     *     </tr><tr>
     *
     * <td>0            </td><td>Unknown the last requested state for the element is unknown.</td>
     * </tr><tr>
     *
     * <td>2 Enabled</td><td></td>
     * </tr><tr>
     *
     * <td>3 Disabled    </td><td>requests an immediate disabling of the element, such that it will not execute or accept any commands or processing requests.
     *</td>
     * </tr><tr>
     *
     * <td>4 Shut Down   </td><td>Requests an orderly transition to the Disabled state, and might involve removing power, to completely erase any existing state.
     *</td>
     * </tr><tr>
     *
     * <td>5 No Change   </td><td>Deprecated in lieu of indicating the last requested state is "Unknown" (0). If the last requested or desired state is unknown, RequestedState should have the value "Unknown" (0), but may have the value "No Change" (5).
     *</td>
     * </tr><tr>
     *
     * <td>6 Offline    </td><td>The element has been requested to transition to the Enabled but Offline EnabledState.</td>
     * </tr><tr>
     *
     * <td>7 Test</td><td></td>
     * </tr><tr>
     *
     * <td>8 Deferred</td><td></td>
     * </tr><tr>
     *
     * <td>9 Quiesce</td><td></td>
     * </tr><tr>
     *
     * <td>10 Reboot     </td><td>Refers to doing a "Shut Down" and then moving to an "Enabled" state.</td>
     * </tr><tr>
     *
     * <td>11 Reset      </td><td>Reset indicates that the element is first "Disabled" and then "Enabled".</td>
     * </tr><tr>
     *
     * <td>12 Not Applicable   </td><td>Knowledge of the last RequestedState is not supported for the EnabledLogicalElement.</td>
     * </tr><tr>
     *
     * <td>13 32767 DMTF Reserved</td><td></td>
     * </tr><tr>
     *
     * <td>32768 ... 65535 Vendor Reserved</td><td></td>
     * </tr>
     * </table>
     * @return RequestedState
     */
    Integer getRequestedState(){ Scriptom.isNull(activeX.RequestedState) ? null : activeX.RequestedState }
    Integer getDirectionality(){ Scriptom.isNull(activeX.Directionality) ? null : activeX.Directionality }
    Integer getAggregationBehavior(){ Scriptom.isNull(activeX.AggregationBehavior) ? null : activeX.AggregationBehavior }
    String getLocalAddress(){ Scriptom.isNull(activeX.LocalAddress) ? null : activeX.LocalAddress }
    Integer getLocalPort(){ Scriptom.isNull(activeX.LocalPort) ? null : activeX.LocalPort }
}
