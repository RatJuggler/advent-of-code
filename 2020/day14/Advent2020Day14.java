package day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


interface Decoder {
    void apply(final Map<Long, Long> memory, final String mask, final long address, final long value);
}


class DecoderV1 implements Decoder {

    DecoderV1() {}

    private long applyMask(final String mask, final long value) {
        long result = value;
        for (int i = 0; i < mask.length(); i++) {
            long bitMask = 1L << (mask.length() - 1 - i);
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

    @Override
    public void apply(final Map<Long, Long> memory, final String mask, final long address, final long value) {
        memory.put(address, this.applyMask(mask, value));
    }
}


class DecoderV2 implements Decoder {

    DecoderV2() {}

    private List<Long> applyMask(final String mask, final long originalAddress) {
        List<Long> addresses = new ArrayList<>();
        addresses.add(originalAddress);
        for (int i = 0; i < mask.length(); i++) {
            long bitMask = 1L << (mask.length() - 1 - i);
            List<Long> newAddresses = new ArrayList<>(addresses.size());
            for (long address : addresses) {
                switch (mask.charAt(i)) {
                    case '1':
                        newAddresses.add(address | bitMask);
                        break;
                    case '0':
                        newAddresses.add(address);
                        break;
                    case 'X':
                        newAddresses.add(address | bitMask);
                        newAddresses.add(address & ~bitMask);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown bit type!");
                }
            }
            addresses = newAddresses;
        }
        return addresses;
    }

    @Override
    public void apply(final Map<Long, Long> memory, final String mask, final long address, final long value) {
        for (Long newAddress : this.applyMask(mask, address)) memory.put(newAddress, value);
    }
}


class DockingEmulator {

    private final List<String> program;
    private final Decoder decoder;

    DockingEmulator(final List<String> program, final Decoder decoder) {
        this.program = Collections.unmodifiableList(program);
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

    private void execute(final String instruction, final String mask, final Map<Long, Long> memory) {
        Matcher m = this.parseMemInstruction(instruction);
        long address = Long.parseLong(m.group("address"));
        long value = Long.parseLong(m.group("value"));
        this.decoder.apply(memory, mask, address, value);
    }

    long run() {
        int pc = 0;
        String mask = "";
        Map<Long, Long> memory = new HashMap<>();
        while (pc < this.program.size()) {
            String instruction = this.program.get(pc++);
            if (instruction.startsWith("mask = ")) {
                mask = instruction.substring(7);
            } else if (instruction.startsWith("mem")) {
                this.execute(instruction, mask, memory);
            } else {
                throw new IllegalStateException(String.format("Unknown instruction '%s'", instruction));
            }
        }
        return memory.values().stream().mapToLong(Long::longValue).sum();
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
