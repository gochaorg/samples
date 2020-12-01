package org.example.ha.tezt

import java.nio.charset.Charset
import java.util.function.Consumer

class JarStarter extends ProcessStarter {
    //region override parent methods - return type
    @Override synchronized JarStarter start() {
        super.start()
        this
    }

    @Override
    JarStarter output(Consumer<OutputHandler> handler) {
        super.output(handler)
        this
    }

    @Override
    JarStarter restartable(boolean restartable) {
        super.restartable(restartable)
        this
    }

    @Override
    JarStarter inheritEnviroment(boolean inherit) {
        super.inheritEnviroment(inherit)
        this
    }

    @Override
    JarStarter enviroment(Map<String, String> env) {
        super.enviroment(env)
        this
    }

    @Override
    JarStarter workDir(File wd) {
        super.workDir(wd)
        this
    }
    JarStarter workDir(xyz.cofe.io.fs.File wd) {
        super.workDir(wd?.toFile())
        this
    }

    @Override
    JarStarter observersAsDaemon(boolean v) {
        super.observersAsDaemon(v)
        return this
    }

    @Override
    JarStarter checkOutputReady(boolean v) {
        super.checkOutputReady(v)
        this
    }

    @Override
    JarStarter outputCharset(Charset cs) {
        super.outputCharset(cs)
        this
    }

    @Override
    JarStarter outputCharset(String cs) {
        super.outputCharset(cs)
        this
    }

    @Override
    JarStarter killObserverTimeout(long timeout) {
        super.killObserverTimeout(timeout)
        this
    }
    //endregion

    //region javaExe : File
    File javaExe
    JarStarter javaExe(File v){
        setJavaExe(v)
        this
    }
    JarStarter javaExe(xyz.cofe.io.fs.File v){
        setJavaExe(v?.toFile())
        this
    }
    //endregion
    //region jar : File
    File jar
    JarStarter jar(File v){
        setJar(v)
        this
    }
    JarStarter jar(xyz.cofe.io.fs.File v){
        setJar(v?.toFile())
        this
    }
    //endregion

    //region systemProperties : Map<String,String>
    public final Map<String,String> systemProperties = [:]
    //endregion

    //region memoryMax
    public String memoryMax
    JarStarter memoryMaxBytes( long bytes ){
        memoryMax = bytes.toString()
        this
    }
    JarStarter memoryMaxKBytes( long kbytes ){
        memoryMax = kbytes.toString()+'k'
        this
    }
    JarStarter memoryMaxMBytes( long mbytes ){
        memoryMax = mbytes.toString()+'m'
        this
    }
    JarStarter memoryMaxGBytes( long gbytes ){
        memoryMax = gbytes.toString()+'g'
        this
    }
    //endregion
    //region build command
    @Override
    String getCommand() {
        File exe = getJavaExe()
        if( exe==null )throw new IllegalStateException("property 'javaExe' is null")

        File jar1 = getJar()
        if( jar1==null )throw new IllegalStateException("property 'jar' is null")

        StringBuilder sb = new StringBuilder()
        sb << "\"$exe\""
        if( memoryMax!=null ){
            sb << " -Xmx$memoryMax"
        }
        systemProperties.each {k,v ->
            if( k!=null && v!=null ){
                sb << " \"-D$k=$v\""
            }
        }
        sb << " -jar \"$jar1\""
        return sb.toString()
    }

    @Override
    void setCommand(String command) {
        throw new RuntimeException("property command read only")
    }
    //endregion
}
