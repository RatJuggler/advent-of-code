package day14;


class DockingEmulator {

    private final long[] memory = new long[65536];

    DockingEmulator() {}

    static DockingEmulator fromFile(final String filename) {
        return new DockingEmulator();
    }

    long run() {
        return 0L;
    }
}


public class Advent2020Day14 {

    public static void testDockingEmulator() {
        long expectedSum = 165L;
        DockingEmulator emulator = DockingEmulator.fromFile("2020/day14/test14a.txt");
        long actualSum = emulator.run();
        assert actualSum == expectedSum : String.format("Expected memory sum to be %d not %d!%n", expectedSum, actualSum);
    }

    public static void main(final String[] args) {
        testDockingEmulator();
    }
}
