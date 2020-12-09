package day6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Advent2020Day6 {

    private static final Function<List<String>, Long> countUniqueAnswers =
            answers -> String.join("", answers).chars().distinct().count();

    private static final Function<List<String>, Long> countDuplicateAnswers =
            answers -> String.join("", answers).chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.groupingBy(k -> k, Collectors.counting())).entrySet().stream()
                    .filter(e -> e.getValue() == answers.size())
                    .count();

    private static void testCountAnswers(final List<String> answers, final Function<List<String>, Long> count, final long expected) {
        assert count.apply(answers) == expected :
                String.format("Expected unique answer count for \"%s\" to be %d!", answers, expected);
    }

    private static long totalAnswers(final String filename, final Function<List<String>, Long> count) throws FileNotFoundException {
        long totalAnswers = 0;
        List<String> groupAnswers = new ArrayList<>();
        Scanner s = new Scanner(new File(filename));
        while (s.hasNext()) {
            String line = s.nextLine();
            if ("".equals(line)) {
                totalAnswers += count.apply(groupAnswers);
                groupAnswers.clear();
            } else {
                groupAnswers.add(line);
            }
        }
        totalAnswers += count.apply(groupAnswers);
        return totalAnswers;
    }

    public static void main(final String[] args) throws FileNotFoundException {
        testCountAnswers(List.of("abcx", "abcy", "abcz"), countUniqueAnswers, 6);
        assert totalAnswers("2020/day6/test6a.txt", countUniqueAnswers) == 11 : "Expected total unique answers to be 11!";
        System.out.printf("Day 6, part 1, total unique answer count is %d.%n", totalAnswers("2020/day6/input6.txt", countUniqueAnswers));
        testCountAnswers(List.of("abcx", "abcy", "abcz"), countDuplicateAnswers, 3);
        assert totalAnswers("2020/day6/test6a.txt", countDuplicateAnswers) == 6 : "Expected total duplicate answers to be 6!";
        System.out.printf("Day 6, part 1, total duplicate answer count is %d.%n", totalAnswers("2020/day6/input6.txt", countDuplicateAnswers));
    }
}
