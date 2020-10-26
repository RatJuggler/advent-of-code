package day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class Point {

    final int x;
    final int y;

    Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{x=" + x + ", y=" + y + '}';
    }
}


class Maze {

    final List<String> layout;
    final Map<Integer, Point> poi = new HashMap<>();

    Maze(final List<String> layout) {
        this.layout = layout;
        int y = 0;
        for (String row : layout) {
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                if (Character.isDigit(c)) poi.put(Integer.parseInt(String.valueOf(c)), new Point(x, y));
            }
            y++;
        }
    }

    static Maze fromFile(final String filename) throws IOException {
        List<String> layout = Files.readAllLines(Paths.get(filename));
        return new Maze(layout);
    }

    String showPOI() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.poi.size(); i++) {
            sb.append(i).append(" = ").append(this.poi.get(i)).append('\n');
        }
        return sb.toString();
    }
}


public class Advent24 {

    private static void test1() throws IOException {
        Maze maze = Maze.fromFile("2016/day24/test24a.txt");
        System.out.println(maze.showPOI());
    }

    private static void part1() throws IOException {
        Maze maze = Maze.fromFile("2016/day24/input24.txt");
        System.out.println(maze.showPOI());
    }

    public static void main(String[] args) throws IOException {
        test1();
        part1();
    }
}
