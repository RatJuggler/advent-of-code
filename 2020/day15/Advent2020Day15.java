package day15;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


class Indexes {

    private int current;
    private int previous = -1;

    Indexes(final int current) {
        this.current = current;
    }

    boolean firstIndex() {
        return this.previous == -1;
    }

    int newLastSpoken() {
        return current - previous;
    }

    void add(final int newIndex) {
        this.previous = this.current;
        this.current = newIndex;
    }
}


public class Advent2020Day15 {

    private static int numberGame(final String starting, final int toTurn) {
        List<Integer> start = Arrays.stream(starting.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        Map<Integer, Indexes> spoken = new HashMap<>();
        for (int i = 0; i < start.size(); i++)
            spoken.put(start.get(i), new Indexes(i));
        int index = start.size();
        Integer lastSpoken = start.get(index - 1);
        Indexes indexes = spoken.get(lastSpoken);
        while (index < toTurn) {
            lastSpoken = indexes.firstIndex() ? 0 : indexes.newLastSpoken();
            indexes = spoken.get(lastSpoken);
            if (indexes != null) {
                indexes.add(index);
            } else {
                indexes = new Indexes(index);
                spoken.put(lastSpoken, indexes);
            }
            index++;
        }
        return lastSpoken;
    }

    private static void testNumberGame2020(final String starting, final int expected) {
        int actual = numberGame(starting, 2020);
        assert actual == expected : String.format("Expected number spoken to be %d not %d!%n", expected, actual);
    }

    private static void testNumberGame30000000(final String starting, final int expected) {
        int actual = numberGame(starting, 30000000);
        assert actual == expected : String.format("Expected number spoken to be %d not %d!%n", expected, actual);
    }

    public static void main(final String[] args) {
        long startTime = System.currentTimeMillis();
        testNumberGame2020("0,3,6", 436);
        testNumberGame2020("1,3,2", 1);
        testNumberGame2020("2,1,3", 10);
        testNumberGame2020("1,2,3", 27);
        testNumberGame2020("2,3,1", 78);
        testNumberGame2020("3,2,1", 438);
        testNumberGame2020("3,1,2", 1836);
        System.out.printf("Day 15, Part 1, last number spoken is %s\n", numberGame("11,18,0,20,1,7,16", 2020));
        testNumberGame30000000("0,3,6", 175594);
        testNumberGame30000000("1,3,2", 2578);
        testNumberGame30000000("2,1,3", 3544142);
        testNumberGame30000000("1,2,3", 261214);
        testNumberGame30000000("2,3,1", 6895259);
        testNumberGame30000000("3,2,1", 18);
        testNumberGame30000000("3,1,2", 362);
        System.out.printf("Day 15, Part 2, last number spoken is %s\n", numberGame("11,18,0,20,1,7,16", 30000000));
        System.out.println((System.currentTimeMillis() - startTime) / 1000);
    }
}
