package day6;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


public class Advent6 {

    private static String decodeMessage(final String filename) throws IOException {
        int[][] occurrences = new int[6][26];
        for (int i = 0; i < 6; i++) {
            Arrays.fill(occurrences[i], 0);
        }
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            for (int i = 0; i < 6; i++) {
                occurrences[i][row.charAt(i) - 'a']++;
            }
        }
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int highestOccurrences = 0;
            int atIndex = 0;
            for (int j = 0; j < 26; j++) {
                if (occurrences[i][j] > highestOccurrences) {
                    highestOccurrences = occurrences[i][j];
                    atIndex = j;
                }
            }
            message.append((char) ('a' + atIndex));
        }
        return message.toString();
    }

    private static void testDecodeMessage() throws IOException {
        String filename = "2016/day6/test6a.txt";
        String expectedMessage = "easter";
        String message = decodeMessage(filename);
        assert message.equals(expectedMessage) : String.format("Expect message '%s' but was '%s'!", expectedMessage, message);
    }

    public static void main(String[] argc) throws IOException {
        testDecodeMessage();
    }

}
