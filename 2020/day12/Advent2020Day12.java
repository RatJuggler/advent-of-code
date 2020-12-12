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

    void movePosition(final char direction, final int value) {
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

    char nsDirection() {
        return this.nsPosition > 0 ? 'N' : 'S';
    }

    char ewDirection() {
        return this.ewPosition > 0 ? 'E' : 'W';
    }

    int manhattan() {
        return Math.abs(this.nsPosition) + Math.abs(this.ewPosition);
    }
}


class ShipPosition extends Position {

    private int angle = 90;

    ShipPosition(int nsPosition, int ewPosition) {
        super(nsPosition, ewPosition);
    }

    void rotateShip(final int angle) {
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
        this.movePosition(translateAngle(), value);
    }
}


class Ship {

    private final ShipPosition shipPosition = new ShipPosition(0, 0);
    private final Position waypointPosition = new Position(1, 10);

    Ship() {}

    void moveShipDirectly(final String instruction) {
        char action = instruction.charAt(0);
        int value = Integer.parseInt(instruction.substring(1));
        if ("NSEW".indexOf(action) >= 0)
            this.shipPosition.movePosition(action, value);
        else if (action == 'L')
            this.shipPosition.rotateShip(360 - value);
        else if (action == 'R')
            this.shipPosition.rotateShip(value);
        else if (action == 'F')
            this.shipPosition.moveForward(value);
        else
            throw new IllegalArgumentException("Unknown action: " + action);
    }

    private void rotateWaypoint(final int angle) {
        int tempEW = this.waypointPosition.ewPosition;
        switch (angle) {
            case 90:
                this.waypointPosition.ewPosition = this.waypointPosition.nsPosition;
                this.waypointPosition.nsPosition = -tempEW;
                break;
            case 180:
                this.waypointPosition.ewPosition = -tempEW;
                this.waypointPosition.nsPosition = -this.waypointPosition.nsPosition;
                break;
            case 270:
                this.waypointPosition.ewPosition = -this.waypointPosition.nsPosition;
                this.waypointPosition.nsPosition = tempEW;
                break;
            default: throw new IllegalArgumentException("Unexpected angle: " + angle);
        }
    }

    private void moveShipToWaypoint(final int value) {
        this.shipPosition.movePosition(this.waypointPosition.nsDirection(), value * Math.abs(this.waypointPosition.nsPosition));
        this.shipPosition.movePosition(this.waypointPosition.ewDirection(), value * Math.abs(this.waypointPosition.ewPosition));
    }

    void moveViaWaypoint(final String instruction) {
        char action = instruction.charAt(0);
        int value = Integer.parseInt(instruction.substring(1));
        if ("NSEW".indexOf(action) >= 0)
            this.waypointPosition.movePosition(action, value);
        else if (action == 'L')
            this.rotateWaypoint(360 - value);
        else if (action == 'R')
            this.rotateWaypoint(value);
        else if (action == 'F')
            this.moveShipToWaypoint(value);
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
