package xyz.cofe.win.wmi

/**
 * Тип данных WMI
 * https://msdn.microsoft.com/ru-ru/library/aa393974(v=vs.85).aspx
 */
enum WMIType {
    SINT8,
    UINT8,
    SINT16,
    UINT16,
    SINT32,
    UINT32,
    SINT64,
    UINT64,
    REAL32,
    REAL64,
    STRING,
    BOOLEAN,
    OBJECT,
    DATETIME,
    REFERENCE,
    CHAR16,
    UNDEFINED;

    static WMIType valueOf( Number n ){
        if( n==null )return UNDEFINED
        switch (n){
            case 2: return SINT16
            case 3: return SINT32
            case 4: return REAL32
            case 5: return REAL64
            case 8: return STRING
            case 11: return BOOLEAN
            case 13: return OBJECT
            case 16: return SINT8
            case 17: return UINT8
            case 18: return UINT16
            case 19: return UINT32
            case 20: return SINT64
            case 21: return UINT64
            case 101: return DATETIME
            case 102: return REFERENCE
            case 103: return CHAR16
            default: return UNDEFINED
        }
    }
}