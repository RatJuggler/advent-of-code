package day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


class EnergySource3D {

    private static final int DIMENSION_SIZE = 29;
    private static final int CENTER_POINT = (DIMENSION_SIZE + 1) / 2;

    private char[][][] pocketDimensions = new char[DIMENSION_SIZE][DIMENSION_SIZE][DIMENSION_SIZE];

    EnergySource3D(final List<String> initialState) {
        for (char[][] zDimension : this.pocketDimensions)
            for (char[] yDimension : zDimension)
                Arrays.fill(yDimension, '.');
        for (int y = 0; y < initialState.size(); y++)
            for (int x = 0; x < initialState.get(y).length(); x++)
                this.pocketDimensions
                        [CENTER_POINT]
                        [CENTER_POINT - initialState.size() + y]
                        [CENTER_POINT - initialState.get(y).length() + x] = initialState.get(y).charAt(x);
    }

    static EnergySource3D fromFile(final String filename) {
        List<String> initialState;
        try {
            initialState = Files.readAllLines(Paths.get(filename));
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Unable to read initial state file!", ioe);
        }
        return new EnergySource3D(initialState);
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

class EnergySource4D {

    private static final int DIMENSION_SIZE = 39;
    private static final int CENTER_POINT = (DIMENSION_SIZE + 1) / 2;

    private char[][][][] pocketDimensions = new char[DIMENSION_SIZE][DIMENSION_SIZE][DIMENSION_SIZE][DIMENSION_SIZE];

    EnergySource4D(final List<String> initialState) {
        for (char[][][] wDimension : this.pocketDimensions)
            for (char[][] zDimension : wDimension)
                for (char[] yDimension : zDimension)
                    Arrays.fill(yDimension, '.');
        for (int y = 0; y < initialState.size(); y++)
            for (int x = 0; x < initialState.get(y).length(); x++)
                this.pocketDimensions
                        [CENTER_POINT]
                        [CENTER_POINT]
                        [CENTER_POINT - initialState.size() + y]
                        [CENTER_POINT - initialState.get(y).length() + x] = initialState.get(y).charAt(x);
    }

    static EnergySource4D fromFile(final String filename) {
        List<String> initialState;
        try {
            initialState = Files.readAllLines(Paths.get(filename));
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Unable to read initial state file!", ioe);
        }
        return new EnergySource4D(initialState);
    }

    private void dumpDimensions(final int generation) {
        System.out.println("\nGeneration: " + generation);
        for (int w = 0; w < this.pocketDimensions.length; w++) {
            for (int z = 0; z < this.pocketDimensions.length; z++) {
                System.out.println("\nz=" + z + ", w=" + w);
                for (int y = 0; y < this.pocketDimensions[z].length; y++)
                    System.out.println(String.valueOf(this.pocketDimensions[w][z][y]));
            }
        }
    }

    private char[][][][] copyDimensions() {
        char[][][][] newDimensions = new char[DIMENSION_SIZE][DIMENSION_SIZE][DIMENSION_SIZE][DIMENSION_SIZE];
        for (int w = 0; w < this.pocketDimensions.length; w++)
            for (int z = 0; z < this.pocketDimensions[w].length; z++)
                for (int y = 0; y < this.pocketDimensions[w][z].length; y++)
                    System.arraycopy(this.pocketDimensions[w][z][y], 0, newDimensions[w][z][y], 0, this.pocketDimensions[w][z][y].length);
        return newDimensions;
    }

    private char newStatus(final int w, final int z, final int y, final int x) {
        int[][] dOffsets = {{-1, -1, -1, -1}, {-1, -1, -1,  0}, {-1, -1, -1, 1},
                            {-1, -1,  0, -1}, {-1, -1,  0,  0}, {-1, -1,  0, 1},
                            {-1, -1,  1, -1}, {-1, -1,  1,  0}, {-1, -1,  1, 1},
                            {-1,  0, -1, -1}, {-1,  0, -1,  0}, {-1,  0, -1, 1},
                            {-1,  0,  0, -1}, {-1,  0,  0,  0}, { -1, 0,  0, 1},
                            {-1,  0,  1, -1}, {-1,  0,  1,  0}, {-1,  0,  1, 1},
                            {-1,  1, -1, -1}, {-1,  1, -1,  0}, {-1,  1, -1, 1},
                            {-1,  1,  0, -1}, {-1,  1,  0,  0}, {-1,  1,  0, 1},
                            {-1,  1,  1, -1}, {-1,  1,  1,  0}, {-1,  1,  1, 1},

                            { 0, -1, -1, -1}, { 0, -1, -1,  0}, { 0, -1, -1, 1},
                            { 0, -1,  0, -1}, { 0, -1,  0,  0}, { 0, -1,  0, 1},
                            { 0, -1,  1, -1}, { 0, -1,  1,  0}, { 0, -1,  1, 1},
                            { 0,  0, -1, -1}, { 0,  0, -1,  0}, { 0,  0, -1, 1},
                            { 0,  0,  0, -1},                   { 0,  0,  0, 1},
                            { 0,  0,  1, -1}, { 0,  0,  1,  0}, { 0,  0,  1, 1},
                            { 0,  1, -1, -1}, { 0,  1, -1,  0}, { 0,  1, -1, 1},
                            { 0,  1,  0, -1}, { 0,  1,  0,  0}, { 0,  1,  0, 1},
                            { 0,  1,  1, -1}, { 0,  1,  1,  0}, { 0,  1,  1, 1},

                            { 1, -1, -1, -1}, { 1, -1, -1,  0}, { 1, -1, -1, 1},
                            { 1, -1,  0, -1}, { 1, -1,  0,  0}, { 1, -1,  0, 1},
                            { 1, -1,  1, -1}, { 1, -1,  1,  0}, { 1, -1,  1, 1},
                            { 1,  0, -1, -1}, { 1,  0, -1,  0}, { 1,  0, -1, 1},
                            { 1,  0,  0, -1}, { 1,  0,  0,  0}, { 1,  0,  0, 1},
                            { 1,  0,  1, -1}, { 1,  0,  1,  0}, { 1,  0,  1, 1},
                            { 1,  1, -1, -1}, { 1,  1, -1,  0}, { 1,  1, -1, 1},
                            { 1,  1,  0, -1}, { 1,  1,  0,  0}, { 1,  1,  0, 1},
                            { 1,  1,  1, -1}, { 1,  1,  1,  0}, { 1,  1,  1, 1}};
        int activeNeighbours = 0;
        for (int[] dOffset : dOffsets)
            activeNeighbours += this.pocketDimensions[w + dOffset[0]][z + dOffset[1]][y + dOffset[2]][x + dOffset[3]] == '#' ? 1 : 0;
        char current = this.pocketDimensions[w][z][y][x];
        if (current == '.')
            return activeNeighbours == 3 ? '#' : '.';
        else if (current == '#')
            return activeNeighbours == 2 || activeNeighbours == 3 ? '#' : '.';
        else
            throw new IllegalStateException("Unknown state encountered!");
    }

    private void cycle() {
        char[][][][] newDimensions = copyDimensions();
        for (int w = 1; w < this.pocketDimensions.length - 1; w++)
            for (int z = 1; z < this.pocketDimensions[w].length - 1; z++)
                for (int y = 1; y < this.pocketDimensions[w][z].length - 1; y++)
                    for (int x = 1; x < this.pocketDimensions[w][z][y].length - 1; x++)
                        newDimensions[w][z][y][x] = this.newStatus(w, z, y, x);
        this.pocketDimensions = newDimensions;
    }

    private int countCubes() {
        int cubeCount = 0;
        for (char[][][] wDimension : this.pocketDimensions)
            for (char[][] zDimension : wDimension)
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

    private static void testEnergySource3D() {
        int expectedCubes = 112;
        EnergySource3D source = EnergySource3D.fromFile("2020/day17/test17a.txt");
        int actualCubes = source.cycles(6);
        assert actualCubes == expectedCubes : String.format("Expected number of cubes to be %d not %d!%n", expectedCubes, actualCubes);
    }

    private static void testEnergySource4D() {
        int expectedCubes = 848;
        EnergySource4D source = EnergySource4D.fromFile("2020/day17/test17a.txt");
        int actualCubes = source.cycles(6);
        assert actualCubes == expectedCubes : String.format("Expected number of cubes to be %d not %d!%n", expectedCubes, actualCubes);
    }

    public static void main(final String[] args) {
        testEnergySource3D();
        EnergySource3D source3d = EnergySource3D.fromFile("2020/day17/input17.txt");
        System.out.printf("Day 17, Part 1 number of cubes after 6 cycles is %d.%n", source3d.cycles(6));
        testEnergySource4D();
        EnergySource4D source4d = EnergySource4D.fromFile("2020/day17/input17.txt");
        System.out.printf("Day 17, Part 2 number of cubes after 6 cycles is %d.%n", source4d.cycles(6));
    }
}
