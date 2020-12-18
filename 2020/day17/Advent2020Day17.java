package day17;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


class EnergySource {

    private static final int CYCLES = 6;
    private static final int DIMENSION_SIZE = 29;
    private static final int CENTER_POINT = DIMENSION_SIZE / 2;

    private int dimensions;
    private char[] pocketDimensions;
    private int[][] dOffsets;

//    {-1, -1, -1}, {-1, -1,  0}, {-1, -1, 1}
//    {-1,  0, -1}, {-1,  0,  0}, {-1,  0, 1}
//    {-1,  1, -1}, {-1,  1,  0}, {-1,  1, 1}
//    { 0, -1, -1}, { 0, -1,  0}, { 0, -1, 1}
//    { 0,  0, -1},               { 0,  0, 1}
//    { 0,  1, -1}, { 0,  1,  0}, { 0,  1, 1}
//    { 1, -1, -1}, { 1, -1,  0}, { 1, -1, 1}
//    { 1,  0, -1}, { 1,  0,  0}, { 1,  0, 1}
//    { 1,  1, -1}, { 1,  1,  0}, { 1,  1, 1}

    EnergySource(final int dimensions, final List<String> initialState) {
        this.dimensions = dimensions;
        this.pocketDimensions = new char[BigInteger.valueOf(DIMENSION_SIZE).pow(dimensions).intValue()];
        this.dOffsets = new int[BigInteger.valueOf(3).pow(dimensions).intValue() - 1][dimensions];
        Arrays.fill(this.pocketDimensions, '.');
        for (int y = 0; y < initialState.size(); y++)
            for (int x = 0; x < initialState.get(y).length(); x++) {
                int offset = 0;
                // w, z
                for (int p = dimensions - 1; p > 1; p--)
                    offset += CENTER_POINT * BigInteger.valueOf(DIMENSION_SIZE).pow(p).intValue();
                // y
                offset += DIMENSION_SIZE * (CENTER_POINT - initialState.size() + y);
                // x
                offset += CENTER_POINT - initialState.get(y).length() + x;
                this.pocketDimensions[offset] = initialState.get(y).charAt(x);
            }
    }

    static EnergySource fromFile(final String filename, final int dimensions) {
        List<String> initialState;
        try {
            initialState = Files.readAllLines(Paths.get(filename));
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Unable to read initial state file!", ioe);
        }
        return new EnergySource(dimensions, initialState);
    }

    private char[] copyDimensions() {
        char[] newDimensions = new char[BigInteger.valueOf(DIMENSION_SIZE).pow(this.dimensions).intValue()];
        System.arraycopy(this.pocketDimensions, 0, newDimensions, 0, this.pocketDimensions.length);
        return newDimensions;
    }

    private int calculateOffset(final int i, final int[] dOffset) {
        int offset = 0;
        offset += i + dOffset[this.dimensions];
        return offset;
    }

    private char newStatus(final int i) {
        int activeNeighbours = 0;
        for (int[] dOffset : this.dOffsets)
            activeNeighbours += this.pocketDimensions[this.calculateOffset(i, dOffset)] == '#' ? 1 : 0;
        char current = this.pocketDimensions[i];
        if (current == '.')
            return activeNeighbours == 3 ? '#' : '.';
        else if (current == '#')
            return activeNeighbours == 2 || activeNeighbours == 3 ? '#' : '.';
        else
            throw new IllegalStateException("Unknown state encountered!");
    }

    private void cycle() {
        char[] newDimensions = copyDimensions();
        for (int i = 0; i < this.pocketDimensions.length; i++)
             newDimensions[i] = this.newStatus(i);
        this.pocketDimensions = newDimensions;
    }

    private int countCubes() {
        int cubeCount = 0;
        for (char position : this.pocketDimensions)
            if (position == '#') cubeCount++;
        return cubeCount;
    }

    int cycles() {
        for (int i = 0; i < CYCLES; i++)
            cycle();
        return this.countCubes();
    }
}


public class Advent2020Day17 {

    private static int runEnergySource(final String filename, final int dimensions) {
        EnergySource source = EnergySource.fromFile(filename, dimensions);
        return source.cycles();
    }

    private static void testEnergySource(final int dimensions, final int expectedCubes) {
        int actualCubes = runEnergySource("2020/day17/test17a.txt", dimensions);
        assert actualCubes == expectedCubes : String.format("Expected number of cubes to be %d not %d!%n", expectedCubes, actualCubes);
    }

    public static void main(final String[] args) {
        testEnergySource(3, 112);
        System.out.printf("Day 17, Part 1 number of cubes after 6 cycles is %d.%n",
                runEnergySource("2020/day17/input17.txt", 3));
        testEnergySource(4, 848);
        System.out.printf("Day 17, Part 2 number of cubes after 6 cycles is %d.%n",
                runEnergySource("2020/day17/input17.txt", 4));
    }
}
