import groovy.json.JsonSlurper
import okhttp3.*
import org.example.stat.TimedNumSamples
import xyz.cofe.text.FullDecFormat

import java.util.concurrent.ConcurrentHashMap

OkHttpClient client = new OkHttpClient();
Request req = new Request.Builder().url("http://localhost:28010/api/sa/summ/sq?value=25&i=1001").build()

class InstanceStat {
    int successCount = 0
    long successDuration = 0

    synchronized void reset(){}
    synchronized void success( long duration ){
        successCount++
        successDuration += duration
        times.add(duration)
    }

    TimedNumSamples times = TimedNumSamples.msec(10000)
}
instances = new ConcurrentHashMap<String,InstanceStat>()

TimedNumSamples failsWindow = TimedNumSamples.msec(10000)
TimedNumSamples totalWindow = TimedNumSamples.msec(10000)
long totalCount = 0
long totalTimes = 0

long lastEcho = 0
long tStart = System.currentTimeMillis()

FullDecFormat durFmt = new FullDecFormat("###.###")

while (true) {
    long tDuration = System.currentTimeMillis() - tStart
    if( tDuration>60000*5 )break

    long startReqTime = System.currentTimeMillis()
    try (Response response = client.newCall(req).execute()) {
        long endReqTime = System.currentTimeMillis()
        long duration = endReqTime - startReqTime

        totalCount++
        totalTimes += duration
        totalWindow.add(duration)

        if (response.code() == 200) {
            String body = response.body().string();
            def json = new JsonSlurper().parseText(body)
            InstanceStat inst = instances.computeIfAbsent(
                json.instanceId as String, k -> new InstanceStat()
            )
            inst.success(duration)
        } else {
            failsWindow.add(duration)
        }
    }

    long tEchoPause = System.currentTimeMillis() - lastEcho
    if( tEchoPause>3000 ){
        lastEcho = System.currentTimeMillis()
        println "t=${tDuration}ms stat"
        println "  total count=${totalCount}, duration=${totalTimes}ms"

        def totalWndSt = totalWindow.stat
        println "    total.wnd dur=${totalWndSt.sum}sum.ms cnt=${totalWndSt.count}"

        def failWndSt = failsWindow.stat
        println "    fail.wnd dur=${failWndSt.avg}avg.ms cnt=${failWndSt.count}"

        double sum = 0
        sum = instances.collect { i,s -> s.times.stat.sum }.sum()

        instances.each {instId, instSt1 ->
            def instSt = instSt1.times.stat
            def pct = totalWndSt.sum!=0 ? instSt.sum / sum : -1.0
            println "    inst $instId" +
                " pct=${durFmt.format(pct*100)}%" +
                " cnt=${durFmt.format(instSt.count)}" +
                " 50%=${durFmt.format(instSt.centile50)}ms" +
                " avg=${durFmt.format(instSt.avg)}ms" +
                " dev=${durFmt.format(instSt.stdev)}ms" +
                " max=${durFmt.format(instSt.max)}ms" +
                " sum=${instSt.sum}ms"
        }
    }
}
