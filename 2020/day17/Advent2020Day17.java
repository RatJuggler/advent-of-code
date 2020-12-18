package day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


class CubeLocation {

    final int[] location;

    CubeLocation(final int[] location) {
        this.location = location;
    }

    CubeLocation offsetCube(final int[] dOffset) {
        int[] newLocation = Arrays.copyOf(this.location, this.location.length);
        for (int i = 0; i < newLocation.length; i++)
            newLocation[i] += dOffset[i];
        return new CubeLocation(newLocation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CubeLocation that = (CubeLocation) o;
        return Arrays.equals(this.location, that.location);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.location);
    }
}


class EnergySource {

    private static final int CYCLES = 6;

    private static final int[][] dOFFSETS =
           {{-1, -1, -1}, {-1, -1,  0}, {-1, -1, 1},
            {-1,  0, -1}, {-1,  0,  0}, {-1,  0, 1},
            {-1,  1, -1}, {-1,  1,  0}, {-1,  1, 1},
            { 0, -1, -1}, { 0, -1,  0}, { 0, -1, 1},
            { 0,  0, -1}, { 0,  0,  0}, { 0,  0, 1},
            { 0,  1, -1}, { 0,  1,  0}, { 0,  1, 1},
            { 1, -1, -1}, { 1, -1,  0}, { 1, -1, 1},
            { 1,  0, -1}, { 1,  0,  0}, { 1,  0, 1},
            { 1,  1, -1}, { 1,  1,  0}, { 1,  1, 1}};

    private Set<CubeLocation> cubes = new HashSet<>();

    private static int[] createLocation(final int dimensions, final int y, final int x) {
        int[] location = new int[dimensions];
        Arrays.fill(location, 0);
        location[dimensions - 1] = x;
        location[dimensions - 2] = y;
        return location;
    }

    EnergySource(final int dimensions, final List<String> initialState) {
        for (int y = 0; y < initialState.size(); y++)
            for (int x = 0; x < initialState.get(y).length(); x++)
                if (initialState.get(y).charAt(x) == '#')
                    this.cubes.add(new CubeLocation(createLocation(dimensions, y, x)));
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
        int activeNeighbours = 0;
        for (int[] dOffset : dOFFSETS) {
            if (Arrays.stream(dOffset).anyMatch(n -> n != 0)) {
                CubeLocation offsetCube = cube.offsetCube(dOffset);
                if (this.cubes.contains(offsetCube)) activeNeighbours++;
            }
        }
        return activeNeighbours;
    }

    private Set<CubeLocation> cubeChanges(final CubeLocation cube) {
        Set<CubeLocation> changes = new HashSet<>();
        for (int[] dOffset : dOFFSETS) {
            CubeLocation checkLocation = cube.offsetCube(dOffset);
            int activeNeighbours = this.countActiveNeighbours(checkLocation);
            if (checkLocation.equals(cube)) {
                if (activeNeighbours == 2 || activeNeighbours == 3)
                    changes.add(checkLocation);
            } else if (activeNeighbours == 3) {
                changes.add(checkLocation);
            }
        }
        return changes;
    }

    private void cycle() {
        Set<CubeLocation> newCubes = new HashSet<>();
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
