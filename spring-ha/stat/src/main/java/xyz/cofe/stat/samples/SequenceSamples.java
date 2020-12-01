package xyz.cofe.stat.samples;

import java.util.concurrent.atomic.AtomicLong;

public class SequenceSamples<A> extends Samples<A> {
    private volatile long window;
    public long getWindow(){ return this.window; }
    public void setWindow(long value){ this.window = value; }

    protected final AtomicLong seq = new AtomicLong();

    @Override
    public Long next() {
        return seq.getAndIncrement();
    }

    @Override
    protected void add0(A value) {
        super.add0(value);
        if( window>0 ){
            if( size()>window ){
                long maxw = getMaxKey().get();
                range.modify( r -> r.start().to(maxw-window, true).delete() );
            }
        }
    }
}
