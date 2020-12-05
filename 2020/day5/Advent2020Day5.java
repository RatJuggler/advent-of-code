package day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


class Seat {

    private final int row;
    private final int column;

    Seat(final int row, final int column) {
        this.row = row;
        this.column = column;
    }

    static Seat createFromCode(final String seatCode) {
        return new Seat(0, 0);
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
