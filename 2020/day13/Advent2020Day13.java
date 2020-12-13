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

    private static int findDepartureTime(final Notes notes) {
        int time = notes.startTime;
        int departureFound = 0;
        do {
            for (int i = 0; i < notes.buses.length; i++) {
                int nextDepart = (time / notes.buses[i]) * notes.buses[i];
                if (nextDepart == time) {
                    departureFound = time;
                    break;
                }
            }
            time++;
        } while (departureFound == 0);
        return departureFound;
    }

    private static void testFindDepartureTime() throws IOException {
        int expectedTime = 944;
        Notes notes = Notes.fromFile("2020/day13/test13a.txt");
        int actualTime = findDepartureTime(notes);
        assert actualTime == expectedTime : String.format("Expected departure time to be %d not %d!%n", expectedTime, actualTime);
    }

    public static void main(final String[] args) throws IOException {
        testFindDepartureTime();
    }
}
