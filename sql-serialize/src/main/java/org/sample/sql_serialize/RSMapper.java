package org.sample.sql_serialize;

import xyz.cofe.fn.Consumer1;
import xyz.cofe.fn.Consumer3;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RSMapper<T> {
    public RSMapper(Class<T> cls){
        if( cls==null )throw new IllegalArgumentException( "cls==null" );
        var ann = cls.getAnnotation(sqlmap.class);
        for( var fld : cls.getFields() ){
            if( (fld.getModifiers() & Modifier.STATIC)>0 )continue;

            var fann = fld.getAnnotation(sqlmap.class);
            if( fann!=null && fann.ignore() )continue;
            if( ann!=null && List.of(ann.ignoreFields()).contains(fld.getName()) )continue;

            var fldt = fld.getType();
            dataMappers.add( (trgt, rs, ferr) -> {
                String name = fann!=null && fann.value().length()>0 ? fann.value() : fld.getName();
                try {
                    Object dataValue = rs.getObject(name);
                    if( dataValue!=null ){
                        if( dataValue instanceof Number ){
                            var num = (Number)dataValue;
                            try {
                                if( fldt==byte.class ) fld.set(trgt, num.byteValue());
                                else if( fldt==boolean.class )fld.set(trgt, num.intValue() != 0);
                                else if( fldt==short.class ) fld.set(trgt, num.shortValue() );
                                else if( fldt==int.class ) fld.set(trgt, num.intValue() );
                                else if( fldt==long.class ) fld.set(trgt, num.longValue() );
                                else if( fldt==float.class ) fld.set(trgt, num.floatValue() );
                                else if( fldt==double.class ) fld.set(trgt, num.doubleValue() );
                                else if( fldt==Byte.class ) fld.set(trgt, num.byteValue() );
                                else if( fldt==Short.class ) fld.set(trgt, num.shortValue() );
                                else if( fldt==Integer.class ) fld.set(trgt, num.intValue() );
                                else if( fldt==Long.class ) fld.set(trgt, num.longValue() );
                                else if( fldt==Float.class ) fld.set(trgt, num.floatValue() );
                                else if( fldt==Double.class ) fld.set(trgt, num.doubleValue() );
                                else if( fldt== BigInteger.class ){
                                    if( num instanceof BigInteger ) fld.set(trgt, (BigInteger)num );
                                    else if( num instanceof BigDecimal) fld.set(trgt, ((BigDecimal)num).toBigInteger() );
                                    else fld.set(trgt, BigInteger.valueOf(num.longValue()));
                                }
                                else if( fldt==BigDecimal.class ){
                                    if( num instanceof BigInteger ) fld.set(trgt, new BigDecimal((BigInteger) num) );
                                    else if( num instanceof BigDecimal) fld.set(trgt, (BigDecimal)num );
                                    else fld.set(trgt, BigDecimal.valueOf(num.doubleValue()));
                                }else {
                                    ferr.accept(new IllegalStateException("can't map "+name+":"+dataValue.getClass()+" to field "+fld.getName()));
                                }
                            } catch (IllegalAccessException e) {
                                ferr.accept(e);
                            }
                        } else {
                            try {
                                if( fldt.isAssignableFrom(dataValue.getClass()) ) {
                                    if( dataValue instanceof String ) {
                                        if( fann!=null && fann.trim() )dataValue = ((String)dataValue).trim();
                                    }
                                    fld.set(trgt, dataValue);
                                }else if( (fldt==boolean.class || fldt==Boolean.class) && dataValue instanceof Boolean ){
                                    fld.set(trgt, (Boolean)dataValue);
                                }else{
                                    ferr.accept(new IllegalStateException("can't map "+name+":"+dataValue.getClass()+" to field "+fld.getName()));
                                }
                            } catch (IllegalAccessException e) {
                                ferr.accept(e);
                            }
                        }
                    }
                } catch (SQLException e) {
                    ferr.accept(e);
                }
            });
        }
    }

    private final List<Consumer3<T,ResultSet, Consumer1<Throwable>>> dataMappers = new ArrayList<>();
    public void map(T target, ResultSet rs) throws SQLException {
        if( target==null )throw new IllegalArgumentException( "target==null" );
        if( rs==null )throw new IllegalArgumentException( "rs==null" );
        var err = new Throwable[]{ null };
        for( var fld : dataMappers){
            //noinspection ConstantConditions
            err[0] = null;
            fld.accept(target,rs, e -> err[0]=e );
            if( err[0]!=null ){
                if( err[0] instanceof SQLException ) {
                    throw (SQLException) err[0];
                }else{
                    throw new SQLException(err[0]);
                }
            }
        }
    }
}
