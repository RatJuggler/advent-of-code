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

    final long id;
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

    void rotate() {
        int tempTop = this.top;
        this.top = this.left;
        this.left = this.bottom;
        this.bottom = this.right;
        this.right = tempTop;
    }

    private int reverse(final int side) {
        StringBuilder reverse = new StringBuilder(this.size).append(Integer.toBinaryString(side));
        while (reverse.length() < this.size)
            reverse.insert(0, '0');
        return Integer.parseInt(reverse.reverse().toString(), 2);
    }

    void horizontalFlip() {
        int tempTop = this.top;
        this.top = this.bottom;
        this.bottom = tempTop;
        this.left = reverse(this.left);
        this.right = reverse(this.right);
    }

    void verticalFlip() {
        this.top = reverse(this.top);
        this.bottom = reverse(this.bottom);
        int templeft = this.left;
        this.left = this.right;
        this.right = templeft;
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

    private void flip(final int flip, final ImageTile tile) {
        if (flip == 0) {
            tile.horizontalFlip();
        } else if (flip == 1) {
            tile.horizontalFlip();
            tile.verticalFlip();
        } else if (flip == 2) {
            tile.verticalFlip();
            tile.horizontalFlip();
            tile.verticalFlip();
        } else {
            tile.verticalFlip();
            tile.horizontalFlip();
        }
    }

    long assemble() {
        List<ImageTile> topEdges = new ArrayList<>(this.tiles);
        List<ImageTile> bottomEdges = new ArrayList<>(this.tiles);
        List<ImageTile> leftEdges = new ArrayList<>(this.tiles);
        List<ImageTile> rightEdges = new ArrayList<>(this.tiles);
        for (ImageTile checkTile: this.tiles) {
            for (int checkFlip = 0; checkFlip < 4; checkFlip++) {
                for (int checkRotate = 0; checkRotate < 4; checkRotate++) {
                    for (ImageTile withTile : this.tiles) {
                        for (int withFlip = 0; withFlip < 4; withFlip++) {
                            for (int withRotate = 0; withRotate < 4; withRotate++) {
                                if (checkTile.id == withTile.id) continue;
                                if (checkTile.top == withTile.bottom) topEdges.remove(checkTile);
                                if (checkTile.bottom == withTile.top) bottomEdges.remove(checkTile);
                                if (checkTile.left == withTile.right) leftEdges.remove(checkTile);
                                if (checkTile.right == withTile.left) rightEdges.remove(checkTile);
                                withTile.rotate();
                            }
                            flip(withFlip, withTile);
                        }
                    }
                    checkTile.rotate();
                }
                flip(checkFlip, checkTile);
            }
        }
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
        // Simple test file with no rotations or flips.
//        testImageAssembler("2020/day20/test20a.txt", 11L * 13L * 33L * 31L);
        // Test file with rotations and flips.
        testImageAssembler("2020/day20/test20b.txt", 1951L * 3079L * 1171L * 2971L);
        System.out.printf("Day 20, part 1, corner product is %d.%n", assembleTiles("2020/day20/input20.txt"));
    }
}
