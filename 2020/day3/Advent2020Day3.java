package day3;


class MapOfTrees {

    MapOfTrees() {}

    static MapOfTrees fromFile(final String filename) {
        return new MapOfTrees();
    }

    int countTrees(final int slopeX, final int slopeY) {
        return 0;
    }
}


public class Advent2020Day3 {

    static void testPart1TreeCount() {
        int expectedTreeCount = 7;
        MapOfTrees mapOfTrees = MapOfTrees.fromFile("2020/day3/test3a.txt");
        int actualTreeCount = mapOfTrees.countTrees(3, 1);
        assert actualTreeCount == expectedTreeCount :
                String.format("Expected to encounter %d trees not %d!%n", expectedTreeCount, actualTreeCount);
    }

    public static void main(final String[] args) {
        testPart1TreeCount();
    }
}
