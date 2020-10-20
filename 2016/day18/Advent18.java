package day18;

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

    String nextRow() {
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
        this.room.add(newRow.toString());
        return newRow.toString();
    }

    int safeTiles(final int rows) {
        while (this.room.size() < rows) {
            this.nextRow();
        }
        int safeTiles = 0;
        for (String row : this.room) {
            safeTiles += row.chars().filter(c -> c == '.').count();
            System.out.println(row);
        }
        return safeTiles;
    }
}


public class Advent18 {

    private static void testSafeTileCount() {
        int expectedSafeCount = 38;
        TrapDetector detector = new TrapDetector(".^^.^.^^^^");
        int actualSafeCount = detector.safeTiles(10);
        assert actualSafeCount == expectedSafeCount : String.format("Expected safe count to be '%s' but was '%s'!", expectedSafeCount, actualSafeCount);
    }

    private static void testDetect() {
        TrapDetector detector = new TrapDetector("..^^.");
        String expectedSecondRow = ".^^^^";
        String actualSecondRow = detector.nextRow();
        assert actualSecondRow.equals(expectedSecondRow) : String.format("Expected 2nd row to be '%s' but was '%s'!", expectedSecondRow, actualSecondRow);
        String expectedThirdRow = "^^..^";
        String actualThirdRow = detector.nextRow();
        assert actualThirdRow.equals(expectedThirdRow) : String.format("Expected 3rd row to be '%s' but was '%s'!", expectedThirdRow, actualThirdRow);
    }

    public static void main(String[] args) {
        testDetect();
        testSafeTileCount();
    }
}
