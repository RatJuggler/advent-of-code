package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;


abstract class SeatingSystem {

    protected char[] layout;
    protected final int rows;
    protected final int columns;
    private final int occupationTolerance;

    SeatingSystem(final String layout, final int rows, final int occupationTolerance) {
        this.layout = layout.toCharArray();
        this.rows = rows;
        this.columns = layout.length() / rows;
        this.occupationTolerance = occupationTolerance;
    }

    abstract int checkPosition(final int position, final int dRow, final int dColumn);

    int countOccupied(final int position) {
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
            char oldContent = this.layout[i];
            char newContent = oldContent;
            if (oldContent != '.') {
                int adjacentOccupied = this.countOccupied(i);
                if (oldContent == 'L' && adjacentOccupied == 0) {
                    newContent = '#';
                    seatChange = true;
                } else if (oldContent == '#' && adjacentOccupied >= this.occupationTolerance) {
                    newContent = 'L';
                    seatChange = true;
                }
            }
            newLayout[i] = newContent;
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


class AdjacentSeatingSystem extends SeatingSystem {

    AdjacentSeatingSystem(final String layout, final int rows) {
        super(layout, rows, 4);
    }

    @Override
    int checkPosition(final int position, final int dRow, final int dColumn) {
        int row = position / this.columns + dRow;
        int column = position % this.columns + dColumn;
        if (row < 0 || column < 0 || row >= this.rows || column >= this.columns)
            return 0;
        else
            return this.layout[row * this.columns + column] == '#' ? 1 : 0;
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
        while (row >= 0 && column >= 0 && row < this.rows && column < this.columns) {
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
