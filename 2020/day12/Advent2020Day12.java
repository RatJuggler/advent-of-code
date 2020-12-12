package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


class Position {

    int nsPosition;
    int ewPosition;

    Position(final int nsPosition, final int ewPosition) {
        this.nsPosition = nsPosition;
        this.ewPosition = ewPosition;
    }

    void move(final char direction, final int value) {
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
}


class ShipPosition extends Position {

    private int angle = 90;

    ShipPosition() {
        super(0, 0);
    }

    void rotate(final int angle) {
        this.angle = (this.angle + angle) % 360;
    }

    private char translateAngle() {
        switch (this.angle) {
            case 0: return 'N';
            case 90: return 'E';
            case 180: return 'S';
            case 270: return 'W';
            default: throw new IllegalArgumentException("Unexpected angle: " + this.angle);
        }
    }

    void moveForward(final int value) {
        this.move(translateAngle(), value);
    }

    void moveToWaypoint(final Waypoint waypoint, final int value) {
        this.move(waypoint.nsDirection(), value * Math.abs(waypoint.nsPosition));
        this.move(waypoint.ewDirection(), value * Math.abs(waypoint.ewPosition));
    }

    int manhattan() {
        return Math.abs(this.nsPosition) + Math.abs(this.ewPosition);
    }
}


class Waypoint extends Position {

    Waypoint() {
        super(1, 10);
    }

    void rotate(final int angle) {
        int tempEW = this.ewPosition;
        switch (angle) {
            case 90:
                this.ewPosition = this.nsPosition;
                this.nsPosition = -tempEW;
                break;
            case 180:
                this.ewPosition = -tempEW;
                this.nsPosition = -this.nsPosition;
                break;
            case 270:
                this.ewPosition = -this.nsPosition;
                this.nsPosition = tempEW;
                break;
            default: throw new IllegalArgumentException("Unexpected angle: " + angle);
        }
    }

    char nsDirection() {
        return this.nsPosition > 0 ? 'N' : 'S';
    }

    char ewDirection() {
        return this.ewPosition > 0 ? 'E' : 'W';
    }
}


class Ship {

    private final ShipPosition shipPosition = new ShipPosition();
    private final Waypoint waypoint = new Waypoint();

    Ship() {}

    void moveShipDirectly(final String instruction) {
        char action = instruction.charAt(0);
        int value = Integer.parseInt(instruction.substring(1));
        if ("NSEW".indexOf(action) >= 0)
            this.shipPosition.move(action, value);
        else if (action == 'L')
            this.shipPosition.rotate(360 - value);
        else if (action == 'R')
            this.shipPosition.rotate(value);
        else if (action == 'F')
            this.shipPosition.moveForward(value);
        else
            throw new IllegalArgumentException("Unknown action: " + action);
    }

    void moveViaWaypoint(final String instruction) {
        char action = instruction.charAt(0);
        int value = Integer.parseInt(instruction.substring(1));
        if ("NSEW".indexOf(action) >= 0)
            this.waypoint.move(action, value);
        else if (action == 'L')
            this.waypoint.rotate(360 - value);
        else if (action == 'R')
            this.waypoint.rotate(value);
        else if (action == 'F')
            this.shipPosition.moveToWaypoint(this.waypoint, value);
        else
            throw new IllegalArgumentException("Unknown action: " + action);
    }

    int manhattan() {
        return this.shipPosition.manhattan();
    }
}


public class Advent2020Day12 {

    private static int followNavigationInstructionsWithShip(final String filename) {
        Ship ship = new Ship();
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(ship::moveShipDirectly);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Problem reading navigation file: " + filename, ioe);
        }
        return ship.manhattan();
    }

    private static int followNavigationInstructionsWithWaypoint(final String filename) {
        Ship ship = new Ship();
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(ship::moveViaWaypoint);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Problem reading navigation file: " + filename, ioe);
        }
        return ship.manhattan();
    }

    private static void testShipMovement() {
        int expectedManhattan = 25;
        int actualManhattan = followNavigationInstructionsWithShip("2020/day12/test12a.txt");
        assert actualManhattan == expectedManhattan :
                String.format("Expected Manhattan distance to be %d not %d!%n", expectedManhattan, actualManhattan);
    }

    private static void testWaypointMovement() {
        int expectedManhattan = 286;
        int actualManhattan = followNavigationInstructionsWithWaypoint("2020/day12/test12a.txt");
        assert actualManhattan == expectedManhattan :
                String.format("Expected Manhattan distance to be %d not %d!%n", expectedManhattan, actualManhattan);
    }

    public static void main(final String[] args) {
        testShipMovement();
        System.out.printf("Day 12, Part 1 Manhattan distance is %d.%n", followNavigationInstructionsWithShip("2020/day12/input12.txt"));
        testWaypointMovement();
        System.out.printf("Day 12, Part 2 Manhattan distance is %d.%n", followNavigationInstructionsWithWaypoint("2020/day12/input12.txt"));
    }
}
