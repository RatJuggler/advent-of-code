package day17;


class EnergySource {

    EnergySource() {}

    static EnergySource fromFile(final String filename) {
        return new EnergySource();
    }

    int cycles(final int runFor) {
        return 0;
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
    }
}
