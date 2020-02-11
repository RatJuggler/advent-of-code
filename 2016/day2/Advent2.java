package day2;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

final class Button {

    Button() {}

}


final class Keypad {

    private final Button one = new Button();

    Keypad() {}
}


final class Advent2 {

    private static String determineCode(final String filename) throws IOException {
        // Initialise keypad to start from 5
        // for each line from file
        //     for each character on line
        //         move on keypad in direction of character
        //     record current keypad number as next digit of code
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(System.out::println);
        }
        return "0000";
    }

    private static void testDetermineCode(final String filename, final String expectedCode) throws IOException {
        String code = determineCode(filename);
        assert code.equals(expectedCode):
                String.format("Expected code '%s' but was '%s'!", expectedCode, code);
    }

    public static void main(final String[] args) throws IOException {
        testDetermineCode("2016/day2/test2a.txt", "1985");
        String code = determineCode("2016/day2/input2.txt");
        System.out.println(String.format("Day 2, Part 2 the bathroom code is '%s'.", code));
    }

}
