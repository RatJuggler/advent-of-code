package day10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Advent2020Day10 {

    private static List<Integer> readAdapters(final String filename) throws FileNotFoundException {
        List<Integer> numbers = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextInt())
            numbers.add(scanner.nextInt());
        return numbers;
    }

    private static int findDifferencesProduct(final List<Integer> adapters) {
        adapters.sort(Integer::compareTo);
        Map<Integer, Integer> differences = new HashMap<>();
        differences.put(adapters.get(0), 1);
        for (int i = 0; i < adapters.size() - 1; i++) {
            int difference = adapters.get(i + 1) - adapters.get(i);
            differences.put(difference, differences.computeIfAbsent(difference, d -> 0) + 1);
        }
        System.out.println(differences);
        return differences.get(1) * (differences.get(3) + 1);
    }

    private static void testDifferencesProduct(final String filename, final int expectedDifferencesProduct)
            throws FileNotFoundException {
        List<Integer> testAdapters = readAdapters(filename);
        long differencesProduct = findDifferencesProduct(testAdapters);
        assert differencesProduct == expectedDifferencesProduct :
                String.format("Expected to find difference product of '%s' but was '%s'!", expectedDifferencesProduct, differencesProduct);
    }

    public static void main(final String[] args) throws FileNotFoundException {
        testDifferencesProduct("2020/day10/test10a.txt", 35);
        testDifferencesProduct("2020/day10/test10b.txt", 220);
        List<Integer> adapters = readAdapters("2020/day10/input10.txt");
        System.out.printf("Day 10, Part 1 difference product is %d.%n", findDifferencesProduct(adapters));
    }
}
