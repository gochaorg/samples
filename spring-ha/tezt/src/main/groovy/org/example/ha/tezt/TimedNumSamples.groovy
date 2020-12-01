package org.example.ha.tezt

import java.util.concurrent.TimeUnit
import java.util.function.Supplier

/**
 * Числа расположенные на временной шкале
 */
class TimedNumSamples extends TimedSamples<Number> implements SampleStatCompute, ReadWriteLocking, NumSamples {
    TimedNumSamples(Supplier<Long> now, TimeUnit timeUnit, long window) {
        super(now, timeUnit, window)
    }

    TimedNumSamples(Supplier<Long> now, TimeUnit timeUnit) {
        super(now, timeUnit)
    }

    /**
     * Создание временной шкалы, наносекунды
     * @param window размер окна в наносекундах
     * @return временная шкала
     */
    static TimedNumSamples nsec( long window ){
        new TimedNumSamples({ System.nanoTime() } as Supplier<Long>, TimeUnit.NANOSECONDS, window )
    }

    /**
     * Создание временной шкалы, миллисекунды
     * @param window размер окна в миллисекундах
     * @return временная шкала
     */
    static TimedNumSamples msec( long window ){
        new TimedNumSamples({ System.currentTimeMillis() } as Supplier<Long>, TimeUnit.MILLISECONDS, window )
    }

    /**
     * Создание временной шкалы, секунды
     * @param window размер окна в секундах
     * @return временная шкала
     */
    static TimedNumSamples sec( long window ){
        new TimedNumSamples({ System.currentTimeMillis()/ 1000L } as Supplier<Long>, TimeUnit.SECONDS, window )
    }

    @Override
    TimedNumSamples clone() {
        TimedNumSamples cloned = new TimedNumSamples(now,timeUnit,window)
        readLock({
            cloned.values.putAll( values )
        } as Runnable)
        return cloned
    }

    @Override
    protected void add0(Number value) {
        values.merge( next(), value, (a,b)->a+b );
        if( window>0 && size()>0 ){
            long maxw = now.get() as long
            range( r -> r.start().to(maxw-window,true).delete())
        }
    }
}
