package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Function2Test {

    private Function2<Integer, Integer, Integer> sum = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer fst, Integer snd) {
            return fst + snd;
        }
    };

    private Function2<Integer, Integer, Integer> del = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer fst, Integer snd) {
            return fst / snd;
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
        assertEquals(9, (int) sum.apply(4, 5));
        assertEquals(2, (int) del.apply(10, 5));
    }

    @Test
    public void testCompose() throws Exception {
        assertEquals("31", sum.compose(toString).apply(16, 15));
        assertEquals("0", sum.compose(toString).apply(0, 0));
    }

    @Test
    public void testBind1() throws Exception {
        Function1<Integer, Integer> sum16 = sum.bind1(16);
        assertEquals(31, (int) sum16.apply(15));
        assertEquals(0, (int) sum16.apply(-16));

        Function1<Integer, Integer> del50 = del.bind1(50);
        assertEquals(5, (int) del50.apply(10));
    }

    @Test
    public void testBind2() throws Exception {
        Function1<Integer, Integer> sum16 = sum.bind2(16);
        assertEquals(31, (int) sum16.apply(15));
        assertEquals(0, (int) sum16.apply(-16));


        Function1<Integer, Integer> del10 = del.bind2(10);
        assertEquals(5, (int) del10.apply(50));
    }

    @Test
    public void testCurry() throws Exception {
        Function1<Integer, Function1<Integer, Integer>> currySum = sum.curry();
        assertEquals(16, (int) currySum.apply(15).apply(1));
        assertEquals(9, (int) currySum.apply(21).apply(-12));

        Function1<Integer, Function1<Integer, Integer>> curryDel = del.curry();
        assertEquals(10, (int) curryDel.apply(100).apply(10));
    }
}
