package day6;

public class Advent6 {

    private static String decodeMessage(final String filename) {
        return "xxx";
    }

    private static void testDecodeMessage() {
        String filename = "2016/day6/test6a.txt";
        String expectedMessage = "easter";
        String message = decodeMessage(filename);
        assert message.equals(expectedMessage) : String.format("Expect message '%s' but was '%s'!", expectedMessage, message);
    }

    public static void main(String[] argc) {
        testDecodeMessage();
    }

}
