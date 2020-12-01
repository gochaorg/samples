package org.example.stat

/**
 * Числовые значения
 */
trait NumSamples implements ReadWriteLocking {
    abstract NavigableMap<Long,Number> getValues();
    void merge( NumSamples samples ){
        if( samples==null )throw new IllegalArgumentException("samples==null")
        writeLock({
            samples.readLock({
                samples.getValues().forEach({k,v ->
                    getValues().merge( k,v, {a,b -> a+b})
                })
            } as Runnable)
        } as Runnable)
    }
}
