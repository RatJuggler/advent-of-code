package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Advent9 {

    private static String parseDecompress(final String sequence) {
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
        return decompressed + decompress(remaining.substring(take));
    }

    private static String decompress(final String sequence) {
        System.out.println(sequence);
        if (sequence.isEmpty()) {
            return sequence;
        }
        char process = sequence.charAt(0);
        if (process == '(') {
            return parseDecompress(sequence);
        }
        return process + decompress(sequence.substring(1));
    }

    private static void testDecompress(final String sequence, final String expectedResult) {
        String decompressed = decompress(sequence);
        assert decompressed.equals(expectedResult):
                String.format("Expected decompressed result to be '%s' but was '%s'!", expectedResult, decompressed);
    }

    private static Integer decompressedLength(final String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.map(Advent9::decompress)
                    .mapToInt(String::length)
                    .sum();
        }
    }

    public static void main(final String[] args) throws IOException {
        testDecompress("ADVENT", "ADVENT");
        testDecompress("A(1x5)BC", "ABBBBBC");
        testDecompress("(3x3)XYZ", "XYZXYZXYZ");
        testDecompress("A(2x2)BCD(2x2)EFG", "ABCBCDEFEFG");
        testDecompress("(6x1)(1x3)A", "(1x3)A");
        testDecompress("X(8x2)(3x3)ABCY", "X(3x3)ABC(3x3)ABCY");
        System.out.printf("Day 9, Part 1 uncompressed length is %d.%n", decompressedLength("2016/day9/input9.txt"));
    }

}
