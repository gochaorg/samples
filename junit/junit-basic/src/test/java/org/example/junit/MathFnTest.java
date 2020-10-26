package org.example.junit;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class MathFnTest {
    @Tag(T.Stable)
    @Test
    public void test01(){
        for( int i=-10; i<20; i++ )
        System.out.println("fib("+i+")="+MathFn.fib(i));
    }
}
