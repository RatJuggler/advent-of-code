package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

final class Position {

    private boolean visited_twice;
    // Always start at origin facing North.
    private int x = 0;
    private int y = 0;
    private char heading = 'N';

    Position(final boolean visited_twice) {
        this.visited_twice = visited_twice;
    }

    void move(final char turn, final int distance) {
        assert turn == 'R' || turn == 'L': String.format("Cannot turn %s!", turn);
        switch (this.heading) {
            case 'N':
                if (turn == 'R') {
                    this.heading = 'E';
                    this.x += distance;
                } else {
                    this.heading = 'W';
                    this.x -= distance;
                }
                break;
            case 'E':
                if (turn == 'R') {
                    this.heading = 'S';
                    this.y -= distance;
                } else {
                    this.heading = 'N';
                    this.y += distance;
                }
                break;
            case 'S':
                if (turn == 'R') {
                    this.heading = 'W';
                    this.x -= distance;
                } else {
                    this.heading = 'E';
                    this.x += distance;
                }
                break;
            case 'W':
                if (turn == 'R') {
                    this.heading = 'N';
                    this.y += distance;
                } else {
                    this.heading = 'S';
                    this.y -= distance;
                }
                break;
            default:
                throw new IllegalStateException(String.format("Impossible heading found %s!", this.heading));
        }
    }

    int getDistance() {
        return Math.abs(this.x) + Math.abs(this.y);
    }

    @Override
    public String toString() {
        return String.format("Position(x: %d, y: %d, heading: %s)", this.x, this.y, this.heading);
    }
}

final class Advent1 {

    private static String readInputFile() throws IOException {
        Path filePath = Paths.get("2016/day1/input1.txt");
        return Files.readString(filePath);
    }

    private static int blocks_away(final String directions, final boolean visited_twice) {
        Position position = new Position(visited_twice);
        StringTokenizer tokenizer = new StringTokenizer(directions, ", ");
        while (tokenizer.hasMoreElements()) {
            String direction = tokenizer.nextToken();
            char turn = direction.charAt(0);
            int distance = Integer.parseInt(direction.substring(1));
            position.move(turn, distance);
            System.out.println(String.format("Turn: %s, Distance: %d -> %s", turn, distance, position));
        }
        return position.getDistance();
    }

    private static void test_blocks_away(final String directions, final int expected_distance, final boolean visited_twice) {
        int distance = blocks_away(directions, visited_twice);
        assert distance == expected_distance:
                String.format("Expected a distance of %d but was %d!", expected_distance, distance);
    }

    private static void test_blocks_away(final String directions, final int expected_distance) {
        test_blocks_away(directions, expected_distance, false);
    }

    public static void main(final String[] args) throws IOException {
        test_blocks_away("R2, L3", 5);
        test_blocks_away("R2, R2, R2", 2);
        test_blocks_away("R5, L5, R5, R3", 12);
        String directions = readInputFile();
        int distance = blocks_away(directions, false);
        System.out.println(String.format("Day 1, Part 1 the Easter Bunny HQ is %d blocks away.", distance));
        test_blocks_away("R8, R4, R4, R8", 4, true);
        distance = blocks_away(directions, true);
        System.out.println(String.format("Day 1, Part 2 the Easter Bunny HQ is %d blocks away.", distance));
    }

}
