package day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


class HHGC {

    private final List<String> rom;
    private int accumulator = 0;

    HHGC(final List<String> rom) {
        this.rom = rom;
    }

    static HHGC fromROMFile(final String filename) throws IOException {
        List<String> rom = Files.readAllLines(Paths.get(filename));
        return new HHGC(rom);
    }

    private String[] decodeInstruction(final String instruction) {
        return instruction.split(" ");
    }

    void bootUntilLoop() {
        int pc = 0;
        List<Integer> pcHistory = new ArrayList<>();
        while (!pcHistory.contains(pc)) {
            pcHistory.add(pc);
            String instruction = this.rom.get(pc);
            String[] decode = this.decodeInstruction(instruction);
            switch (decode[0]) {
                case "acc":
                    this.accumulator += Integer.parseInt(decode[1]);
                    break;
                case "jmp":
                    pc += Integer.parseInt(decode[1]) - 1;
                    break;
                case "nop":
                    break;
                default:
                    throw new IllegalStateException(String.format("Unknown instruction '%s'", decode[0]));
            }
            pc++;
        }
    }

    int getAccumulator() {
        return this.accumulator;
    }
}


public class Advent2020Day8 {

    private static void part1() throws IOException {
        HHGC hhgc = HHGC.fromROMFile("2020/day8/input8.txt");
        hhgc.bootUntilLoop();
        System.out.printf("Day 8, Part 1, accumulator just before infinite loop is %s\n", hhgc.getAccumulator());
    }

    private static void test() throws IOException {
        int expected = 5;
        HHGC hhgc = HHGC.fromROMFile("2020/day8/test8a.txt");
        hhgc.bootUntilLoop();
        int actual = hhgc.getAccumulator();
        assert actual == expected : String.format("Expected accumulator to be '%s' but was '%s'!", expected, actual);
    }

    public static void main(final String[] args) throws IOException {
        test();
        part1();
    }
}
