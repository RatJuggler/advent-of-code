package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


class BagRule {

    final String adjective;
    final String colour;
    final Map<String, Integer> contains = new HashMap<>();

    BagRule(final String adjective, final String colour) {
        this.adjective = adjective;
        this.colour = colour;
    }

    static String id(final String adjective, final String colour) {
        return adjective + " " + colour;
    }

    String getId() {
        return id(this.adjective, this.colour);
    }

    void mustContain(final String id, final int qty) {
        this.contains.put(id, qty);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("BagRule{'").append(this.getId()).append("', contains=(");
        if (this.contains.isEmpty()) {
            sb.append("NO OTHER BAGS");
        } else {
            Iterator<Map.Entry<String, Integer>> containsBags = this.contains.entrySet().iterator();
            while (containsBags.hasNext()) {
                Map.Entry<String, Integer> entry = containsBags.next();
                sb.append(entry.getValue()).append(' ').append(entry.getKey());
                if (containsBags.hasNext()) sb.append(", ");
            }
        }
        return sb.append(")}").toString();
    }
}


class BagRules {

    final Map<String, BagRule> bagRules = new HashMap<>();

    BagRules() {}

    BagRules readBagRules(final String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(this::addBagRuleFromLine);
        }
        return this;
    }

    int countContainingBagColours(final String findId) {
        Stack<String> history = new Stack<>();
        Set<String> found = new HashSet<>();
        Stack<Stack<String>> next = new Stack<>();
        next.push(new Stack<>());
        next.peek().addAll(this.bagRules.keySet());
        while (!next.isEmpty()) {
            Stack<String> contains = next.peek();
            if (contains.isEmpty()) {
                next.pop();
                if (!history.isEmpty()) history.pop();
            } else {
                BagRule bagRule = this.bagRules.get(contains.pop());
                if (!findId.equals(bagRule.getId()) && !bagRule.contains.isEmpty()) {
                    history.push(bagRule.getId());
                    if (bagRule.contains.containsKey(findId)) {
                        found.addAll(history);
                        history.pop();
                    } else {
                        next.push(new Stack<>());
                        next.peek().addAll(bagRule.contains.keySet());
                    }
                }
            }
        }
        return found.size();
    }

    private int countBagsIn(String findId) {
        int count = 1;
        BagRule from = this.bagRules.get(findId);
        for (Map.Entry<String, Integer> entry : from.contains.entrySet()) {
            count += entry.getValue() * countBagsIn(entry.getKey());
        }
        return count;
    }

    int countContainedBags(String findId) {
        return countBagsIn(findId) - 1;
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

    private BagRule getBagRule(final String adjective, final String colour) {
        return this.bagRules.get(BagRule.id(adjective, colour));
    }

    private void addBagRuleFromLine(final String line) {
        Matcher m = this.parseLine(line);
        String adjective = m.group("adjective");
        String colour = m.group("colour");
        BagRule newBagRule = this.getBagRule(adjective, colour);
        if (newBagRule != null) throw new IllegalStateException();
        newBagRule = new BagRule(adjective, colour);
        while (m.find()) {
            String containsAdjective = m.group("adjective");
            String containsColour = m.group("colour");
            if (!"no".equals(containsAdjective) && !"other".equals(containsColour)) {
                int qty = Integer.parseInt(m.group("qty"));
                newBagRule.mustContain(BagRule.id(containsAdjective, containsColour), qty);
            }
        }
        this.bagRules.put(newBagRule.getId(), newBagRule);
    }

    @Override
    public String toString() {
        return "BagRules{" + bagRules.values() + '}';
    }
}


public class Advent2020Day7 {

    private static int countContainingBagColours(final String filename) throws IOException {
        return new BagRules().readBagRules(filename).countContainingBagColours(BagRule.id("shiny", "gold"));
    }

    private static int countContainedBags(final String filename) throws IOException {
        return new BagRules().readBagRules(filename).countContainedBags(BagRule.id("shiny", "gold"));
    }

    public static void main(final String[] args) throws IOException {
        assert countContainingBagColours("2020/day7/test7a.txt") == 4 : "Expected containing bag colour count to be 4!";
        System.out.printf("Day 7, part 1, containing bag colour count is %d.%n", countContainingBagColours("2020/day7/input7.txt"));
        assert countContainedBags("2020/day7/test7a.txt") == 32 : "Expected contained bag count to be 32!";
        assert countContainedBags("2020/day7/test7b.txt") == 126 : "Expected contained bag count to be 126!";
        System.out.printf("Day 7, part 2, contained bag count is %d.%n", countContainedBags("2020/day7/input7.txt"));
    }
}
