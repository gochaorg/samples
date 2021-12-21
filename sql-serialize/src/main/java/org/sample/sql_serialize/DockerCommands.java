package org.sample.sql_serialize;

import xyz.cofe.fn.Tuple2;
import xyz.cofe.io.fn.IOFun;
import xyz.cofe.text.Text;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

public class DockerCommands {
    private volatile Map<String,String> dockerCommands;
    public Map<String,String> getDockerCommands(){
        if( dockerCommands!=null )return dockerCommands;
        synchronized (this){
            if( dockerCommands!=null )return dockerCommands;
            Map<String,String> m = new LinkedHashMap<>();

            var url = PrepareDB.class.getResource("/docker-sample.sh");
            try {
                var txt = IOFun.readText(url,"utf-8");
                String cmd_id = null;
                StringBuilder sb = new StringBuilder();

                Pattern idPtrn = Pattern.compile("^\\s*id\\s*:\\s*(.+)");
                for( var line : Text.splitNewLines(txt) ){
                    if( line.trim().startsWith("#") ){
                        line = Text.trimStart(line.trim(),"#");
                        var m_id = idPtrn.matcher(line);
                        if( m_id.matches() ){
                            var new_cmd_id = m_id.group(1);
                            if( !new_cmd_id.equals(cmd_id) ){
                                if( sb.length()>0 && cmd_id!=null ){
                                    m.put(cmd_id, sb.toString().trim() );
                                }
                                cmd_id = new_cmd_id;
                                sb.setLength(0);
                            }
                        }
                    }else{
                        if( line.trim().length()>0 ) {
                            sb.append(line).append("\n");
                        }
                    }
                }

                //noinspection ConstantConditions
                if( sb.length()>0 && cmd_id!=null ){
                    m.put(cmd_id, sb.toString().trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            dockerCommands = Collections.unmodifiableMap(m);
            return m;
        }
    }

    public static class ExecResult {
        public List<String> cmd;
        public String stdout;
        public String stderr;
        public int exitCode;
        public IOException err;
        public String toString(){
            StringBuilder sb = new StringBuilder();
            if( cmd!=null ){
                sb.append("command: ");
                int i = -1;
                for( var c : cmd ){
                    i++;
                    if( i>0 )sb.append(" ");
                    sb.append(c);
                }
                sb.append("\n");
            }

            sb.append("exitCode: ").append(exitCode).append("\n");

            if( stdout!=null && stdout.length()>0 ){
                sb.append("stdout:").append("\n");
                sb.append(Text.indent("  ",stdout));
            }

            if( stderr!=null && stderr.length()>0 ){
                sb.append("stderr:").append("\n");
                sb.append(Text.indent("  ",stderr));
            }

            return sb.toString();
        }
    }
    public ExecResult exec( String cmd, long timeout, Charset cs ){
        if( cs==null )cs = Charset.defaultCharset();

        ExecResult res = new ExecResult();

        ProcessBuilder pb = new ProcessBuilder();
        pb.command("bash", "-c", cmd);
        res.cmd = pb.command();

        try {
            Process prc = pb.start();
            long t0 = System.currentTimeMillis();
            while (prc.isAlive()){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long td = System.currentTimeMillis() - t0;
                if( td>timeout && timeout>0 ){
                    prc.destroyForcibly();
                }
            }
            res.exitCode = prc.exitValue();
            var stdout = prc.getInputStream();
            var stderr = prc.getErrorStream();
            res.stdout = IOFun.readText(stdout,cs);
            res.stderr = IOFun.readText(stderr,cs);
            res.err = null;
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            res.exitCode = -1;
            res.err = e;
            res.stderr = "";
            res.stdout = "";
            return res;
        }
    }
    public ExecResult exec( String cmd, long timeout ){
        return exec(cmd, timeout, null);
    }

    public ExecResult createContainer(){
        String cmd = getDockerCommands().get("create");
        if( cmd==null )throw new IllegalStateException("command create not found");

        return exec(cmd,1000L*20L);
    }

    public static class Container {
        public String id;
        public String name;
        public String statusRaw;
        public String ports;
        public boolean isRunning(){
            var status = statusRaw;
            if( status!=null && status.startsWith("Up") )return true;
            return false;
        }
        public boolean isExited(){
            var status = statusRaw;
            if( status!=null && status.startsWith("Exited") )return true;
            return false;
        }
    }
    public Tuple2<ExecResult,List<Container>> listContainers(){
        String cmd = getDockerCommands().get("list");
        if( cmd==null )throw new IllegalStateException("command list not found");

        var res = exec(cmd, 1000L*5L);
        if( res.exitCode!=0 ){
            return Tuple2.of(res,List.of());
        }

        var list = new ArrayList<Container>();
        String[] lines = Text.splitNewLines(res.stdout);

        Container cnt = null;
        Pattern idPtrn = Pattern.compile("^\\s*id\\s*:\\s*(.+)");
        Pattern namePtrn = Pattern.compile("^\\s*name\\s*:\\s*(.+)");
        Pattern statusPtrn = Pattern.compile("^\\s*status\\s*:\\s*(.+)");
        Pattern portPtrn = Pattern.compile("^\\s*port\\s*:\\s*(.+)");
        for( var line: lines ){
            var m_id = idPtrn.matcher(line);
            var m_name = namePtrn.matcher(line);
            var m_status = statusPtrn.matcher(line);
            var m_port = portPtrn.matcher(line);
            if( m_id.matches() ){
                if( cnt!=null ){
                    list.add(cnt);
                }
                cnt = new Container();
                cnt.id = m_id.group(1);
            }else if( m_name.matches() ){
                if( cnt!=null )cnt.name = m_name.group(1);
            }else if( m_status.matches() ){
                if( cnt!=null )cnt.statusRaw = m_status.group(1);
            }else if( m_port.matches() ){
                if( cnt!=null )cnt.ports = m_port.group(1);
            }
        }
        if( cnt!=null && !list.contains(cnt) && cnt.id!=null ){
            list.add(cnt);
        }

        return Tuple2.of(res,list);
    }

    public ExecResult stopContainer(){
        String cmd = getDockerCommands().get("stop");
        if( cmd==null )throw new IllegalStateException("command stop not found");

        return exec(cmd,1000L*20L);
    }

    public ExecResult startContainer(){
        String cmd = getDockerCommands().get("start");
        if( cmd==null )throw new IllegalStateException("command start not found");

        return exec(cmd,1000L*20L);
    }

    public ExecResult deleteContainer(){
        String cmd = getDockerCommands().get("delete");
        if( cmd==null )throw new IllegalStateException("command delete not found");

        return exec(cmd,1000L*20L);
    }
}
