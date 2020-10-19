package day17;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


class Position {

    final int x;
    final int y;
    final String path;

    Position(final int x, final int y, final String path) {
        this.x = x;
        this.y = y;
        this.path = path;
    }

    Position up() {
        return new Position(this.x, this.y - 1, this.path + "U");
    }

    Position down() {
        return new Position(this.x, this.y + 1, this.path + "D");
    }

    Position left() {
        return new Position(this.x - 1, this.y, this.path + "L");
    }

    Position right() {
        return new Position(this.x + 1, this.y, this.path + "R");
    }
}


class Vault {

    private final int sizeX;
    private final int sizeY;
    private final String passcode;

    Vault(final String passcode, final int sizeX, final int sizeY) {
        this.passcode = passcode;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    private static String md5(final String input) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        byte[] digest = md5.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1));
        }
        return sb.toString();
    }

    private boolean isOpen(final char door) {
        return "bcdef".indexOf(door) >= 0;
    }

    private List<Position> generateMoves(final Position currentPosition) {
        List<Position> nextPositions = new ArrayList<>();
        String doors = md5(this.passcode + currentPosition.path).substring(0, 4);
        // Up
        if (this.isOpen(doors.charAt(0)) && currentPosition.y > 0) {
            nextPositions.add(currentPosition.up());
        }
        // Down
        if (this.isOpen(doors.charAt(1)) && currentPosition.y < this.sizeY) {
            nextPositions.add(currentPosition.down());
        }
        // Left
        if (this.isOpen(doors.charAt(2)) && currentPosition.x > 0) {
            nextPositions.add(currentPosition.left());
        }
        // Right
        if (this.isOpen(doors.charAt(3)) && currentPosition.x < this.sizeY) {
            nextPositions.add(currentPosition.right());
        }
        return nextPositions;
    }

    String navigate() {
        Queue<Position> nextPositions = new ArrayDeque<>();
        int shortestPath = Integer.MAX_VALUE;
        String path = null;
        nextPositions.add(new Position(0, 0, ""));
        while (nextPositions.peek() != null) {
            Position position = nextPositions.poll();
            if (position.path.length() < shortestPath) {
                if (position.x == this.sizeX && position.y == this.sizeY) {
                    path = position.path;
                    shortestPath = path.length();
                    System.out.println("Queue: " + nextPositions.size() + ", Shortest Path: " + shortestPath + ", " + path);
                } else {
                    nextPositions.addAll(this.generateMoves(position));
                }
            }
        }
        return path;
    }
}


public class Advent17 {

    private static void part1() {
        String path = new Vault("dmypynyp", 3, 3).navigate();
        System.out.printf("Part 1, shortest path found = %s\n", path);
    }

    private static void test(final String passcode, final String shortestPath) {
        String path = new Vault(passcode, 3, 3).navigate();
        assert path.equals(shortestPath) : String.format("Expected shortest path to be '%s' but was '%s'!", shortestPath, path);
    }

    public static void main(String[] args) {
        test("ihgpwlah", "DDRRRD");
        test("kglvqrro", "DDUDRLRRUDRD");
        test("ulqzkmiv", "DRURDRUDDLLDLUURRDULRLDUUDDDRR");
        part1();
    }
}
