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

    private static int find2020DoubleProduct(final String filename) throws FileNotFoundException {
        List<Integer> expenses = readExpenses(filename);
        expenses.sort(Integer::compareTo);
        int expense1 = 0, expense2 = 0;
        for (int i = 0; i < expenses.size(); i++) {
            expense1 = expenses.get(i);
            for (int j = 0; j < expenses.size(); j++) {
                if (j != i && expense1 + expenses.get(j) >= 2020) {
                    expense2 = expenses.get(j);
                    break;
                }
            }
            if (expense1 + expense2 == 2020) break;
        }
        if (expense1 + expense2 != 2020) throw new IllegalArgumentException();
        return expense1 * expense2;
    }

    private static int find2020TripleProduct(final String filename) throws FileNotFoundException {
        List<Integer> expenses = readExpenses(filename);
        expenses.sort(Integer::compareTo);
        int expense1 = 0, expense2 = 0, expense3 = 0;
        for (int i = 0; i < expenses.size(); i++) {
            expense1 = expenses.get(i);
            for (int j = 0; j < expenses.size(); j++) {
                if (j == i) continue;
                expense2 = expenses.get(j);
                for (int k = 0; k < expenses.size(); k++) {
                    if (k != i && k != j && expense1 + expense2 + expenses.get(k) >= 2020) {
                        expense3 = expenses.get(k);
                        break;
                    }
                }
                if (expense1 + expense2 + expense3 == 2020) break;
            }
            if (expense1 + expense2 + expense3 == 2020) break;
        }
        if (expense1 + expense2 + expense3 != 2020) throw new IllegalArgumentException();
        return expense1 * expense2 * expense3;
    }

    private static void testFind2020DoubleProduct() throws FileNotFoundException {
        int result = find2020DoubleProduct("2020/day1/test1a.txt");
        assert result == 514579 : String.format("Expected result not found, was %d!", result);
    }

    private static void testFind2020TripleProduct() throws FileNotFoundException {
        int result = find2020TripleProduct("2020/day1/test1a.txt");
        assert result == 241861950 : String.format("Expected result not found, was %d!", result);
    }

    public static void main(final String[] args) throws FileNotFoundException {
        testFind2020DoubleProduct();
        System.out.printf("Day 1, Part 1 the answer is %d.%n", find2020DoubleProduct("2020/day1/input1.txt"));
        testFind2020TripleProduct();
        System.out.printf("Day 1, Part 2 the answer is %d.%n", find2020TripleProduct("2020/day1/input1.txt"));
    }
}
