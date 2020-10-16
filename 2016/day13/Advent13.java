package day13;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;


class Position implements Cloneable {

    final int x;
    final int y;
    int steps;

    Position(final int x, final int y, final int steps) {
        this.x = x;
        this.y = y;
        this.steps = steps;
    }

    boolean isFinalPosition(final int toX, final int toY) {
        return x == toX && y == toY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return this.x == position.x && this.y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}


class Office {

    private final char[][] space;
    private final Map<Integer, Position> history = new HashMap<>();
    private int minimumSteps = Integer.MAX_VALUE;


    Office(final int seed, final int sizeX, final int sizeY) {
        this.space = new char[sizeY][sizeX];
        for (int y = 0; y < sizeY; y++) {
            char[] strip = new char[sizeX];
            for (int x = 0; x < sizeX; x++) {
                int find = (x * x) + (3 * x) + (2 * x * y) + y + (y * y) + seed;
                String binary = Integer.toBinaryString(find);
                long ones = binary.chars().filter(ch -> ch == '1').count();
                if (ones % 2 == 0)
                    strip[x] = '.';
                else
                    strip[x] = '#';
            }
            this.space[y] = strip;
        }
    }

    private List<Position> generateNextPositions(final Position currentPosition) {
        Position newPosition;
        List<Position> nextPositions = new ArrayList<>();
        if (currentPosition.x < this.space[0].length - 1) {
            newPosition = new Position(currentPosition.x + 1, currentPosition.y, currentPosition.steps + 1);
            if (this.space[newPosition.y][newPosition.x] == '.') nextPositions.add(newPosition);
        }
        if (currentPosition.x > 0) {
            newPosition = new Position(currentPosition.x - 1, currentPosition.y, currentPosition.steps + 1);
            if (this.space[newPosition.y][newPosition.x] == '.') nextPositions.add(newPosition);
        }
        if (currentPosition.y < this.space.length - 1) {
            newPosition = new Position(currentPosition.x, currentPosition.y + 1, currentPosition.steps + 1);
            if (this.space[newPosition.y][newPosition.x] == '.') nextPositions.add(newPosition);
        }
        if (currentPosition.y > 0) {
            newPosition = new Position(currentPosition.x, currentPosition.y - 1, currentPosition.steps + 1);
            if (this.space[newPosition.y][newPosition.x] == '.') nextPositions.add(newPosition);
        }
        return nextPositions;
    }

    int findPathTo(final int toX, final int toY) {
        Queue<Position> nextPositions = new ArrayDeque<>();
        nextPositions.add(new Position(1, 1, 0));
        while (nextPositions.peek() != null) {
            Position position = nextPositions.poll();
            if (position.steps < this.minimumSteps) {
                if (position.isFinalPosition(toX, toY)) {
                    this.minimumSteps = position.steps;
                    System.out.println("Queue: " + nextPositions.size() + ", History: " + this.history.size() + ", Minimum steps: " + this.minimumSteps);
                } else {
                    int hash = position.hashCode();
                    Position found = history.get(hash);
                    if (found == null) {
                        this.history.put(hash, position);
                    } else {
                        if (position.steps >= found.steps) continue;
                        found.steps = position.steps;
                    }
                    nextPositions.addAll(this.generateNextPositions(position));
                }
            }
        }
        return this.minimumSteps;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.space.length * this.space[0].length);
        StringBuilder row1 = new StringBuilder().append("   ");
        StringBuilder row2 = new StringBuilder().append("   ");
        for (int x = 0; x < this.space[0].length; x++) {
            row1.append(x / 10);
            row2.append(x % 10);
        }
        sb.append(row1).append('\n').append(row2).append('\n');
        for (int y = 0; y < this.space.length; y++) {
            sb.append(String.format("%02d ", y)).append(this.space[y]).append('\n');
        }
        return sb.toString();
    }
}


public class Advent13 {


    private static void part2() {
    }

    private static void part1() {
        Office office = new Office(1358, 50, 50);
        System.out.println(office.toString());
        System.out.printf("Part 1, steps taken = %s\n", office.findPathTo(31, 39));
    }

    private static void test() {
        int expectedSteps = 11;
        Office office = new Office(10, 10, 7);
        System.out.println(office.toString());
        int actualSteps = office.findPathTo(7, 4);
        assert actualSteps == expectedSteps : String.format("Expected to take '%s' steps but was '%s'!", expectedSteps, actualSteps);
    }

    public static void main(final String[] args) {
        test();
        part1();
        part2();
    }
}
