package day6;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;


public class Advent2020Day6 {

    private static long countUniqueAnswers(final String answers) {
        return answers.chars().distinct().count();
    }

    private static void testCountUniqueAnswers(final String answers, final long expected) {
        assert countUniqueAnswers(answers) == expected :
                String.format("Expected unique answer count for \"%s\" to be %d!", answers, expected);
    }

    private static void testPart1CountUniqueAnswers() {
        testCountUniqueAnswers("abcxabcyabcz", 6);
    }

    private static long totalUniqueAnswers(final String filename) throws IOException {
        long totalUniqueAnswers = 0;
        StringBuilder groupAnswers = new StringBuilder();
        try (Scanner s = new Scanner(new File(filename))) {
            while (s.hasNext()) {
                String line = s.nextLine();
                if ("".equals(line)) {
                    totalUniqueAnswers += countUniqueAnswers(groupAnswers.toString());
                    groupAnswers = new StringBuilder();
                }
                groupAnswers.append(line);
            }
        }
        totalUniqueAnswers += countUniqueAnswers(groupAnswers.toString());
        return totalUniqueAnswers;
    }

    public static void main(final String[] args) throws IOException {
        testPart1CountUniqueAnswers();
        assert totalUniqueAnswers("2020/day6/test6a.txt") == 11 : "Expected total unique answers to be 11!";
        System.out.printf("Day 5, part 1, total unique answer count is %d.%n", totalUniqueAnswers("2020/day6/input6.txt"));
    }
}
