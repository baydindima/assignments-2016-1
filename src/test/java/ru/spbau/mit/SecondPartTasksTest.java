package ru.spbau.mit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.UncheckedIOException;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static ru.spbau.mit.SecondPartTasks.*;


public class SecondPartTasksTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testFindQuotes() {
        List<String> paths = Arrays.asList(
                "./tmp/text1.txt",
                "./tmp/text2.txt",
                "./tmp/text3.txt",
                "./tmp/text4.txt"
        );
        List<String> result = Arrays.asList(
                "and she has no funds to reward you.",
                "a mouth like a slash in a rubber sheet,"
        );
        assertEquals(result, findQuotes(paths, "she"));
        assertEquals(Collections.emptyList(), findQuotes(paths, "never gone give you up"));

        exception.expect(UncheckedIOException.class);
        findQuotes(Collections.singletonList("wrong path"), "wrong");
    }

    @Test
    public void testPiDividedBy4() {
        double result = piDividedBy4();
        assertTrue(result >= 0);
        assertTrue(result <= 1);
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> writers = new HashMap<String, List<String>>() {{
            put("Vasya", Arrays.asList("First letter", "Second Letter"));
            put("Petya", Arrays.asList("First letter", "Second Letter"));
            put("Sanya", Arrays.asList("First letter", "Second Letter", "Third Letter"));
        }};
        assertEquals("Sanya", findPrinter(writers));

        exception.expect(NullPointerException.class);
        findPrinter(Collections.emptyMap());
    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> пятерочка = new HashMap<String, Integer>() {{
            put("Водка", 300);
            put("Молоко", 50);
            put("Доширак", 30);
            put("Пельмени", 150);
        }};

        Map<String, Integer> магнит = new HashMap<String, Integer>() {{
            put("Хлеб", 20);
            put("Картошка", 15);
            put("Пиво", 90);
            put("Водка", 300);
        }};

        Map<String, Integer> дикси = new HashMap<String, Integer>() {{
            put("Чипсы", 70);
            put("Молоко", 50);
            put("Пиво", 90);
            put("Доширак", 30);
        }};

        Map<String, Integer> результат = new HashMap<String, Integer>() {{
            put("Водка", 600);
            put("Молоко", 100);
            put("Доширак", 60);
            put("Пельмени", 150);
            put("Пиво", 180);
            put("Чипсы", 70);
            put("Хлеб", 20);
            put("Картошка", 15);
        }};

        assertEquals(результат,
                calculateGlobalOrder(Arrays.asList(пятерочка, магнит, дикси))
        );

        assertEquals(Collections.emptyMap(),
                Collections.emptyMap());
    }
}
