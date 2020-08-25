package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Advent9 {

    private static int parseDecompress(final String sequence) {
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
        return decompressed.length() + decompress(remaining.substring(take));
    }

    private static int decompress(final String sequence) {
        System.out.println(sequence);
        if (sequence.isEmpty()) {
            return 0;
        }
        char process = sequence.charAt(0);
        if (process == '(') {
            return parseDecompress(sequence);
        }
        return 1 + decompress(sequence.substring(1));
    }

    private static void testDecompress(final String sequence, final int expectedLength) {
        int decompressedLength = decompress(sequence);
        assert decompressedLength == expectedLength:
                String.format("Expected decompressed length to be %d but was %d!", expectedLength, decompressedLength);
    }

    private static Integer decompressedLength(final String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.mapToInt(Advent9::decompress)
                    .sum();
        }
    }

    public static void main(final String[] args) throws IOException {
        testDecompress("ADVENT", 6);
        testDecompress("A(1x5)BC", 7);
        testDecompress("(3x3)XYZ", 9);
        testDecompress("A(2x2)BCD(2x2)EFG", 11);
        testDecompress("(6x1)(1x3)A", 6);
        testDecompress("X(8x2)(3x3)ABCY", 18);
        System.out.printf("Day 9, Part 1 uncompressed length is %d.%n", decompressedLength("2016/day9/input9.txt"));
    }

}
