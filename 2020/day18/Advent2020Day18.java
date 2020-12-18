package day18;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


class Expression {

    Expression(final String expression) {}

    long evaluate() {
        return 0;
    }
}


public class Advent2020Day18 {

    private static long sumExpressions(final String filename) {
        long sum = 0;
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Expression expression = new Expression(line);
                sum += expression.evaluate();
            }
        } catch (FileNotFoundException fnf) {
            throw new IllegalArgumentException("Unable to read expressions file!", fnf);
        }
        return sum;
    }

    private static void testSumExpressions() {
        long expectedSum = 26457;
        long actualSum = sumExpressions("2020/day18/test18a.txt");
        assert actualSum == expectedSum : String.format("Expected sum of expressions to be %d not %d!%n", expectedSum, actualSum);
    }

    public static void main(final String[] args) {
        testSumExpressions();
        System.out.printf("Day 18, Part 1 sum of expressions is %d.%n", sumExpressions("2020/day18/input18.txt"));
    }
}
