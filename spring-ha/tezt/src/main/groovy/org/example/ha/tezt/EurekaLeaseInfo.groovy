package org.example.ha.tezt

import groovy.transform.ToString

@ToString(includeNames = true)
class EurekaLeaseInfo {
    private def json
    EurekaLeaseInfo( json ){
        this.json = json
    }

    int getRenewalIntervalInSecs(){ json.renewalIntervalInSecs as int }
    int getDurationInSecs(){ json.durationInSecs as int }
    long getRegistrationTimestamp(){ json.registrationTimestamp as long }
    long getLastRenewalTimestamp(){ json.lastRenewalTimestamp as long }
    long getEvictionTimestamp(){ json.evictionTimestamp as long }
    long getServiceUpTimestamp(){ json.serviceUpTimestamp as long }
}
