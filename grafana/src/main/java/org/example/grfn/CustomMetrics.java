package org.example.grfn;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomMetrics {
    public final Counter counter1;
    public final Counter counter2a;
    public final Timer timer1;

    // ссылка на реестр метрик
    public final MeterRegistry registry;

    // кеш метрик
    public final Map<String,Counter> dynCounterByName = new ConcurrentHashMap<>();

    /**
     * Создание или получение ранее созданной метрики
     * @param name имя метрики
     * @return метрика
     */
    public Counter counter(String name){
        if( name==null )throw new IllegalArgumentException( "name==null" );
        // создание метрики, если еще не была создана либо возвращение ранее созданной метрики
        //    можно создать с метками (tags)
        return dynCounterByName.computeIfAbsent(name, registry::counter);
    }

    public CustomMetrics( MeterRegistry registry, AppConfig conf ){
        this.registry = registry;

        // Простой монотонный счетчик
        counter1 = registry.counter("counter1");

        // Это некий таймер
        // Это равно значно Tags.of("BusinessMethod","C101")
        timer1 = registry.timer("compute1", "BusinessMethod", "C101" );

        // некие общие теги
        // эти теги не будут распространены на другие счетчики созданные до этого вызова,
        // надо делать вручную на ранее созданные
        String appName = conf.getAppName();
        registry.config().commonTags(
            Tags.of("appName", appName)
        );

        // вот так нельзя, просто Tag для counter
        //   counter2a = registry.counter("counter3", "tag_b", "x");
        //
        // а надо так
        //   counter2a = registry.counter("counter2", Tags.of("tag2a","2a"));
        counter2a = registry.counter("counter2", Tags.of("tag2a","2a"));
    }
}
