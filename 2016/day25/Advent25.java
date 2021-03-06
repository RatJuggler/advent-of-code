package day25;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Computer {

    private final List<String> program;
    private final Map<String, Integer> registers = new HashMap<>();
    private int line;

    Computer(final List<String> program) {
        this.program = program;
    }

    static Computer fromFile(final String filename) throws IOException {
        List<String> program = Files.readAllLines(Paths.get(filename));
        return new Computer(program);
    }

    int getRegister(final String register) {
        return this.registers.get(register);
    }

    private void updateRegister(final String register, final int change) {
        this.registers.put(register, this.getRegister(register) + change);
    }

    private String[] decode(final int line) {
        return this.program.get(line).split(" ");
    }

    private boolean validRegister(final String register) {
        return "a|b|c|d".contains(register);
    }

    private int decodeArgument(final String argument) {
        if (this.validRegister(argument))
            return this.getRegister(argument);
        else
            return Integer.parseInt(argument);
    }

    private String getLines(final int fromLine, final int lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = fromLine; i < fromLine + lines; i++) {
            sb.append(this.program.get(i)).append(' ');
        }
        return sb.toString();
    }

    private Matcher findCode(final String pattern, final int fromLine, final int lines) {
        String code = this.getLines(fromLine, lines);
        Pattern r = Pattern.compile(pattern);
        return r.matcher(code);
    }

    private boolean optimiseMultiply(final int fromLine) {
        final String multiply = "^cpy (?<b>\\S) (?<c>\\S) inc (?<a>\\S) dec \\2 jnz \\2 -2 dec (?<d>\\S) jnz \\4 -5 $";
        Matcher m = findCode(multiply, fromLine, 6);
        if (!m.find()) return false;
        String register = m.group("a");
        String x = m.group("b");
        String y = m.group("d");
        this.updateRegister(register, this.decodeArgument(x) * this.decodeArgument(y));
        return true;
    }

    private boolean optimiseAdd(final int fromLine) {
        final String add = "^inc (?<a>\\S) dec (?<b>\\S) jnz \\2 -2 $";
        Matcher m = findCode(add, fromLine, 3);
        if (!m.find()) return false;
        String register = m.group("a");
        String x = m.group("b");
        this.updateRegister(register, this.decodeArgument(x));
        return true;
    }

    private String toggle(final int tglLine) {
        String[] decode = this.decode(tglLine);
        if (decode.length == 2) {
            String toggle = "inc";
            if (decode[0].equals(toggle)) toggle = "dec";
            return toggle + " " + decode[1];
        } else if (decode.length == 3) {
            String toggle = "jnz";
            if (decode[0].equals(toggle)) toggle = "cpy";
            return toggle + " " + decode[1] + " " + decode[2];
        }
        throw new IllegalStateException("Unknown instruction to toggle: " + decode[0]);
    }

    void initialise(final int a, final int b, final int c, final int d) {
        this.registers.put("a", a);
        this.registers.put("b", b);
        this.registers.put("c", c);
        this.registers.put("d", d);
        this.line = 0;
    }

    int runToOut() {
        while (line < this.program.size()) {
            if (line < this.program.size() - 5 && this.optimiseMultiply(line)) {
                line += 6;
            } else if (line < this.program.size() - 2 && this.optimiseAdd(line)) {
                line += 3;
            } else {
                String[] decode = this.decode(line);
                switch (decode[0]) {
                    case "cpy":
                        String copyTo = decode[2];
                        if (this.validRegister(copyTo))
                            this.registers.put(copyTo, this.decodeArgument(decode[1]));
                        break;
                    case "inc":
                        String inc = decode[1];
                        if (this.validRegister(inc))
                            this.updateRegister(inc, 1);
                        break;
                    case "dec":
                        String dec = decode[1];
                        if (this.validRegister(dec))
                            this.updateRegister(dec, -1);
                        break;
                    case "jnz":
                        if (this.decodeArgument(decode[1]) != 0)
                            line += this.decodeArgument(decode[2]) - 1;
                        break;
                    case "tgl":
                        int tglLine = line + this.decodeArgument(decode[1]);
                        if (tglLine < this.program.size()) {
                            this.program.set(tglLine, this.toggle(tglLine));
                        }
                        break;
                    case "out":
                        line++;
                        return this.decodeArgument(decode[1]);
                    default:
                        throw new IllegalStateException(String.format("Unknown command '%s'", decode[0]));
                }
                line++;
            }
        }
        return -1;
    }
}


public class Advent25 {

    private static void part1() throws IOException {
        Computer computer = Computer.fromFile("2016/day25/input25.txt");
        int a = 0;
        boolean found = false;
        while (!found) {
            System.out.println("Trying: " + a);
            computer.initialise(a, 0, 0, 0);
            found = true;
            for (int n = 0; n < 99; n++) {
                int tick = computer.runToOut();
                int tock = computer.runToOut();
                System.out.print(tick + " " + tock + " ");
                if (tick != 0 || tock != 1) {
                    found = false;
                    break;
                }
            }
            System.out.println();
            a++;
        }
        System.out.printf("Part 1, initial register 'a' to %s\n", a - 1);
    }

    public static void main(String[] args) throws IOException {
        part1();
    }
}
