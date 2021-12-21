package org.example.grfn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Main {
    @GetMapping("/")
    public String hello(){
        return "hello";
    }

    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }
}
