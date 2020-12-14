package day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


interface Decoder {
    void apply(final long[] memory, final String mask, final int address, final long value);
}


class DecoderV1 implements Decoder {

    DecoderV1() {}

    private long applyMask(final String mask, final long value) {
        long result = value;
        for (int i = 0; i < mask.length(); i++) {
            long bitMask = 1L << (35 - i);
            switch (mask.charAt(i)) {
                case '1':
                    result = result | bitMask;
                    break;
                case '0':
                    result = result & ~bitMask;
                    break;
                case 'X':
                    break;
                default:
                    throw new IllegalArgumentException("Unknown bit type!");
            }
        }
        return result;
    }

    public void apply(final long[] memory, final String mask, final int address, final long value) {
        memory[address] = this.applyMask(mask, value);
    }
}


class DecoderV2 implements Decoder {

    DecoderV2() {}

    public void apply(final long[] memory, final String mask, final int address, final long value) {

    }
}


class DockingEmulator {

    private final List<String> program;
    private final Decoder decoder;
    private final long[] memory = new long[65536];

    DockingEmulator(final List<String> program, final Decoder decoder) {
        this.program = program;
        this.decoder = decoder;
    }

    static DockingEmulator fromFile(final String filename, final Decoder decoder) {
        List<String> program;
        try {
            program = Files.readAllLines(Paths.get(filename));
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Unable to read program file!", ioe);
        }
        return new DockingEmulator(program, decoder);
    }

    private Matcher parseMemInstruction(final String instruction) {
        String pattern = "^mem\\[(?<address>\\d+)\\] = (?<value>\\d+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(instruction);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse instruction: " + instruction);
        }
        return m;
    }

    private void execute(final String instruction, final String mask) {
        Matcher m = this.parseMemInstruction(instruction);
        int address = Integer.parseInt(m.group("address"));
        long value = Long.parseLong(m.group("value"));
        this.decoder.apply(this.memory, mask, address, value);
    }

    long run() {
        int pc = 0;
        String mask = "";
        while (pc < this.program.size()) {
            String instruction = this.program.get(pc++);
            if (instruction.startsWith("mask = ")) {
                mask = instruction.substring(7);
            } else if (instruction.startsWith("mem")) {
                this.execute(instruction, mask);
            } else {
                throw new IllegalStateException(String.format("Unknown instruction '%s'", instruction));
            }
        }
        return Arrays.stream(this.memory).sum();
    }
}


public class Advent2020Day14 {

    public static void testDockingEmulatorV1() {
        long expectedSum = 165L;
        DockingEmulator emulator = DockingEmulator.fromFile("2020/day14/test14a.txt", new DecoderV1());
        long actualSum = emulator.run();
        assert actualSum == expectedSum : String.format("Expected memory sum to be %d not %d!%n", expectedSum, actualSum);
    }

    public static void testDockingEmulatorV2() {
        long expectedSum = 208L;
        DockingEmulator emulator = DockingEmulator.fromFile("2020/day14/test14b.txt", new DecoderV2());
        long actualSum = emulator.run();
        assert actualSum == expectedSum : String.format("Expected memory sum to be %d not %d!%n", expectedSum, actualSum);
    }

    public static void main(final String[] args) {
        testDockingEmulatorV1();
        DockingEmulator emulatorV1 = DockingEmulator.fromFile("2020/day14/input14.txt", new DecoderV1());
        System.out.printf("Day 14, Part 1, sum of memory is %s\n", emulatorV1.run());
        testDockingEmulatorV2();
        DockingEmulator emulatorV2 = DockingEmulator.fromFile("2020/day14/input14.txt", new DecoderV2());
        System.out.printf("Day 14, Part 2, sum of memory is %s\n", emulatorV2.run());
    }
}
