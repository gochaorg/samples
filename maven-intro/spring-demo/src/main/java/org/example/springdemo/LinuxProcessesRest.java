package org.example.springdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.cofe.fn.Tuple2;
import xyz.cofe.io.fs.File;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/linux/proc")
public class LinuxProcessesRest {
    private static final Logger log = LoggerFactory.getLogger(LinuxProcessesRest.class);

    protected Map<String,Long> lastEcho = new ConcurrentHashMap<>();

    protected Optional<String> readProcInfo(File procFile, String name ){
        return readProcInfo(procFile,name,null);
    }

    protected Optional<String> readProcInfo(File procFile, String name, Consumer<Throwable> errRead ){
        if( procFile==null )throw new IllegalArgumentException( "procFile==null" );
        if( name==null )throw new IllegalArgumentException( "name==null" );
        var targetFile = procFile.resolve(name);
        try {
            String content = targetFile.readText(Charset.defaultCharset());
            if( content!=null && content.length()>0 ){
                return Optional.of(content);
            }else{
                return Optional.empty();
            }
        } catch( Throwable err ) {
            long lecho = lastEcho.getOrDefault(targetFile.toString(),0L);
            if( (System.currentTimeMillis()-lecho)>1000L*60L*5L ){
                log.warn("can't read "+targetFile+", err ", err);
                lastEcho.put(targetFile.toString(),System.currentTimeMillis());
            }else{
                log.debug("can't read "+targetFile+", err ", err);
            }
            if( errRead!=null ){
                errRead.accept(err);
            }
            return Optional.empty();
        }
    }

    private static final List<Tuple2<String,Optional<Function<String,Object>>>> extractData = new ArrayList<>(){{
        add( Tuple2.of("sessionid", Optional.empty()) );
        add( Tuple2.of("cmdline", Optional.of(
            str -> Arrays.asList(str.split("\u0000"))
        )) );
        add( Tuple2.of("environ", Optional.of(
            str -> Arrays.asList(str.split("\u0000"))
        )) );
        add( Tuple2.of("status", Optional.empty()));
    }};

    @GetMapping()
    public Object processes(){
        File procDir = new File("/proc");
        List<File> procList = new ArrayList<>();
        procDir.dirIterator().forEachRemaining( f -> {
            if( f.isReadable() && f.isDir() && f.getName().matches("\\d+") ){
                procList.add(f);
            }
        });

        return procList.stream().map( procFile -> {
            try {
                var info = new LinkedHashMap<String,Object>();

                info.put("id", procFile.getName());
                extractData.forEach( ext -> {
                    readProcInfo(procFile, ext.a()).ifPresent( str -> {
                        if( ext.b().isPresent() ){
                            info.put(ext.a(), ext.b().get().apply(str));
                        }else{
                            info.put(ext.a(), str);
                        }
                    });
                } );

                var exe = procFile.resolve("exe");
                try {
                    var targetExe = exe.toReal();
                    info.put("exe",targetExe.toString());
                } catch( Throwable err ){
                    Long lecho = lastEcho.getOrDefault(exe.toString(),0L);
                    if( (System.currentTimeMillis()-lecho)>1000L*60L*5L ){
                        lastEcho.put(exe.toString(),System.currentTimeMillis());
                        log.warn("can't read "+exe+", err ", err);
                    }else {
                        log.debug("can't read " + exe + ", err ", err);
                    }
                }

                return info;
            } catch( Throwable err ){
                log.warn("can't read "+procFile+", err ", err);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
