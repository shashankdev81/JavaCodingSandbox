package org.problems.mlc;

import java.util.Objects;

public class Value<T> {
    private T value;

    public Double getTimeToAccess() {
        return Double.valueOf(timeToAccess / 1000);
    }

    public void setTimeToAccess(Long timeToAccess) {
        this.timeToAccess = timeToAccess;
    }

    private Long timeToAccess;

    public T get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value<?> value1 = (Value<?>) o;
        return value.equals(value1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public Value(T value, Long timeToAccess) {
        this.value = value;
        this.timeToAccess = timeToAccess;
    }

    public Value(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" + value + '}';
    }
}
