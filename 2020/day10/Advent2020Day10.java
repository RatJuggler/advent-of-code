package day10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Advent2020Day10 {

    private static List<Integer> readAdapters(final String filename) throws FileNotFoundException {
        List<Integer> numbers = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextInt())
            numbers.add(scanner.nextInt());
        return numbers;
    }

    private static int findDifferencesProduct(final List<Integer> adapters) {
        // Sort adapters with the outlet at the beginning and the device at the end.
        adapters.add(0);
        adapters.sort(Integer::compareTo);
        adapters.add(adapters.get(adapters.size() - 1) + 3);
        // Count the differences.
        Map<Integer, Long> differences = IntStream.range(0, adapters.size() - 1)
                .mapToObj(i -> adapters.get(i + 1) - adapters.get(i))
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        return (int) (differences.get(1) * differences.get(3));
    }

    private static long findArrangements(final List<Integer> adapters) {
        // Sort adapters with the outlet at the beginning and the device at the end.
        adapters.add(0);
        adapters.sort(Integer::compareTo);
        adapters.add(adapters.get(adapters.size() - 1) + 3);
        // Count the differences.
        List<Integer> differences = IntStream.range(0, adapters.size() - 1)
                .mapToObj(i -> adapters.get(i + 1) - adapters.get(i))
                .collect(Collectors.toList());
        System.out.println(differences);
        long arrangements = 1;
        int i = 0;
        while (i < differences.size()) {
            if (differences.get(i) == 1 && differences.get(i + 1) == 1 && differences.get(i + 2) == 1 && differences.get(i + 3) == 1) {
                arrangements *= 7;
                i += 3;
            } else if (differences.get(i) == 1 && differences.get(i + 1) == 1 && differences.get(i + 2) == 1 && differences.get(i + 3) == 3) {
                arrangements *= 4;
                i += 3;
            } else if (differences.get(i) == 1 && differences.get(i + 1) == 1 && differences.get(i + 2) == 3) {
                arrangements *= 2;
                i += 2;
            } else {
                i++;
            }

            // (0)  1  4  5  6  7 10 11 12 15 16 19 (22)
            //    1  3  1  1  1  3  1  1  3  1  3  3

            //    1  3 (1  1  1  3)(1  1  3) 1  3  3 => 4 * 2 = 8

            // 10 11 12 13 14 => 11,12,13 | 11,12 | 11,13 | 12,13 | 11 | 12 | 13 = 7
            //   1  1  1  1
            // 10 11 12 13 16 => 11,12,13 | 11,13 | 12,13 | 13 = 4
            //   1  1  1  3
            // 10 11 12 15 => 11,12 | 12 = 2
            //   1  1  3

            // 10 11 14
            //   1  3
            // 10 13 14
            //   3  1
            // 10 13 16
            //   3  3
        }
        return arrangements;
    }

    private static void testDifferencesProduct(final List<Integer> adapters, final int expectedDifferencesProduct) {
        int differencesProduct = findDifferencesProduct(adapters);
        assert differencesProduct == expectedDifferencesProduct :
                String.format("Expected to find difference product of '%s' but was '%s'!", expectedDifferencesProduct, differencesProduct);
    }

    private static void testAdapterArrangements(final List<Integer> adapters, final long expectedArrangements) {
        long arrangements = findArrangements(adapters);
        assert arrangements == expectedArrangements :
                String.format("Expected to find '%s' arrangements but found '%s'!", expectedArrangements, arrangements);
    }

    private static void testAdapters(final String filename, final int expectedDifferencesProduct, final long expectedArrangements)
            throws FileNotFoundException {
        List<Integer> testAdapters = readAdapters(filename);
        testDifferencesProduct(testAdapters, expectedDifferencesProduct);
        testAdapterArrangements(testAdapters, expectedArrangements);
    }

    public static void main(final String[] args) throws FileNotFoundException {
        testAdapters("2020/day10/test10a.txt", 35, 8);
        testAdapters("2020/day10/test10b.txt", 220, 19208);
        List<Integer> adapters = readAdapters("2020/day10/input10.txt");
        System.out.printf("Day 10, Part 1 difference product is %d.%n", findDifferencesProduct(adapters));
    }
}
