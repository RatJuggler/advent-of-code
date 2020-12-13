package day13;


class Notes {

    final int startTime;
    final int[] buses;

    Notes(final int startTIme, final int[] buses) {
        this.startTime = startTIme;
        this.buses = buses;
    }

    static Notes fromFile(final String filename) {
        int startTime = 0;
        int[] buses = new int[0];
        return new Notes(startTime, buses);
    }
}


public class Advent2020Day13 {

    private static int findDepartureTime(final Notes notes) {
        return 0;
    }

    private static void testFindDepartureTime() {
        int expectedTime = 944;
        Notes notes = Notes.fromFile("2020/day13/test13a.txt");
        int actualTime = findDepartureTime(notes);
        assert actualTime == expectedTime : String.format("Expected departure time to be %d not %d!%n", expectedTime, actualTime);
    }

    public static void main(final String[] args) {
        testFindDepartureTime();
    }
}
