package org.example.eureka.api

import groovy.transform.ToString

@ToString(includeNames = true)
class EurekaAppInstancePort {
    private def json
    EurekaAppInstancePort( json ){
        if( json==null ){
            json = [('$'):0, ('@enabled'):'false']
        }
        this.@json = json
    }

    Integer getPort(){ json['$'] != null ? json['$'] as Integer : null }
    Boolean getEnabled(){ json['@enabled'] != null ? json['@enabled'] as Boolean : null }
}
