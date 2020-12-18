package day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


class CubeLocation {

    final int z;
    final int y;
    final int x;

    CubeLocation(final int z, final int y, final int x) {
        this.z = z;
        this.y = y;
        this.x = x;
    }

    CubeLocation offsetCube(final int dZ, final int dY, final int dX) {
        return new CubeLocation(this.z + dZ, this.y + dY, this.x + dX);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CubeLocation that = (CubeLocation) o;
        return z == that.z && y == that.y && x == that.x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(z, y, x);
    }
}


class EnergySource {

    private static final int CYCLES = 6;

    private int dimensions;
    private List<CubeLocation> cubes = new ArrayList<>();

    EnergySource(final int dimensions, final List<String> initialState) {
        this.dimensions = dimensions;
        for (int y = 0; y < initialState.size(); y++)
            for (int x = 0; x < initialState.get(y).length(); x++)
                if (initialState.get(y).length() == '#')
                    this.cubes.add(new CubeLocation(0, y, x));
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

    private int countActiveNeighbours(final CubeLocation cube) {
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
        for (int[] dOffset : dOffsets) {
            CubeLocation offsetCube = cube.offsetCube(dOffset[0], dOffset[1], dOffset[1]);
            activeNeighbours += this.cubes.contains(offsetCube) ? 1 : 0;
        }
        return activeNeighbours;
    }

    private List<CubeLocation> cubeChanges(final CubeLocation cube) {
        List<CubeLocation> changes = new ArrayList<>();
        int activeNeighbours = this.countActiveNeighbours(cube);
        if (activeNeighbours == 2 || activeNeighbours == 3)
            changes.add(cube);
        return changes;
    }

    private void cycle() {
        List<CubeLocation> newCubes = new ArrayList<>();
        for (CubeLocation cube: this.cubes)
             newCubes.addAll(this.cubeChanges(cube));
        this.cubes = newCubes;
    }

    int cycles() {
        for (int i = 0; i < CYCLES; i++)
            cycle();
        return this.cubes.size();
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
