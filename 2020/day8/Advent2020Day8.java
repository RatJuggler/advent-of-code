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

    boolean boot() {
        int pc = 0;
        List<Integer> pcHistory = new ArrayList<>();
        while (!pcHistory.contains(pc) && pc < this.rom.size()) {
            pcHistory.add(pc);
            String instruction = this.rom.get(pc++);
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
        }
        return pcHistory.contains(pc);
    }

    int getAccumulator() {
        return this.accumulator;
    }
}


class  HHGCTestHarness {

    private final List<String> originalRom;
    private int alterInstructionAtPC;

    HHGCTestHarness(final String filename) throws IOException {
        this.originalRom = Files.readAllLines(Paths.get(filename));
    }

    private String alterInstruction(final String newInstruction, final String currentInstruction) {
        return newInstruction + " " + currentInstruction.split(" ")[1];
    }

    private List<String> alterRom() {
        List<String> newTestRom = new ArrayList<>(this.originalRom);
        String instructionToAlter;
        do {
            instructionToAlter = newTestRom.get(this.alterInstructionAtPC);
            if (instructionToAlter.startsWith("nop")) {
                newTestRom.set(this.alterInstructionAtPC, this.alterInstruction("jmp", instructionToAlter));
            } else if (instructionToAlter.startsWith("jmp")) {
                newTestRom.set(this.alterInstructionAtPC, this.alterInstruction("nop", instructionToAlter));
            }
            if (++this.alterInstructionAtPC == newTestRom.size())
                throw new IllegalStateException("No more instructions to alter in ROM!");
        } while (!instructionToAlter.startsWith("nop") && !instructionToAlter.startsWith("jmp"));
        return newTestRom;
    }

    int fixBoot() {
        HHGC hhgc;
        this.alterInstructionAtPC = 0;
        List<String> testRom = this.originalRom;
        boolean looping;
        do {
            hhgc = new HHGC(testRom);
            looping = hhgc.boot();
            if (looping) {
                testRom = this.alterRom();
            }
        } while (looping);
        return hhgc.getAccumulator();
    }
}


public class Advent2020Day8 {

    private static void testPart1() throws IOException {
        int expected = 5;
        HHGC hhgc = HHGC.fromROMFile("2020/day8/test8a.txt");
        boolean looping = hhgc.boot();
        int actual = hhgc.getAccumulator();
        assert looping && actual == expected :
                String.format("Expected accumulator on looping to be '%s' but was '%s'!", expected, actual);
    }

    private static void part1() throws IOException {
        HHGC hhgc = HHGC.fromROMFile("2020/day8/input8.txt");
        hhgc.boot();
        System.out.printf("Day 8, Part 1, accumulator on looping is %s\n", hhgc.getAccumulator());
    }

    private static void testPart2() throws IOException {
        int expected = 8;
        HHGCTestHarness harness = new HHGCTestHarness("2020/day8/test8a.txt");
        int actual = harness.fixBoot();
        assert actual == expected : String.format("Expected accumulator when fixed to be '%s' but was '%s'!", expected, actual);
    }

    private static void part2() throws IOException {
        HHGCTestHarness harness = new HHGCTestHarness("2020/day8/input8.txt");
        System.out.printf("Day 8, Part 2, accumulator after fixing looping is %s\n", harness.fixBoot());
    }

    public static void main(final String[] args) throws IOException {
        testPart1();
        part1();
        testPart2();
        part2();
    }
}
