package org.example.ha.zuul.lb;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.WeightedResponseTimeRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyWeightedResponseTimeRule extends WeightedResponseTimeRule {
    private final static Logger log = LoggerFactory.getLogger(MyWeightedResponseTimeRule.class);

    public MyWeightedResponseTimeRule() {
        log.info("constructor");
    }

    public MyWeightedResponseTimeRule(ILoadBalancer lb) {
        super(lb);
        log.info("constructor with balancer");
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
        log.info("setLoadBalancer");
    }
}
