package org.sample.sql_serialize;

import org.junit.jupiter.api.Test;
import xyz.cofe.text.Text;

public class DockerCommandTest {
    //@Test
    public void listCmd(){
        new DockerCommands().getDockerCommands().forEach( (name,cmd) -> {
            System.out.println("name "+name);
            System.out.println(Text.indent("  ", cmd));
        });
    }

    //@Test
    public void createContainer(){
        var dc = new DockerCommands();
        var res = dc.createContainer();
        System.out.println("create "+res);
    }

    //@Test
    public void listContainers(){
        var dc = new DockerCommands();
        var res = dc.listContainers();
        for( var cnt : res.b() ){
            System.out.println("container "+cnt.id+" name="+cnt.name +" status="+cnt.statusRaw);
        }
    }

    //@Test
    public void stopContainers(){
        var dc = new DockerCommands();
        var res = dc.stopContainer();
        System.out.println("stop "+res);
    }

    //@Test
    public void startContainers(){
        var dc = new DockerCommands();
        var res = dc.startContainer();
        System.out.println("start "+res);
    }

    //@Test
    public void deleteContainers(){
        var dc = new DockerCommands();
        var res = dc.deleteContainer();
        System.out.println("delete "+res);
    }
}
