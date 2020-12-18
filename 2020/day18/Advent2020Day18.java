package day18;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;


//    Expression => Operation
//    Operation  => Operand (('+' / '-' / '*') Operand)*
//    Operand    => [0-9]+ / '(' Expression ')'


class Operand {

    private final String operand;

    Operand(final String operand) {
        this.operand = operand;
    }

    private boolean isNumeric() {
        if (this.operand == null) return false;
        Pattern pattern = Pattern.compile("^-?\\d+$");
        return pattern.matcher(this.operand).matches();
    }

    long value() {
        if (this.isNumeric()) return Long.parseLong(this.operand);
        Expression expression = new Expression(new Tokeniser(this.operand));
        return expression.evaluate();
    }
}


class Operation {

    private final Operand operand1;
    private final String operator;
    private final Operand operand2;

    Operation(final Operand operand1, final String operator, final Operand operand2) {
        this.operand1 = operand1;
        this.operator = operator;
        this.operand2 = operand2;
    }

    long evaluate() {
        if ("+".equals(this.operator))
            return this.operand1.value() + this.operand2.value();
        else if ("-".equals(this.operator))
            return this.operand1.value() - this.operand2.value();
        else if ("*".equals(this.operator))
            return this.operand1.value() * this.operand2.value();
        else
            throw new IllegalArgumentException("Unknown operator found!");
    }
}


class Tokeniser {

    private final String expression;
    private int i = 0;

    Tokeniser(final String expression) {
        this.expression = expression;
    }

    private String parseBrackets() {
        int n = 1;
        int j = ++this.i;
        while (n > 0) {
            if (this.expression.charAt(j) == '(') n++;
            if (this.expression.charAt(j) == ')') n--;
            j++;
        }
        if (n < 0) throw new IllegalArgumentException("Brackets don't match in expression!");
        String token = this.expression.substring(this.i, j - 1);
        this.i = j + 1;
        return token;
    }

    String nextToken() {
        String token;
        if (this.expression.charAt(i) == '(') {
            token = parseBrackets();
        } else {
            int j = this.expression.indexOf(' ', this.i);
            if (j == -1) {
                token = this.expression.substring(i);
                this.i = this.expression.length();
            } else {
                token = this.expression.substring(this.i, j);
                this.i = j + 1;
            }
        }
        return token;
    }

    boolean hasNextToken() {
        return this.i < this.expression.length();
    }
}

class Expression {

    private final Tokeniser tokeniser;

    Expression(final Tokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }

    long evaluate() {
        Operand operand1 = new Operand(this.tokeniser.nextToken());
        String operator = this.tokeniser.nextToken();
        Operand operand2 = new Operand(this.tokeniser.nextToken());
        Operation operation = new Operation(operand1, operator, operand2);
        long result = operation.evaluate();
        while (this.tokeniser.hasNextToken()) {
            operand1 = new Operand(String.valueOf(result));
            operator = this.tokeniser.nextToken();
            operand2 = new Operand(this.tokeniser.nextToken());
            operation = new Operation(operand1, operator, operand2);
            result = operation.evaluate();
        }
        return result;
    }
}


public class Advent2020Day18 {

    private static long sumExpressions(final String filename) {
        long sum = 0;
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Expression expression = new Expression(new Tokeniser(line));
                sum += expression.evaluate();
            }
        } catch (FileNotFoundException fnf) {
            throw new IllegalArgumentException("Unable to read expressions file!", fnf);
        }
        return sum;
    }

    private static void testSumExpressions(final long expectedSum) {
        long actualSum = sumExpressions("2020/day18/test18a.txt");
        assert actualSum == expectedSum : String.format("Expected sum of expressions to be %d not %d!%n", expectedSum, actualSum);
    }

    public static void main(final String[] args) {
        testSumExpressions(26457);
        System.out.printf("Day 18, Part 1 sum of expressions is %d.%n", sumExpressions("2020/day18/input18.txt"));
        testSumExpressions(694173);
    }
}
