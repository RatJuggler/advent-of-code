package day9;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Scanner;
import java.util.stream.IntStream;


public class Advent2020Day9 {

    private static List<Long> readNumbers(final String filename) throws FileNotFoundException {
        List<Long> numbers = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLong())
            numbers.add(scanner.nextLong());
        return numbers;
    }

    private static boolean findTwoNumbersSum(final List<Long> numbers, final long totalToFind) {
        for (int i = 0; i < numbers.size(); i++)
            for (int j = 0; j < numbers.size(); j++)
                if (j != i && numbers.get(i) + numbers.get(j) == totalToFind) return true;
        return false;
    }

    private static long findEncryptionWeakness(final List<Long> numbers, final long totalToFind) {
        long total = 0;
        int i = 0, j = 0;
        while (total != totalToFind && i < numbers.size()) {
            total = numbers.get(i++);
            j = i;
            while (total < totalToFind && j < numbers.size())
                total += numbers.get(j++);
        }
        if (total != totalToFind) throw new IllegalStateException();
        LongSummaryStatistics summary = numbers.subList(i - 1, j - 1).stream().mapToLong(l -> l).summaryStatistics();
        return summary.getMin() + summary.getMax();
    }

    private static long findFirstNonSum(final List<Long> numbers, final int preambleLength) {
        return IntStream.range(preambleLength, numbers.size())
                .filter(i -> !findTwoNumbersSum(numbers.subList(i - preambleLength, i), numbers.get(i)))
                .mapToLong(numbers::get)
                .findFirst()
                .orElseThrow();
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
