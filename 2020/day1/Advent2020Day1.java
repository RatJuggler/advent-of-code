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

    private static int findTwoExpensesProduct(final List<Integer> expenses, final int totalToFind) {
        for (int i = 0; i < expenses.size(); i++)
            for (int j = 0; j < expenses.size(); j++)
                if (j != i) {
                    if (expenses.get(i) + expenses.get(j) > totalToFind)
                        break;
                    if (expenses.get(i) + expenses.get(j) == totalToFind)
                        return expenses.get(i) * expenses.get(j);
                }
        throw new IllegalStateException();
    }

    private static int findThreeExpensesProduct(final List<Integer> expenses, final int totalToFind) {
        for (int i = 0; i < expenses.size(); i++)
            for (int j = 0; j < expenses.size(); j++)
                if (j != i) {
                    int subTotal = expenses.get(i) + expenses.get(j);
                    if (subTotal > totalToFind)
                        break;
                    for (int k = 0; k < expenses.size(); k++)
                        if (k != i && k != j) {
                            if (subTotal + expenses.get(k) > totalToFind)
                                break;
                            if (subTotal + expenses.get(k) == totalToFind)
                                return expenses.get(i) * expenses.get(j) * expenses.get(k);
                        }
                }
        throw new IllegalStateException();
    }

    private static void testFind2020DoubleProduct(final List<Integer> expenses) {
        int result = findTwoExpensesProduct(expenses, 2020);
        assert result == 514579 : String.format("Expected result not found, was %d!", result);
    }

    private static void testFind2020TripleProduct(final List<Integer> expenses) {
        int result = findThreeExpensesProduct(expenses, 2020);
        assert result == 241861950 : String.format("Expected result not found, was %d!", result);
    }

    public static void main(final String[] args) throws FileNotFoundException {
        List<Integer> testExpenses = readExpenses("2020/day1/test1a.txt");
        testFind2020DoubleProduct(testExpenses);
        testFind2020TripleProduct(testExpenses);
        List<Integer> expenses = readExpenses("2020/day1/input1.txt");
        System.out.printf("Day 1, Part 1 the answer is %d.%n", findTwoExpensesProduct(expenses, 2020));
        System.out.printf("Day 1, Part 2 the answer is %d.%n", findThreeExpensesProduct(expenses, 2020));
    }
}
