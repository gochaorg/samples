package xyz.cofe.stat.samples;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class TimedSamples<A> extends Samples<A> {
    public TimedSamples(Supplier<Long> now, TimeUnit timeUnit, long window){
        if( now==null )throw new IllegalArgumentException("now==null");
        if( timeUnit==null )throw new IllegalArgumentException("timeUnit==null");

        this.now = now;
        this.timeUnit = timeUnit;
        this.window = window;
    }

    public TimedSamples(Supplier<Long> now, TimeUnit timeUnit){
        this(now,timeUnit,0);
    }

    private volatile long window;
    public long getWindow(){ return this.window; }
    public void setWindow(long value){ this.window = value; }

    public final Supplier<Long> now;
    public final TimeUnit timeUnit;

    @Override
    public Long next() {
        return now.get();
    }

    @Override
    protected void add0(A value) {
        super.add0(value);
        if( window>0 && size()>0 ){
            long maxw = now.get();
            range.modify( r -> r.start().to(maxw-window,true).delete() );
        }
    }
}
