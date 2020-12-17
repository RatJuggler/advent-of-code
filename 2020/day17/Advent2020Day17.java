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

    private char[][] buildEmptyPlane(final int size) {
        char[][] emptyPlane = new char[size][];
        for (int y = 0; y < size; y++) {
            emptyPlane[y] = buildEmptyLine(size);
        }
        return emptyPlane;
    }

    private char[][][] expandDimensions() {
        int newZSize = this.pocketDimensions.length + 2;
        char[][][] newDimensions = new char[newZSize][][];
        int newYSize = this.pocketDimensions[0].length + 2;
        newDimensions[0] = buildEmptyPlane(newYSize);
        int newXSize = this.pocketDimensions[0][0].length + 2;
        for (int z = 0; z < newZSize - 2; z++) {
            char[][] newPlane = new char[newYSize][];
            newPlane[0] = buildEmptyLine(newXSize);
            for (int y = 0; y < newYSize - 2; y++) {
                newPlane[y + 1] = ('.' + String.valueOf(this.pocketDimensions[z][y]) + '.').toCharArray();
            }
            newPlane[newYSize - 1] = buildEmptyLine(newXSize);
            newDimensions[z + 1] = newPlane;
        }
        newDimensions[newZSize - 1] = buildEmptyPlane(newYSize);
        return newDimensions;
    }

    private void dumpDimensions(final int generation) {
        System.out.println("\nGeneration: " + generation);
        for (int z = 0; z < this.pocketDimensions.length; z++) {
            System.out.println("\nz=" + z);
            for (int y = 0; y < this.pocketDimensions[z].length; y++) {
                System.out.println(String.valueOf(this.pocketDimensions[z][y]));
            }
        }
    }

    private void cycle() {
        char[][][] newDimensions = this.expandDimensions();
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
        int expectedCubes = 117;
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
