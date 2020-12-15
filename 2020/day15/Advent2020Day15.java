package day15;

public class Advent2020Day15 {

    private static int numberGame(final String starting) {
        return 0;
    }

    private static void testNumberGame(final String starting, final int expected) {
        int actual = numberGame(starting);
        assert actual == expected : String.format("Expected number spoken to be %d not %d!%n", expected, actual);
    }

    public static void main(final String[] args) {
        testNumberGame("0 3 6", 436);
        testNumberGame("1 3 2", 1);
        testNumberGame("2 1 3", 10);
        testNumberGame("1 2 3", 27);
        testNumberGame("2 3 1", 78);
        testNumberGame("3 2 1", 438);
        testNumberGame("3 1 2", 1836);
    }
}
