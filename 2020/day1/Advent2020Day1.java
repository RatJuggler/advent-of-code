package day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

final class Advent2020Day1 {

    private static List<Integer> readExpenses(final String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        List<Integer> expenses = new ArrayList<>();
        while (scanner.hasNextInt()) {
            expenses.add(scanner.nextInt());
        }
        return expenses;
    }

    private static int find2020Product(final String filename) throws FileNotFoundException {
        List<Integer> expenses = readExpenses(filename);
        expenses.sort(Integer::compareTo);
        int expense1 = 0, expense2 = 0;
        for (int e1 : expenses) {
            for (int e2 : expenses) {
                if (e1 + e2 >= 2020) {
                    expense2 = e2;
                    break;
                }
            }
            if (e1 + expense2 == 2020) {
                expense1 = e1;
                break;
            }
        }
        if (expense1 == 0 || expense2 == 0) throw new IllegalArgumentException();
        return expense1 * expense2;
    }

    private static void testFind2020Product() throws FileNotFoundException {
        int result = find2020Product("2020/day1/test1a.txt");
        assert result == 514579 : String.format("Expected result not found, was %d!", result);
    }

    public static void main(final String[] args) throws FileNotFoundException {
        testFind2020Product();
        System.out.printf("Day 1, Part 1 the answer is %d.%n", find2020Product("2020/day1/input1.txt"));
    }
}
