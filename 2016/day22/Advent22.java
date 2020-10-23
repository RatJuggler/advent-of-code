package day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
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
}


class ClusterStorage {

    final Node[] cluster;

    ClusterStorage(final Node[] cluster) { this.cluster = cluster; }

    static ClusterStorage fromFile(final String filename) throws IOException {
        Node[] cluster;
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            Node[][] test1 = stream.filter(l -> l.startsWith("/dev/grid"))
                            .map(Node::fromLine)
                            .collect(Collectors.groupingBy(n -> n.y))
                            .values()
                            .stream()
                            .map(c -> c.toArray(Node[]::new))
                            .toArray(Node[][]::new);
            Node[] test2 = Arrays.stream(test1).flatMap(row -> Arrays.stream(row).distinct()).toArray(Node[]::new);
            System.out.println(Arrays.toString(test1));
            System.out.println(Arrays.toString(test2));
        }
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            cluster = stream.filter(l -> l.startsWith("/dev/grid"))
                            .map(Node::fromLine)
                            .toArray(Node[]::new);
            System.out.println(Arrays.toString(cluster));
        }
        return new ClusterStorage(cluster);
    }

    int countViablePairs() {
        int viablePairs = 0;
        for (int a = 0; a < this.cluster.length; a++) {
            Node nodeA = this.cluster[a];
            for (int b = a; b < this.cluster.length; b++) {
                Node nodeB = this.cluster[b];
                if (nodeA.used > 0 && nodeB.avail >= nodeA.used) viablePairs++;
                if (nodeB.used > 0 && nodeA.avail >= nodeB.used) viablePairs++;
            }
        }
        return viablePairs;
    }
}


public class Advent22 {

    private static void test() throws IOException {
        ClusterStorage storage = ClusterStorage.fromFile("2016/day22/test22a.txt");
        System.out.printf("Test, viable pairs = %s\n", storage.countViablePairs());
    }

    private static void part1() throws IOException {
        ClusterStorage storage = ClusterStorage.fromFile("2016/day22/input22.txt");
        System.out.printf("Part 1, viable pairs = %s\n", storage.countViablePairs());
    }

    public static void main(String[] args) throws IOException {
        test();
        part1();
    }
}
