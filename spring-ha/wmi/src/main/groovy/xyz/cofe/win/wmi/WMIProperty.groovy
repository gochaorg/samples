package xyz.cofe.win.wmi

/**
 * Свойство WMI Объекта
 */
class WMIProperty {
    String name
    WMIType type
    boolean array
    boolean local
    def origin
    def value

    /**
     * Получение списка свойств объекта wmi
     * @param wmiObject Объект wmi
     * @return свойства
     */
    static List<WMIProperty> propertiesOf( wmiObject ){
        if( wmiObject==null )throw new IllegalArgumentException("wmiObject == null")
        List<WMIProperty> res = new ArrayList<>()
        for( prop in wmiObject.Properties_ ){
            WMIProperty wp = new WMIProperty()
            wp.name = prop.Name
            wp.type = WMIType.valueOf( prop.CIMType as Number )
            wp.array = prop.IsArray
            wp.local = prop.IsLocal
            wp.origin = prop.Origin
            wp.value = prop.Value
            res.add(wp)
        }
        return res
    }

    @Override
    public String toString() {
        return "WMIProperty{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", array=" + array +
                ", local=" + local +
                ", origin=" + origin +
                ", value=" + value +
                '}';
    }
}
