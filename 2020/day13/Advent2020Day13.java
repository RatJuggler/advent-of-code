package day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


class Notes {

    final int startTime;
    final int[] buses;

    Notes(final int startTIme, final int[] buses) {
        this.startTime = startTIme;
        this.buses = buses;
    }

    static Notes fromFile(final String filename) throws IOException {
        List<String> notes = Files.readAllLines(Paths.get(filename));
        int startTime = Integer.parseInt(notes.get(0));
        int[] buses = Arrays.stream(notes.get(1).split(","))
                .filter(i -> !"x".equals(i))
                .mapToInt(Integer::parseInt)
                .toArray();
        return new Notes(startTime, buses);
    }
}


public class Advent2020Day13 {

    private static int findWaitProduct(final Notes notes) {
        int time = notes.startTime;
        do {
            for (int i = 0; i < notes.buses.length; i++) {
                int nextDepart = (time / notes.buses[i]) * notes.buses[i];
                if (nextDepart == time) return (time - notes.startTime) * notes.buses[i];
            }
            time++;
        } while (true);
    }

    private static void testFindWaitProduct() throws IOException {
        int expectedTime = 295;
        Notes notes = Notes.fromFile("2020/day13/test13a.txt");
        int actualTime = findWaitProduct(notes);
        assert actualTime == expectedTime : String.format("Expected wait product to be %d not %d!%n", expectedTime, actualTime);
    }

    public static void main(final String[] args) throws IOException {
        testFindWaitProduct();
        Notes notes = Notes.fromFile("2020/day13/input13.txt");
        System.out.printf("Day 13, Part 1 earliest wait product is %d.%n", findWaitProduct(notes));
    }
}
