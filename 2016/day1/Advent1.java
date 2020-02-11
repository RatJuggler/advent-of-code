package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;


final class Location {

    final int x;
    final int y;

    Location(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    int absDistance() {
        return Math.abs(this.x) + Math.abs(this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return this.x == location.x && this.y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public String toString() {
        return "{x: " + this.x + ", y: " + this.y + '}';
    }
}


final class Position {

    private Location location;
    private char heading;
    private Map<Location, Integer> history = new LinkedHashMap<>();

    Position() {
        // Always start at origin facing North.
        newLocation(0, 0);
        this.heading = 'N';
    }

    private void newLocation(final int x, final int y) {
        this.location = new Location(x, y);
        this.history.merge(this.location, 1, Integer::sum);
    }

    private void moveX(final char newHeading, final int distance) {
        assert newHeading == 'E' || newHeading == 'W': String.format("Cannot move %s!", newHeading);
        this.heading = newHeading;
        for (int i = 0; i < distance; i++) {
            newLocation(this.location.x + (newHeading == 'E' ? 1 : -1), this.location.y);
        }
    }

    private void moveY(final char newHeading, final int distance) {
        assert newHeading == 'N' || newHeading == 'S': String.format("Cannot move %s!", newHeading);
        this.heading = newHeading;
        for (int i = 0; i < distance; i++) {
            newLocation(this.location.x, this.location.y + (newHeading == 'N' ? 1 : -1));
        }
    }

    private void move(final char turn, final int distance) {
        assert turn == 'R' || turn == 'L': String.format("Cannot turn %s!", turn);
        switch (this.heading) {
            case 'N':
                moveX(turn == 'R' ? 'E' : 'W', distance);
                break;
            case 'E':
                moveY(turn == 'R' ? 'S' : 'N', distance);
                break;
            case 'S':
                moveX(turn == 'R' ? 'W' : 'E', distance);
                break;
            case 'W':
                moveY(turn == 'R' ? 'N' : 'S', distance);
                break;
            default:
                throw new IllegalStateException(String.format("Impossible heading found %s!", this.heading));
        }
    }

    void update(final String direction) {
        char turn = direction.charAt(0);
        int distance = Integer.parseInt(direction.substring(1));
        this.move(turn, distance);
        System.out.println(String.format("Turn: %s, Distance: %d -> %s", turn, distance, this));
    }

    int getDistance(final Function<Map.Entry<Location, Integer>, Boolean> filter,
                    final BiFunction<Map.Entry<Location, Integer>, Map.Entry<Location, Integer>, Map.Entry<Location, Integer>> select) {
        Location visited = history.entrySet()
                .stream()
                .filter(filter::apply)
                .reduce(select::apply)
                .map(Map.Entry::getKey)
                .get();
        return visited.absDistance();
    }

    @Override
    public String toString() {
        return String.format("Position(location: %s, heading: %s)", this.location, this.heading);
    }
}


final class Advent1 {

    private static String readInputFile() throws IOException {
        Path filePath = Paths.get("2016/day1/input1.txt");
        return Files.readString(filePath);
    }

    private static int blocksAway(final String directions,
                                  final Function<Map.Entry<Location, Integer>, Boolean> filter,
                                  final BiFunction<Map.Entry<Location, Integer>, Map.Entry<Location, Integer>, Map.Entry<Location, Integer>> select) {
        Position position = new Position();
        for (String direction: directions.split(", ")) {
            position.update(direction);
        }
        return position.getDistance(filter, select);
    }

    private static void testBlocksAway(final String directions, final int expectedDistance,
                                       final Function<Map.Entry<Location, Integer>, Boolean> filter,
                                       final BiFunction<Map.Entry<Location, Integer>, Map.Entry<Location, Integer>, Map.Entry<Location, Integer>> select) {
        int distance = blocksAway(directions, filter, select);
        assert distance == expectedDistance:
                String.format("Expected a distance of %d but was %d!", expectedDistance, distance);
    }

    public static void main(final String[] args) throws IOException {
        Function<Map.Entry<Location, Integer>, Boolean> filter1 = entry -> entry.getValue() > 0;
        BiFunction<Map.Entry<Location, Integer>, Map.Entry<Location, Integer>, Map.Entry<Location, Integer>> select1 =
                (firstEntry, secondEntry) -> secondEntry;
        testBlocksAway("R2, L3", 5, filter1, select1);
        testBlocksAway("R2, R2, R2", 2, filter1, select1);
        testBlocksAway("R5, L5, R5, R3", 12, filter1, select1);
        String directions = readInputFile();
        int distance1 = blocksAway(directions, filter1, select1);
        System.out.println(String.format("Day 1, Part 1 the Easter Bunny HQ is %d blocks away.", distance1));
        Function<Map.Entry<Location, Integer>, Boolean> filter2 = entry -> entry.getValue() > 1;
        BiFunction<Map.Entry<Location, Integer>, Map.Entry<Location, Integer>, Map.Entry<Location, Integer>> select2 =
                (firstEntry, secondEntry) -> firstEntry;
        testBlocksAway("R8, R4, R4, R8", 4, filter2, select2);
        int distance2 = blocksAway(directions, filter2, select2);
        System.out.println(String.format("Day 1, Part 2 the Easter Bunny HQ is %d blocks away.", distance2));
    }

}
