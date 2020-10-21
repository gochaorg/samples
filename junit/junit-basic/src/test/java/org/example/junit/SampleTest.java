package org.example.junit;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

@DisplayName("A special test case")
public class SampleTest {
    public Random random = new Random();

    public int summ( int a, int b ){
        int r = random.nextInt(10);
        if( r!=0 ){
            return a+b;
        }else{
            return a+b+b;
        }
    }

    @DisplayName("summ with random bug")
    @Test
    public void summ(){
        System.out.println("summ with random bug");

        int a = random.nextInt(10)+1;
        int b = random.nextInt(10)+1;
        int expect = a + b;
        int result = summ(a,b);
        boolean matched = expect == result;

        String message = "summ( "+a+", "+b+" ) => { expect="+expect+" result="+result+" }";
        System.out.println(message);
        assertTrue( matched, message );
    }
}
