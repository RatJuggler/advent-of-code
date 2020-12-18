package day18;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;


//    Expression => Operation
//    Operation  => Operand (('+' / '-' / '*') Operand)*
//    Operand    => [0-9]+ / '(' Expression ')'


abstract class Node {

    Node() {}

    abstract long evaluate();
}


class Operand extends Node {

    private final String operand;

    Operand(final String operand) {
        super();
        this.operand = operand;
    }

    @Override
    long evaluate() {
        return Long.parseLong(this.operand);
    }
}


class Operation extends Node {

    private final Node operand1;
    private final String operator;
    private final Node operand2;

    Operation(final Node operand1, final String operator, final Node operand2) {
        super();
        this.operand1 = operand1;
        this.operator = operator;
        this.operand2 = operand2;
    }

    @Override
    long evaluate() {
        if ("+".equals(this.operator))
            return this.operand1.evaluate() + this.operand2.evaluate();
        else if ("*".equals(this.operator))
            return this.operand1.evaluate() * this.operand2.evaluate();
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
        while (n > 0 && j < this.expression.length()) {
            if (this.expression.charAt(j) == '(') n++;
            if (this.expression.charAt(j) == ')') n--;
            j++;
        }
        if (n != 0) throw new IllegalArgumentException("Brackets don't match in expression!");
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

    private static final Pattern PATTERN = Pattern.compile("^-?\\d+$");

    private final Tokeniser tokeniser;
    private final String operatorPrecedence;

    Expression(final Tokeniser tokeniser, final String operatorPrecedence) {
        this.tokeniser = tokeniser;
        this.operatorPrecedence = operatorPrecedence;
    }

    private boolean isNumeric(final String token) {
        if (token == null)
            return false;
        else
            return PATTERN.matcher(token).matches();
    }

    private Node buildNode(final String token) {
        if (this.isNumeric(token))
            return new Operand(token);
        else
            return new Expression(new Tokeniser(token), this.operatorPrecedence).buildTree();
    }

    private Node buildTree() {
        Stack<Node> nodes = new Stack<>();
        while (this.tokeniser.hasNextToken()) {
            String token = this.tokeniser.nextToken();
            if (!"+*".contains(token))
                nodes.push(this.buildNode(token));
            else if (this.operatorPrecedence.contains(token))
                nodes.push(new Operation(nodes.pop(), token, this.buildNode(this.tokeniser.nextToken())));
        }
        Node node = nodes.remove(0);
        while (!nodes.isEmpty())
            node = new Operation(node, "*", nodes.remove(0));
        return node;
    }

    long evaluate() {
        return this.buildTree().evaluate();
    }
}


public class Advent2020Day18 {

    private static long sumExpressions(final String filename, final String operatorPrecedence) {
        long sum = 0;
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Expression expression = new Expression(new Tokeniser(line), operatorPrecedence);
                sum += expression.evaluate();
            }
        } catch (FileNotFoundException fnf) {
            throw new IllegalArgumentException("Unable to read expressions file!", fnf);
        }
        return sum;
    }

    private static void testSumExpressions(final String operatorPrecedence, final long expectedSum) {
        long actualSum = sumExpressions("2020/day18/test18a.txt", operatorPrecedence);
        assert actualSum == expectedSum : String.format("Expected sum of expressions to be %d not %d!%n", expectedSum, actualSum);
    }

    public static void main(final String[] args) {
        testSumExpressions("+*", 26457);
        System.out.printf("Day 18, Part 1 sum of expressions is %d.%n", sumExpressions("2020/day18/input18.txt", "+*"));
        testSumExpressions("+", 694173);
        System.out.printf("Day 18, Part 2 sum of expressions is %d.%n", sumExpressions("2020/day18/input18.txt", "+"));
    }
}
