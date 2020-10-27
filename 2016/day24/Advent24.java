package day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        List<Point> history = new ArrayList<>();
        nextPoint.add(this.poi.get(start));
        while (nextPoint.peek() != null) {
            Point point = nextPoint.poll();
            char c = this.layout.get(point.y).charAt(point.x);
            if (Character.isDigit(c)) {
                int current = distances.getOrDefault(c, Integer.MAX_VALUE);
                if (point.steps < current) distances.put(c, point.steps);
            }
            if (!history.contains(point)) {
                history.add(point);
                for (Point newPoint : this.generateNextPoints(point)) {
                    if (this.layout.get(newPoint.y).charAt(newPoint.x) != '#') nextPoint.add(newPoint);
                }
            }
        }
        return distances;
    }

    void findDistances() {
        for (char from : this.poi.keySet()) {
            this.distances.put(from, this.findDistances(from));
        }
    }

    private int totalSteps(final List<Character> visited, boolean returnTo) {
        int totalSteps = 0;
        char previous = visited.get(0);
        for (Character c : visited) {
            totalSteps += this.distances.get(previous).get(c);
            previous = c;
        }
        if (returnTo)
            totalSteps += this.distances.get(previous).get(visited.get(0));
        return totalSteps;
    }

    private int visit(final List<Character> visited, final boolean returnTo, final int minimumSteps) {
        int newMinimum = minimumSteps;
        for (Character c : this.poi.keySet()) {
            if (!visited.contains(c)) {
                visited.add(c);
                if (visited.size() == this.poi.size())
                    newMinimum = Math.min(this.totalSteps(visited, returnTo), newMinimum);
                else
                    newMinimum = visit(visited, returnTo, newMinimum);
                visited.remove(c);
            }
        }
        return newMinimum;
    }

    int visitAll(final boolean returnTo) {
        List<Character> visited = new ArrayList<>();
        visited.add('0');
        return visit(visited, returnTo, Integer.MAX_VALUE);
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
        int expected = 14;
        int actual = maze.visitAll(false);
        assert actual == expected : String.format("Expected minimum steps to be '%s' but was '%s'!", expected, actual);
    }

    private static void test2() throws IOException {
        Maze maze = Maze.fromFile("2016/day24/test24a.txt");
        maze.findPOI();
        maze.findDistances();
        int expected = 20;
        int actual = maze.visitAll(true);
        assert actual == expected : String.format("Expected minimum steps to be '%s' but was '%s'!", expected, actual);
    }

    private static void part1() throws IOException {
        Maze maze = Maze.fromFile("2016/day24/input24.txt");
        maze.findPOI();
        System.out.println(maze.showPOI());
        maze.findDistances();
        System.out.println(maze.showDistances());
        System.out.println("Day 24, Part 1, minimum number of steps to visit all = " + maze.visitAll(false));
    }

    private static void part2() throws IOException {
        Maze maze = Maze.fromFile("2016/day24/input24.txt");
        maze.findPOI();
        maze.findDistances();
        System.out.println("Day 24, Part 2, minimum number of steps to visit all and return = " + maze.visitAll(true));
    }

    public static void main(String[] args) throws IOException {
        test1();
        part1();
        test2();
        part2();
    }
}
