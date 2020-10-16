package day12;

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
        this.registers.put("a", 0);
        this.registers.put("b", 0);
        this.registers.put("c", 0);
        this.registers.put("d", 0);
    }

    static Computer fromFile(final String filename) throws IOException {
        List<String> program = Files.readAllLines(Paths.get(filename));
        return new Computer(program);
    }

    int getRegister(final String register) {
        return this.registers.get(register);
    }

    private int decodeArgument(final String argument) {
        if (Character.isAlphabetic(argument.charAt(0)))
            return this.getRegister(argument);
        else
            return Integer.parseInt(argument);
    }

    void run() {
        int line = 0;
        while (line < this.program.size()) {
            String instruction = this.program.get(line);
            String[] decode = instruction.split(" ");
            switch (decode[0]) {
                case "cpy":
                    this.registers.put(decode[2], this.decodeArgument(decode[1]));
                    break;
                case "inc":
                    this.registers.put(decode[1], this.registers.get(decode[1]) + 1);
                    break;
                case "dec":
                    this.registers.put(decode[1], this.registers.get(decode[1]) - 1);
                    break;
                case "jnz":
                    if (this.decodeArgument(decode[1]) != 0)
                        line += Integer.parseInt(decode[2]) - 1;
                    break;
                default:
                    throw new IllegalStateException(String.format("Unknown command '%s'", decode[0]));
            }
            line++;
        }
    }

}


public class Advent12 {

    private static void part2() {
    }

    private static void part1() throws IOException {
        Computer computer = Computer.fromFile("2016/day12/input12.txt");
        computer.run();
        System.out.printf("Register 'a' = %s", computer.getRegister("a"));
    }

    private static void test() throws IOException {
        int expectedA = 42;
        Computer computer = Computer.fromFile("2016/day12/test12.txt");
        computer.run();
        int actualA = computer.getRegister("a");
        assert actualA == expectedA : String.format("Expected register 'a' to be '%s' but was '%s'!", expectedA, actualA);
    }

    public static void main(final String[] args) throws IOException {
        test();
        part1();
        part2();
    }

}
