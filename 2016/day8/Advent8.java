package day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;


class Screen {

    private final static char PIXEL_ON = '#';
    private final static char PIXEL_OFF = '.';

    private final List<char[]> display = new ArrayList<>();

    public Screen(final int width, final int height) {
        for (int i = 0; i < height; i++) {
            char[] row = new char[width];
            Arrays.fill(row, PIXEL_OFF);
            this.display.add(row);
        }
    }

    void applyInstruction(final String instruction) {
        String pattern = "^(?<command>rect|rotate column|rotate row)(?: | x=| y=)(?<arg1>\\d+)(?:x| by )(?<arg2>\\d+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(instruction);
        while (m.find()) {
            String command = m.group("command");
            String arg1 = m.group("arg1");
            String arg2 = m.group("arg2");
            System.out.println(String.format("%s(%s, %s)", command, arg1, arg2));
        }
    }

    int countLitPixels() {
        int litPixels = 0;
        for (char[] row : this.display) {
            litPixels += IntStream.range(0, row.length)
                    .mapToObj(c -> row[c])
                    .filter(c -> c == PIXEL_ON)
                    .count();
        }
        return litPixels;
    }

}


public class Advent8 {

    private static int swipeCard(final String filename, final int width, final int height) throws IOException {
        Screen screen = new Screen(width, height);
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(screen::applyInstruction);
        }
        return screen.countLitPixels();
    }

    private static void testSwipeCard() throws IOException {
        String filename = "2016/day8/test8a.txt";
        int expectedLitPixels = 6;
        int litPixels = swipeCard(filename, 7, 3);
        assert litPixels == expectedLitPixels:
                String.format("Expected %d pixels to be lit but was %d!", expectedLitPixels, litPixels);
    }

    public static void main(String[] args) throws IOException {
        testSwipeCard();
        int litPixels = swipeCard("2016/day8/input8.txt", 50, 6);
        System.out.println(String.format("Day 8, Part 1 found %d pixels lit.", litPixels));
    }
}
