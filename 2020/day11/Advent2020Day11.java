package day11;


class SeatingSystem {

    SeatingSystem() {}

    static SeatingSystem fromFile(final String filename) {
        return new SeatingSystem();
    }

    int seatPeople() {
        return 0;
    }


}
public class Advent2020Day11 {

    private static void testCountOccupiedSeats() {
        int expectedOccupied = 37;
        SeatingSystem seating = SeatingSystem.fromFile("2020/day11/test11a.txt");
        int actualOccupied = seating.seatPeople();
        assert actualOccupied == expectedOccupied :
                String.format("Expected oocupied seats to be %d not %d!%n", expectedOccupied, actualOccupied);
    }

    public static void main(final String[] args) {
        testCountOccupiedSeats();
    }
}
