package day20;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


class ImageTile {

    private final List<String> tile;

    ImageTile(final List<String> tile) {
        this.tile = Collections.unmodifiableList(tile);
    }
}


class ImageAssembler {

    private final List<ImageTile> tiles;

    ImageAssembler(final List<ImageTile> tiles) {
        this.tiles = Collections.unmodifiableList(tiles);
    }

    private static List<ImageTile> readImageTiles(final String filename) {
        List<ImageTile> tiles = new ArrayList<>();
        List<String> tile = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.length() != 0) {
                    tile.add(line);
                } else {
                    tiles.add(new ImageTile(tile));
                    tile = new ArrayList<>();
                }
            }
            tiles.add(new ImageTile(tile));
        } catch (FileNotFoundException fnf) {
            throw new IllegalArgumentException("Unable to read message file!", fnf);
        }
        return tiles;
    }

    static ImageAssembler fromFile(final String filename) {
        List<ImageTile> tiles = readImageTiles(filename);
        return new ImageAssembler(tiles);
    }

    long assemble() {
        return 0;
    }
}


public class Advent2020Day20 {

    private static long assembleTiles(final String filename) {
        return ImageAssembler.fromFile(filename).assemble();
    }

    private static void testImageAssembler() {
        long expectedCornerProduct = 20899048083289L;
        long actualCornerProduct = assembleTiles("2020/day20/test20a.txt");
        assert actualCornerProduct == expectedCornerProduct :
                String.format("Expected assembled corner product to be %d not %d!%n", expectedCornerProduct, actualCornerProduct);
    }

    public static void main(final String[] args) {
        testImageAssembler();
        System.out.printf("Day 20, part 1, corner product is %d.%n", assembleTiles("2020/day20/input20.txt"));
    }
}
