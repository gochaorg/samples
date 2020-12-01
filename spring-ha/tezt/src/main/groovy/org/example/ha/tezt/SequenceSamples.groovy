package org.example.ha.tezt

import groovy.transform.TypeChecked

import java.util.concurrent.atomic.AtomicLong

@TypeChecked
class SequenceSamples<A> extends Samples<A> {
    //region window : long - размер окна
    private volatile long window
    public long getWindow(){ this.@window }
    public void setWindow(long value){ this.@window = value }
    //endregion
    //region реализация добавления значения
    protected AtomicLong seq = new AtomicLong()

    @Override
    Long next() {
        return seq.getAndIncrement() as Long
    }

    @Override
    protected void add0(A value) {
        super.add0(value)
        if( window>0 ){
            if( size()>window ){
                long maxw = getMaxKey().get() as long
                range( r -> r.start().to(maxw-window,true).delete())
            }
        }
    }
    //endregion
    //region клонирование
    @Override
    SequenceSamples<A> clone() {
        SequenceSamples<A> cloned = new SequenceSamples<A>()
        cloned.window = window;
        readLock({
            cloned.values.putAll(values)
        } as Runnable)
        return cloned;
    }
    //endregion
}
