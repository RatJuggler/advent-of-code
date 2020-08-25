package day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;


class Screen {

    private final static char PIXEL_ON = '#';
    private final static char PIXEL_OFF = '.';

    private final int width;
    private final int height;
    private final char[] display;

    public Screen(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.display = new char[width * height];
        Arrays.fill(this.display, PIXEL_OFF);
    }

    void applyInstruction(final String instructionString) {
        Instruction instruction = new InstructionFactory().getInstruction(instructionString);
        instruction.apply();
    }

    long countLitPixels() {
        return IntStream.range(0, this.display.length)
                .mapToObj(c -> this.display[c])
                .filter(c -> c == PIXEL_ON)
                .count();
    }

    void display() {
        for (int i = 0; i < this.display.length; i++) {
            if (i % this.width == 0) {
                System.out.println();
            }
            System.out.print(this.display[i]);
        }
    }

    class InstructionFactory {

        InstructionFactory() {}

        Instruction getInstruction(final String instructionString) {
            String pattern = "^(?<command>rect|rotate column|rotate row)(?: | x=| y=)(?<arg1>\\d+)(?:x| by )(?<arg2>\\d+)$";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(instructionString);
            if (!m.find()) {
                throw new IllegalStateException("Unable to parse instruction: " + instructionString);
            }
            String command = m.group("command");
            String arg1 = m.group("arg1");
            String arg2 = m.group("arg2");
            System.out.printf("%s(%s, %s)%n", command, arg1, arg2);
            return buildInstruction(command, Integer.parseInt(arg1), Integer.parseInt(arg2));
        }

        Instruction buildInstruction(final String command, final int arg1, final int arg2) {
            switch (command) {
                case "rect":
                    return new Rect(arg1, arg2);
                case "rotate row":
                    return new RotateRow(arg1, arg2);
                case "rotate column":
                    return new RotateColumn(arg1, arg2);
                    default:
                        throw new IllegalStateException(String.format("Unknown command '%s'", command));
            }
        }
    }

    abstract static class Instruction {

        final int arg1;
        final int arg2;

        Instruction(final int arg1, final int arg2) {
            this.arg1 = arg1;
            this.arg2 = arg2;
        }

        abstract void apply();
    }

    class Rect extends Instruction {

        Rect(final int arg1, final int arg2) {
            super(arg1, arg2);
        }

        void apply() {
            for (int i = 0; i < this.arg2; i++) {
                int row = i * Screen.this.width;
                for (int j = 0; j < this.arg1; j++) {
                    Screen.this.display[row + j] = Screen.PIXEL_ON;
                }
            }
        }
    }

    class RotateRow extends Instruction {

        RotateRow(final int arg1, final int arg2) {
            super(arg1, arg2);
        }

        void apply() {
            int row = this.arg1 * Screen.this.width;
            char[] rowContents = new char[Screen.this.width];
            System.arraycopy(Screen.this.display, row, rowContents, 0, Screen.this.width);
            for (int i = 0; i < Screen.this.width; i++) {
                int shiftTo = (i + this.arg2) % Screen.this.width;
                Screen.this.display[row + shiftTo] = rowContents[i];
            }
        }
    }

    class RotateColumn extends Instruction {

        RotateColumn(final int arg1, final int arg2) {
            super(arg1, arg2);
        }

        void apply() {
            char[] columnContents = new char[Screen.this.height];
            for (int i = 0; i < Screen.this.height; i++) {
                columnContents[i] = Screen.this.display[i * Screen.this.width + this.arg1];
            }
            for (int i = 0; i < Screen.this.height; i++) {
                int shiftTo = ((i + this.arg2) % Screen.this.height) * Screen.this.width;
                Screen.this.display[shiftTo + this.arg1] = columnContents[i];
            }
        }

    }
}


public class Advent8 {

    private static Screen swipeCard(final String filename, final int width, final int height) throws IOException {
        Screen screen = new Screen(width, height);
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(screen::applyInstruction);
        }
        return screen;
    }

    private static void testSwipeCard() throws IOException {
        String filename = "2016/day8/test8a.txt";
        long expectedLitPixels = 6;
        Screen screen = swipeCard(filename, 7, 3);
        long litPixels = screen.countLitPixels();
        assert litPixels == expectedLitPixels:
                String.format("Expected %d pixels to be lit but was %d!", expectedLitPixels, litPixels);
    }

    public static void main(String[] args) throws IOException {
        testSwipeCard();
        Screen screen = swipeCard("2016/day8/input8.txt", 50, 6);
        System.out.printf("Day 8, Part 1 found %d pixels lit.%n", screen.countLitPixels());
        System.out.println("Day 8, Part 2");
        screen.display();
    }

}
