package org.example.ha.tezt

/**
 * Последовательность чисел
 */
class SequenceNumSamples extends SequenceSamples<Number> implements SampleStatCompute, ReadWriteLocking, NumSamples {
    //region клонирование
    @Override
    SequenceNumSamples clone() {
        SequenceNumSamples cloned = new SequenceNumSamples()
        cloned.window = window;
        readLock({
            cloned.values.putAll(values)
        } as Runnable)
        return cloned;
    }
    //endregion

    @Override
    protected void add0(Number value) {
        values.merge( next(), value, (a,b)->a+b );
        if( window>0 ){
            if( size()>window ){
                long maxw = getMaxKey().get() as long
                range( r -> r.start().to(maxw-window,true).delete())
            }
        }
    }
}
