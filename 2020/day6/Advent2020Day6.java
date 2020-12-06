package day6;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Advent2020Day6 {

    private static int countUniqueAnswers(final String answers) {
        return 0;
    }

    private static void testCountUniqueAnswers(final String answers, final int expected) {
        assert countUniqueAnswers(answers) == expected :
                String.format("Expected unqiue answer count for \"%s\" to be %d!", answers, expected);
    }

    private static void testPart1CountUniqueAnswers() {
        testCountUniqueAnswers("abcx\n" +
                "abcy\n" +
                "abcz", 11);
    }

    private static int totalUniqueAnswers(final String filename) throws IOException {
        int totalUniqueAnswers = 0;
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
    }
}
