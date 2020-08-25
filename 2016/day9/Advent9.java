package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Advent9 {

    private static int parseDecompress1(final String sequence) {
        String pattern = "^\\((?<take>\\d+)x(?<repeat>\\d+)\\)(?<remaining>.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(sequence);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse decompress sequence: " + sequence);
        }
        int take = Integer.parseInt(m.group("take"));
        int repeat = Integer.parseInt(m.group("repeat"));
        String remaining = m.group("remaining");
        String decompressed = remaining.substring(0, take).repeat(repeat);
        return decompressed.length() + decompress1(remaining.substring(take));
    }

    private static int decompress1(final String sequence) {
        System.out.println(sequence);
        if (sequence.isEmpty()) {
            return 0;
        }
        char process = sequence.charAt(0);
        if (process == '(') {
            return parseDecompress1(sequence);
        }
        return 1 + decompress1(sequence.substring(1));
    }

    private static int parseDecompress2(final String sequence) {
        String pattern = "^\\((?<take>\\d+)x(?<repeat>\\d+)\\)(?<remaining>.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(sequence);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse decompress sequence: " + sequence);
        }
        int take = Integer.parseInt(m.group("take"));
        int repeat = Integer.parseInt(m.group("repeat"));
        String remaining = m.group("remaining");
        String decompressed = remaining.substring(0, take).repeat(repeat);
        return decompress2(decompressed) + decompress2(remaining.substring(take));
    }

    private static int decompress2(final String sequence) {
        System.out.println(sequence);
        if (sequence.isEmpty()) {
            return 0;
        }
        char process = sequence.charAt(0);
        if (process == '(') {
            return parseDecompress2(sequence);
        }
        return 1 + decompress2(sequence.substring(1));
    }

    private static void testDecompress1(final String sequence, final int expectedLength) {
        int decompressedLength = decompress1(sequence);
        assert decompressedLength == expectedLength :
                String.format("Expected decompressed length to be %d but was %d!", expectedLength, decompressedLength);
    }

    private static Integer decompressedLength1(final String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.mapToInt(Advent9::decompress1)
                    .sum();
        }
    }

    private static void testDecompress2(final String sequence, final int expectedLength) {
        int decompressedLength = decompress2(sequence);
        assert decompressedLength == expectedLength :
                String.format("Expected decompressed length to be %d but was %d!", expectedLength, decompressedLength);
    }

    public static void main(final String[] args) throws IOException {
        testDecompress1("ADVENT", 6);
        testDecompress1("A(1x5)BC", 7);
        testDecompress1("(3x3)XYZ", 9);
        testDecompress1("A(2x2)BCD(2x2)EFG", 11);
        testDecompress1("(6x1)(1x3)A", 6);
        testDecompress1("X(8x2)(3x3)ABCY", 18);
        System.out.printf("Day 9, Part 1 uncompressed length is %d.%n", decompressedLength1("2016/day9/input9.txt"));
        testDecompress2("(3x3)XYZ", 9);
        testDecompress2("X(8x2)(3x3)ABCY", 20);
        testDecompress2("(27x12)(20x12)(13x14)(7x10)(1x12)A", 241920);
        testDecompress2("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN", 445);
    }

}
