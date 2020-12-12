package day12;


class Ship {

    private int nsPosition = 0;
    private int ewPosition = 0;
    private int direction = 0;

    Ship() {}

    void move(final int instruction) {

    }

    int manhattan() {
        return this.nsPosition + this.ewPosition;
    }
}


public class Advent2020Day12 {

    private static int followNavigationInstructions(final String filename) {
        return 0;
    }

    private static void testShipMovement() {
        int expectedManhattan = 25;
        int actualManhattan = followNavigationInstructions("2020/day12/test12a.txt");
        assert actualManhattan == expectedManhattan :
                String.format("Expected Manhattan distance to be %d not %d!%n", expectedManhattan, actualManhattan);
    }

    public static void main(final String[] args) {
        testShipMovement();
    }
}
