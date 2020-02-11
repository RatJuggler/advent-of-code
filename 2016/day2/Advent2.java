package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


final class Button {

    private final char id;
    private final char[] moveToButton;

    Button(final char id, final char[] moveToButton) {
        this.id = id;
        this.moveToButton = moveToButton;
    }

    char getId() {
        return this.id;
    }

    char move(final char direction) {
        switch (direction) {
            case 'U':
                return this.moveToButton[0];
            case 'R':
                return this.moveToButton[1];
            case 'D':
                return this.moveToButton[2];
            case 'L':
                return this.moveToButton[3];
            default:
                throw new IllegalStateException(String.format("Impossible direction found '%s'!", direction));
        }
    }

    @Override
    public String toString() {
        return String.format("Button(id:%s, moveToButton:%s)", this.id, Arrays.toString(this.moveToButton));
    }
}


final class Keypad {

    private Map<Character, Button> buttons = new HashMap<>();
    private Button button;
    private String code = "";

    Keypad() {
        // U, R, D, L
        addButton('1', new char[]{'1', '2', '4', '1'});
        addButton('2', new char[]{'2', '3', '5', '1'});
        addButton('3', new char[]{'3', '3', '6', '2'});
        addButton('4', new char[]{'1', '5', '7', '4'});
        addButton('5', new char[]{'2', '6', '8', '4'});
        addButton('6', new char[]{'3', '6', '9', '5'});
        addButton('7', new char[]{'4', '8', '7', '7'});
        addButton('8', new char[]{'5', '9', '8', '7'});
        addButton('9', new char[]{'6', '9', '9', '8'});
        this.button = this.buttons.get('5');
    }

    private void addButton(final char id, final char[] moveToButton) {
        this.buttons.put(id, new Button(id, moveToButton));
    }

    void followInstruction(final char instruction) {
        char newButton = this.button.move(instruction);
        if (this.buttons.get(newButton) == null) {
            throw new IllegalStateException(String.format("Button not found for instruction '%s' from button '%s'!", instruction, this.button));
        }
        this.button = this.buttons.get(newButton);
        System.out.println(String.format("%s -> %s", instruction, this.button));
    }

    void followInstructions(final String instructions) {
        for (char instruction: instructions.toCharArray()) {
            followInstruction(instruction);
        }
        this.code += this.button.getId();
        System.out.println(String.format("Code now -> %s", this.code));
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
