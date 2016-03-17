package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Function1Test {
    private Function1<String, Integer> getStringLength = new Function1<String, Integer>() {
        @Override
        public Integer apply(String value) {
            return value.length();
        }
    };

    private Function1<Integer, Integer> getSquare = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer value) {
            return value * value;
        }
    };

    private Function1<Object, String> toString = new Function1<Object, String>() {
        @Override
        public String apply(Object value) {
            return value.toString();
        }
    };


    @Test
    public void testApply() throws Exception {
        assertEquals(3, (int) getStringLength.apply("abc"));
        assertEquals(1, (int) getStringLength.apply("a"));
        assertEquals(0, (int) getStringLength.apply(""));
        assertEquals(0, (int) getSquare.apply(0));
        assertEquals(256, (int) getSquare.apply(16));
    }

    @Test
    public void testCompose() throws Exception {
        Function1<String, Integer> getSquareOfLength = getStringLength.compose(getSquare);
        assertEquals(9, (int) getSquareOfLength.apply("abc"));
        assertEquals(1, (int) getSquareOfLength.apply("a"));
        assertEquals(0, (int) getSquareOfLength.apply(""));
        Function1<Integer, String> getSquareSting = getSquare.compose(toString);
        assertEquals("16", getSquareSting.apply(4));
        assertEquals("1", getSquareSting.apply(1));
        assertEquals("0", getSquareSting.apply(0));
    }
}
