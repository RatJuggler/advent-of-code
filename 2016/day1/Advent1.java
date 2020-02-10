package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

final class Position {

    // Always start at origin facing North.
    private int x = 0;
    private int y = 0;
    private char heading = 'N';

    Position() {}

    void move(final char turn, final int distance) {
        switch (heading) {
            case 'N':
                if (turn == 'R') {
                    heading = 'E';
                    x += distance;
                } else {
                    heading = 'W';
                    x -= distance;
                }
                break;
            case 'E':
                if (turn == 'R') {
                    heading = 'S';
                    y -= distance;
                } else {
                    heading = 'N';
                    y += distance;
                }
                break;
            case 'S':
                if (turn == 'R') {
                    heading = 'W';
                    x -= distance;
                } else {
                    heading = 'E';
                    x += distance;
                }
                break;
            case 'W':
                if (turn == 'R') {
                    heading = 'N';
                    y += distance;
                } else {
                    heading = 'S';
                    y -= distance;
                }
                break;
            default:
                throw new IllegalStateException(String.format("Impossible heading found %s!", heading));
        }
    }

    int getDistance() {
        return Math.abs(this.x) + Math.abs(this.y);
    }

    @Override
    public String toString() {
        return "Position(x:" + x + ", y:" + y + ", heading:" + heading + ')';
    }
}

final class Advent1 {

    private static String readInputFile() throws IOException {
        Path filePath = Paths.get("2016/day1/input1.txt");
        return Files.readString(filePath);
    }

    private static int blocks_away(final String directions) {
        Position position = new Position();
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

    private static void test_blocks_away(final String directions, final int expected_distance) {
        int distance = blocks_away(directions);
        assert distance == expected_distance:
                String.format("Expected a distance of %d but was %d!", expected_distance, distance);
    }

    public static void main(final String[] args) throws IOException {
        test_blocks_away("R2, L3", 5);
        test_blocks_away("R2, R2, R2", 2);
        test_blocks_away("R5, L5, R5, R3", 12);
        String directions = readInputFile();
        int distance = blocks_away(directions);
        System.out.println(String.format("Day 1, Part 1 the Easter Bunny HQ is %d blocks away.", distance));
    }

}
