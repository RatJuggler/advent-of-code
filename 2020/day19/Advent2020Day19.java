package day19;


class MessageValidator {

    MessageValidator() {}

    static MessageValidator fromFile(final String filename) {
        return new MessageValidator();
    }

    int validate() {
        return 0;
    }
}


public class Advent2020Day19 {

    private static void testMessageValidator() {
        int expectedMessages = 2;
        MessageValidator validator = MessageValidator.fromFile("2020/day19/test19a.txt");
        int actualMessages = validator.validate();
        assert actualMessages == expectedMessages :
                String.format("Expected valid messages to be %d not %d!%n", expectedMessages, actualMessages);
    }

    public static void main(final String[] args) {
        testMessageValidator();
    }
}
