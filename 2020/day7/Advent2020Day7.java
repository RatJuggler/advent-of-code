package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


class Bag {

    final String adjective;
    final String colour;
    final Map<Bag, Integer> contains = new HashMap<>();

    Bag(final String adjective, final String colour) {
        this.adjective = adjective;
        this.colour = colour;
    }

    static String makeId(final String adjective, final String colour) {
        return adjective + " " + colour;
    }

    String getId() {
        return makeId(this.adjective, this.colour);
    }

    void add(final int qty, final Bag bag) {
        this.contains.put(bag, qty);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("Bag{'").append(this.getId()).append("', contains=(");
        if (this.contains.isEmpty()) {
            sb.append("NO OTHER BAGS");
        } else {
            Iterator<Map.Entry<Bag, Integer>> containsBags = this.contains.entrySet().iterator();
            while (containsBags.hasNext()) {
                Map.Entry<Bag, Integer> entry = containsBags.next();
                sb.append(entry.getValue()).append(' ').append(entry.getKey().getId());
                if (containsBags.hasNext()) sb.append(", ");
            }
        }
        return sb.append(")}").toString();
    }
}


class BagFactory {

    final Map<String, Bag> bags = new HashMap<>();

    BagFactory() {}

    Bag getBag(final String adjective, final String colour) {
        return this.bags.get(Bag.makeId(adjective, colour));
    }

    BagFactory readBags(final String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(this::addBagFromLine);
        }
        return this;
    }

    private Matcher parseLine(final String line) {
        String pattern = "(?<qty>\\d+)? ?(?<adjective>\\w+) (?<colour>\\w+) bags?";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse bag line: " + line);
        }
        return m;
    }

    private void addBagFromLine(final String line) {
        Matcher m = this.parseLine(line);
        String adjective = m.group("adjective");
        String colour = m.group("colour");
        Bag newBag = this.getBag(adjective, colour);
        if (newBag != null) throw new IllegalStateException();
        newBag = new Bag(adjective, colour);
        while (m.find()) {
            String containsAdjective = m.group("adjective");
            String containsColour = m.group("colour");
            if (!"no".equals(containsAdjective) && !"other".equals(containsColour)) {
                int qty = Integer.parseInt(m.group("qty"));
                Bag containsBag = this.getBag(containsAdjective, containsColour);
                if (containsBag == null) containsBag = new Bag(containsAdjective, containsColour);
                newBag.add(qty, containsBag);
            }
        }
        this.bags.put(newBag.getId(), newBag);
    }

    @Override
    public String toString() {
        return "BagFactory{" + bags.values() + '}';
    }
}


public class Advent2020Day7 {

    private static int countContainingBagColours(final String filename) throws IOException {
        BagFactory bagFactory = new BagFactory().readBags(filename);
        System.out.println(bagFactory.toString());
        int colourCount = 0;
        return colourCount;
    }

    public static void main(final String[] args) throws IOException {
        assert countContainingBagColours("2020/day7/test7a.txt") == 4 : "Expected containing bag colour count to be 4!";
        System.out.printf("Day 7, part 1, containing bag colour count is %d.%n", countContainingBagColours("2020/day7/input7.txt"));
    }
}
