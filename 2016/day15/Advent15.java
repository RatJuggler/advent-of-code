package day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


class Disc {

    private final int disc;
    private final int positions;
    private final int currentPosition;

    Disc(final int disc, final int positions, final int startPosition) {
        this.disc = disc;
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
        int disc = Integer.parseInt(m.group("disc"));
        int positions = Integer.parseInt(m.group("positions"));
        int startPosition = Integer.parseInt(m.group("startPosition"));
        return new Disc(disc, positions, startPosition);
    }

    boolean atSlot(final int elapsed) {
        return (this.disc + this.currentPosition + elapsed) % this.positions == 0;
    }

    @Override
    public String toString() {
        return "Disc" + this.disc + "{positions=" + this.positions + ", currentPosition=" + this.currentPosition + '}';
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

    int pushButton() {
        int time = 0;
        boolean capsuleDropped = false;
        while (!capsuleDropped) {
            capsuleDropped = true;
            for (Disc disc : discs) {
                if (!disc.atSlot(time)) {
                    capsuleDropped = false;
                    break;
                }
            }
            time++;
        }
        return time - 1;
    }

    @Override
    public String toString() {
        return Arrays.toString(discs);
    }
}


public class Advent15 {

    private static void part2() throws IOException {
        Sculpture sculpture = Sculpture.fromFile("2016/day15/input15p2.txt");
        int capsuleTime  = sculpture.pushButton();
        System.out.printf("Day 15, Part 2 button should be pressed at time %s.%n", capsuleTime);
    }

    private static void part1() throws IOException {
        Sculpture sculpture = Sculpture.fromFile("2016/day15/input15p1.txt");
        int capsuleTime  = sculpture.pushButton();
        System.out.printf("Day 15, Part 1 button should be pressed at time %s.%n", capsuleTime);
    }

    private static void test() throws IOException {
        int expectedTime = 5;
        Sculpture sculpture = Sculpture.fromFile("2016/day15/test15a.txt");
        int capsuleTime  = sculpture.pushButton();
        assert capsuleTime == expectedTime: String.format("Expected time '%s' but was '%s'!", expectedTime, capsuleTime);
    }

    public static void main(String[] args) throws IOException {
        test();
        part1();
        part2();
    }
}
