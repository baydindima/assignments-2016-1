package ru.spbau.mit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static ru.spbau.mit.SecondPartTasks.*;


public class SecondPartTasksTest {
    final static String txt1 = "\"The Burden of Truth\"\n" +
            "\n" +
            "The Comtessa's father sits with impeccable posture.\n" +
            "An empty bottle of port rests on his desk;\n" +
            "his empty strong box still rests on the floor.\n" +
            "A portrait of the Comtessa hangs over the mantle,\n" +
            "barely visible in the light of the dying hearth.\n" +
            "\"So,\" he says, looking past you.\n" +
            "\"You must have encountered many distressing sights,\n" +
            "in your work. How do you live with what you know?\"";

    final static String txt2 = "\"The beginning of courage\"\n" +
            "\n" +
            "Her father reaches for his bottle;\n" +
            "its lightness seems to surprise him,\n" +
            "for he sets it back with a forlorn sigh.\n" +
            "\"Perhaps you're right. But what if that truth is absurd, senseless?\n" +
            "The soulless epidemic; that d__nable Brass Embassy.\n" +
            "And you, unable to even bring back my -\"\n" +
            "He shakes his head, and gestures that you should leave.";
    final static String txt3 = "\"The pearls!\"\n" +
            "\n" +
            "The thieves are long gone,\n" +
            "but you track down the moon-pearls to a dingy pawnbroker's shop in Spite.\n" +
            "The foul-mouthed owner isn't keen on returning them,\n" +
            "but capitulates when you mention the Constables.\n" +
            "The Keen-Eyed Lapidary is greatly relieved by their return:\n" +
            "but the pearls are all spoken for,\n" +
            "and she has no funds to reward you.\n" +
            "She promises to return the favour in future.";
    final static String txt4 = "\"A perfect vantage point\"\n" +
            "\n" +
            "Sprawled on the hospital roof,\n" +
            "you watch as bandaged invalids are\n" +
            "wheeled into the ward and left in rows.\n" +
            "Before the night is out, a spirifer comes to call at their bedsides.\n" +
            "He is an unnerving sight; eyes like yellow diamonds,\n" +
            "a mouth like a slash in a rubber sheet,\n" +
            "and the most unnaturally enormous hat.\n" +
            "\n" +
            "He leans over a bedside,\n" +
            "and, with a device like a brass tuning fork,\n" +
            "gently winds the soul from an invalid's mouth.\n" +
            "\n" +
            "It is a simple matter to follow him home.\n" +
            "A quick tap on the back of the head with your trusty knoblolly and he falls stunned.\n" +
            "You resist the urge to murder him and help yourself to his stock instead.";
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testFindQuotes() throws FileNotFoundException {
        File tmp = new File("tmp");
        tmp.mkdir();

        PrintWriter out = new PrintWriter("/tmp/text1.txt");
        out.write(txt1);
        out.close();
        out = new PrintWriter("/tmp/text2.txt");
        out.write(txt2);
        out.close();
        out = new PrintWriter("/tmp/text3.txt");
        out.write(txt3);
        out.close();
        out = new PrintWriter("/tmp/text4.txt");
        out.write(txt4);
        out.close();

        List<String> paths = Arrays.asList(
                "/tmp/text1.txt",
                "/tmp/text2.txt",
                "/tmp/text3.txt",
                "/tmp/text4.txt"
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
