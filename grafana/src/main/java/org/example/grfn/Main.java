package org.example.grfn;

import io.prometheus.client.Counter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@SpringBootApplication
@RestController
public class Main {
    // ссылка bean со счетчиками
    private final CustomMetrics customMetrics;

    public Main(CustomMetrics metrics){
        customMetrics = metrics;
    }

    @GetMapping("/compute/{count}")
    public Object compute( @PathVariable("count") int count ){
        return customMetrics.timer1.record(()->{
            long v = 0;

            customMetrics.timer1.record(()->{
            });

            Random rnd = new Random();
            for( int i=0; i<count; i++ ){
                v += rnd.nextInt(100);
            }
            return v;
        });
    }

    @GetMapping("/")
    public void hello( HttpServletResponse response ) throws IOException {
        customMetrics.counter1.increment();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html; charset=utf-8");

        var out = new HtmlBuilder(response);
        out
            .a("/hello1", "hello1").br()
            .a("/compute/100", "/compute/100").br()
        ;

        out.form("/dyn/counter").blank().body( frm -> {
            frm.text("dynamic counter").br();
            frm.inputText("name");
            frm.submit();
        });

        out.flush();
    }

    @GetMapping("/hello1")
    public String hello1(){
        // увеличение счетчика
        customMetrics.counter2a.increment();
        return "hello";
    }

    @GetMapping("/dyn/counter")
    public String dynamicCounter(@RequestParam("name") String name){
        // увеличение значения метрики
        customMetrics.counter(name).increment();
        return "counter "+name;
    }

    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }
}
