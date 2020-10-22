package day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Transformer {

    Transformer() {}

    static String move(final String s, int x, int y) {
        char move = s.charAt(x);
        String r = s.substring(0, x) + s.substring(x + 1);
        return r.substring(0, y) + move + r.substring(y);
    }

    static String reverse(final String s, final int x, final int y) {
        String reversed = new StringBuilder(s.substring(x, y + 1)).reverse().toString();
        return s.substring(0, x) + reversed + s.substring(y + 1);
    }

    static String rotate(final String s, final char x) {
        int i = s.indexOf(x);
        return rotate(s, "right", 1 + i + (i >= 4 ? 1 : 0));
    }

    static String rotate(final String s, final String d, int x) {
        x = x % s.length();
        if (d.equals("left")) {
            return s.substring(x) + s.substring(0, x);
        } else if (d.equals("right")) {
            return s.substring(s.length() - x) + s.substring(0, s.length() - x);
        } else {
            throw new IllegalStateException("Unknown direction to rotate: " + d);
        }
    }

    static String swap(final String s, int x, int y) {
        if (x > y) {
            int i = x; x = y; y = i;
        }
        return s.substring(0, x) + s.charAt(y) + s.substring(x + 1, y) + s.charAt(x) + s.substring(y + 1);
    }

    static String swap(final String s, final char x, final char y) {
        return s.replace(x, '*').replace(y, x).replace('*', y);
    }

}

class Scrambler {

    private static final String PARSE_PATTERN =
            "^(?<action>move|reverse|rotate|swap) (?<on>positions?|letter|based on position of letter|left|right) " +
            "(?<arg1>\\d|\\D) ?(?<with>to position|through|with position|with letter|steps?)? ?(?<arg2>\\d|\\D)?$";
    private static final Pattern INSTRUCTION_PARSER = Pattern.compile(PARSE_PATTERN);

    private final List<String> instructions;

    Scrambler(final String filename) throws IOException {
        this.instructions = Files.readAllLines(Path.of(filename));
    }

    private Matcher parseInstruction(final String instruction) {
        Matcher m = INSTRUCTION_PARSER.matcher(instruction);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse instruction: " + instruction);
        }
        return m;
    }

    private String swap(final String password, final Matcher m) {
        String on = m.group("on");
        String arg1 = m.group("arg1");
        String arg2 = m.group("arg2");
        if (on.equals("letter")) {
            return Transformer.swap(password, arg1.charAt(0), arg2.charAt(0));
        } else {
            return Transformer.swap(password, Integer.parseInt(arg1), Integer.parseInt(arg2));
        }
    }

    private String rotate(final String password, final Matcher m) {
        String on = m.group("on");
        String arg1 = m.group("arg1");
        if (on.startsWith("based")) {
            return Transformer.rotate(password, arg1.charAt(0));
        } else {
            return Transformer.rotate(password, on, Integer.parseInt(arg1));
        }
    }

    private String reverse(final String password, final Matcher m) {
        int x = Integer.parseInt(m.group("arg1"));
        int y = Integer.parseInt(m.group("arg2"));
        return Transformer.reverse(password, x, y);
    }

    private String move(final String password, final Matcher m) {
        int x = Integer.parseInt(m.group("arg1"));
        int y = Integer.parseInt(m.group("arg2"));
        return Transformer.move(password, x, y);
    }

    String scramble(final String password) {
        String newPassword = password;
        for (String instruction : this.instructions) {
            Matcher m = parseInstruction(instruction);
            String action = m.group("action");
            switch (action) {
                case "move":
                    newPassword = move(newPassword, m);
                    break;
                case "reverse":
                    newPassword = reverse(newPassword, m);
                    break;
                case "rotate":
                    newPassword = rotate(newPassword, m);
                    break;
                case "swap":
                    newPassword = swap(newPassword, m);
                    break;
                default:
                    throw new IllegalStateException("Unknown instruction: " + instruction);
            }
        }
        return newPassword;
    }
}


public class Advent21 {

    private static void testTransforms() {
        assert Transformer.move("abcde", 1, 4).equals("acdeb");
        assert Transformer.reverse("abcde", 2, 3).equals("abdce");
        assert Transformer.rotate("abcde", 'b').equals("deabc");
        assert Transformer.rotate("abcde", "left", 2).equals("cdeab");
        assert Transformer.rotate("abcde", "right", 4).equals("bcdea");
        assert Transformer.swap("abcde", 1, 4).equals("aecdb");
        assert Transformer.swap("abcde", 'd', 'a').equals("dbcae");
    }

    private static void test() throws IOException {
        String expected = "decab";
        Scrambler scrambler = new Scrambler("2016/day21/test21a.txt");
        String actual = scrambler.scramble("abcde");
        assert actual.equals(expected) : String.format("Expected scrambled password to be '%s' but was '%s'!", expected, actual);
    }

    private static void part1() throws IOException {
        Scrambler scrambler = new Scrambler("2016/day21/input21.txt");
        System.out.printf("Part 1, scrambled password = %s\n", scrambler.scramble("abcdefgh"));
    }

    public static void main(String[] args) throws IOException {
        testTransforms();
        test();
        part1();
    }

}
