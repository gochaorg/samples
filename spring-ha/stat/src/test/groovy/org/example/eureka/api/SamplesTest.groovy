package org.example.eureka.api

import org.example.stat.Samples
import org.example.stat.SequenceNumSamples
import org.example.stat.TimedNumSamples
import org.junit.jupiter.api.Test

class SamplesTest {
    @Test
    void test01(){
        def samples = new SequenceNumSamples()
        Random rnd = new Random()
        for( int i=0; i<100; i++ ){
            //samples.add(rnd.nextInt(100))
            samples.add(i)
        }

        println "$samples.size $samples.minKey $samples.maxKey"

        println samples.range( rng -> rng.from(0).until(10).values )

        samples.range( rng -> rng.from(0).until(10).delete() )
        println "$samples.size $samples.minKey $samples.maxKey"

        println "stat"
        println samples.stat


        println "merged stat"
        samples.merge( samples.clone() )
        println samples.stat
    }

    @Test
    void test02(){
        def samples = Samples.<Integer>sequence(30)
        Random rnd = new Random()
        for( int i=0; i<50; i++ ){
            samples.add(i)
            println "$samples.size $samples.minKey $samples.maxKey"
        }
    }

    @Test
    void test03(){
        def samples = TimedNumSamples.msec(1000)

        long t0 = System.currentTimeMillis()
        Random rnd = new Random()

        long echo = 0
        while (true) {
            long t1 = System.currentTimeMillis()
            if( (t1-t0)>5000L )break

            samples.add(rnd.nextInt(100))
            Thread.sleep(10)

            if( (t1-echo)>500 ) {
                echo = t1
                def minMax = samples.minMaxKey.get()
                println("duration window ${minMax.max - minMax.min} with ${samples.size} items")
                println(
                    samples.stat.properties.collect {
                        "  $it.key = $it.value"
                    }.join('\n')
                )
            }
        }
    }
}
