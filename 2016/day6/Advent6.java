package day6;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Advent6 {

    private static String decodeMessage(final String filename, final int messageWidth) throws IOException {
        int[][] occurrences = new int[messageWidth][26];
        for (int i = 0; i < messageWidth; i++) {
            Arrays.fill(occurrences[i], 0);
        }
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.map(String::toCharArray)
                    .forEach(r -> IntStream.range(0, messageWidth)
                            .mapToObj(i -> new int[] {i, r[i] - 'a'})
                            .forEach(j -> occurrences[j[0]][j[1]]++));
        }
        for (int i = 0; i < messageWidth; i++) {
            System.out.println(Arrays.toString(occurrences[i]));
        }
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < messageWidth; i++) {
            int highestOccurrenceAt = 0;
            for (int j = 0; j < occurrences[i].length; j++) {
                if (occurrences[i][j] > occurrences[i][highestOccurrenceAt]) {
                    highestOccurrenceAt = j;
                }
            }
            message.append((char) ('a' + highestOccurrenceAt));
        }
        return message.toString();
    }

    private static void testDecodeMessage() throws IOException {
        String filename = "2016/day6/test6a.txt";
        String expectedMessage = "easter";
        String message = decodeMessage(filename, expectedMessage.length());
        assert message.equals(expectedMessage) : String.format("Expect message '%s' but was '%s'!", expectedMessage, message);
    }

    public static void main(String[] argc) throws IOException {
        testDecodeMessage();
    }

}
