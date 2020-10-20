package day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


class TrapDetector {

    private final List<String> room = new ArrayList<>();

    TrapDetector(final String firstRow) {
        this.room.add(firstRow);
    }

    private boolean isNewTrap(final char leftTile, final char centerTile, final char rightTile) {
        return (leftTile == '^' && centerTile == '^' && rightTile == '.') ||
                (leftTile == '.' && centerTile == '^' && rightTile == '^') ||
                (leftTile == '^' && centerTile == '.' && rightTile == '.') ||
                (leftTile == '.' && centerTile == '.' && rightTile == '^');
    }

    private String detectRow() {
        StringBuilder newRow = new StringBuilder();
        String currentRow = this.room.get(this.room.size() - 1);
        for (int tile = 0; tile < currentRow.length(); tile++) {
            char leftTile = '.';
            if (tile > 0) leftTile = currentRow.charAt(tile - 1);
            char centerTile = currentRow.charAt(tile);
            char rightTile = '.';
            if (tile < currentRow.length() - 1) rightTile = currentRow.charAt(tile + 1);
            if (this.isNewTrap(leftTile, centerTile, rightTile))
                newRow.append('^');
            else
                newRow.append('.');
        }
        return newRow.toString();
    }

    List<String> detect(final int rows) {
        while (this.room.size() < rows) {
            this.room.add(this.detectRow());
        }
        return room;
    }

    int safeTiles() {
        int safeTiles = 0;
        for (String row : this.room) {
            safeTiles += row.chars().filter(c -> c == '.').count();
        }
        return safeTiles;
    }
}


public class Advent18 {

    private static void part2() throws IOException {
        String firstRow = Files.readString(Paths.get("2016/day18/input18.txt"));
        TrapDetector detector = new TrapDetector(firstRow);
        detector.detect(400000);
        System.out.printf("Part 2, number of safe tiles = %s\n", detector.safeTiles());
    }

    private static void part1() throws IOException {
        String firstRow = Files.readString(Paths.get("2016/day18/input18.txt"));
        TrapDetector detector = new TrapDetector(firstRow);
        detector.detect(40);
        System.out.printf("Part 1, number of safe tiles = %s\n", detector.safeTiles());
    }

    private static void testSafeTileCount() {
        int expectedSafeCount = 38;
        TrapDetector detector = new TrapDetector(".^^.^.^^^^");
        detector.detect(10);
        int actualSafeCount = detector.safeTiles();
        assert actualSafeCount == expectedSafeCount : String.format("Expected safe count to be '%s' but was '%s'!", expectedSafeCount, actualSafeCount);
    }

    private static void testDetect() {
        TrapDetector detector = new TrapDetector("..^^.");
        List<String> room = detector.detect(3);
        String expectedSecondRow = ".^^^^";
        String actualSecondRow = room.get(1);
        assert actualSecondRow.equals(expectedSecondRow) : String.format("Expected 2nd row to be '%s' but was '%s'!", expectedSecondRow, actualSecondRow);
        String expectedThirdRow = "^^..^";
        String actualThirdRow = room.get(2);
        assert actualThirdRow.equals(expectedThirdRow) : String.format("Expected 3rd row to be '%s' but was '%s'!", expectedThirdRow, actualThirdRow);
    }

    public static void main(String[] args) throws IOException {
        testDetect();
        testSafeTileCount();
        part1();
        part2();
    }
}
