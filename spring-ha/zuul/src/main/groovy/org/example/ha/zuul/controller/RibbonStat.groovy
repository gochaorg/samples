package org.example.ha.zuul.controller

import com.netflix.client.config.IClientConfig
import com.netflix.loadbalancer.ILoadBalancer
import com.netflix.loadbalancer.LoadBalancerStats
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ribbon/stat")
class RibbonStat {
    @Autowired
    private LoadBalancerClient lbClient;

//    @Autowired
//    private ApplicationContext appContext;
//
//    @Autowired
//    private IClientConfig ribbonClientConfig;

    @GetMapping("")
    public Map<String,Object> stat(){
        println lbClient
        println appContext

        def map = new LinkedHashMap()
        map
    }
}
