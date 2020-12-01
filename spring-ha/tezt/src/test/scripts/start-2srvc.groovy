import xyz.cofe.io.fs.File
import xyz.cofe.text.Text

//region resolving eurekaJar, serviceJar, zuulJar
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
//region javaExe
javaExe = new File(
    System.properties['java.home'].toString()
).walk().go().filter {f -> f.name.equalsIgnoreCase('java.exe') }.head()
//endregion

println "starting eureka"
eurekaProc = "\"${javaExe}\" -jar \"${eurekaJar}\"".execute()

StringBuilder eurekaOutput = new StringBuilder()
def checkStarted = {
    boolean started = eurekaOutput.toString().contains("Started Eureka")
    if( started ){
        println "started!"
    }
}
eurekaProc.consumeProcessOutputStream(new Appendable() {
    @Override
    Appendable append(CharSequence csq) throws IOException {
        eurekaOutput.append(csq)
        checkStarted()
        return this
    }

    @Override
    Appendable append(CharSequence csq, int start, int end) throws IOException {
        eurekaOutput.append(csq,start,end)
        checkStarted()
        return this
    }

    @Override
    Appendable append(char c) throws IOException {
        eurekaOutput.append(c)
        checkStarted()
        return this
    }
})

eurekaProc.waitForOrKill( 1000L * 30L )