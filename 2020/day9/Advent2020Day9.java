package day9;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Advent2020Day9 {

    private static List<Integer> readNumbers(final String filename) throws FileNotFoundException {
        List<Integer> numbers = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextInt()) {
            numbers.add(scanner.nextInt());
        }
        numbers.sort(Integer::compareTo);
        return numbers;
    }

    private static boolean findTwoNumbersSum(final List<Integer> numbers, final int totalToFind) {
        int number1 = 0, number2 = 0;
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

    private static int findFirstNonSum(final String filename, final int preambleLength) throws FileNotFoundException {
        List<Integer> numbers = readNumbers(filename);
        for (int i = preambleLength; i < numbers.size(); i++) {
            int totalToFind = numbers.get(i);
            List<Integer> preamble = numbers.subList(i - preambleLength, i);
            if (!findTwoNumbersSum(preamble, totalToFind)) return totalToFind;
        }
        throw new IllegalStateException();
    }

    private static void testPart1() throws FileNotFoundException {
        int expected = 127;
        int actual = findFirstNonSum("2020/day9/test9a.txt", 5);
        assert actual == expected : String.format("Expected to find '%s' but found '%s'!", expected, actual);
    }

    public static void main(final String[] args) throws FileNotFoundException {
        testPart1();
        System.out.printf("Day 9, Part 1 found %d.%n", findFirstNonSum("2020/day9/input9.txt", 25));
    }
}
