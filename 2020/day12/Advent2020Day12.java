package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


abstract class Position {

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

    abstract void rotate(final int angle);

    abstract void forward(final int value);

    void action(final String instruction) {
        char action = instruction.charAt(0);
        int value = Integer.parseInt(instruction.substring(1));
        if ("NSEW".indexOf(action) >= 0)
            this.move(action, value);
        else if (action == 'L')
            this.rotate(360 - value);
        else if (action == 'R')
            this.rotate(value);
        else if (action == 'F')
            this.forward(value);
        else
            throw new IllegalArgumentException("Unknown action: " + action);
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

    void forward(final int value) {
        this.move(translateAngle(), value);
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

    int manhattan() {
        return Math.abs(this.nsPosition) + Math.abs(this.ewPosition);
    }
}


class Waypoint extends Position {

    private final ShipPosition shipPosition;

    Waypoint(final ShipPosition shipPosition) {
        super(1, 10);
        this.shipPosition = shipPosition;
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

    void forward(final int value) {
        this.shipPosition.move(this.nsPosition > 0 ? 'N' : 'S', value * Math.abs(this.nsPosition));
        this.shipPosition.move(this.ewPosition > 0 ? 'E' : 'W', value * Math.abs(this.ewPosition));
    }
}


class Ship {

    private final ShipPosition shipPosition = new ShipPosition();
    private final Waypoint waypoint = new Waypoint(this.shipPosition);

    Ship() {}

    void moveDirectly(final String instruction) {
        this.shipPosition.action(instruction);
    }

    void moveViaWaypoint(final String instruction) {
        this.waypoint.action(instruction);
    }

    int manhattan() {
        return this.shipPosition.manhattan();
    }
}


public class Advent2020Day12 {

    private static int shipMovement(final String filename) {
        Ship ship = new Ship();
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(ship::moveDirectly);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Problem reading navigation file: " + filename, ioe);
        }
        return ship.manhattan();
    }

    private static int waypointMovement(final String filename) {
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
        int actualManhattan = shipMovement("2020/day12/test12a.txt");
        assert actualManhattan == expectedManhattan :
                String.format("Expected Manhattan distance to be %d not %d!%n", expectedManhattan, actualManhattan);
    }

    private static void testWaypointMovement() {
        int expectedManhattan = 286;
        int actualManhattan = waypointMovement("2020/day12/test12a.txt");
        assert actualManhattan == expectedManhattan :
                String.format("Expected Manhattan distance to be %d not %d!%n", expectedManhattan, actualManhattan);
    }

    public static void main(final String[] args) {
        testShipMovement();
        System.out.printf("Day 12, Part 1 Manhattan distance is %d.%n", shipMovement("2020/day12/input12.txt"));
        testWaypointMovement();
        System.out.printf("Day 12, Part 2 Manhattan distance is %d.%n", waypointMovement("2020/day12/input12.txt"));
    }
}
