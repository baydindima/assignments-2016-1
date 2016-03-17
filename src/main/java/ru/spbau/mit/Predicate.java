package ru.spbau.mit;

public abstract class Predicate<T> extends Function1<T, Boolean> {
    static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object value) {
            return true;
        }
    };

    static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object value) {
            return false;
        }
    };

    public abstract Boolean apply(T value);

    public Predicate<T> or(final Predicate<? super T> predicate) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T value) {
                return Predicate.this.apply(value) || predicate.apply(value);
            }
        };
    }

    public Predicate<T> and(final Predicate<? super T> predicate) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T value) {
                return Predicate.this.apply(value) && predicate.apply(value);
            }
        };
    }

    public Predicate<T> not() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T value) {
                return !Predicate.this.apply(value);
            }
        };
    }
}
