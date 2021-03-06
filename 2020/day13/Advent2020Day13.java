package day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


class Notes {

    final long startTime;
    final long[] buses;

    Notes(final long startTIme, final long[] buses) {
        this.startTime = startTIme;
        this.buses = buses;
    }

    static Notes fromFile(final String filename) throws IOException {
        List<String> notes = Files.readAllLines(Paths.get(filename));
        long startTime = Long.parseLong(notes.get(0));
        long[] buses = Arrays.stream(notes.get(1).split(","))
                .map(n -> "x".equals(n) ? "0" : n)
                .mapToLong(Long::parseLong)
                .toArray();
        return new Notes(startTime, buses);
    }
}


public class Advent2020Day13 {

    private static long findWaitProduct(final Notes notes) {
        long time = notes.startTime;
        do {
            for (int i = 0; i < notes.buses.length; i++)
                if (notes.buses[i] != 0 && time % notes.buses[i] == 0)
                    return (time - notes.startTime) * notes.buses[i];
            time++;
        } while (true);
    }

    //  7    13    x    x   59    x   31   19
    //  n   n+1  n+2  n+3  n+4  n+5  n+6  n+7
    // n-4  n-3  n-2  n-1    n  n+1  n+2  n+3

    private static long findOrderedDeparture(final Notes notes) {
        long time = 0;
        // Convert the input bus primitive long array to a list of long objects.
        List<Long> index = Arrays.stream(notes.buses).boxed().collect(Collectors.toList());
        // Filter and sort in descending order back to an array of primitive long.
        long[] check = index.stream().filter(n -> n != 0).sorted(Collections.reverseOrder()).mapToLong(Long::longValue).toArray();
        long increment = check[0];
        int[] offset = new int[check.length];
        int incIndex = index.indexOf(increment);
        for (int i = 0; i < check.length; i++)
            offset[i] = index.indexOf(check[i]) - incIndex;
        System.out.println(Arrays.toString(check));
        System.out.println(Arrays.toString(offset));
        nextTime:
        do {
            time += increment;
            for (int i = 0; i < check.length; i++)
                if ((time + offset[i]) % check[i] != 0)
                    continue nextTime;
            return time - incIndex;
        } while (true);
    }

    private static void testFindWaitProduct() throws IOException {
        long expectedProduct = 295;
        Notes notes = Notes.fromFile("2020/day13/test13a.txt");
        long actualProduct = findWaitProduct(notes);
        assert actualProduct == expectedProduct :
                String.format("Expected wait product to be %d not %d!%n", expectedProduct, actualProduct);
    }

    private static void testFindOrderedDeparture(final String filename, final long expectedDeparture) throws IOException {
        Notes notes = Notes.fromFile(filename);
        long actualDeparture = findOrderedDeparture(notes);
        assert actualDeparture == expectedDeparture :
                String.format("Expected earliest departure to be %d not %d!%n", expectedDeparture, actualDeparture);
    }

    public static void main(final String[] args) throws IOException {
        testFindWaitProduct();
        testFindOrderedDeparture("2020/day13/test13a.txt", 1068781);
        testFindOrderedDeparture("2020/day13/test13b.txt", 3417);
        testFindOrderedDeparture("2020/day13/test13c.txt", 754018);
        testFindOrderedDeparture("2020/day13/test13d.txt", 779210);
        testFindOrderedDeparture("2020/day13/test13e.txt", 1261476);
        testFindOrderedDeparture("2020/day13/test13f.txt", 1202161486);
        Notes notes = Notes.fromFile("2020/day13/input13.txt");
        System.out.printf("Day 13, Part 1 wait product is %d.%n", findWaitProduct(notes));
        System.out.printf("Day 13, Part 2 earliest departure is %d.%n", findOrderedDeparture(notes));
    }
}
