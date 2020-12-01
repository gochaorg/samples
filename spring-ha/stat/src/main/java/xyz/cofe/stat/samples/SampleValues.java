package xyz.cofe.stat.samples;

import java.util.List;

/**
 * Выборка значений
 * @param <A> тип значений
 */
public interface SampleValues<A> {
    /**
     * Получение выборки значений
     * @return выборка
     */
    List<A> getSampleValues();
}
