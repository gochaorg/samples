package org.example.ha.srvc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/summ")
public class Summa {
    @Value("${ha-test.instanceId:undef}")
    private String instanceId;

    @Value("${ha-test.sleepTime:0}")
    private volatile long sleepTime = 0;

    @GetMapping("/sleepTime")
    public long getSleepTime(){
        return sleepTime;
    }

    @PutMapping("/sleepTime")
    public long setSleepTime( @RequestBody long value ){
        sleepTime = value;
        return sleepTime;
    }

    @RequestMapping("/sq")
    public Map<String,Object> squareRoot(
        @RequestParam("value") double d,
        @RequestParam(value = "i", defaultValue = "3") int iter
    ){
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("value", d);
        m.put("instanceId",instanceId);

        if( iter<=0 )return m;
        double r = d;
        double f = 1;
        while( iter>0 ){
            iter--;
            r = d * f;
            double c = r * r;
            if( c>d ){
                f = f * 0.9;
            }else if( c<d ){
                f = f * 1.1;
            }
        }
        System.out.println("[HA:"+instanceId+"] squareRoot "+d+" "+iter+" r="+r);
        m.put("value", r);

        long sleeping = sleepTime;
        if( sleeping>0 ){
            System.out.println("sleep "+sleeping);
            try{
                Thread.sleep(sleeping);
            } catch( InterruptedException e ) {
                e.printStackTrace();
            }
        }

        return m;
    }
}
