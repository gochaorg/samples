package org.example.stat


import java.util.concurrent.TimeUnit
import java.util.function.Supplier

/**
 * Значения расположенные по времени
 * @param <A> тип значений
 */
class TimedSamples<A> extends Samples<A> {
    private TimedSamples(){}
    public TimedSamples(Supplier<Long> now, TimeUnit timeUnit, long window=0){
        if( now==null )throw new IllegalArgumentException("now==null");
        if( timeUnit==null )throw new IllegalArgumentException("timeUnit==null");

        this.now = now;
        this.timeUnit = timeUnit;
        this.@window = window;
    }

    //region window : long
    private volatile long window
    public long getWindow(){ this.@window }
    public void setWindow(long value){ this.@window = value }
    //endregion

    @SuppressWarnings('GrFinalVariableAccess')
    public final Supplier<Long> now;

    @SuppressWarnings('GrFinalVariableAccess')
    public final TimeUnit timeUnit;

    @Override
    Long next() {
        return now.get()
    }

    @Override
    protected void add0(A value) {
        super.add0(value)
        if( window>0 && size()>0 ){
            long maxw = now.get() as long
            range( r -> r.start().to(maxw-window,true).delete())
        }
    }

    @Override
    TimedSamples<A> clone() {
        TimedSamples<A> cloned = new TimedSamples<A>()
        cloned.window = window
        readLock({
            cloned.values.putAll( values )
        } as Runnable)
        return cloned
    }
}
