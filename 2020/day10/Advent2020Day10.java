package day10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
        return 0;
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
    }
}
