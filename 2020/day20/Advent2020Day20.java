package day20;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class ImageTile {

    private final long id;
    private final List<String> tile;
    private final int size;
    int top;
    int bottom;
    int left;
    int right;

    private static int convertToBinary(final String side) {
        String binary = side.replace('#', '1').replace('.', '0');
        return Integer.parseInt(binary, 2);
    }

    ImageTile(final long id, final List<String> tile) {
        this.id = id;
        this.tile = Collections.unmodifiableList(tile);
        this.size = this.tile.size();
        this.top = convertToBinary(tile.get(0));
        this.bottom = convertToBinary(tile.get(this.size - 1));
        StringBuilder sbLeft = new StringBuilder(this.size);
        StringBuilder sbRight = new StringBuilder(this.size);
        for (int i = 0; i < this.size; i++) {
            sbLeft.append(this.tile.get(i).charAt(0));
            sbRight.append(this.tile.get(i).charAt(this.size - 1));
        }
        this.left = convertToBinary(sbLeft.toString());
        this.right = convertToBinary(sbRight.toString());
    }

    private static long parseTileId(final String tileId) {
        String pattern = "^Tile (?<id>\\d+):$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(tileId);
        if (!m.find()) throw new IllegalStateException("Unable to parse tile id: " + tileId);
        return Long.parseLong(m.group("id"));
    }

    static ImageTile fromStrings(final List<String> tile) {
        long id = parseTileId(tile.remove(0));
        return new ImageTile(id, tile);
    }

    @Override
    public String toString() {
        return String.format("ImageTile{id=%d, size=%d, top=%d, right=%d, bottom=%d, left=%d}", id, size, top, right, bottom, left);
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
                if (line.startsWith("//")) continue;
                if (line.length() != 0) {
                    tile.add(line);
                } else {
                    tiles.add(ImageTile.fromStrings(tile));
                    tile = new ArrayList<>();
                }
            }
            tiles.add(ImageTile.fromStrings(tile));
        } catch (FileNotFoundException fnf) {
            throw new IllegalArgumentException("Unable to read tiles file!", fnf);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ImageAssembler{\n");
        for (ImageTile tile: this.tiles)
            sb.append(tile.toString()).append('\n');
        return sb.append("}\n").toString();
    }
}


public class Advent2020Day20 {

    private static long assembleTiles(final String filename) {
        ImageAssembler assembler = ImageAssembler.fromFile(filename);
        System.out.print(assembler);
        return assembler.assemble();
    }

    private static void testImageAssembler(final String filename, final long expectedCornerProduct) {
        long actualCornerProduct = assembleTiles(filename);
        assert actualCornerProduct == expectedCornerProduct :
                String.format("Expected assembled corner product to be %d not %d!%n", expectedCornerProduct, actualCornerProduct);
    }

    public static void main(final String[] args) {
        testImageAssembler("2020/day20/test20a.txt", 1951L * 3079L * 1171L * 2971L);
        System.out.printf("Day 20, part 1, corner product is %d.%n", assembleTiles("2020/day20/input20.txt"));
    }
}
