package day9;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Scanner;


public class Advent2020Day9 {

    private static List<Long> readNumbers(final String filename) throws FileNotFoundException {
        List<Long> numbers = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLong()) numbers.add(scanner.nextLong());
        return numbers;
    }

    private static boolean findTwoNumbersSum(final List<Long> numbers, final long totalToFind) {
        long number1 = 0, number2 = 0;
        findSum:
        for (int i = 0; i < numbers.size(); i++) {
            number1 = numbers.get(i);
            for (int j = 0; j < numbers.size(); j++) {
                if (j != i && number1 + numbers.get(j) == totalToFind) {
                    number2 = numbers.get(j);
                    break findSum;
                }
            }
        }
        return number1 + number2 == totalToFind;
    }

    private static long findEncryptionWeakness(final List<Long> numbers, final long totalToFind) {
        long total = 0;
        int i, j = 0;
        for (i = 0; i < numbers.size(); i++) {
            total = numbers.get(i);
            for (j = i + 1; j < numbers.size(); j++) {
                total += numbers.get(j);
                if (total >= totalToFind) break;
            }
            if (total == totalToFind) break;
        }
        if (total != totalToFind) throw new IllegalStateException();
        LongSummaryStatistics summary = numbers.subList(i, j).stream().mapToLong(l -> l).summaryStatistics();
        return summary.getMin() + summary.getMax();
    }

    private static long findFirstNonSum(final List<Long> numbers, final int preambleLength) {
        for (int i = preambleLength; i < numbers.size(); i++) {
            long totalToFind = numbers.get(i);
            List<Long> preamble = numbers.subList(i - preambleLength, i);
            if (!findTwoNumbersSum(preamble, totalToFind)) return totalToFind;
        }
        throw new IllegalStateException();
    }

    private static void test(final List<Long> numbers) {
        long expectedFirstNonSum = 127;
        long firstNonSum = findFirstNonSum(numbers, 5);
        assert firstNonSum == expectedFirstNonSum : String.format("Expected to find '%s' but found '%s'!", expectedFirstNonSum, firstNonSum);
        long expectedWeakness = 62;
        long weakness = findEncryptionWeakness(numbers, firstNonSum);
        assert weakness == expectedWeakness : String.format("Expected weakness to be '%s' but was '%s'!", expectedWeakness, weakness);
    }

    public static void main(final String[] args) throws FileNotFoundException {
        List<Long> testNumbers = readNumbers("2020/day9/test9a.txt");
        test(testNumbers);
        List<Long> numbers = readNumbers("2020/day9/input9.txt");
        long firstNonSum = findFirstNonSum(numbers, 25);
        System.out.printf("Day 9, Part 1 found %d.%n", firstNonSum);
        System.out.printf("Day 9, Part 2 encryption weakness is %d.%n", findEncryptionWeakness(numbers, firstNonSum));
    }
}
