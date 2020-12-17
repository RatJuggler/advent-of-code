package day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


class EnergySource {

    private char[][][] pocketDimensions = new char[1][][];

    EnergySource(final List<String> initialState) {
        char[][] initialPlane = new char[initialState.size()][];
        for (int y = 0; y < initialState.size(); y++)
            initialPlane[y] = initialState.get(y).toCharArray();
        this.pocketDimensions[0] = initialPlane;
    }

    static EnergySource fromFile(final String filename) {
        List<String> initialState;
        try {
            initialState = Files.readAllLines(Paths.get(filename));
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Unable to read initial state file!", ioe);
        }
        return new EnergySource(initialState);
    }

    private char[] buildEmptyLine(final int size) {
        return ".".repeat(size).toCharArray();
    }

    private char[][] buildEmptyPlane(final int ySize, final int xSize) {
        char[][] emptyPlane = new char[ySize][];
        for (int y = 0; y < ySize; y++)
            emptyPlane[y] = buildEmptyLine(xSize);
        return emptyPlane;
    }

    private char[][] buildNewPlane(final int ySize, final int xSize, final int fromZ) {
        char[][] newPlane = new char[ySize][];
        newPlane[0] = buildEmptyLine(xSize);
        for (int y = 1; y < ySize - 1; y++)
            newPlane[y] = ('.' + String.valueOf(this.pocketDimensions[fromZ][y - 1]) + '.').toCharArray();
        newPlane[ySize - 1] = buildEmptyLine(xSize);
        return newPlane;
    }

    private char[][][] expandDimensions() {
        int newZSize = this.pocketDimensions.length + 2;
        char[][][] newDimensions = new char[newZSize][][];
        int newYSize = this.pocketDimensions[0].length + 2;
        int newXSize = this.pocketDimensions[0][0].length + 2;
        newDimensions[0] = buildEmptyPlane(newYSize, newXSize);
        for (int z = 1; z < newZSize - 1; z++)
            newDimensions[z] = buildNewPlane(newYSize, newXSize, z - 1);
        newDimensions[newZSize - 1] = buildEmptyPlane(newYSize, newXSize);
        return newDimensions;
    }

    private void dumpDimensions(final int generation) {
        System.out.println("\nGeneration: " + generation);
        for (int z = 0; z < this.pocketDimensions.length; z++) {
            System.out.println("\nz=" + z);
            for (int y = 0; y < this.pocketDimensions[z].length; y++)
                System.out.println(String.valueOf(this.pocketDimensions[z][y]));
        }
    }

    boolean validPosition(final int z, final int y, final int x) {
        return 0 <= z && z < this.pocketDimensions.length &&
               0 <= y && y < this.pocketDimensions[0].length &&
               0 <= x && x < this.pocketDimensions[0][0].length;
    }

    int checkPosition(final int z, final int y, final int x) {
        if (validPosition(z, y, x))
            return this.pocketDimensions[z][y][x] == '#' ? 1 : 0;
        else
            return 0;
    }

    private char newStatus(final int z, final int y, final int x) {
        int[][] dOffsets = {{-1, -1, -1}, {-1, -1,  0}, {-1, -1, 1},
                            {-1,  0, -1}, {-1,  0,  0}, {-1,  0, 1},
                            {-1,  1, -1}, {-1,  1,  0}, {-1,  1, 1},
                            { 0, -1, -1}, { 0, -1,  0}, { 0, -1, 1},
                            { 0,  0, -1},               { 0,  0, 1},
                            { 0,  1, -1}, { 0,  1,  0}, { 0,  1, 1},
                            { 1, -1, -1}, { 1, -1,  0}, { 1, -1, 1},
                            { 1,  0, -1}, { 1,  0,  0}, { 1,  0, 1},
                            { 1,  1, -1}, { 1,  1,  0}, { 1,  1, 1}};
        int activeNeighbours = 0;
        for (int[] dOffset : dOffsets)
            activeNeighbours += checkPosition(z + dOffset[0], y + dOffset[1], x + dOffset[2]);
        char current = this.pocketDimensions[z][y][x];
        if (current == '.')
            return activeNeighbours == 3 ? '#' : '.';
        else if (current == '#')
            return activeNeighbours == 2 || activeNeighbours == 3 ? '#' : '.';
        else
            throw new IllegalStateException("Unknown state encountered!");
    }

    private void cycle() {
        char[][][] newDimensions = this.expandDimensions();
        this.pocketDimensions = this.expandDimensions();
        for (int z = 0; z < this.pocketDimensions.length; z++)
            for (int y = 0; y < this.pocketDimensions[z].length; y++)
                for (int x = 0; x < this.pocketDimensions[z][y].length; x++)
                    newDimensions[z][y][x] = this.newStatus(z, y, x);
        this.pocketDimensions = newDimensions;
    }

    private int countCubes() {
        int cubeCount = 0;
        for (char[][] zDimension : this.pocketDimensions)
            for (char[] yDimension : zDimension)
                for (char xDimension : yDimension)
                    if (xDimension == '#') cubeCount++;
        return cubeCount;
    }

    int cycles(final int runFor) {
        dumpDimensions(0);
        for (int i = 0; i < runFor; i++) {
            cycle();
            dumpDimensions(i + 1);
        }
        return this.countCubes();
    }
}


public class Advent2020Day17 {

    private static void testEnergySource() {
        int expectedCubes = 112;
        EnergySource source = EnergySource.fromFile("2020/day17/test17a.txt");
        int actualCubes = source.cycles(3);
        assert actualCubes == expectedCubes : String.format("Expected number of cubes to be %d not %d!%n", expectedCubes, actualCubes);
    }

    public static void main(final String[] args) {
        testEnergySource();
        EnergySource source = EnergySource.fromFile("2020/day17/input17.txt");
        System.out.printf("Day 17, Part 1 number of cubes after 6 cycles is %d.%n", source.cycles(6));
    }
}
