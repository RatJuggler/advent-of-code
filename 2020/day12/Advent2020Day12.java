package day12;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class Ship {

    private int nsPosition = 0;
    private int ewPosition = 0;
    private int direction = 90;

    Ship() {}

    private void moveInDirection(final char direction, final int value) {
        switch (direction) {
            case 'N':
                this.nsPosition += value;
                break;
            case 'S':
                this.nsPosition -= value;
                break;
            case 'E':
                this.ewPosition += value;
                break;
            case 'W':
                this.ewPosition -= value;
                break;
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }

    private char translateDirection() {
        switch (this.direction) {
            case 0: return 'N';
            case 90:
            case -270:
                return 'E';
            case 180:
            case -180:
                return 'S';
            case 270:
            case -90:
                return 'W';
            default:
                throw new IllegalArgumentException("Unknown direction: " + this.direction);
        }
    }

    private void performAction(final char action, final int value) {
        if ("NSEW".indexOf(action) >= 0)
            this.moveInDirection(action, value);
        else if (action == 'L')
            this.direction = (this.direction - value) % 360;
        else if (action == 'R')
            this.direction = (this.direction + value) % 360;
        else if (action == 'F')
            this.moveInDirection(translateDirection(), value);
        else
            throw new IllegalArgumentException("Unknown action: " + action);
    }

    void move(final String instruction) {
        char action = instruction.charAt(0);
        int value = Integer.parseInt(instruction.substring(1));
        this.performAction(action, value);
    }

    int manhattan() {
        return Math.abs(this.nsPosition) + Math.abs(this.ewPosition);
    }
}


public class Advent2020Day12 {

    private static int followNavigationInstructions(final String filename) {
        Ship ship = new Ship();
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(ship::move);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Problem reading navigation file: " + filename);
        }
        return ship.manhattan();
    }

    private static void testShipMovement() {
        int expectedManhattan = 25;
        int actualManhattan = followNavigationInstructions("2020/day12/test12a.txt");
        assert actualManhattan == expectedManhattan :
                String.format("Expected Manhattan distance to be %d not %d!%n", expectedManhattan, actualManhattan);
    }

    public static void main(final String[] args) {
        testShipMovement();
        System.out.printf("Day 12, Part 1 Manhattan distance is %d.%n", followNavigationInstructions("2020/day12/input12.txt"));
    }
}
