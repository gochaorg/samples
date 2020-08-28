package xyz.cofe.sample.websock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class WebServMain {
    public static void main(String[] args){
        SpringApplication.run(WebServMain.class,args);
    }

    @GetMapping("")
    public Object index(){
        return "ok";
    }
}
