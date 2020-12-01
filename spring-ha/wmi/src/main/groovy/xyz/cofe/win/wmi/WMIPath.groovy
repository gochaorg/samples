package xyz.cofe.win.wmi

import groovy.transform.ToString

/**
 * WMI Путь к объекту
 */
@ToString(includeNames = true)
class WMIPath {
    String path
    String wmiClass
    boolean isWmiClass
    boolean singleton
    String namespace
    String server
    String displayName
    String authority

    static WMIPath pathOf( wmiObject ){
        if( wmiObject==null )throw new IllegalArgumentException("wmiObject == null")
        WMIPath wp = new WMIPath()
        wp.path = wmiObject.Path_.Path as String
        wp.wmiClass = wmiObject.Path_.Class as String
        wp.isWmiClass = wmiObject.Path_.IsClass as boolean
        wp.singleton = wmiObject.Path_.IsSingleton as boolean
        wp.namespace = wmiObject.Path_.Namespace as String
        wp.server = wmiObject.Path_.Server as String
        wp.displayName = wmiObject.Path_.DisplayName
        wp.authority = wmiObject.Path_.Authority
        return wp
    }

    @Override
    public String toString() {
        return "WMIPath{" +
                "path='" + path + '\'' +
                ", wmiClass='" + wmiClass + '\'' +
                ", isWmiClass=" + isWmiClass +
                ", singleton=" + singleton +
                ", namespace='" + namespace + '\'' +
                ", server='" + server + '\'' +
                ", displayName='" + displayName + '\'' +
                ", authority='" + authority + '\'' +
                '}';
    }
}
