import groovy.transform.EqualsAndHashCode
import groovy.transform.Sortable
import groovy.transform.ToString
import org.example.ha.tezt.JarStarter
import org.example.ha.tezt.LineInterceptor
import org.example.ha.tezt.NetStat
import org.example.ha.tezt.ProcessStarter
import org.example.ha.tezt.SocketUseInfo
import xyz.cofe.io.fs.File
import xyz.cofe.text.Text
import xyz.cofe.win.*
import xyz.cofe.win.wmi.CIMV2WMI
import xyz.cofe.win.wmi.MSFT_NetTCPConnection
import xyz.cofe.win.wmi.Win32_Process
import xyz.cofe.win.wmi.WmiNamespaces

import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

//region определение переменных eurekaJar, serviceJar, zuulJar::xyz.cofe.io.fs.File
static File resolveProjDir() {
    File curDir = new File(".").toAbsolute().normalize()

    File found = null
    while (curDir!=null){

        if( curDir.resolve('eureka').isDir() &&
                curDir.resolve('service-a').isDir() &&
                curDir.resolve( 'zuul' ).isDir() ){
            found = curDir
            break
        }

        curDir = curDir.parent
    }

    found
}
projDir = resolveProjDir()
if( projDir==null ){
    println("project directory not found")
    return
}

eurekaJar = projDir.resolve("eureka/target/eureka-1.0-SNAPSHOT.jar")
serviceJar = projDir.resolve("service-a/target/service-a-1.0-SNAPSHOT.jar")
zuulJar = projDir.resolve("zuul/target/zuul-1.0-SNAPSHOT.jar")

allJarExists = [ eurekaJar, serviceJar, zuulJar ].collect {
    if( !it.exists ){
        println( "not found $it" )
    }
    it.exists
}.inject { a,b -> a && b }

if( !allJarExists ){
    return
}else{
    println "all jars resolved"
}
//endregion
//region определение переменных javaExe:xyz.cofe.io.fs.File
javaExe = new File(
    System.properties['java.home'].toString()
).walk().go().filter {f -> f.name.equalsIgnoreCase('java.exe') }.head()
//endregion
//region просмотр занятых портов
def starting = ([ 28000:'eureka'
                , 28010:'zuul'
                , 28100:'service-1'
                , 28101:'service-2'
                , 28103:'service-2'
                , 28104:'service-3'
                , 28105:'service-4'
                ]) as Map<Integer,Object>

// Получение списка используемых портов
println "inspect used tcp sockets"
Collection<Integer> targetPorts = starting.keySet()
println "target port $targetPorts"
def nstat = NetStat.fetch()
def busedPorts = (List<SocketUseInfo>)nstat.findAll { si ->
    boolean found = si.local.port in targetPorts
    found
}
def killJobs = [:]

if( busedPorts.size()>0 ){
    println( "bused ports:")
    busedPorts.each { si ->
        println "bused local=$si.local remote=$si.remote proc=$si.process"
        if( si.process.name.equalsIgnoreCase("java.exe") &&
            si.process.commandLine.contains('spring-ha')
        ) {
            killJobs[si] = { Win32_Process proc ->
                if( proc.processId == si.process.pid ){
                    println "kill pid=$proc.processId name=$proc.name"
                    proc.terminate(1)
                }
            }
        }
    }
}

boolean allKillJobResolved = busedPorts.isEmpty() ?: busedPorts.collect { si -> killJobs.containsKey(si) }.inject {a,b -> a && b}
if( !allKillJobResolved ){
    println "restart job not created"
    return
}

WinAPI.run({winapi ->
    WmiNamespaces.CIMV2.connect(winapi) {wmi ->
        Win32_Process.list(wmi).each {proc ->
            killJobs.values().each { killing -> killing(proc) }
        }
    }
})
//endregion

//outputTriggers: [(~/(?is)Started +Eureka/): {
//    println("catched $it")
//}]).start()

println "start eureka"
def proc = new JarStarter()
    .javaExe(javaExe)
    .jar(eurekaJar)
    .observersAsDaemon(false)
    .output( h -> {
        h.trigger( ~/(?is)Started +Eureka/ ) { String line, JarStarter starter ->
            println("eureka started")
            starter.detach()
        }
    }).start()

//proc.process.waitFor(20, TimeUnit.SECONDS)
println "finished"

//println "starting eureka"
//eurekaProc = "\"${javaExe}\" -jar \"${eurekaJar}\"".execute()
//
//StringBuilder eurekaOutput = new StringBuilder()
//def checkStarted = {
//    boolean started = eurekaOutput.toString().contains("Started Eureka")
//    if( started ){
//        println "started!"
//    }
//}
//eurekaProc.consumeProcessOutputStream(new Appendable() {
//    @Override
//    Appendable append(CharSequence csq) throws IOException {
//        eurekaOutput.append(csq)
//        checkStarted()
//        return this
//    }
//
//    @Override
//    Appendable append(CharSequence csq, int start, int end) throws IOException {
//        eurekaOutput.append(csq,start,end)
//        checkStarted()
//        return this
//    }
//
//    @Override
//    Appendable append(char c) throws IOException {
//        eurekaOutput.append(c)
//        checkStarted()
//        return this
//    }
//})
//
//eurekaProc.waitForOrKill( 1000L * 30L )