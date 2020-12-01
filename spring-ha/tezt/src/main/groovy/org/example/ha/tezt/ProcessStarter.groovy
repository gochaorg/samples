package org.example.ha.tezt

import java.nio.charset.Charset
import java.time.Duration
import java.time.Instant
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeoutException
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.regex.Pattern

class ProcessStarter {
    String command
    protected volatile Process processInstance
    synchronized Process getProcess(){
        return this.@processInstance
    }

    protected void killTree(Process process){
        if( process==null )throw new IllegalArgumentException("process==null");
        def hdnl = ProcessHandle.of(process.pid())
        List<ProcessHandle> lst = []
        if( hdnl.isPresent() ){
            lst.add(hdnl.get())
        }
        killTree(lst)
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    protected void killTree(List<ProcessHandle> processes, Duration timeout=null){
        Instant started = Instant.now()
        while (true){
            if( timeout!=null ){
                Duration curDuration = Duration.between(started, Instant.now())
                if( curDuration > timeout ){
                    throw new TimeoutException("can't all kill :(")
                }
            }

            List<ProcessHandle> liveProcessesList = processes.findAll { it.alive }
            if( liveProcessesList.empty ){
                break
            }

            Map<Long,ProcessHandle> childrenProcess = [:]
            liveProcessesList.each {childrenProcess[it.pid()] = it }
            liveProcessesList.each {pr ->
                pr.children().each {ch ->
                    if(ch.alive){
                        childrenProcess[ch.pid()] = ch
                    }
                }
            }

            processes = childrenProcess.values()
            processes.each {ph ->
                ph.destroyForcibly()
            }
        }
    }

    boolean restartable = false
    ProcessStarter restartable(boolean restartable){
        setRestartable(restartable)
        this
    }

    boolean inheritEnviroment = true
    void setInheritEnviroment(boolean v){
        if( isRunning() )throw new IllegalStateException("process already runned")
        inheritEnviroment = v
    }
    ProcessStarter inheritEnviroment(boolean inherit){
        setInheritEnviroment(inherit)
        this
    }

    Map<String,String> enviroment
    void setEnviroment(Map<String,String> m){
        if( isRunning() )throw new IllegalStateException("process already runned")
        enviroment = m
    }
    ProcessStarter enviroment(Map<String,String> env){
        enviroment = env
        this
    }

    File workDir
    void setWorkDir( File wd ){
        if( isRunning() )throw new IllegalStateException("process already runned")
        workDir = wd
    }
    ProcessStarter workDir(File wd){
        setWorkDir(wd)
        this
    }

    volatile Map<Pattern,BiConsumer<String, ProcessStarter>> outputTriggers
    class OutputHandler {
        OutputHandler trigger(Pattern pattern, BiConsumer<String, ProcessStarter> call){
            if( pattern==null )throw new IllegalArgumentException("pattern==null");
            if( call==null )throw new IllegalArgumentException("call==null");
            if( outputTriggers==null ){
                outputTriggers = [:]
            }
            outputTriggers[pattern] = call
            this
        }
        OutputHandler trigger(Pattern pattern, Consumer<String> call){
            if( pattern==null )throw new IllegalArgumentException("pattern==null");
            if( call==null )throw new IllegalArgumentException("call==null");
            if( outputTriggers==null ){
                outputTriggers = [:]
            }
            outputTriggers[pattern] = { line,ps -> call(line) } as BiConsumer<String,ProcessStarter>
            this
        }
        OutputHandler trigger(Pattern pattern, Closure call){
            if( pattern==null )throw new IllegalArgumentException("pattern==null");
            if( call==null )throw new IllegalArgumentException("call==null");
            BiConsumer cons = (line, ps) -> {
                if( call.parameterTypes.length>=2 ){
                    List params = []
                    def untypedParam = { int i ->
                        if (i == 0) {
                            params.add(line)
                        } else if (i == 1) {
                            params.add(ps)
                        }
                    }
                    call.parameterTypes.eachWithIndex { Class entry, int i ->
                        if (entry == Object) {
                            untypedParam(i)
                        } else if (entry.isAssignableFrom(String)) {
                            params.add(line)
                        } else if (entry.isAssignableFrom(ProcessStarter)) {
                            params.add(ps)
                        } else {
                            untypedParam(i)
                        }
                    }
                    call(params[0], params[1])
                }else if( call.parameterTypes.length==1 ){
                    call(line)
                }else {
                    call()
                }
            }
            trigger(pattern,cons)
        }
    }
    ProcessStarter output( Consumer<OutputHandler> handler ){
        if( handler==null )throw new IllegalArgumentException("handler==null");
        if( isRunning() ){
            throw new IllegalStateException("process already runned")
        }
        handler.accept(new OutputHandler())
        return this
    }

    static class NullWriter extends Writer {
        @Override void write(char[] cbuf, int off, int len) throws IOException {}
        @Override void flush() throws IOException {}
        @Override void close() throws IOException {}
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    protected LineInterceptor createLineInterceptor(){
        new LineInterceptor(new NullWriter())
    }

    boolean observersAsDaemon = true
    ProcessStarter observersAsDaemon(boolean v){
        observersAsDaemon = v
        this
    }

    boolean checkOutputReady = true
    ProcessStarter checkOutputReady(boolean v){
        checkOutputReady = v
        this
    }

    Charset outputCharset
    ProcessStarter outputCharset(Charset cs){
        outputCharset = cs
        this
    }
    ProcessStarter outputCharset(String cs){
        outputCharset = cs != null ? Charset.forName(cs) : null
        this
    }

    protected final List<Thread> observerThreads = new CopyOnWriteArrayList<>()
    protected void addObserver(Thread thread,String name=null){
        if( thread==null )throw new IllegalArgumentException("thread==null");
        observerThreads.add(thread)
        if( name!=null ){
            thread.name = name
        }
    }
    Thread[] getObservers(){
        observerThreads.toArray(new Thread[0])
    }

    synchronized void detachObservers(){
        observerThreads.each {Thread th ->
            try {
                th.interrupt()
                long timeout = 1000L
                long t0 = System.currentTimeMillis()
                while (th.alive) {
                    th.interrupt()
                    long tdiff = System.currentTimeMillis() - t0
                    if (tdiff > timeout) {
                        th.stop()
                    }
                }
            } catch ( Throwable err ){
                println "detachObservers err $err"
            }
        }
        observerThreads.clear()
    }
    synchronized void stop(){
        if( processInstance==null ){
            throw new IllegalStateException("process not started")
        }
        if( !processInstance.alive ){
            return
        }

        detachObservers()

        killTree(processInstance)
        if (processInstance.alive) {
            throw new IllegalStateException("BUG, can't stop process, is alive")
        }
    }
    synchronized ProcessStarter start(){
        if( processInstance!=null ){
            if( processInstance.alive ) {
                if (!restartable) {
                    throw new IllegalStateException("process already started")
                } else {
                    stop()
                }
            }
            processInstance = null
        }

        String cmd = getCommand()
        if( cmd==null )throw new IllegalStateException("command is null")

        Map<String,String> env = getEnviroment()

        if( env==null ){
            env = [:]
        }

        if( inheritEnviroment ){
            Map<String,String> curEnv = System.getenv()
            curEnv.each { ck, cv ->
                if( !env.containsKey(ck) && cv!=null ){
                    env[ck] = cv
                }
            }
        }

        def removeEnvKeys = env.findAll {k,v -> v==null }.collect {k,v -> k }
        removeEnvKeys.each {env.remove(it) }

        File wd = getWorkDir()
        if( wd==null ){
            wd = new File('.').absoluteFile.canonicalFile
        }

        List<String> envLines = env.collect { k,v -> "$k=$v".toString() }

        processInstance = cmd.execute(envLines.toList(),wd)

        ProcessStarter pSelf = this
        Map<Pattern,BiConsumer<String, ProcessStarter>> outTriggers = outputTriggers
        if( outTriggers!=null ){
            LineInterceptor lineInterceptor = createLineInterceptor()
            lineInterceptor.listen((String line) -> {
                outTriggers.each {ptrn, cons ->
                    if( ptrn!=null && cons!=null ){
                        if( ptrn.matcher(line).find() ){
                            cons.accept(line, pSelf)
                        }
                    }
                }
            })

            InputStreamReader stdoutReader =
                outputCharset!=null ?
                    new InputStreamReader(process.getInputStream(), outputCharset) :
                    new InputStreamReader(process.getInputStream())

            InputStreamReader stderrReader =
                outputCharset!=null ?
                    new InputStreamReader(process.getErrorStream(), outputCharset) :
                    new InputStreamReader(process.getErrorStream())

            InputReadTransfer stdoutTransfer = new InputReadTransfer(stdoutReader, lineInterceptor)
            InputReadTransfer stderrTransfer = new InputReadTransfer(stderrReader, lineInterceptor)

            [stderrTransfer, stdoutTransfer].each {
                it.daemon = observersAsDaemon
                it.checkReady = checkOutputReady
            }

            addObserver( stdoutTransfer,
                "interceptor output of pid=${processInstance.pid()}" )
            addObserver( stderrTransfer,
                "interceptor error of pid=${processInstance.pid()}" )

            stdoutTransfer.start()
            stderrTransfer.start()
        }

        this
    }
    boolean isRunning(){
        Process proc = processInstance
        if( proc!=null ){
            proc.alive
        }
        return false
    }
    synchronized Process detach(){
        detachObservers();
        def proc = processInstance
        processInstance = null
        proc
    }
}
