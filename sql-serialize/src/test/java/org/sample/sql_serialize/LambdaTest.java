package org.sample.sql_serialize;

import org.junit.jupiter.api.Test;
import xyz.cofe.fn.Fn0;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LambdaTest {
    private static SerializedLambda getSerializedLambda(Serializable lambda) {
        for (Class<?> cl = lambda.getClass(); cl != null; cl = cl.getSuperclass()) {
            try {
                Method m = cl.getDeclaredMethod("writeReplace");
                m.setAccessible(true);
                Object replacement = m.invoke(lambda);
                if (!(replacement instanceof SerializedLambda)) {
                    break;
                }
                return (SerializedLambda) replacement;
            } catch (NoSuchMethodException e) {
                // skip, continue
            } catch (IllegalAccessException | InvocationTargetException | SecurityException e) {
                throw new IllegalStateException("Failed to call writeReplace", e);
            }
        }
        return null;
    }

    public <R> Class<?> supInfo(Fn0<R> info){
        var c = info.getClass();
        try {
            var m = c.getMethod("apply");
            System.out.println("m = "+m);
            System.out.println("r = "+m.getReturnType());

            var sl = getSerializedLambda(info);
            if( sl!=null ){
                var cl = sl.getImplClass();
                var mn = sl.getImplMethodName();
                var ms = sl.getImplMethodSignature();
                System.out.println("cl="+cl+" mn="+mn+" ms="+ms);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void testSup(){
        supInfo(()->"aaa");
    }
}
