package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


final class Keypad {

    private char button = '5';
    private String code = "";

    Keypad() {}

    void followInstruction(final char instruction) {
        switch (this.button) {
            case '1':
                if (instruction == 'R') {
                    this.button = '2';
                } else if (instruction == 'D') {
                    this.button = '4';
                }
                break;
            case '2':
                if (instruction == 'R') {
                    this.button = '3';
                } else if (instruction == 'D') {
                    this.button = '5';
                } else if (instruction == 'L') {
                    this.button = '1';
                }
                break;
            case '3':
                if (instruction == 'D') {
                    this.button = '6';
                } else if (instruction == 'L') {
                    this.button = '2';
                }
                break;
            case '4':
                if (instruction == 'U') {
                    this.button = '1';
                } else if (instruction == 'R') {
                    this.button = '5';
                } else if (instruction == 'D') {
                    this.button = '7';
                }
                break;
            case '5':
                if (instruction == 'U') {
                    this.button = '2';
                } else if (instruction == 'R') {
                    this.button = '6';
                } else if (instruction == 'D') {
                    this.button = '8';
                } else if (instruction == 'L') {
                    this.button = '4';
                }
                break;
            case '6':
                if (instruction == 'U') {
                    this.button = '3';
                } else if (instruction == 'D') {
                    this.button = '9';
                } else if (instruction == 'L') {
                    this.button = '5';
                }
                break;
            case '7':
                if (instruction == 'U') {
                    this.button = '4';
                } else if (instruction == 'R') {
                    this.button = '8';
                }
                break;
            case '8':
                if (instruction == 'U') {
                    this.button = '5';
                } else if (instruction == 'R') {
                    this.button = '9';
                } else if (instruction == 'L') {
                    this.button = '7';
                }
                break;
            case '9':
                if (instruction == 'U') {
                    this.button = '6';
                } else if (instruction == 'L') {
                    this.button = '8';
                }
                break;
            default:
                throw new IllegalStateException(String.format("Impossible button found '%s'!", this.button));
        }
        System.out.println(String.format("%s -> %s", instruction, this.button));
    }

    void followInstructions(final String instructions) {
        for (char instruction: instructions.toCharArray()) {
            followInstruction(instruction);
        }
        this.code += this.button;
    }

    String codeFound() {
        return this.code;
    }
}


final class Advent2 {

    private static String determineCode(final String filename) throws IOException {
        Keypad keypad = new Keypad();
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(keypad::followInstructions);
        }
        return keypad.codeFound();
    }

    private static void testDetermineCode(final String filename, final String expectedCode) throws IOException {
        String code = determineCode(filename);
        assert code.equals(expectedCode):
                String.format("Expected code '%s' but was '%s'!", expectedCode, code);
    }

    public static void main(final String[] args) throws IOException {
        testDetermineCode("2016/day2/test2a.txt", "1985");
        String code = determineCode("2016/day2/input2.txt");
        System.out.println(String.format("Day 2, Part 1 the bathroom code is '%s'.", code));
    }

}
