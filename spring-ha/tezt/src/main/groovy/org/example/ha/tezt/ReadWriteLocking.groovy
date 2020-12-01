package org.example.ha.tezt

import java.util.function.Supplier

interface ReadWriteLocking {
    public <R> R writeLock(Supplier<R> code );
    public void writeLock( Runnable code )

    public <R> R readLock( Supplier<R> code )
    public void readLock( Runnable code )
}