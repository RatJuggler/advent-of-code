package day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


class Disc {

    private final int positions;
    private int currentPosition;

    Disc(final int positions, final int startPosition) {
        this.positions = positions;
        this.currentPosition = startPosition;
    }

    static Disc fromLine(final String line) {
        String pattern = "Disc #(?<disc>\\d+) has (?<positions>\\d+) positions; at time=0, it is at position (?<startPosition>\\d+).";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse disc line: " + line);
        }
        int positions = Integer.parseInt(m.group("positions"));
        int startPosition = Integer.parseInt(m.group("startPosition"));
        return new Disc(positions, startPosition);
    }

    int turn() {
        this.currentPosition = (this.currentPosition + 1) % this.positions;
        return this.currentPosition;
    }

    @Override
    public String toString() {
        return "Disc{positions=" + positions + ", currentPosition=" + currentPosition + '}';
    }
}


class Sculpture {

    private final Disc[] discs;

    Sculpture(final Disc[] discs) { this.discs = discs; }

    static Sculpture fromFile(final String filename) throws IOException {
        Disc[] discs;
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            discs = stream.map(Disc::fromLine).toArray(Disc[]::new);
        }
        return new Sculpture(discs);
    }

    @Override
    public String toString() {
        return Arrays.toString(discs);
    }
}


public class Advent15 {

    private static void part1() {
    }

    private static void test() throws IOException {
        Sculpture sculpture = Sculpture.fromFile("2016/day15/test15a.txt");
        System.out.println(sculpture.toString());
    }

    public static void main(String[] args) throws IOException {
        test();
        part1();
    }
}
