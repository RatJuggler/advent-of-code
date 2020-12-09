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
        expenses.sort(Integer::compareTo);
        return expenses;
    }

    private static int findTwoExpensesProduct(final String filename, final int totalToFind) throws FileNotFoundException {
        List<Integer> expenses = readExpenses(filename);
        int expense1 = 0;
        int i = 0, j = 0;
        while (expense1 + (i == j ? 0 : expenses.get(j)) != totalToFind && i < expenses.size()) {
            expense1 = expenses.get(i);
            j = i++;
            while (expense1 + (j == i ? 0 : expenses.get(j)) < totalToFind && j < expenses.size())
                j++;
        }
        if (expense1 + expenses.get(j) != totalToFind) throw new IllegalStateException();
        return expense1 * expenses.get(j);
    }

    private static int findThreeExpensesProduct(final String filename, final int totalToFind) throws FileNotFoundException {
        List<Integer> expenses = readExpenses(filename);
        int expense1 = 0, expense2 = 0, expense3 = 0;
        expensesFound:
        for (int i = 0; i < expenses.size(); i++) {
            expense1 = expenses.get(i);
            for (int j = 0; j < expenses.size(); j++) {
                if (j == i || expense1 + expenses.get(j) >= totalToFind) continue;
                expense2 = expenses.get(j);
                for (int k = 0; k < expenses.size(); k++) {
                    if (k != i && k != j && expense1 + expense2 + expenses.get(k) >= totalToFind) {
                        expense3 = expenses.get(k);
                        break;
                    }
                }
                if (expense1 + expense2 + expense3 == totalToFind) break expensesFound;
            }
        }
        if (expense1 + expense2 + expense3 != totalToFind) throw new IllegalArgumentException();
        return expense1 * expense2 * expense3;
    }

    private static void testFind2020DoubleProduct() throws FileNotFoundException {
        int result = findTwoExpensesProduct("2020/day1/test1a.txt", 2020);
        assert result == 514579 : String.format("Expected result not found, was %d!", result);
    }

    private static void testFind2020TripleProduct() throws FileNotFoundException {
        int result = findThreeExpensesProduct("2020/day1/test1a.txt", 2020);
        assert result == 241861950 : String.format("Expected result not found, was %d!", result);
    }

    public static void main(final String[] args) throws FileNotFoundException {
        testFind2020DoubleProduct();
        System.out.printf("Day 1, Part 1 the answer is %d.%n", findTwoExpensesProduct("2020/day1/input1.txt", 2020));
        testFind2020TripleProduct();
        System.out.printf("Day 1, Part 2 the answer is %d.%n", findThreeExpensesProduct("2020/day1/input1.txt", 2020));
    }
}
