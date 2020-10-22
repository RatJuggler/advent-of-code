package day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


class Node {

    private static final String PARSE_PATTERN =
            "^(?<node>\\S+) +(?<size>\\d+)T +(?<used>\\d+)T +(?<avail>\\d+)T +(?<peruse>\\d+)%$";
    private static final Pattern NODE_PARSER = Pattern.compile(PARSE_PATTERN);

    final String name;
    final int size;
    final int used;
    final int avail;

    Node(final String name, final int size, final int used, final int avail) {
        this.name = name;
        this.size = size;
        this.used = used;
        this.avail = avail;
    }

    static Node fromLine(final String line) {
        Matcher m = NODE_PARSER.matcher(line);
        if (!m.find()) throw new IllegalStateException("Unable to parse Node storage: " + line);
        String name = m.group("node");
        int size = Integer.parseInt(m.group("size"));
        int used = Integer.parseInt(m.group("used"));
        int avail = Integer.parseInt(m.group("avail"));
        return new Node(name, size, used, avail);
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
            cluster = stream.filter(l -> l.startsWith("/dev/grid"))
                            .map(Node::fromLine)
                            .toArray(Node[]::new);
        }
        return new ClusterStorage(cluster);
    }

    int countViablePairs() {
        int viablePairs = 0;
        for (int a = 0; a < cluster.length; a++) {
            Node nodeA = this.cluster[a];
            for (int b = a; b < cluster.length; b++) {
                Node nodeB = this.cluster[b];
                if (nodeA.used > 0 && nodeB.avail >= nodeA.used) viablePairs++;
                if (nodeB.used > 0 && nodeA.avail >= nodeB.used) viablePairs++;
            }
        }
        return viablePairs;
    }
}


public class Advent22 {

    private static void part1() throws IOException {
        ClusterStorage storage = ClusterStorage.fromFile("2016/day22/input22.txt");
        System.out.printf("Part 1, viable pairs = %s\n", storage.countViablePairs());
    }

    public static void main(String[] args) throws IOException {
        part1();
    }
}
