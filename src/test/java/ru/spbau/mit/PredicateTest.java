package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PredicateTest {
    private Predicate<Integer> moreThanFive = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer value) {
            return value > 5;
        }
    };

    private Predicate<Integer> isEven = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer value) {
            return value % 2 == 0;
        }
    };

    @Test
    public void truePredicateReturnsTrue() throws Exception {
        assertTrue(Predicate.ALWAYS_TRUE.apply("false"));
        assertTrue(Predicate.ALWAYS_TRUE.apply(false));
        assertTrue(Predicate.ALWAYS_TRUE.apply(1));
    }

    @Test
    public void falsePredicateReturnsFalse() throws Exception {
        assertFalse(Predicate.ALWAYS_FALSE.apply("true"));
        assertFalse(Predicate.ALWAYS_FALSE.apply(true));
        assertFalse(Predicate.ALWAYS_FALSE.apply(0));
    }

    @Test
    public void testApply() throws Exception {
        assertTrue(moreThanFive.apply(7));
        assertFalse(moreThanFive.apply(4));
        assertTrue(isEven.apply(4));
        assertFalse(moreThanFive.apply(5));
    }

    @Test
    public void testOr() throws Exception {
        Predicate<Integer> moreThanFiveOrIsEven = moreThanFive.or(isEven);
        assertTrue(moreThanFiveOrIsEven.apply(6)); // True True
        assertTrue(moreThanFiveOrIsEven.apply(7)); // True False
        assertTrue(moreThanFiveOrIsEven.apply(4)); // False True
        assertFalse(moreThanFiveOrIsEven.apply(3)); // False False
    }

    @Test
    public void testAnd() throws Exception {
        Predicate<Integer> moreThanFiveAndIsEven = moreThanFive.and(isEven);
        assertTrue(moreThanFiveAndIsEven.apply(6)); // True True
        assertFalse(moreThanFiveAndIsEven.apply(7)); // True False
        assertFalse(moreThanFiveAndIsEven.apply(4)); // False True
        assertFalse(moreThanFiveAndIsEven.apply(3)); // False False
    }

    @Test
    public void testNot() throws Exception {
        assertFalse(moreThanFive.not().apply(7));
        assertTrue(moreThanFive.not().apply(4));
        assertFalse(isEven.not().apply(4));
        assertTrue(moreThanFive.not().apply(5));
    }
}
