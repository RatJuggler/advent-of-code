package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;


class SeatingSystem {

    private char[] layout;
    private final int rows;
    private final int columns;

    SeatingSystem(final String layout, final int rows) {
        this.layout = layout.toCharArray();
        this.rows = rows;
        this.columns = layout.length() / rows;
    }

    static SeatingSystem fromFile(final String filename) throws IOException {
        List<String> layout = Files.readAllLines(Paths.get(filename));
        return new SeatingSystem(String.join("", layout), layout.size());
    }

    private int checkPosition(final int position, final int dRow, final int dColumn) {
        int row = position / this.columns + dRow;
        int column = position % this.columns + dColumn;
        if (row < 0 || column < 0 || row >= this.rows || column >= this.columns)
            return 0;
        else
            return this.layout[row * this.columns + column] == '#' ? 1 : 0;
    }

    private int adjacentOccupied(final int position) {
        return checkPosition(position, -1, -1) +
            checkPosition(position, -1, 0) +
            checkPosition(position, -1, 1) +
            checkPosition(position, 0, -1) +
            checkPosition(position, 0, 1) +
            checkPosition(position, 1, -1) +
            checkPosition(position, 1, 0) +
            checkPosition(position, 1, 1);
    }

    private boolean applySeatingRules() {
        boolean seatChange = false;
        char[] newLayout = new char[this.layout.length];
        for (int i = 0; i < this.layout.length; i++) {
            char oldPosition = this.layout[i];
            char newPosition = oldPosition;
            if (oldPosition != '.') {
                int adjacentOccupied = this.adjacentOccupied(i);
                if (oldPosition == 'L' && adjacentOccupied == 0) {
                    newPosition = '#';
                    seatChange = true;
                } else if (oldPosition == '#' && adjacentOccupied > 3) {
                    newPosition = 'L';
                    seatChange = true;
                }
            }
            newLayout[i] = newPosition;
        }
        this.layout = newLayout;
        return seatChange;
    }

    long seatPeople() {
        while (this.applySeatingRules());
        return IntStream.range(0, this.layout.length)
                .mapToObj(i -> this.layout[i])
                .filter(c -> c == '#')
                .count();
    }
}


public class Advent2020Day11 {

    private static void testCountOccupiedSeats() throws IOException {
        long expectedOccupied = 37;
        SeatingSystem seating = SeatingSystem.fromFile("2020/day11/test11a.txt");
        long actualOccupied = seating.seatPeople();
        assert actualOccupied == expectedOccupied :
                String.format("Expected oocupied seats to be %d not %d!%n", expectedOccupied, actualOccupied);
    }

    public static void main(final String[] args) throws IOException {
        testCountOccupiedSeats();
        SeatingSystem seating = SeatingSystem.fromFile("2020/day11/input11.txt");
        System.out.printf("Day 11, Part 1 results in %d occupied seats.%n", seating.seatPeople());
    }
}
