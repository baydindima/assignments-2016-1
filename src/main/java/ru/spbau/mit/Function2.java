package ru.spbau.mit;

public abstract class Function2<F, S, R> {

    public abstract R apply(F fst, S snd);

    public <R2> Function2<F, S, R2> compose(final Function1<? super R, R2> function1) {
        return new Function2<F, S, R2>() {
            @Override
            public R2 apply(F fst, S snd) {
                return function1.apply(Function2.this.apply(fst, snd));
            }
        };
    }

    public <T extends F> Function1<S, R> bind1(final T fst) {
        return new Function1<S, R>() {
            @Override
            public R apply(S value) {
                return Function2.this.apply(fst, value);
            }
        };
    }

    public <T extends S> Function1<F, R> bind2(final T snd) {
        return new Function1<F, R>() {
            @Override
            public R apply(F value) {
                return Function2.this.apply(value, snd);
            }
        };
    }

    public Function1<F, Function1<S, R>> curry() {
        return new Function1<F, Function1<S, R>>() {
            @Override
            public Function1<S, R> apply(F value) {
                return bind1(value);
            }
        };
    }

}
