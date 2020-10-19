package day17;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiFunction;


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

    final String navigate(final int initialPathLength, final BiFunction<String, Integer, Boolean> pathLengthCheck) {
        Queue<Position> nextPositions = new ArrayDeque<>();
        int pathLength = initialPathLength;
        String path = null;
        nextPositions.add(new Position(0, 0, ""));
        while (nextPositions.peek() != null) {
            Position position = nextPositions.poll();
            if (pathLengthCheck.apply(position.path, pathLength)) {
                if (position.x == this.sizeX && position.y == this.sizeY) {
                    path = position.path;
                    pathLength = path.length();
//                    System.out.println("Queue: " + nextPositions.size() + ", Path: " + pathLength + ", " + path);
                } else {
                    nextPositions.addAll(this.generateMoves(position));
                }
            }
        }
        return path;
    }

    String navigateLongestPath() {
        return this.navigate(0, (path, longestPath) -> path.length() >= longestPath);
    }

    String navigateShortestPath() {
        return this.navigate(Integer.MAX_VALUE, (path, shortestPath) -> path.length() < shortestPath);
    }
}


public class Advent17 {

    private static void part2() {
        String path = new Vault("dmypynyp", 3, 3).navigateLongestPath();
        System.out.printf("Part 2, longest path found = %s\n", path.length());
    }

    private static void part1() {
        String path = new Vault("dmypynyp", 3, 3).navigateShortestPath();
        System.out.printf("Part 1, shortest path found = %s\n", path);
    }

    private static void test2(final String passcode, final int longestPath) {
        String path = new Vault(passcode, 3, 3).navigateLongestPath();
        assert path.length() == longestPath : String.format("Expected longest path to be '%s' but was '%s'!", longestPath, path.length());
    }

    private static void test1(final String passcode, final String shortestPath) {
        String path = new Vault(passcode, 3, 3).navigateShortestPath();
        assert path.equals(shortestPath) : String.format("Expected shortest path to be '%s' but was '%s'!", shortestPath, path);
    }

    public static void main(String[] args) {
        test1("ihgpwlah", "DDRRRD");
        test1("kglvqrro", "DDUDRLRRUDRD");
        test1("ulqzkmiv", "DRURDRUDDLLDLUURRDULRLDUUDDDRR");
        part1();
        test2("ihgpwlah", 370);
        test2("kglvqrro", 492);
        test2("ulqzkmiv", 830);
        part2();
    }
}
