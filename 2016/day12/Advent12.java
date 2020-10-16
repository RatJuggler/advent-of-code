package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


class Computer {

    private final List<String> program;

    Computer(final List<String> program) {
        this.program = program;
    }

    static Computer fromFile(final String filename) throws IOException {
        List<String> program = Files.readAllLines(Paths.get(filename));
        return new Computer(program);
    }

    void run() {
        for (String instruction : this.program) {
            System.out.println(instruction);
        }
    }

}


public class Advent12 {

    private static void part2() {
    }

    private static void part1() throws IOException {
        Computer computer = Computer.fromFile("2016/day12/input12.txt");
        computer.run();
    }

    private static void test() throws IOException {
        Computer computer = Computer.fromFile("2016/day12/test12.txt");
        computer.run();
    }

    public static void main(final String[] args) throws IOException {
        test();
        part1();
        part2();
    }

}
