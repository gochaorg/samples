package xyz.cofe.win.wmi

import org.codehaus.groovy.scriptom.Scriptom
import org.codehaus.groovy.scriptom.util.wbemscripting.WbemDateTime

class Win32_Process {
    public static List<Win32_Process> list( CIMV2WMI wmi ){
        if( wmi==null )throw new IllegalArgumentException("wmi==null");
        List<Win32_Process> list = new ArrayList<>()
        wmi.execQuery( "SELECT * FROM Win32_Process", wmiObj -> {
            list.add(new Win32_Process(activeX: wmiObj))
        })
        return list
    }

    public static Win32_Process get( CIMV2WMI wmi, int pid ){
        if( wmi==null )throw new IllegalArgumentException("wmi==null");
        def ax = wmi.getObject("\\\\.\\root\\cimv2:Win32_Process.Handle=\"${pid}\"")
        new Win32_Process(activeX: ax)
    }

    def activeX
    String getCreationClassName(){ Scriptom.isNull(activeX.CreationClassName) ? null : activeX.CreationClassName }
    String getCaption(){ Scriptom.isNull(activeX.Caption) ? null : activeX.Caption }
    String getCommandLine(){ Scriptom.isNull(activeX.CommandLine) ? null : activeX.CommandLine }
    Date getCreationDate(){
        if( Scriptom.isNull(activeX.CreationDate) )return null
        WbemDateTime.toJavaDate(activeX.CreationDate)
    }
    String getCSCreationClassName(){ Scriptom.isNull(activeX.CSCreationClassName) ? null : activeX.CSCreationClassName }
    String getCSName(){ Scriptom.isNull(activeX.CSName) ? null : activeX.CSName }
    String getDescription(){ Scriptom.isNull(activeX.Description) ? null : activeX.Description }
    String getExecutablePath(){ Scriptom.isNull(activeX.ExecutablePath) ? null : activeX.ExecutablePath }
    int getExecutionState(){ activeX.ExecutionState }
    String getHandle(){ Scriptom.isNull(activeX.Handle) ? null : activeX.Handle }
    int getHandleCount(){ activeX.HandleCount }
    Date getInstallDate(){
        if( Scriptom.isNull(activeX.InstallDate) )return null
        WbemDateTime.toJavaDate(activeX.InstallDate)
    }
    long getKernelModeTime(){ activeX.KernelModeTime }
    long getMaximumWorkingSetSize(){ activeX.MaximumWorkingSetSize }
    long getMinimumWorkingSetSize(){ activeX.MinimumWorkingSetSize }
    String getName(){ Scriptom.isNull(activeX.Name) ? null : activeX.Name }
    String getOSCreationClassName(){ Scriptom.isNull(activeX.OSCreationClassName) ? null : activeX.OSCreationClassName }
    String getOSName(){ Scriptom.isNull(activeX.OSName) ? null : activeX.OSName }
    long getOtherOperationCount(){ activeX.OtherOperationCount }
    long getOtherTransferCount(){ activeX.OtherTransferCount }
    int getPageFaults(){ activeX.PageFaults }
    int getPageFileUsage(){ activeX.PageFileUsage }
    int getParentProcessId(){ activeX.ParentProcessId }
    int getPeakPageFileUsage(){ activeX.PeakPageFileUsage }
    long getPeakVirtualSize(){ activeX.PeakVirtualSize }
    long getPeakWorkingSetSize(){ activeX.PeakWorkingSetSize }
    Integer getPriority(){
        if( Scriptom.isNull(activeX.Priority) )return null
        activeX.Priority
    }
    long getPrivatePageCount(){ activeX.PrivatePageCount }
    int getProcessId(){ activeX.ProcessId }
    int getQuotaNonPagedPoolUsage(){ activeX.QuotaNonPagedPoolUsage }
    int getQuotaPagedPoolUsage(){ activeX.QuotaPagedPoolUsage }
    int getQuotaPeakNonPagedPoolUsage(){ activeX.QuotaPeakNonPagedPoolUsage }
    int getQuotaPeakPagedPoolUsage(){ activeX.QuotaPeakPagedPoolUsage }
    long getReadOperationCount(){ activeX.ReadOperationCount }
    long getReadTransferCount(){ activeX.ReadTransferCount }
    int getSessionId(){ activeX.SessionId }
    String getStatus(){ Scriptom.isNull(activeX.Status) ? null : activeX.Status }
    Date getTerminationDate(){
        if( Scriptom.isNull(activeX.TerminationDate) )return null
        WbemDateTime.toJavaDate(activeX.TerminationDate)
    }
    int getThreadCount(){ activeX.ThreadCount }
    long getUserModeTime(){ activeX.UserModeTime }
    long getVirtualSize(){ activeX.VirtualSize }
    String getWindowsVersion(){ Scriptom.isNull(activeX.WindowsVersion) ? null : activeX.WindowsVersion }
    long getWorkingSetSize(){ activeX.WorkingSetSize }
    long getWriteOperationCount(){ activeX.WriteOperationCount }
    long getWriteTransferCount(){ activeX.WriteTransferCount }

    void terminate(int exitCode){
        activeX.Terminate(exitCode)
    }
}
