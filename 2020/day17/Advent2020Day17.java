package day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


class EnergySource {

    private static final int DIMENSION_SIZE = 99;
    private static final int CENTER_POINT = (DIMENSION_SIZE + 1) / 2;

    private char[][][] pocketDimensions = new char[DIMENSION_SIZE][DIMENSION_SIZE][DIMENSION_SIZE];

    EnergySource(final List<String> initialState) {
        for (char[][] zDimension : this.pocketDimensions)
            for (char[] yDimension : zDimension)
                Arrays.fill(yDimension, '.');
        for (int y = 0; y < initialState.size(); y++)
            for (int x = 0; x < initialState.get(y).length(); x++)
                this.pocketDimensions[CENTER_POINT]
                        [CENTER_POINT - initialState.size() + y]
                        [CENTER_POINT - initialState.get(y).length() + x] = initialState.get(y).charAt(x);
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

    private void dumpDimensions(final int generation) {
        System.out.println("\nGeneration: " + generation);
        for (int z = 0; z < this.pocketDimensions.length; z++) {
            System.out.println("\nz=" + z);
            for (int y = 0; y < this.pocketDimensions[z].length; y++)
                System.out.println(String.valueOf(this.pocketDimensions[z][y]));
        }
    }

    private char[][][] copyDimensions() {
        char[][][] newDimensions = new char[DIMENSION_SIZE][DIMENSION_SIZE][DIMENSION_SIZE];
        for (int z = 0; z < this.pocketDimensions.length; z++)
            for (int y = 0; y < this.pocketDimensions[z].length; y++)
                System.arraycopy(this.pocketDimensions[z][y], 0, newDimensions[z][y], 0, this.pocketDimensions[z][y].length);
        return newDimensions;
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
            activeNeighbours += this.pocketDimensions[z + dOffset[0]][y + dOffset[1]][x + dOffset[2]] == '#' ? 1 : 0;
        char current = this.pocketDimensions[z][y][x];
        if (current == '.')
            return activeNeighbours == 3 ? '#' : '.';
        else if (current == '#')
            return activeNeighbours == 2 || activeNeighbours == 3 ? '#' : '.';
        else
            throw new IllegalStateException("Unknown state encountered!");
    }

    private void cycle() {
        char[][][] newDimensions = copyDimensions();
        for (int z = 1; z < this.pocketDimensions.length - 1; z++)
            for (int y = 1; y < this.pocketDimensions[z].length - 1; y++)
                for (int x = 1; x < this.pocketDimensions[z][y].length - 1; x++)
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
//        dumpDimensions(0);
        for (int i = 0; i < runFor; i++) {
            cycle();
//            dumpDimensions(i + 1);
        }
        return this.countCubes();
    }
}


public class Advent2020Day17 {

    private static void testEnergySource() {
        int expectedCubes = 112;
        EnergySource source = EnergySource.fromFile("2020/day17/test17a.txt");
        int actualCubes = source.cycles(6);
        assert actualCubes == expectedCubes : String.format("Expected number of cubes to be %d not %d!%n", expectedCubes, actualCubes);
    }

    public static void main(final String[] args) {
        testEnergySource();
        EnergySource source = EnergySource.fromFile("2020/day17/input17.txt");
        System.out.printf("Day 17, Part 1 number of cubes after 6 cycles is %d.%n", source.cycles(6));
    }
}
