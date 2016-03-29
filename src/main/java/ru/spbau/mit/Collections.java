package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class Collections {

    private Collections() {
    }

    public static <T, R> Collection<R> map(Iterable<T> initCollection, Function1<? super T, R> func) {
        Collection<R> result = getEmptyCollection();
        for (T t : initCollection) {
            result.add(func.apply(t));
        }
        return java.util.Collections.unmodifiableCollection(result);
    }

    private static <T> Collection<T> getEmptyCollection() {
        return new ArrayList<>();
    }

    public static <T> Collection<T> filter(Iterable<T> initCollection, Predicate<? super T> predicate) {
        Collection<T> result = getEmptyCollection();
        for (T t : initCollection) {
            if (predicate.apply(t)) {
                result.add(t);
            }
        }
        return java.util.Collections.unmodifiableCollection(result);
    }

    public static <T> Collection<T> takeWhile(Iterable<T> initCollection, Predicate<? super T> predicate) {
        Collection<T> result = getEmptyCollection();
        for (T t : initCollection) {
            if (!predicate.apply(t)) {
                break;
            }
            result.add(t);
        }
        return java.util.Collections.unmodifiableCollection(result);
    }

    public static <T> Collection<T> takeUnless(Iterable<T> initCollection, Predicate<? super T> predicate) {
        return takeWhile(initCollection, predicate.not());
    }

    private static <T, R> R foldrIterator(Iterator<T> iterator,
                                          R initValue, Function2<? super T, ? super R, R> function) {
        R snd = initValue;
        if (iterator.hasNext()) {
            T fst = iterator.next();
            snd = foldrIterator(iterator, initValue, function);
            return function.apply(fst, snd);
        }
        return snd;
    }

    public static <T, R> R foldr(Iterable<T> initCollection,
                                 R initValue, Function2<? super T, ? super R, R> function) {
        return foldrIterator(initCollection.iterator(), initValue, function);
    }

    public static <T, R> R foldl(Iterable<T> initCollection,
                                 R initValue, Function2<? super R, ? super T, R> function) {
        for (T snd : initCollection) {
            initValue = function.apply(initValue, snd);
        }
        return initValue;
    }

}
