package day15;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Advent2020Day15 {

    private static int numberGame(final String starting) {
        List<Integer> spoken = Arrays.stream(starting.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        while (spoken.size() < 2020) {
            Integer lastSpoken = spoken.get(spoken.size() - 1);
            int[] occurrences = IntStream.range(0, spoken.size())
                    .filter(i -> Objects.equals(lastSpoken, spoken.get(i)))
                    .toArray();
            if (occurrences.length == 1) {
                spoken.add(0);
            } else {
                spoken.add(occurrences[occurrences.length - 1] - occurrences[occurrences.length - 2]);
            }
        }
        return spoken.get(spoken.size() - 1);
    }

    private static void testNumberGame(final String starting, final int expected) {
        int actual = numberGame(starting);
        assert actual == expected : String.format("Expected number spoken to be %d not %d!%n", expected, actual);
    }

    public static void main(final String[] args) {
        testNumberGame("0,3,6", 436);
        testNumberGame("1,3,2", 1);
        testNumberGame("2,1,3", 10);
        testNumberGame("1,2,3", 27);
        testNumberGame("2,3,1", 78);
        testNumberGame("3,2,1", 438);
        testNumberGame("3,1,2", 1836);
        System.out.printf("Day 15, Part 1, last number spoken is %s\n", numberGame("11,18,0,20,1,7,16"));
    }
}
