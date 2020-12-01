package org.example.ha.tezt

import groovy.transform.ToString
import xyz.cofe.win.wmi.Win32_Process

@ToString(includeNames = true)
class OsProcess {
    /** пример: 12260 */
    int pid

    /** пример:  */
    Integer ppid;

    /** пример: java.exe */
    String caption

    /** пример: ""C:\Program Files\BellSoft\LibericaJDK-15-Full\bin\java.exe" -Xmx700m -Djava.awt.headless=true -Djdt.compiler.useSingleThread=true -Dpreload.project.path=C:/Users/65kgp/code/samples/spring-ha -Dpreload.config.path=C:/Users/65kgp/AppData/Roaming/JetBrains/IdeaIC2020.2/options -Dcompile.parallel=false -Drebuild.on.dependency.change=true -Dio.netty.initialSeedUniquifier=1059860388415158468 -Dfile.encoding=windows-1251 -Duser.language=ru -Duser.country=RU -Didea.paths.selector=IdeaIC2020.2 "-Didea.home.path=C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2020.2.3" -Didea.config.path=C:\Users\65kgp\AppData\Roaming\JetBrains\IdeaIC2020.2 -Didea.plugins.path=C:\Users\65kgp\AppData\Roaming\JetBrains\IdeaIC2020.2\plugins -Djps.log.dir=C:/Users/65kgp/AppData/Local/JetBrains/IdeaIC2020.2/log/build-log "-Djps.fallback.jdk.home=C:/Program Files/JetBrains/IntelliJ IDEA Community Edition 2020.2.3/jbr" -Djps.fallback.jdk.version=11.0.8 -Dio.netty.noUnsafe=true -Djava.io.tmpdir=C:/Users/65kg" */
    String commandLine

    /** пример: "C:\Program Files\BellSoft\LibericaJDK-15-Full\bin\java.exe" */
    String executablePath

    /** пример: "Майкрософт Windows 10 Корпоративная 2016 с долгосрочным обслуживанием|C:\Windows|\Device\Harddisk0\Partition2" */
    String OSName

    /** пример: "10.0.14393" */
    String OSVersion

    /** пример: 39  */
    int threadCount

    /** пример: 1 */
    int sessionId

    /** пример: "java.exe" */
    String name

    Date creationDate;

    Date terminationDate;

    Integer pageFaults;

    Integer priority;

    Integer quotaNonPagedPoolUsage;

    Integer quotaPagedPoolUsage;

    Integer pageFileUsage;

    OsProcess(){}
    OsProcess(Win32_Process proc){
        if( proc!=null ){
            pid = proc.processId
            caption = proc.caption
            commandLine = proc.commandLine
            executablePath = proc.executablePath
            OSName = proc.OSName
            OSVersion = proc.windowsVersion
            threadCount = proc.threadCount
            sessionId = proc.sessionId
            name = proc.name
            ppid = proc.parentProcessId
            creationDate = proc.creationDate
            terminationDate = proc.terminationDate
            pageFaults = proc.pageFaults
            priority = proc.priority
            quotaNonPagedPoolUsage = proc.quotaNonPagedPoolUsage
            quotaPagedPoolUsage = proc.quotaPagedPoolUsage
            pageFileUsage = proc.pageFileUsage
        }
    }
}
