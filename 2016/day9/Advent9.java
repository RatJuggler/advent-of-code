package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Advent9 {

    private static Matcher parseDecompress(final String sequence) {
        String pattern = "^\\((?<take>\\d+)x(?<repeat>\\d+)\\)(?<remaining>.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(sequence);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse decompress sequence: " + sequence);
        }
        return m;
    }

    private static long decompress1(final String sequence) {
        if (sequence.isEmpty()) {
            return 0;
        }
        char process = sequence.charAt(0);
        if (process == '(') {
            Matcher m = parseDecompress(sequence);
            int take = Integer.parseInt(m.group("take"));
            int repeat = Integer.parseInt(m.group("repeat"));
            String remaining = m.group("remaining");
            return (take * repeat) + decompress1(remaining.substring(take));
        }
        return 1 + decompress1(sequence.substring(1));
    }

    private static long decompress2(final String sequence) {
        if (sequence.isEmpty()) {
            return 0;
        }
        char process = sequence.charAt(0);
        if (process == '(') {
            Matcher m = parseDecompress(sequence);
            int take = Integer.parseInt(m.group("take"));
            int repeat = Integer.parseInt(m.group("repeat"));
            String remaining = m.group("remaining");
            return (decompress2(remaining.substring(0, take)) * repeat) + decompress2(remaining.substring(take));
        }
        return 1 + decompress2(sequence.substring(1));
    }

    private static void testDecompress(final String sequence, final long expectedLength, Function<String, Long> decompress) {
        Long decompressedLength = decompress.apply(sequence);
        assert decompressedLength == expectedLength :
                String.format("Expected decompressed length to be %d but was %d!", expectedLength, decompressedLength);
    }

    private static Long decompressedLength(Function<String, Long> decompress) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get("2016/day9/input9.txt"))) {
            return stream.map(decompress)
                    .reduce(0L, Long::sum);
        }
    }

    public static void main(final String[] args) throws IOException {
        testDecompress("ADVENT", 6, Advent9::decompress1);
        testDecompress("A(1x5)BC", 7, Advent9::decompress1);
        testDecompress("(3x3)XYZ", 9, Advent9::decompress1);
        testDecompress("A(2x2)BCD(2x2)EFG", 11, Advent9::decompress1);
        testDecompress("(6x1)(1x3)A", 6, Advent9::decompress1);
        testDecompress("X(8x2)(3x3)ABCY", 18, Advent9::decompress1);
        System.out.printf("Day 9, Part 1 uncompressed length is %d.%n", decompressedLength(Advent9::decompress1));
        testDecompress("(3x3)XYZ", 9, Advent9::decompress2);
        testDecompress("X(8x2)(3x3)ABCY", 20, Advent9::decompress2);
        testDecompress("(27x12)(20x12)(13x14)(7x10)(1x12)A", 241920, Advent9::decompress2);
        testDecompress("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN", 445, Advent9::decompress2);
        System.out.printf("Day 9, Part 2 uncompressed length is %d.%n", decompressedLength(Advent9::decompress2));
    }

}
