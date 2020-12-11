package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


abstract class SeatingSystem {

    char[] layout;
    final int rows;
    final int columns;
    final int occupationTolerance;

    SeatingSystem(final String layout, final int rows, final int occupationTolerance) {
        this.layout = layout.toCharArray();
        this.rows = rows;
        this.columns = layout.length() / rows;
        this.occupationTolerance = occupationTolerance;
    }

    boolean validPosition(final int row, final int column) {
        return  row >= 0 && column >= 0 && row < this.rows && column < this.columns;
    }

    abstract int checkPosition(final int position, final int dRow, final int dColumn);

    private int countOccupied(final int position) {
        int occupied = 0;
        int[][] dOffsets = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int[] dOffset : dOffsets)
            occupied += checkPosition(position, dOffset[0], dOffset[1]);
        return occupied;
    }

    private char seatOccupied(final int position) {
        char c = this.layout[position];
        if (c != '.') {
            int adjacentOccupied = this.countOccupied(position);
            if (c == 'L' && adjacentOccupied == 0)
                return '#';
            else if (c == '#' && adjacentOccupied >= this.occupationTolerance)
                return 'L';
        }
        return c;
    }

    private boolean applySeatingRules() {
        AtomicBoolean seatChange = new AtomicBoolean(false);
        this.layout = IntStream.range(0, this.layout.length)
                .mapToObj(i -> {
                    char newC = this.seatOccupied(i);
                    if (newC != this.layout[i]) seatChange.set(true);
                    return newC;
                })
                .map(String::valueOf)
                .collect(Collectors.joining())
                .toCharArray();
        return seatChange.get();
    }

    long seatPeople() {
        while (this.applySeatingRules())
            assert true;
        return IntStream.range(0, this.layout.length)
                .mapToObj(i -> this.layout[i])
                .filter(c -> c == '#')
                .count();
    }
}


class AdjacentSeatingSystem extends SeatingSystem {

    AdjacentSeatingSystem(final String layout, final int rows) {
        super(layout, rows, 4);
    }

    @Override
    int checkPosition(final int position, final int dRow, final int dColumn) {
        int row = position / this.columns + dRow;
        int column = position % this.columns + dColumn;
        if (this.validPosition(row, column))
            return this.layout[row * this.columns + column] == '#' ? 1 : 0;
        else
            return 0;
    }
}


class LineOfSightSeatingSystem extends SeatingSystem {

    LineOfSightSeatingSystem(final String layout, final int rows) {
        super(layout, rows, 5);
    }

    @Override
    int checkPosition(final int position, final int dRow, final int dColumn) {
        int row = position / this.columns + dRow;
        int column = position % this.columns + dColumn;
        while (this.validPosition(row, column)) {
            char content = this.layout[row * this.columns + column];
            if (content != '.') return content == '#' ? 1 : 0;
            row += dRow;
            column += dColumn;
        }
        return 0;
    }
}


class SeatingSystemFactory {

    private SeatingSystemFactory() {}

    static SeatingSystem fromFile(final String system, final String filename) throws IOException {
        List<String> layout = Files.readAllLines(Paths.get(filename));
        if ("Adjacent".equalsIgnoreCase(system))
            return new AdjacentSeatingSystem(String.join("", layout), layout.size());
        else if ("LineOfSight".equalsIgnoreCase(system))
            return new LineOfSightSeatingSystem(String.join("", layout), layout.size());
        else
            throw new IllegalArgumentException("Unknown seating system: " + system);
    }
}


public class Advent2020Day11 {

    private static void testAdjacentSeatingSystem() throws IOException {
        long expectedOccupied = 37;
        SeatingSystem seating = SeatingSystemFactory.fromFile("Adjacent","2020/day11/test11a.txt");
        long actualOccupied = seating.seatPeople();
        assert actualOccupied == expectedOccupied :
                String.format("Expected occupied seats to be %d not %d!%n", expectedOccupied, actualOccupied);
    }

    private static void testLineOfSightSeatingSystem() throws IOException {
        long expectedOccupied = 26;
        SeatingSystem seating = SeatingSystemFactory.fromFile("LineOfSight","2020/day11/test11a.txt");
        long actualOccupied = seating.seatPeople();
        assert actualOccupied == expectedOccupied :
                String.format("Expected occupied seats to be %d not %d!%n", expectedOccupied, actualOccupied);
    }

    public static void main(final String[] args) throws IOException {
        testAdjacentSeatingSystem();
        SeatingSystem adjacent = SeatingSystemFactory.fromFile("Adjacent", "2020/day11/input11.txt");
        System.out.printf("Day 11, Part 1 results in %d occupied seats.%n", adjacent.seatPeople());
        testLineOfSightSeatingSystem();
        SeatingSystem lineOfSight = SeatingSystemFactory.fromFile("LineOfSight", "2020/day11/input11.txt");
        System.out.printf("Day 11, Part 2 results in %d occupied seats.%n", lineOfSight.seatPeople());
    }
}
