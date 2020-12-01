package org.example.ha.tezt

class InputReadTransfer extends Thread {
    volatile Reader reader
    volatile Writer writer
    volatile boolean checkReady = false
    protected volatile int bufferSize = 80
    volatile boolean useYield = true

    InputReadTransfer(Reader reader, Writer writer1){
        this.reader = reader
        this.writer = writer1
    }
    InputReadTransfer(Reader reader, Writer writer1, int buffSize){
        this.reader = reader
        this.writer = writer1

        if( buffSize<1 )throw new IllegalArgumentException("buffSize<1");
        bufferSize = buffSize
    }

    @Override
    void run() {
        Reader from = reader
        Writer to = writer

        char[] buff = new char[bufferSize]

        while (true){
            if( currentThread().interrupted )break

            if( from==null )break
            if( to==null )break

            if( checkReady ){
                if( !from.ready() ){
                    if( useYield ){
                        yield()
                    }
                    continue
                }
            }

            try {
                int readed = from.read(buff)
                if (readed > 0) {
                    to.write(buff, 0, readed)
                } else if (readed < 0) {
                    break
                }
            } catch( InterruptedException ignored ){
                break
            }
        }
    }
}
