package day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


class TrapDetector {

    private final List<String> room = new ArrayList<>();

    private static String detectRow(final String currentRow) {
        StringBuilder newRow = new StringBuilder();
        for (int tile = 0; tile < currentRow.length(); tile++) {
            char leftTile = tile > 0 ? currentRow.charAt(tile - 1) : '.';
            char rightTile = tile < currentRow.length() - 1 ? currentRow.charAt(tile + 1) : '.';
            newRow.append(leftTile == rightTile ? '.' : '^');
        }
        return newRow.toString();
    }

    TrapDetector(final String firstRow, final int rows) {
        String nextRow = firstRow;
        while (this.room.size() < rows) {
            this.room.add(nextRow);
            nextRow = TrapDetector.detectRow(nextRow);
        }
    }

    String getRoomRow(final int row) {
        return this.room.get(row);
    }

    long safeTiles() {
        return this.room.stream()
                .map(row -> row.chars().filter(c -> c == '.').count())
                .reduce( 0L, Long::sum);
    }
}


public class Advent18 {

    private static void part2() throws IOException {
        String firstRow = Files.readString(Paths.get("2016/day18/input18.txt"));
        TrapDetector detector = new TrapDetector(firstRow, 400000);
        System.out.printf("Part 2, number of safe tiles = %s\n", detector.safeTiles());
    }

    private static void part1() throws IOException {
        String firstRow = Files.readString(Paths.get("2016/day18/input18.txt"));
        TrapDetector detector = new TrapDetector(firstRow, 40);
        System.out.printf("Part 1, number of safe tiles = %s\n", detector.safeTiles());
    }

    private static void testSafeTileCount() {
        long expectedSafeCount = 38;
        TrapDetector detector = new TrapDetector(".^^.^.^^^^", 10);
        long actualSafeCount = detector.safeTiles();
        assert actualSafeCount == expectedSafeCount : String.format("Expected safe count to be '%s' but was '%s'!", expectedSafeCount, actualSafeCount);
    }

    private static void testDetect() {
        TrapDetector detector = new TrapDetector("..^^.", 3);
        String expectedSecondRow = ".^^^^";
        String actualSecondRow = detector.getRoomRow(1);
        assert actualSecondRow.equals(expectedSecondRow) : String.format("Expected 2nd row to be '%s' but was '%s'!", expectedSecondRow, actualSecondRow);
        String expectedThirdRow = "^^..^";
        String actualThirdRow = detector.getRoomRow(2);
        assert actualThirdRow.equals(expectedThirdRow) : String.format("Expected 3rd row to be '%s' but was '%s'!", expectedThirdRow, actualThirdRow);
    }

    public static void main(String[] args) throws IOException {
        testDetect();
        testSafeTileCount();
        part1();
        part2();
    }
}
