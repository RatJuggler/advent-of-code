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
        String binaryRow = seatCode.substring(0, 7).replace('B', '1').replace('F', '0');
        String binaryColumn = seatCode.substring(7).replace('R', '1').replace('L', '0');
        int row = Integer.parseInt(binaryRow, 2);
        int column = Integer.parseInt(binaryColumn, 2);
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

    private static int[] readBoardingPasses(final String filename) {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.map(Seat::createFromCode)
                    .mapToInt(Seat::id)
                    .sorted()
                    .toArray();
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Problem reading boarding pass file!", ioe);
        }
    }

    private static int findMissingSeatId(final int[] seats) {
        for (int i = 0; i < seats.length - 1; i++) {
            int nextSeat = seats[i] + 1;
            if (seats[i + 1] != nextSeat)
                return nextSeat;
        }
        return -1;
    }

    private static void testPart1HighestSeat() {
        int expectedHighest = 820;
        int[] testSeats = readBoardingPasses("2020/day5/test5a.txt");
        int actualHighest = testSeats[testSeats.length - 1];
        assert actualHighest == expectedHighest :
                String.format("Expected highest seat Id to be %d, but, was %d!", expectedHighest, actualHighest);
    }

    public static void main(final String[] args) {
        testPart1SeatIdGenerator();
        testPart1HighestSeat();
        int[] seats = readBoardingPasses("2020/day5/input5.txt");
        System.out.printf("Day 5, part 1, highest seat Id is %d.%n", seats[seats.length - 1]);
        System.out.printf("Day 5, part 2, my seat Id is %d.%n", findMissingSeatId(seats));
    }
}
