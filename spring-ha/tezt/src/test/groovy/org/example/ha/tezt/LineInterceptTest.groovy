package org.example.ha.tezt

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import xyz.cofe.text.Text

import java.util.concurrent.atomic.AtomicInteger

class LineInterceptTest {
//    @Test
//    void test000(){
//        def eapi = new EurekaApi()
//        eapi.apps.each { app ->
//            println "-" * 30
//            println app.name
//            app.instances.each { EurekaAppInstance inst ->
//                println inst.instanceId
//                println inst.port
//                println inst
//            }
//        }
//    }

    @Test
    void test01(){
        AtomicInteger matchCount = new AtomicInteger()

        StringWriter sw = new StringWriter()
        LineInterceptor li = new LineInterceptor(sw)
        li.listen(line -> {
            if( line.contains('match string')){
                matchCount.incrementAndGet()
            }
        })

        li.print("some line\n aa match")
        li.println(" string bla")
        li.println("match string\ngogo")
        li.flush()

        println(Text.indent("output> ",sw.toString()))

        println("matchCount:")
        println(matchCount.get())
        Assertions.assertTrue(matchCount.get()==2)
    }
}
