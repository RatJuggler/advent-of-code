package day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Advent6 {

    private static String decodeMessage(final String filename, final boolean useMax, final int messageWidth) throws IOException {
        List<Map<Character, Integer>> occurrences = new ArrayList<>();
        for (int i = 0; i < messageWidth; i++) {
            occurrences.add(new HashMap<>());
        }
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.map(String::toCharArray)
                    .forEach(r -> IntStream.range(0, messageWidth)
                            .forEach(i -> occurrences.get(i).merge(r[i], 1, Integer::sum)));
        }
        StringBuilder message = new StringBuilder();
        occurrences.iterator().forEachRemaining(o -> {
            System.out.println(o);
            Optional<Map.Entry<Character, Integer>> occurrence;
            if (useMax) {
                occurrence = o.entrySet().stream().max(Map.Entry.comparingByValue());
            } else {
                occurrence = o.entrySet().stream().min(Map.Entry.comparingByValue());
            }
            message.append(occurrence.isPresent() ? occurrence.get().getKey() : '*');
        });
        return message.toString();
    }

    private static void testDecodeMessage(final boolean useMax, final String expectedMessage) throws IOException {
        String filename = "2016/day6/test6a.txt";
        String message = decodeMessage(filename, useMax, expectedMessage.length());
        assert message.equals(expectedMessage) : String.format("Expect message '%s' but was '%s'!", expectedMessage, message);
    }

    public static void main(String[] argc) throws IOException {
        testDecodeMessage(true, "easter");
        String message = decodeMessage("2016/day6/input6.txt", true,8);
        System.out.println(String.format("Day 6, Part 1 decoded message is %s.", message));
        testDecodeMessage(false, "advent");
    }

}
