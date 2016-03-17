package ru.spbau.mit;

public abstract class Function1<T, R> {

    public abstract R apply(T value);

    public <R2> Function1<T, R2> compose(final Function1<? super R, R2> function1) {
        return new Function1<T, R2>() {
            @Override
            public R2 apply(T value) {
                return function1.apply(Function1.this.apply(value));
            }
        };
    }

}
