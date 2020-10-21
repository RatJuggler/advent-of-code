package day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


class Range implements Comparable<Object> {

    long from;
    long to;

    Range(final long from, final long to) {
        this.from = from;
        this.to = to;
    }

    static Range fromLine(final String line) {
        String pattern = "(?<from>\\d+)-(?<to>\\d+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse range line: " + line);
        }
        long from = Long.parseLong(m.group("from"));
        long to = Long.parseLong(m.group("to"));
        return new Range(from, to);
    }

    @Override
    public String toString() {
        return "Range{" + from + "-" + to + '}';
    }

    @Override
    public int compareTo(Object o) {
        Range r = (Range) o;
        return Long.compare(this.from, r.from);
    }
}


public class Advent20 {

    private static long firstFree(final String filename) throws IOException {
        Range[] ranges;
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            ranges = stream.map(Range::fromLine).sorted().toArray(Range[]::new);
        }
        System.out.println(Arrays.toString(ranges));
        List<Range> combined = new ArrayList<>();
        Range current = ranges[0];
        for (int i = 1; i < ranges.length; i++) {
            Range next = ranges[i];
            if (current.to < next.to) {
                if (current.to >= next.from - 1) {
                    current.to = next.to;
                } else {
                    combined.add(current);
                    current = next;
                }
            }
        }
        System.out.println(combined);
        return combined.get(0).to + 1;
    }

    private static void part1() throws IOException {
        System.out.printf("Day 20, Part 1 lowest value unblocked IP is %s.%n", firstFree("2016/day20/input20.txt"));
    }

    public static void main(String[] args) throws IOException {
        part1();
    }
}
