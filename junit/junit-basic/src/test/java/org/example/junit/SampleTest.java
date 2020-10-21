package org.example.junit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.Random;

@DisplayName("A special test case")
public class SampleTest {
    public Random random = new Random();

    public int summV1(int a, int b ){
        int r = random.nextInt(10);
        if( r!=0 ){
            return a+b;
        }else{
            return a+b+b;
        }
    }

    public int summV2(int a, int b ){
        return a+b;
    }

    @Tag(T.UnStable)
    @DisplayName("summ with random bug")
    @Test
    public void summV1(){
        System.out.println("summV1()");
        System.out.println("========");

        for( int i=0; i<20; i++ ) {
            System.out.println("cycle "+i);

            int a = random.nextInt(10) + 1;
            int b = random.nextInt(10) + 1;
            int expect = a + b;
            int result = summV1(a, b);
            boolean matched = expect == result;

            String message = "summ( " + a + ", " + b + " ) => { expect=" + expect + " result=" + result + " }";
            System.out.println(message);
            assertTrue(matched, message);
        }
    }

    @Tag(T.Stable)
    @Tag(T.Atomic)
    @Test
    public void summV2(){
        System.out.println("summV2()");
        System.out.println("========");

        for( int i=0; i<20; i++ ) {
            System.out.println("cycle "+i);

            int a = random.nextInt(10) + 1;
            int b = random.nextInt(10) + 1;
            int expect = a + b;
            int result = summV2(a, b);
            boolean matched = expect == result;

            String message = "summ( " + a + ", " + b + " ) => { expect=" + expect + " result=" + result + " }";
            System.out.println(message);
            assertTrue(matched, message);
        }
    }
}
