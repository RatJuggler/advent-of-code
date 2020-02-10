package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;


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
        return this.x == location.x &&
                this.y == location.y;
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
    private Map<Location, Integer> history = new HashMap<>();
    private Location revisited;

    Position() {
        // Always start at origin facing North.
        newLocation(0, 0);
        this.heading = 'N';
    }

    private void newLocation(final int x, final int y) {
        this.location = new Location(x, y);
        this.history.merge(this.location, 1, Integer::sum);
        if (this.revisited == null && this.history.get(this.location) == 2) {
            this.revisited = this.location;
        }
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

    int getDistance(final boolean visitedTwice) {
        if (visitedTwice) {
            return this.revisited.absDistance();
        } else {
            return this.location.absDistance();
        }
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

    private static int blocksAway(final String directions, final boolean visitedTwice) {
        Position position = new Position();
        StringTokenizer tokenizer = new StringTokenizer(directions, ", ");
        tokenizer.asIterator().forEachRemaining(token -> position.update((String) token));
        return position.getDistance(visitedTwice);
    }

    private static void testBlocksAway(final String directions, final int expectedDistance, final boolean visitedTwice) {
        int distance = blocksAway(directions, visitedTwice);
        assert distance == expectedDistance:
                String.format("Expected a distance of %d but was %d!", expectedDistance, distance);
    }

    private static void testBlocksAway(final String directions, final int expectedDistance) {
        testBlocksAway(directions, expectedDistance, false);
    }

    public static void main(final String[] args) throws IOException {
        testBlocksAway("R2, L3", 5);
        testBlocksAway("R2, R2, R2", 2);
        testBlocksAway("R5, L5, R5, R3", 12);
        String directions = readInputFile();
        int distance1 = blocksAway(directions, false);
        System.out.println(String.format("Day 1, Part 1 the Easter Bunny HQ is %d blocks away.", distance1));
        testBlocksAway("R8, R4, R4, R8", 4, true);
        int distance2 = blocksAway(directions, true);
        System.out.println(String.format("Day 1, Part 2 the Easter Bunny HQ is %d blocks away.", distance2));
    }

}
