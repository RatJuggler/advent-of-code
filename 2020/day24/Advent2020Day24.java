package day24;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Floor {

    private final List<String> blackTiles = new ArrayList<>();

    Floor() {}

    private String followDirections(final String directions) {
        return directions;
    }

    void followInstructions(final String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String directions = scanner.nextLine();
                String flipTile = this.followDirections(directions);
                if (this.blackTiles.contains(flipTile))
                    this.blackTiles.remove(flipTile);
                else
                    this.blackTiles.add(flipTile);
            }
        } catch (FileNotFoundException fnf) {
            throw new IllegalArgumentException("Unable to read tile layout file!", fnf);
        }
    }

    int countBlackTile() {
        return this.blackTiles.size();
    }
}


public class Advent2020Day24 {

    private static int followInstructions(final String filename) {
        Floor floor = new Floor();
        floor.followInstructions(filename);
        return floor.countBlackTile();
    }

    private static void testFlipTiles() {
        int expectedBlackTiles = 10;
        int actualBlackTiles = followInstructions("2020/day24/test24a.txt");
        assert expectedBlackTiles == actualBlackTiles :
                String.format("Expected the final number of black tiles to be %d not %d!%n", expectedBlackTiles, actualBlackTiles);
    }

    public static void main(final String[] args) {
        testFlipTiles();
        System.out.printf("Day 24, part 1, final number of black tiles is %d.%n", followInstructions("2020/day24/input24.txt"));
    }
}
