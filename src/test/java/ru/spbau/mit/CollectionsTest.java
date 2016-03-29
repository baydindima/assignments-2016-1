package ru.spbau.mit;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class CollectionsTest {
    private Predicate<Integer> isEven = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer value) {
            return value % 2 == 0;
        }
    };

    private Function2<String, String, String> concatenate = new Function2<String, String, String>() {
        @Override
        public String apply(String fst, String snd) {
            return fst + snd;
        }
    };

    private Function2<Integer, Integer, Integer> subtraction = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer fst, Integer snd) {
            return fst - snd;
        }
    };

    @Test
    public void testMap() throws Exception {
        Collection<Integer> map = Collections.map(Arrays.asList(1, 3, 6, 12), new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer value) {
                return value * value;
            }
        });
        assertEquals(Arrays.asList(1, 9, 36, 144), new ArrayList<>(map));
    }

    @Test
    public void testFilter() throws Exception {
        Collection<Integer> map = Collections.filter(Arrays.asList(1, 3, 6, 12), isEven);
        Integer[] result = map.toArray(new Integer[map.size()]);
        assertEquals(2, result.length);
        assertEquals(6, (int) result[0]);
        assertEquals(12, (int) result[1]);
    }

    @Test
    public void testTakeWhile() throws Exception {
        Collection<Integer> map = Collections.takeWhile(Arrays.asList(1, 3, 6, 12), isEven.not());
        Integer[] result = map.toArray(new Integer[map.size()]);
        assertEquals(2, result.length);
        assertEquals(1, (int) result[0]);
        assertEquals(3, (int) result[1]);
    }

    @Test
    public void testTakeUnless() throws Exception {
        Collection<Integer> map = Collections.takeUnless(Arrays.asList(1, 3, 6, 12), isEven);
        Integer[] result = map.toArray(new Integer[map.size()]);
        assertEquals(2, result.length);
        assertEquals(1, (int) result[0]);
        assertEquals(3, (int) result[1]);
    }

    @Test
    public void testFoldr() throws Exception {
        assertEquals(" first  second  third  forth start ",
                Collections.foldr(Arrays.asList(" first ", " second ", " third ", " forth "),
                        "start ", concatenate));

        assertEquals(12, (int) Collections.foldr(Arrays.asList(1, 3, 6, 12), 20, subtraction));
    }

    @Test
    public void testFoldl() throws Exception {
        assertEquals("start  first  second  third  forth ",
                Collections.foldl(Arrays.asList(" first ", " second ", " third ", " forth "),
                        "start ", concatenate));

        assertEquals(-2, (int) Collections.foldl(Arrays.asList(1, 3, 6, 12), 20, subtraction));
    }
}
