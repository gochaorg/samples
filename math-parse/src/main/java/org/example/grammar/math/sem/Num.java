package org.example.grammar.math.sem;

import java.util.Objects;
import java.util.Optional;

public class Num {
    private final double value;
    private final boolean hasValue;

    public Num(double value) {
        this.hasValue = true;
        this.value = value;
    }

    public Num() {
        this.hasValue = false;
        this.value = 0;
    }

    public Num(Num sample) {
        if (sample == null) throw new IllegalArgumentException("sample==null");
        this.value = sample.value;
        this.hasValue = sample.hasValue;
    }

    public Optional<Double> value() {
        return hasValue ? Optional.of(value) : Optional.empty();
    }

    public Num plus(Num num) {
        if (num == null) throw new IllegalArgumentException("num==null");
        return hasValue && num.hasValue ? new Num(value + num.value) : new Num();
    }

    public Num minus(Num num) {
        if (num == null) throw new IllegalArgumentException("num==null");
        return hasValue && num.hasValue ? new Num(value - num.value) : new Num();
    }

    public Num multiply(Num num) {
        if (num == null) throw new IllegalArgumentException("num==null");
        return hasValue && num.hasValue ? new Num(value * num.value) : new Num();
    }

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
