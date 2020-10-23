package day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Transformer {

    Transformer() {}

    static String move(final String s, final int x, final int y) {
        char move = s.charAt(x);
        String r = s.substring(0, x) + s.substring(x + 1);
        return r.substring(0, y) + move + r.substring(y);
    }

    static String reverse(final String s, final int x, final int y) {
        String reversed = new StringBuilder(s.substring(x, y + 1)).reverse().toString();
        return s.substring(0, x) + reversed + s.substring(y + 1);
    }

    static String rotateOnLetter(final String s, final char x) {
        int i = s.indexOf(x);
        return rotate(s, "right", 1 + i + (i >= 4 ? 1 : 0));
    }

    static String rotate(final String s, final String d, final int x) {
        int r = x % s.length();
        if (d.equals("left"))
            return s.substring(r) + s.substring(0, r);
        else
            return s.substring(s.length() - r) + s.substring(0, s.length() - r);
    }

    static String swap(final String s, final int x, final int y) {
        if (x > y)
            return s.substring(0, y) + s.charAt(x) + s.substring(y + 1, x) + s.charAt(y) + s.substring(x + 1);
        else
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
        if (!m.find()) throw new IllegalStateException("Unable to parse instruction: " + instruction);
        return m;
    }

    private String swap(final String password, final Matcher m) {
        String on = m.group("on");
        String arg1 = m.group("arg1");
        String arg2 = m.group("arg2");
        if (on.equals("letter"))
            return Transformer.swap(password, arg1.charAt(0), arg2.charAt(0));
        else
            return Transformer.swap(password, Integer.parseInt(arg1), Integer.parseInt(arg2));
    }

    private String rotate(final String password, final Matcher m) {
        String on = m.group("on");
        String arg1 = m.group("arg1");
        if (on.startsWith("based"))
            return Transformer.rotateOnLetter(password, arg1.charAt(0));
        else
            return Transformer.rotate(password, on, Integer.parseInt(arg1));
    }

    private String rotateReverse(final String password, final Matcher m) {
        String on = m.group("on");
        String arg1 = m.group("arg1");
        if (on.startsWith("based")) {
            for (int i = 0; i < password.length(); i++) {
                String reverse = Transformer.rotate(password, "left", i);
                String forward = Transformer.rotateOnLetter(reverse, arg1.charAt(0));
                if (forward.equals(password)) return reverse;
            }
            throw new IllegalStateException("Unable to reverse rotateOnLetter for " + password);
        } else {
            on = on.equals("left") ? "right" : "left";
            return Transformer.rotate(password, on, Integer.parseInt(arg1));
        }
    }

    private String reverse(final String password, final Matcher m) {
        int x = Integer.parseInt(m.group("arg1"));
        int y = Integer.parseInt(m.group("arg2"));
        return Transformer.reverse(password, x, y);
    }

    private String move(final String password, final Matcher m) {
        int arg1 = Integer.parseInt(m.group("arg1"));
        int arg2 = Integer.parseInt(m.group("arg2"));
        return Transformer.move(password, arg1, arg2);
    }

    private String moveReverse(final String password, final Matcher m) {
        int arg1 = Integer.parseInt(m.group("arg1"));
        int arg2 = Integer.parseInt(m.group("arg2"));
        return Transformer.move(password, arg2, arg1);
    }

    String scramble(final String password) {
        String scramble = password;
        for (String instruction : this.instructions) {
            Matcher m = parseInstruction(instruction);
            String action = m.group("action");
            switch (action) {
                case "move":
                    scramble = move(scramble, m);
                    break;
                case "reverse":
                    scramble = reverse(scramble, m);
                    break;
                case "rotate":
                    scramble = rotate(scramble, m);
                    break;
                case "swap":
                    scramble = swap(scramble, m);
                    break;
                default:
                    throw new IllegalStateException("Unknown instruction: " + instruction);
            }
            System.out.println(scramble + " " + instruction);
        }
        return scramble;
    }

    String unscramble(final String password) {
        String unscramble = password;
        for (int i = this.instructions.size() - 1; i >= 0; i--) {
            String instruction = this.instructions.get(i);
            Matcher m = parseInstruction(instruction);
            String action = m.group("action");
            switch (action) {
                case "move":
                    unscramble = moveReverse(unscramble, m);
                    break;
                case "reverse":
                    unscramble = reverse(unscramble, m);
                    break;
                case "rotate":
                    unscramble = rotateReverse(unscramble, m);
                    break;
                case "swap":
                    unscramble = swap(unscramble, m);
                    break;
                default:
                    throw new IllegalStateException("Unknown instruction: " + instruction);
            }
            System.out.println(unscramble + " " + instruction);
        }
        return unscramble;
    }
}


public class Advent21 {

    private static void testTransforms() {
        assert Transformer.move("abcde", 1, 4).equals("acdeb");
        assert Transformer.reverse("abcde", 2, 3).equals("abdce");
        assert Transformer.rotateOnLetter("abcde", 'b').equals("deabc");
        assert Transformer.rotate("abcde", "left", 2).equals("cdeab");
        assert Transformer.rotate("abcde", "right", 4).equals("bcdea");
        assert Transformer.swap("abcde", 1, 4).equals("aecdb");
        assert Transformer.swap("abcde", 'd', 'a').equals("dbcae");
    }

    private static void test2() throws IOException {
        String expected = "abcde";
        Scrambler scrambler = new Scrambler("2016/day21/test21a.txt");
        String actual = scrambler.unscramble("decab");
        assert actual.equals(expected) : String.format("Expected unscrambled password to be '%s' but was '%s'!", expected, actual);
    }

    private static void test1() throws IOException {
        String expected = "decab";
        Scrambler scrambler = new Scrambler("2016/day21/test21a.txt");
        String actual = scrambler.scramble("abcde");
        assert actual.equals(expected) : String.format("Expected scrambled password to be '%s' but was '%s'!", expected, actual);
    }

    private static void part2() throws IOException {
        Scrambler scrambler = new Scrambler("2016/day21/input21.txt");
        System.out.printf("Part 2, unscrambled password = %s\n", scrambler.unscramble("fbgdceah"));
    }

    private static void part1() throws IOException {
        Scrambler scrambler = new Scrambler("2016/day21/input21.txt");
        System.out.printf("Part 1, scrambled password = %s\n", scrambler.scramble("abcdefgh"));
    }

    public static void main(String[] args) throws IOException {
        testTransforms();
        test1();
        part1();
        test2();
        part2();
    }
}
