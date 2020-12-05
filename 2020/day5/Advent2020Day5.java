package day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


class Seat {

    private static final int ROWS = 128;
    private static final int COLUMNS = 8;

    private final int row;
    private final int column;

    Seat(final int row, final int column) {
        this.row = row;
        this.column = column;
    }

    private static int search(final String code, final int space, final char upper) {
        int position = 0;
        int partition = space;
        for (char move : code.toCharArray()) {
            partition /= 2;
            if (move == upper) position += partition;
        }
        return position;
    }

    static Seat createFromCode(final String seatCode) {
        int row = search(seatCode.substring(0, 7), ROWS, 'B');
        int column = search(seatCode.substring(7), COLUMNS, 'R');
        return new Seat(row, column);
    }

    int id() {
        return this.row * 8 + this.column;
    }
}


public class Advent2020Day5 {

    private static void testSeatIdGenerator(final String seatCode, int expectedId) {
        assert Seat.createFromCode(seatCode).id() == expectedId :
                String.format("Expected Id for seat \"%s\" to be %s!", seatCode, expectedId);

    }

    private static void testPart1SeatIdGenerator() {
        testSeatIdGenerator("FBFBBFFRLR", 357);
        testSeatIdGenerator("BFFFBBFRRR", 567);
        testSeatIdGenerator("FFFBBBFRRR", 119);
        testSeatIdGenerator("BBFFBBFRLL", 820);
    }

    private static int findHighestSeatId(String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.map(Seat::createFromCode)
                    .mapToInt(Seat::id)
                    .max()
                    .orElse(-1);
        }
    }

    public static void main(final String[] args) throws IOException {
        testPart1SeatIdGenerator();
        assert findHighestSeatId("2020/day5/test5a.txt") == 820 : "Expected highest seat Id to be 820!";
        System.out.printf("Day 5, part 1, highest seat Id is %d.%n", findHighestSeatId("2020/day5/input5.txt"));
    }
}
