package org.example.grammar.math.sem;

import java.util.Objects;
import java.util.Optional;

/**
 * Число, которое может содержать или не содержать значение
 */
public class Num {
    private final double value;
    private final boolean hasValue;

    /**
     * Конструктор числа со значением
     * @param value значение
     */
    public Num(double value) {
        this.hasValue = true;
        this.value = value;
    }

    /**
     * Конструктор числа без значения
     */
    public Num() {
        this.hasValue = false;
        this.value = 0;
    }

    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    public Num(Num sample) {
        if (sample == null) throw new IllegalArgumentException("sample==null");
        this.value = sample.value;
        this.hasValue = sample.hasValue;
    }

    /**
     * Получение значения
     * @return значение
     */
    public Optional<Double> value() {
        return hasValue ? Optional.of(value) : Optional.empty();
    }

    /**
     * Сложение чисел.
     *
     * <p></p>
     * Бинарная операция, если любой из операндов (или оба) содержит не действительно значение,
     * то результат так же будет не действительным
     *
     * @param num чисел
     * @return сумма чисел
     */
    public Num plus(Num num) {
        if (num == null) throw new IllegalArgumentException("num==null");
        return hasValue && num.hasValue ? new Num(value + num.value) : new Num();
    }

    /**
     * Вычитание чисел
     *
     * <p></p>
     * Бинарная операция, если любой из операндов (или оба) содержит не действительно значение,
     * то результат так же будет не действительным
     *
     * @param num вычитаемое
     * @return разница
     */
    public Num minus(Num num) {
        if (num == null) throw new IllegalArgumentException("num==null");
        return hasValue && num.hasValue ? new Num(value - num.value) : new Num();
    }

    /**
     * Умножение
     *
     * <p></p>
     * Бинарная операция, если любой из операндов (или оба) содержит не действительно значение,
     * то результат так же будет не действительным
     *
     * @param num число
     * @return результат умножения
     */
    public Num multiply(Num num) {
        if (num == null) throw new IllegalArgumentException("num==null");
        return hasValue && num.hasValue ? new Num(value * num.value) : new Num();
    }

    /**
     * Деление чисел
     *
     * <p></p>
     * Бинарная операция, если любой из операндов (или оба) содержит не действительно значение,
     * то результат так же будет не действительным
     *
     * @param num делитель
     * @return результат деления
     */
    public Num division(Num num) {
        if (num == null) throw new IllegalArgumentException("num==null");
        return hasValue && num.hasValue && num.value != 0 ? new Num(value / num.value) : new Num();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Num num = (Num) o;
        return Double.compare(value, num.value) == 0 && hasValue == num.hasValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, hasValue);
    }

    @Override
    public String toString() {
        return value().map(Object::toString).orElse("none");
    }
}
