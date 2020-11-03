package org.example.junit;

public class MathFn {
    public static long fib(int len){
        if( len<1 )return 0;
        if( len==1 )return 1;
        if( len==2 )return 2;
        return fib(len-2) + fib(len-1);
    }
}
