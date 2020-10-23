package day23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class Computer {

    private final List<String> program;
    private final Map<String, Integer> registers = new HashMap<>();

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

    private boolean validRegister(final String register) {
        return "abcd".contains(register);
    }

    private int decodeArgument(final String argument) {
        if (this.validRegister(argument))
            return this.getRegister(argument);
        else
            return Integer.parseInt(argument);
    }

    private String toggle(final String instruction) {
        String[] decode = instruction.split(" ");
        if (decode.length == 2) {
            String toggle = "inc ";
            if (instruction.startsWith(toggle)) toggle = "dec ";
            return toggle + decode[1];
        } else if (decode.length == 3) {
            String toggle = "jnz ";
            if (instruction.startsWith(toggle)) toggle = "cpy ";
            return toggle + decode[1] + " " + decode[2];
        }
        throw new IllegalStateException("Unknown instruction to toggle: " + instruction);
    }

    void run(final int a, final int b, final int c, final int d) {
        this.registers.put("a", a);
        this.registers.put("b", b);
        this.registers.put("c", c);
        this.registers.put("d", d);
        int line = 0;
        while (line < this.program.size()) {
            String instruction = this.program.get(line);
            String[] decode = instruction.split(" ");
            switch (decode[0]) {
                case "cpy":
                    String copyTo = decode[2];
                    if (this.validRegister(copyTo))
                        this.registers.put(copyTo, this.decodeArgument(decode[1]));
                    break;
                case "inc":
                    String inc = decode[1];
                    if (this.validRegister(inc))
                        this.registers.put(inc, this.registers.get(inc) + 1);
                    break;
                case "dec":
                    String dec = decode[1];
                    if (this.validRegister(dec))
                        this.registers.put(dec, this.registers.get(dec) - 1);
                    break;
                case "jnz":
                    if (this.decodeArgument(decode[1]) != 0)
                        line += this.decodeArgument(decode[2]) - 1;
                    break;
                case "tgl":
                    int toTgl = line + this.decodeArgument(decode[1]);
                    if (toTgl < this.program.size()) {
                        String newInstruction = this.toggle(this.program.get(toTgl));
                        this.program.set(toTgl, newInstruction);
                    }
                    break;
                default:
                    throw new IllegalStateException(String.format("Unknown command '%s'", decode[0]));
            }
            line++;
        }
    }
}


public class Advent23 {

    private static void part1() throws IOException {
        Computer computer = Computer.fromFile("2016/day23/input23.txt");
        computer.run(7, 0, 0, 0);
        System.out.printf("Part 1, register 'a' = %s\n", computer.getRegister("a"));
    }

    private static void test() throws IOException {
        int expectedA = 3;
        Computer computer = Computer.fromFile("2016/day23/test23a.txt");
        computer.run(0, 0, 0, 0);
        int actualA = computer.getRegister("a");
        assert actualA == expectedA : String.format("Expected register 'a' to be '%s' but was '%s'!", expectedA, actualA);
    }

    public static void main(String[] args) throws IOException {
        test();
        part1();
    }
}
