package org.example.ha.tezt

import groovy.transform.ToString

@ToString(includeNames = true)
class EurekaAppInstance {
    private def json
    EurekaAppInstance( json ){
        if( json==null )throw new IllegalArgumentException("json==null")
        this.@json = json
        this.@instanceIdValue = json.instanceId as String
    }

    private def instanceIdValue
    String getInstanceId(){ instanceIdValue }
    String getHostName(){ json.hostName }
    String getApp(){ json.app }
    String getIpAddr(){ json.ipAddr }
    String getStatus(){ json.status }
    String getOverriddenStatus(){ json.overriddenStatus }
    @Lazy EurekaAppInstancePort port = new EurekaAppInstancePort(json.port)
    @Lazy EurekaAppInstancePort securePort = new EurekaAppInstancePort(json.securePort)
    Integer getCountryId(){ json.countryId as Integer }
    String getVipAddress(){ json.vipAddress }
    String getSecureVipAddress(){ json.secureVipAddress }
    boolean isCoordinatingDiscoveryServer(){ json.isCoordinatingDiscoveryServer as boolean }
    Long getLastUpdatedTimestamp(){ json.lastUpdatedTimestamp as Long }
    Long getLastDirtyTimestamp(){ json.lastDirtyTimestamp as Long }
    @Lazy EurekaLeaseInfo leaseInfo = new EurekaLeaseInfo( json.leaseInfo )
    String getHomePageUrl(){ json.homePageUrl }
    String getStatusPageUrl(){ json.statusPageUrl }
    String getHealthCheckUrl(){ json.healthCheckUrl }
}
