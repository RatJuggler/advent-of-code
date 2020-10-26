package day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class Node {

    private static final String PARSE_PATTERN =
            "^(?<node>\\S+-x(?<x>\\d+)-y(?<y>\\d+)) +(?<size>\\d+)T +(?<used>\\d+)T +(?<avail>\\d+)T +(?<peruse>\\d+)%$";
    private static final Pattern NODE_PARSER = Pattern.compile(PARSE_PATTERN);

    final String name;
    final int x;
    final int y;
    final int size;
    final int used;
    final int avail;

    Node(final String name, final int x, final int y, final int size, final int used, final int avail) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.size = size;
        this.used = used;
        this.avail = avail;
    }

    Node(final Node from) {
        this.name = from.name;
        this.x = from.x;
        this.y = from.y;
        this.size = from.size;
        this.used = from.used;
        this.avail = from.avail;
    }

    static Node fromLine(final String line) {
        Matcher m = NODE_PARSER.matcher(line);
        if (!m.find()) throw new IllegalStateException("Unable to parse Node storage: " + line);
        String name = m.group("node");
        int x = Integer.parseInt(m.group("x"));
        int y = Integer.parseInt(m.group("y"));
        int size = Integer.parseInt(m.group("size"));
        int used = Integer.parseInt(m.group("used"));
        int avail = Integer.parseInt(m.group("avail"));
        return new Node(name, x, y, size, used, avail);
    }

    @Override
    public String toString() {
        return "Node{" + name + ", size=" + size + ", used=" + used + ", avail=" + avail + '}';
    }

    public boolean equalState(final Node that) {
        return this.name.equals(that.name) &&
                this.x == that.x &&
                this.y == that.y &&
                this.size == that.size &&
                this.used == that.used &&
                this.avail == that.avail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x &&
                y == node.y &&
                size == node.size &&
                used == node.used &&
                avail == node.avail &&
                name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, x, y, size, used, avail);
    }
}


class ClusterStorage {

    final Node[][] cluster;

    ClusterStorage(final Node[][] cluster) { this.cluster = cluster; }

    static ClusterStorage fromFile(final String filename) throws IOException {
        Node[][] cluster;
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            cluster = stream.filter(l -> l.startsWith("/dev/grid"))
                            .map(Node::fromLine)
                            .collect(Collectors.groupingBy(n -> n.y))
                            .values()
                            .stream()
                            .map(c -> c.toArray(Node[]::new))
                            .toArray(Node[][]::new);
        }
        return new ClusterStorage(cluster);
    }

    long countViablePairs() {
        return Arrays.stream(this.cluster)
                    .flatMap(row -> Arrays.stream(row).distinct())
                    .filter(n1 -> Arrays.stream(this.cluster)
                            .flatMap(row -> Arrays.stream(row).distinct())
                            .anyMatch(n2 -> n1 != n2 && n1.used > 0 && n2.avail >= n1.used))
                    .count();
    }

    ClusterStorage makeClone() {
        Node[][] copy = Arrays.stream(this.cluster)
                .flatMap(row -> Arrays.stream(row).distinct())
                .map(Node::new)
                .collect(Collectors.groupingBy(n -> n.y))
                .values()
                .stream()
                .map(c -> c.toArray(Node[]::new))
                .toArray(Node[][]::new);
        return new ClusterStorage(copy);
    }

    @Override
    public String toString() {
        return "ClusterStorage{\n" +
                Arrays.stream(this.cluster).map(Arrays::toString).reduce("", (s, e) -> s + e + "\n") +
                '}';
    }

    public boolean equalState(final ClusterStorage that) {
        for (int y = 0; y < this.cluster.length; y++) {
            Node[] row = this.cluster[y];
            Node[] thatRow = that.cluster[y];
            for (int x = 0; x < row.length; x++) {
                if (!row[x].equalState(thatRow[x])) return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClusterStorage that = (ClusterStorage) o;
        return Arrays.equals(cluster, that.cluster);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cluster);
    }
}


public class Advent22 {

    private static void showChecks(final ClusterStorage original, final ClusterStorage copy) {
        System.out.println(original);
        System.out.println(copy);
        System.out.println("Equal State = " + original.equalState(copy));
        System.out.println("Original HashCode = " + Arrays.deepHashCode(original.cluster));
        System.out.println("Copy HashCode = " + Arrays.deepHashCode(copy.cluster));
        System.out.println("Original HashCode[0][0] = " + original.cluster[0][0].hashCode());
        System.out.println("Copy HashCode[0][0] = " + copy.cluster[0][0].hashCode());
        System.out.println("Original HashCode[1][1] = " + original.cluster[1][1].hashCode());
        System.out.println("Copy HashCode[1][1] = " + copy.cluster[1][1].hashCode());
        System.out.println("Original HashCode[2][2] = " + original.cluster[2][2].hashCode());
        System.out.println("Copy HashCode[2][2] = " + copy.cluster[2][2].hashCode());
    }

    private static void testClone() throws IOException {
        ClusterStorage original = ClusterStorage.fromFile("2016/day22/test22a.txt");
        ClusterStorage copy = original.makeClone();
        showChecks(original, copy);
        original.cluster[1][1] = new Node("Testing", 99, 99, 99, 99, 99);
        copy.cluster[2][2] = new Node("Testing", 99, 99, 99, 99, 99);
        showChecks(original, copy);
        original.cluster[1][1] = copy.cluster[1][1];
        copy.cluster[2][2] = original.cluster[2][2];
        showChecks(original, copy);
    }

    private static void test1() throws IOException {
        ClusterStorage storage = ClusterStorage.fromFile("2016/day22/test22a.txt");
        System.out.printf("Test, viable pairs = %s\n", storage.countViablePairs());
    }

    private static void part1() throws IOException {
        ClusterStorage storage = ClusterStorage.fromFile("2016/day22/input22.txt");
        System.out.printf("Part 1, viable pairs = %s\n", storage.countViablePairs());
    }

    public static void main(String[] args) throws IOException {
        testClone();
        test1();
        part1();
    }
}
