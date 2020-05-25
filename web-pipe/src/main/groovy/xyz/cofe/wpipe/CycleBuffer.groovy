package xyz.cofe.wpipe

/**
 * Посредник, цикличный байтовый буфер.
 *
 * <p>
 * В циклическом буфере подразумевается что используют
 * два участника: писатель и читатель.
 *
 * <p>
 * Писатель пишет данные заполняя буфер {@link #buffer}
 * и передвигая указатель {@link #recived}
 *
 * <p>
 * Читатель читает данные из буфера и передвигает укзатель {@link #transferred}
 *
 * <p>
 * Соответственно есть следующие ограничения:
 * <ul>
 *     <li> Указатели приводятся к позиции в байт буфере {@link #buffer}
 *          <br>
 *          <code>bufferPointer = pointer % buffer.length</code>
 *
 *     <li> Указатель читателя {@link #transferred} не должен опережать
 *          указаетль писателя {@link #recived},
 *          <br> кратко <b>{@link #transferred} &lt;= {@link #recived}</b>
 *
 *     <li> Если указатели равны, тогда считается что все данные прочитаны
 *
 *     <li> Указатель писателя не может быть передвинут далее чем
 *          данные которые еще не прочитаны
 * </ul>
 */
class CycleBuffer {
    /**
     * Конструктор
     * @param bufferSize размер буфера в байтах
     */
    CycleBuffer( int bufferSize){
        if( bufferSize<1 )throw new IllegalArgumentException("bufferSize<1");
        this.@buffer = new byte[bufferSize]
        this.@recived = 0L
        this.@transferred = 0L
    }
    //region buffer : byte[] - буфер
    /**
     * Буфер
     */
    protected byte[] buffer

    /**
     * Возвращает буфер
     * @return буфер
     */
    byte[] getBuffer(){ return this.@buffer; }
    //endregion
    //region recived - кол-во полученных байт, указатель writer
    /**
     * Получено
     */
    protected volatile long recived

    /**
     * Возвращает кол-во полученных байт
     * @return кол-во полученных байт
     */
    long getRecived(){ return this.@recived }
    //endregion
    //region transferred : long - кол-во переданных байт, указатель reader
    /**
     * Передано
     */
    protected volatile long transferred

    /**
     * Возвращает кол-во переданных байт
     * @return кол-во переданных байт
     */
    long getTransferred(){
        return this.@transferred
    }
    //endregion
    //region free : int - кол-во свободных байт
    /**
     * Возвращает кол-во свободных байт
     * @return кол-во свободных байт
     */
    synchronized int getFree(){
        if( recived==transferred )return buffer.length;
        if( recived<transferred )throw new IllegalStateException("recived(=$recived) < transferred(=$transferred)");

        int writePtr = recived % buffer.length;
        int readPtr = transferred % buffer.length;
        if( readPtr==writePtr )return 0;
        if( readPtr>writePtr )return readPtr - writePtr;
        return writePtr - readPtr;
    }
    //endregion
    //region write() - Запись байтов
    /**
     * Запись байтов в циклический буфер и смещение указателя писателя ({@link #recived}.
     * Кол-во записываемых байтов не должно превышать кол-ва свободных байт в буфере
     * @param bytes записываемые байты
     * @param off смещение
     * @param len кол-во записываемых байтов
     */
    synchronized void write( byte[] bytes, int off, int len ){
        if( bytes==null )throw new IllegalArgumentException("bytes==null");
        if( off<0 )throw new IllegalArgumentException("off<0");
        if( len<0 )throw new IllegalArgumentException("len<0");
        if( off+len>bytes.length )
            throw new IllegalArgumentException("off(=$off)+len(=$len)>bytes.length(=$bytes.length)");

        int freeBytes = free
        if( len>freeBytes )throw new NoFreeSpaceError("try write $len bytes, but avaliable $freeBytes free bytes")

        int writePtr = recived % buffer.length;
        int readPtr = transferred % buffer.length;

        if( writePtr<readPtr ){
            if( (readPtr-writePtr)<len )throw new IllegalStateException("bug")
            System.arraycopy(bytes,off,buffer,writePtr,len)
            recived += len
        }else if( writePtr>readPtr ){
            int freeRight = buffer.length - writePtr;
            int freeLeft = readPtr
            if( (freeLeft+freeRight)<len )throw new IllegalStateException("bug")
            if( freeRight>=len ){
                System.arraycopy(bytes,off,buffer,writePtr,len)
                recived += len
            }else{
                int writeRight = freeRight
                int writeLeft = len-writeRight

                System.arraycopy(bytes,off,buffer,writePtr,writeRight)
                if( writeLeft>0 ){
                    System.arraycopy(bytes, off + freeRight, buffer, 0, writeLeft)
                }else{
                    throw new IllegalStateException("bug")
                }

                recived += len
            }
        }else {
            throw new IllegalStateException("bug")
        }
    }
    //endregion
    //region available : int - кол-во доступных байт для чтения
    /**
     * Возвращает кол-во доступных байт для чтения
     * @return кол-во доступных байт для чтения
     */
    synchronized int getAvailable(){
        if( recived==transferred )return 0;
        if( recived<transferred )throw new IllegalStateException("recived(=$recived) < transferred(=$transferred)");

        int av = recived - transferred
        if( av>buffer.length )throw new IllegalStateException("bug")

        return av
    }
    //endregion
    //region read() - Чтение байтов
    /**
     * Чтение байтов из буфера и смещение указателя читателя ({@link #transferred})
     * @param bytes принимаещий буфер
     * @param off смещение
     * @param len максимальное принимаемое кол-во байт
     * @return кол-во принятых байт, 0 - нет доступных данных
     */
    synchronized int read( byte[] bytes, int off, int len ){
        if( bytes==null )throw new IllegalArgumentException("bytes==null");
        if( off<0 )throw new IllegalArgumentException("off<0");
        if( len<0 )throw new IllegalArgumentException("len<0");
        if( off+len>bytes.length ){
            throw new IllegalArgumentException("off(=$off)+len(=$len)>bytes.length(=$bytes.length)");
        }

        long av = recived - transferred
        if( av<0 )throw new IllegalStateException("bug");
        if( av>buffer.length )throw new IllegalStateException("bug");
        if( av==0 )return 0;
        if( av>Integer.MAX_VALUE )throw new IllegalStateException("bug");

        int readSize = av>len ? len : (int)av

        int readPtr = transferred % buffer.length;
        int readRight = (buffer.length - readPtr) >= readSize ? readSize : (buffer.length - readPtr);
        int readLeft = readRight < readSize ? readSize-readRight : 0;

        if( readRight>0 )System.arraycopy(buffer,readPtr,bytes,off,readRight)
        if( readLeft>0 )System.arraycopy(buffer,0,bytes,off+readRight,readLeft)
        transferred += readSize

        return readSize
    }
    //endregion
}
