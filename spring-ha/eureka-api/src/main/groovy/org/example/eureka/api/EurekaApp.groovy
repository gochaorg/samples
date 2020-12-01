package org.example.eureka.api

import groovy.transform.ToString

@ToString(includeNames = true)
class EurekaApp {
    protected def json
    EurekaApp( json ){
        if( json==null )throw new IllegalArgumentException("json==null");
        this.@json = json
    }

    String getName(){ json.name }

    protected @Lazy List<EurekaAppInstance> instancesValues = {
        def res = []
        this.@json.instance.each { jsn ->
            res.add( new EurekaAppInstance(jsn) )
        }
        res
    }()

    List<EurekaAppInstance> getInstances(){ instancesValues }
}
