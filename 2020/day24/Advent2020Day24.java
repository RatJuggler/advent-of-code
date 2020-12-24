package day24;


public class Advent2020Day24 {

    private static int followInstructions(final String filename) {
        return 0;
    }

    private static void testFlipTiles() {
        int expectedBlackTiles = 10;
        int actualBlackTiles = followInstructions("2020/day24/test24a.txt");
        assert expectedBlackTiles == actualBlackTiles :
                String.format("Expected the final number of black tiles to be %d not %d!%n", expectedBlackTiles, actualBlackTiles);
    }

    public static void main(final String[] args) {
        testFlipTiles();
        System.out.printf("Day 24, part 1, final number of black tiles is %d.%n", followInstructions("2020/day24/input24.txt"));
    }
}
