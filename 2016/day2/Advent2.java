package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


final class Button {

    private final char id;
    private final Map<Character, Character> moveToButton;

    Button(final char id, final char[][] moveToButton) {
        this.id = id;
        this.moveToButton = Stream.of(moveToButton)
                .collect(Collectors.toMap(data -> data[0], data -> data[1]));
    }

    char getId() {
        return this.id;
    }

    char move(final char direction) {
        Character moveTo = this.moveToButton.get(direction);
        if (moveTo == null) {
            throw new IllegalStateException(String.format("Direction '%s' not supported in %s!", direction, this));
        }
        return moveTo;
    }

    @Override
    public String toString() {
        String mapAsString = this.moveToButton.keySet().stream()
                .map(key -> key + "=" + this.moveToButton.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
        return String.format("Button(id:%s, moveToButton:%s)", this.id, mapAsString);
    }
}


final class Keypad {

    private final Map<Character, Button> buttons = new HashMap<>();
    private Button button;
    private String code;

    Keypad() {}

    void addButton(final char id, final char[][] moveToButton) {
        this.buttons.put(id, new Button(id, moveToButton));
    }

    void reset() {
        this.button = this.buttons.get('5');
        this.code = "";
    }

    private void followInstruction(final char instruction) {
        char newButton = this.button.move(instruction);
        Button button = this.buttons.get(newButton);
        if (button == null) {
            throw new IllegalStateException(
                    String.format("Button not found for instruction '%s' from button '%s'!", instruction, this.button));
        }
        this.button = button;
        System.out.printf("%s -> %s%n", instruction, this.button);
    }

    void followInstructions(final String instructions) {
        assert !this.buttons.isEmpty(): "This keypad has no buttons!";
        assert this.button != null: "Reset this keypad to use it!";
        for (char instruction: instructions.toCharArray()) {
            followInstruction(instruction);
        }
        this.code += this.button.getId();
        System.out.printf("Code now -> %s%n", this.code);
    }

    String codeFound() {
        return this.code;
    }
}


final class KeypadFactory {

    KeypadFactory() {}

    static Keypad buildSquareKeypad() {
        Keypad keypad = new Keypad();
        keypad.addButton('1', new char[][]{{'U', '1'}, {'R', '2'}, {'D', '4'}, {'L', '1'}});
        keypad.addButton('2', new char[][]{{'U', '2'}, {'R', '3'}, {'D', '5'}, {'L', '1'}});
        keypad.addButton('3', new char[][]{{'U', '3'}, {'R', '3'}, {'D', '6'}, {'L', '2'}});
        keypad.addButton('4', new char[][]{{'U', '1'}, {'R', '5'}, {'D', '7'}, {'L', '4'}});
        keypad.addButton('5', new char[][]{{'U', '2'}, {'R', '6'}, {'D', '8'}, {'L', '4'}});
        keypad.addButton('6', new char[][]{{'U', '3'}, {'R', '6'}, {'D', '9'}, {'L', '5'}});
        keypad.addButton('7', new char[][]{{'U', '4'}, {'R', '8'}, {'D', '7'}, {'L', '7'}});
        keypad.addButton('8', new char[][]{{'U', '5'}, {'R', '9'}, {'D', '8'}, {'L', '7'}});
        keypad.addButton('9', new char[][]{{'U', '6'}, {'R', '9'}, {'D', '9'}, {'L', '8'}});
        return keypad;
    }

    static Keypad buildDiamondKeypad() {
        Keypad keypad = new Keypad();
        keypad.addButton('1', new char[][]{{'U', '1'}, {'R', '1'}, {'D', '3'}, {'L', '1'}});
        keypad.addButton('2', new char[][]{{'U', '2'}, {'R', '3'}, {'D', '6'}, {'L', '2'}});
        keypad.addButton('3', new char[][]{{'U', '1'}, {'R', '4'}, {'D', '7'}, {'L', '2'}});
        keypad.addButton('4', new char[][]{{'U', '4'}, {'R', '4'}, {'D', '8'}, {'L', '3'}});
        keypad.addButton('5', new char[][]{{'U', '5'}, {'R', '6'}, {'D', '5'}, {'L', '5'}});
        keypad.addButton('6', new char[][]{{'U', '2'}, {'R', '7'}, {'D', 'A'}, {'L', '5'}});
        keypad.addButton('7', new char[][]{{'U', '3'}, {'R', '8'}, {'D', 'B'}, {'L', '6'}});
        keypad.addButton('8', new char[][]{{'U', '4'}, {'R', '9'}, {'D', 'C'}, {'L', '7'}});
        keypad.addButton('9', new char[][]{{'U', '9'}, {'R', '9'}, {'D', '9'}, {'L', '8'}});
        keypad.addButton('A', new char[][]{{'U', '6'}, {'R', 'B'}, {'D', 'A'}, {'L', 'A'}});
        keypad.addButton('B', new char[][]{{'U', '7'}, {'R', 'C'}, {'D', 'D'}, {'L', 'A'}});
        keypad.addButton('C', new char[][]{{'U', '8'}, {'R', 'C'}, {'D', 'C'}, {'L', 'B'}});
        keypad.addButton('D', new char[][]{{'U', 'B'}, {'R', 'D'}, {'D', 'D'}, {'L', 'D'}});
        return keypad;
    }
}


final class Advent2 {

    private static String determineCode(final String filename, final Keypad keypad) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(keypad::followInstructions);
        }
        return keypad.codeFound();
    }

    private static void testDetermineCode(final String expectedCode, final Keypad keypad)
            throws IOException {
        String code = determineCode("2016/day2/test2a.txt", keypad);
        assert code.equals(expectedCode):
                String.format("Expected code '%s' but was '%s'!", expectedCode, code);
    }

    public static void main(final String[] args) throws IOException {
        Keypad keypad1 = KeypadFactory.buildSquareKeypad();
        keypad1.reset();
        testDetermineCode("1985", keypad1);
        keypad1.reset();
        String code1 = determineCode("2016/day2/input2.txt", keypad1);
        System.out.printf("Day 2, Part 1 the bathroom code is '%s'.%n", code1);
        Keypad keypad2 = KeypadFactory.buildDiamondKeypad();
        keypad2.reset();
        testDetermineCode("5DB3", keypad2);
        keypad2.reset();
        String code2 = determineCode("2016/day2/input2.txt", keypad2);
        System.out.printf("Day 2, Part 2 the bathroom code is '%s'.%n", code2);
    }

}
