package xyz.cofe.stat.samples;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Сбор статистических данных
 * @param <A> Тип данных
 */
public abstract class Samples<A> implements SampleValues<A> {
    /**
     * Значения
     */
    public final NavigableMap<Long,A> values = new ConcurrentSkipListMap<>();

    //region read/write lock
    public final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * Блокировка для записи значений
     * @param code код исполняемый в блокировке
     * @param <R> возвращаемые данные из кода
     * @return возвращаемые данные из кода
     */
    protected <R> R writeLock( Supplier<R> code ){
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
    protected void writeLock( Runnable code ){
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
    protected <R> R readLock( Supplier<R> code ){
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
    protected void readLock( Runnable code ){
        try {
            rwLock.readLock().lock();
            code.run();
        } finally {
            rwLock.readLock().unlock();
        }
    }
    //endregion
    //region add value
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
        writeLock(()->{
            add0( value );
        });
    }

    /**
     * Добавление значения в выборку - исполняется внутри блокировки
     * @param value значение
     */
    protected void add0(A value){
        values.put( next(), value );
    }
    //endregion

    /**
     * Значения в выборке
     * @return Значения
     */
    @Override
    public List<A> getSampleValues() {
        return readLock(()->new ArrayList<>(values.values()));
    }

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
    public abstract class RangeFrom<TO> {
        protected abstract TO create(Long k,boolean include);
        public TO from( long k ){ return create( k,true ); }
        public TO start(){ return create( null, true ); }
    }

    public class RangeFromModify extends RangeFrom<RangeToModify> {
        @Override
        protected RangeToModify create(Long k, boolean include) {
            return new RangeToModify(k,include);
        }
    }

    public class RangeFromFetch extends RangeFrom<RangeToFetch> {
        @Override
        protected RangeToFetch create(Long k, boolean include) {
            return new RangeToFetch(k,include);
        }
    }

    /**
     * Указывает конец выборки
     */
    public abstract class RangeTo<ACTION> {
        protected Long from;
        protected boolean fromInclude = true;
        public RangeTo(Long from, boolean include){
            this.from = from;
            this.fromInclude = include;
        }
        protected abstract ACTION create( Long from, boolean fromInclude, Long to, boolean toInclude );
        public ACTION to( long k ){
            return create(from, fromInclude, k, true);
        }
        public ACTION to( long k, boolean include ){
            return create(from, fromInclude, k, include);
        }
        public ACTION until( long k ){
            return create(from, fromInclude, k, false );
        }
        public ACTION end(){
            return create(from, fromInclude, null, true);
        }
    }

    public class RangeToFetch extends RangeTo<RangeFetchActions> {
        public RangeToFetch(Long from, boolean include) {
            super(from, include);
        }

        @Override
        protected RangeFetchActions create(Long from, boolean fromInclude, Long to, boolean toInclude) {
            return new RangeFetchActions(from,fromInclude,to,toInclude);
        }
    }

    public class RangeToModify extends RangeTo<RangeModifyActions> {
        public RangeToModify(Long from, boolean include) {
            super(from, include);
        }

        @Override
        protected RangeModifyActions create(Long from, boolean fromInclude, Long to, boolean toInclude) {
            return new RangeModifyActions(from,fromInclude,to,toInclude);
        }
    }

    /**
     * Указывает действие производимое с выборкой
     */
    public abstract class RangeAction {
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
        public List<A> getValues(){
            List<A> lst = new ArrayList<>();
            if( size()<=0 )return lst;

            lst.addAll( getRangedMap().values() );
            return lst;
        }
    }

    public class RangeFetchActions extends RangeAction {
        public RangeFetchActions(Long from, boolean fromInclude, Long to, boolean toInclude) {
            super(from, fromInclude, to, toInclude);
        }
    }

    public class RangeModifyActions extends RangeAction {
        public RangeModifyActions(Long from, boolean fromInclude, Long to, boolean toInclude) {
            super(from, fromInclude, to, toInclude);
        }

        public void delete(){
            if( size()<=0 )return;
            getRangedMap().clear();
        }
    }

    /**
     * Выборка значений
     */
    public class RangeSelection {
        /**
         * Выборка значений
         * @param rangeFun задание диапазона выборки
         * @param <R> результат выборки
         * @return результат выборки
         */
        public <R> R fetch( Function<RangeFromFetch, R> rangeFun ){
            if( rangeFun==null )throw new IllegalArgumentException("rangeFun==null");
            return readLock( ()-> rangeFun.apply(new RangeFromFetch()) );
        }

        /**
         * Выборка значений
         * @param rangeFun задание диапазона выборки
         * @return результат выборки
         */
        public void modify( Consumer<RangeFromModify> rangeFun ){
            if( rangeFun==null )throw new IllegalArgumentException("rangeFun==null");
            writeLock( ()-> rangeFun.accept(new RangeFromModify()) );
        }
    }

    public final RangeSelection range = new RangeSelection();
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
