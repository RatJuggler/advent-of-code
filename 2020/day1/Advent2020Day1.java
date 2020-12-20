package day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


final class Advent2020Day1 {

    private static List<Integer> readExpenses(final String filename) {
        List<Integer> expenses = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextInt())
                expenses.add(scanner.nextInt());
        } catch (FileNotFoundException fnf) {
            throw new IllegalArgumentException("Problem reading expenses file!", fnf);
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
        throw new IllegalStateException("Expected to find a result!");
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
        throw new IllegalStateException("Expected to find a result!");
    }

    private static void testExpenseProduct(final int actualResult, final int expectedResult) {
        assert actualResult == expectedResult :
                String.format("Expected expense product to be %d, but, was %d!", expectedResult, actualResult);
    }

    public static void main(final String[] args) {
        List<Integer> testExpenses = readExpenses("2020/day1/test1a.txt");
        testExpenseProduct(findTwoExpensesProduct(testExpenses, 2020), 514579);
        testExpenseProduct(findThreeExpensesProduct(testExpenses, 2020), 241861950);
        List<Integer> expenses = readExpenses("2020/day1/input1.txt");
        System.out.printf("Day 1, Part 1 the answer is %d.%n", findTwoExpensesProduct(expenses, 2020));
        System.out.printf("Day 1, Part 2 the answer is %d.%n", findThreeExpensesProduct(expenses, 2020));
    }
}
