package org.example.stat;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Сбор статистических данных
 * @param <A> Тип данных
 */
public abstract class Samples<A> implements SampleValues<A>, ReadWriteLocking {
    /**
     * Значения
     */
    protected final NavigableMap<Long,A> values = new ConcurrentSkipListMap<>();

    /**
     * Возвращает значения
     * @return значения
     */
    public NavigableMap<Long,A> getValues(){ return values; }

    /**
     * Клонирование
     * @return клон
     */
    public abstract Samples<A> clone();

    //region read/write lock
    public final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * Блокировка для записи значений
     * @param code код исполняемый в блокировке
     * @param <R> возвращаемые данные из кода
     * @return возвращаемые данные из кода
     */
    public <R> R writeLock( Supplier<R> code ){
        try {
            rwLock.writeLock().lock();
            return code.get();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * Блокировка для записи значений
     * @param code код исполняемый в блокировке
     */
    public void writeLock( Runnable code ){
        try {
            rwLock.writeLock().lock();
            code.run();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * Блокировка для чтения значений
     * @param code код исполняемый в блокировке
     * @param <R> возвращаемые данные из кода
     * @return возвращаемые данные из кода
     */
    public <R> R readLock( Supplier<R> code ){
        try {
            rwLock.readLock().lock();
            return code.get();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * Блокировка для чтения значений
     * @param code код исполняемый в блокировке
     */
    public void readLock( Runnable code ){
        try {
            rwLock.readLock().lock();
            code.run();
        } finally {
            rwLock.readLock().unlock();
        }
    }
    //endregion

    //region add(value)

    /**
     * Генерация ключа для следующего згначения
     * @return ключ
     */
    public abstract Long next();

    /**
     * Добавления значения в выборку
     * @param value значение
     */
    public void add(A value){
        writeLock(()-> add0( value ));
    }

    /**
     * Добавление значения в выборку - исполняется внутри блокировки
     * @param value значение
     */
    protected void add0(A value){
        values.put( next(), value );
    }
    //endregion
    //region merge() - Добавление значений из другого набора данных
    /**
     * Добавление значений из другого набора данных
     * @param sample набор данных
     * @param merge функция слияния
     */
    public void merge(Samples<A> sample, BiFunction<A,A,A> merge){
        if( sample==null )throw new IllegalArgumentException("samples==null");
        if( merge==null )throw new IllegalArgumentException("merge==null");
        writeLock(()->{
            sample.readLock(()->{
                sample.values.forEach( (k,v)->values.merge(k,v,merge) );
            });
        });
    }

    /**
     * Добавление значений из другого набора данных
     * @param samples набор данных
     * @param merge функция слияния
     */
    public void merge(Map<Long,A> samples, BiFunction<A,A,A> merge){
        if( samples==null )throw new IllegalArgumentException("samples==null");
        if( merge==null )throw new IllegalArgumentException("merge==null");
        writeLock(()->{
            samples.forEach((k,v)->values.merge(k,v,merge));
        });
    }

    /**
     * Добавление значений из другого набора данных
     * @param samples набор данных
     * @param merge функция слияния
     */
    public void merge(Iterable<Samples<A>> samples, BiFunction<A,A,A> merge){
        if( samples==null )throw new IllegalArgumentException("samples==null");
        if( merge==null )throw new IllegalArgumentException("merge==null");
        writeLock(()->{
            for( Samples<A> s : samples ){
                if( s!=null ){
                    s.readLock(()->{
                        s.values.forEach( (k,v)->values.merge(k,v,merge) );
                    });
                }
            }
        });
    }
    //endregion
    //region clear() - Удаление всех элементов
    /**
     * Удаление всех элементов
     */
    public void clear(){
        writeLock(values::clear);
    }
    //endregion

    //region выборка значения getSampleValues()
    /**
     * Значения в выборке
     * @return Значения
     */
    @Override
    public List<A> getSampleValues() {
        return readLock(()->new ArrayList<>(values.values()));
    }
    //endregion
    //region find min/max keys

    /**
     * Содержит минимальный и максимальный ключ выборки
     */
    public static class MinMaxKeys {
        public final Long min;
        public final Long max;
        public MinMaxKeys(Long min, Long max) {
            this.min = min;
            this.max = max;
        }
    }

    /**
     * Получение минимального и максимального ключа выборки
     * @return мин/макс ключ
     */
    public Optional<MinMaxKeys> getMinMaxKey(){
        return readLock(()->{
            if( values.isEmpty() )return Optional.empty();
            return Optional.of( new MinMaxKeys(values.firstKey(), values.lastKey()) );
        });
    }

    /**
     * Получение минимального ключа
     * @return минимальный ключ
     */
    public Optional<Long> getMinKey(){
        return readLock(()->{
            if( values.isEmpty() )return Optional.empty();
            return Optional.of(values.firstKey());
        });
    }

    /**
     * Получение максимального ключа
     * @return максимальный ключ
     */
    public Optional<Long> getMaxKey(){
        return readLock(()->{
            if( values.isEmpty() )return Optional.empty();
            return Optional.of(values.lastKey());
        });
    }
    //endregion
    //region getSize() / size()

    /**
     * Получение размера выборки
     * @return размер выборки
     */
    public int size(){return readLock (values::size);}

    /**
     * Получение размера выборки
     * @return размер выборки
     */
    public int getSize(){ return size(); }
    //endregion
    //region range selection

    /**
     * Указывает начало выборки
     */
    public class RangeFrom {
        public RangeTo from( long k ){
            return new RangeTo( k,true );
        }
        public RangeTo start(){
            return  new RangeTo( null, true );
        }
    }

    /**
     * Указывает конец выборки
     */
    public class RangeTo {
        protected Long from;
        protected boolean fromInclude = true;
        public RangeTo(Long from, boolean include){
            this.from = from;
            this.fromInclude = include;
        }
        public RangeAction to( long k ){
            return new RangeAction(from, fromInclude, k, true);
        }
        public RangeAction to( long k, boolean include ){
            return new RangeAction(from, fromInclude, k, include);
        }
        public RangeAction until( long k ){
            return new RangeAction(from, fromInclude, k, false );
        }
        public RangeAction end(){
            return new RangeAction(from, fromInclude, null, true);
        }
    }

    /**
     * Указывает действие производимое с выборкой
     */
    public class RangeAction {
        protected Long from;
        protected boolean fromInclude = true;
        protected Long to;
        protected boolean toInclude = true;

        public RangeAction(Long from, boolean fromInclude, Long to, boolean toInclude){
            this.from = from;
            this.fromInclude = fromInclude;
            this.to = to;
            this.toInclude = toInclude;
        }

        public NavigableMap<Long,A> getRangedMap(){
            NavigableMap<Long,A> m = Samples.this.values;
            if( from!=null ){
                m = m.tailMap(from, fromInclude);
            }
            if( to!=null ){
                m = m.headMap(to,toInclude);
            }
            return m;
        }
        public Object delete(){
            if( size()<=0 )return null;
            getRangedMap().clear();
            return null;
        }
        public List<A> getValues(){
            List<A> lst = new ArrayList<>();
            if( size()<=0 )return lst;

            lst.addAll( getRangedMap().values() );
            return lst;
        }
        public Samples<A> getSamples(){
            Samples<A> samples = Samples.this.clone();
            samples.clear();
            samples.values.putAll( getRangedMap() );
            return samples;
        }
    }

    /**
     * Выборка значений
     * @param rangeFun задание диапазона выборки
     * @param <R> результат выборки
     * @return результат выборки
     */
    public <R> R range( Function<RangeFrom, R> rangeFun ){
        if( rangeFun==null )throw new IllegalArgumentException("rangeFun==null");
        return writeLock( ()-> rangeFun.apply(new RangeFrom()) );
    }
    //endregion
    //region creating instances

    /**
     * Создание последовательной выборки значений с определенным окном
     * @param window размер окна, 0 и меньше - нет окна
     * @param <A> тип значений
     * @return Выборка
     */
    public static <A> SequenceSamples<A> sequence(long window){
        SequenceSamples<A> ss = new SequenceSamples<>();
        ss.setWindow(window);
        return ss;
    }

    /**
     * Создание последовательной выборки значений
     * @param <A> тип значений
     * @return Выборка
     */
    public static <A> SequenceSamples<A> sequence(){
        return new SequenceSamples<>();
    }

    /**
     * Создание выборки по времени значений с определенным окном
     */
    public static class Timed {
        /**
         * Создание выборки по времени, где ключ - значение в милли секундах (1/1000 сек) с определенным окном
         * @param window размер окна в милли секундах
         * @param <A> тип значений
         * @return выборка
         */
        public <A> TimedSamples<A> msec(long window){
            return new TimedSamples<A>(
                System::currentTimeMillis,
                TimeUnit.MILLISECONDS,
                window
            );
        }

        /**
         * Создание выборки по времени, где ключ - значение в нано секундах (1/1_000_000_000 сек) с определенным окном
         * @param window размер окна в нано секундах
         * @param <A> тип значений в выборке
         * @return выборка
         */
        public <A> TimedSamples<A> nsec(long window){
            return new TimedSamples<A>(
                System::nanoTime,
                TimeUnit.NANOSECONDS, window
            );
        }

        /**
         * Создание выборки по времени, где ключ - значение в секундах с определенным окном
         * @param window размер окна в секундах
         * @param <A> тип значений в выборке
         * @return выборка
         */
        public <A> TimedSamples<A> sec(long window){
            return new TimedSamples<A>(
                ()->System.currentTimeMillis()/1000L,
                TimeUnit.SECONDS, window
            );
        }
    }

    /**
     * Создание выборки по времени значений с определенным окном
     */
    public static final Timed timed = new Timed();
    //endregion
}
