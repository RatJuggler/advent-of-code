package day3;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class MapOfTrees {

    private final List<String> map;
    private final int width;

    MapOfTrees(final List<String> map) {
        this.map = map;
        this.width = map.get(0).length();
    }

    static MapOfTrees fromFile(final String filename) throws IOException {
        List<String> map = Files.readAllLines(Paths.get(filename));
        return new MapOfTrees(map);
    }

    int countTrees(final int slopeX, final int slopeY) {
        int treeCount = 0;
        int mapX = 0;
        int mapY = 0;
        while (mapY < this.map.size()) {
            char mapLocation = this.map.get(mapY).charAt(mapX);
            if (mapLocation == '#') treeCount++;
            mapX = (mapX + slopeX) % this.width;
            mapY += slopeY;
        }
        return treeCount;
    }
}


public class Advent2020Day3 {

    private static int countTrees(final String filename, final int slopeX, final int slopeY) throws IOException {
        MapOfTrees mapOfTrees = MapOfTrees.fromFile(filename);
        return mapOfTrees.countTrees(slopeX, slopeY);
    }

    private static void testPart1TreeCount() throws IOException {
        int expectedTreeCount = 7;
        int actualTreeCount = countTrees("2020/day3/test3a.txt", 3, 1);
        assert actualTreeCount == expectedTreeCount :
                String.format("Expected to encounter %d trees not %d!%n", expectedTreeCount, actualTreeCount);
    }

    public static void main(final String[] args) throws IOException {
        testPart1TreeCount();
        System.out.printf("Day 1, part 1, number of trees is %d.%n", countTrees("2020/day3/input3.txt", 3, 1));
    }
}
