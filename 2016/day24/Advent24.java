package day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;


class Point {

    final int x;
    final int y;
    final int steps;

    Point(final int x, final int y, final int steps) {
        this.x = x;
        this.y = y;
        this.steps = steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point{x=" + this.x + ", y=" + this.y + ", steps=" + this.steps + '}';
    }
}


class Maze {

    final List<String> layout;
    final Map<Character, Point> poi = new HashMap<>();
    final Map<Character, Map<Character, Integer>> distances = new HashMap<>();

    Maze(final List<String> layout) {
        this.layout = layout;
    }

    static Maze fromFile(final String filename) throws IOException {
        List<String> layout = Files.readAllLines(Paths.get(filename));
        return new Maze(layout);
    }

    void findPOI() {
        int y = 0;
        for (String row : this.layout) {
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                if (Character.isDigit(c)) this.poi.put(c, new Point(x, y, 0));
            }
            y++;
        }
    }

    private List<Point> generateNextPoints(final Point currentPoint) {
        List<Point> nextPoints = new ArrayList<>();
        nextPoints.add(new Point(currentPoint.x + 1, currentPoint.y, currentPoint.steps + 1));
        nextPoints.add(new Point(currentPoint.x - 1, currentPoint.y, currentPoint.steps + 1));
        nextPoints.add(new Point(currentPoint.x, currentPoint.y + 1, currentPoint.steps + 1));
        nextPoints.add(new Point(currentPoint.x, currentPoint.y - 1, currentPoint.steps + 1));
        return nextPoints;
    }

    private Map<Character, Integer> findDistances(final char start) {
        Map<Character, Integer> distances = new HashMap<>();
        Queue<Point> nextPoint = new ArrayDeque<>();
        Map<Integer, Point> history = new HashMap<>();
        nextPoint.add(this.poi.get(start));
        while (nextPoint.peek() != null) {
            Point point = nextPoint.poll();
            char c = this.layout.get(point.y).charAt(point.x);
            if (Character.isDigit(c)) {
                int current = distances.getOrDefault(c, Integer.MAX_VALUE);
                if (point.steps < current) distances.put(c, point.steps);
            }
            int hash = point.hashCode();
            if (history.get(hash) == null) {
                history.put(hash, point);
                for (Point newPoint : this.generateNextPoints(point)) {
                    if (this.layout.get(newPoint.y).charAt(newPoint.x) != '#') nextPoint.add(newPoint);
                }
            }
        }
        return distances;
    }

    void findDistances() {
        for (char from : this.poi.keySet()) {
            System.out.println("Finding distances from: " + from);
            this.distances.put(from, this.findDistances(from));
            System.out.println(this.distances.get(from));
        }
    }

    String showPOI() {
        StringBuilder sb = new StringBuilder();
        for (char c : this.poi.keySet()) {
            sb.append(c).append(" = ").append(this.poi.get(c)).append('\n');
        }
        return sb.toString();
    }

    String showDistances() {
        StringBuilder sb = new StringBuilder();
        for (char c : this.distances.keySet()) {
            sb.append(c).append(" = ").append(this.distances.get(c)).append('\n');
        }
        return sb.toString();
    }
}


public class Advent24 {

    private static void test1() throws IOException {
        Maze maze = Maze.fromFile("2016/day24/test24a.txt");
        maze.findPOI();
        System.out.println(maze.showPOI());
        maze.findDistances();
        System.out.println(maze.showDistances());
    }

    private static void part1() throws IOException {
        Maze maze = Maze.fromFile("2016/day24/input24.txt");
        maze.findPOI();
        System.out.println(maze.showPOI());
        maze.findDistances();
        System.out.println(maze.showDistances());
    }

    public static void main(String[] args) throws IOException {
        test1();
        part1();
    }
}
