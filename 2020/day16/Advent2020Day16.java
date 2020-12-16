package day16;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


class Parser {

    private Parser() {}

    static Matcher parse(final String toParse, final String pattern) {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(toParse);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse: " + toParse);
        }
        return m;
    }
}


class Range {

    private final int from;
    private final int to;

    Range(final int from, final int to) {
        this.from = from;
        this.to = to;
    }

    static Range fromString(final String rangeToParse) {
        Matcher m = Parser.parse(rangeToParse, "^(?<from>\\d+)-(?<to>\\d+)$");
        int from = Integer.parseInt(m.group("from"));
        int to = Integer.parseInt(m.group("to"));
        return new Range(from, to);
    }

    boolean inRange(final int field) {
        return this.from <= field && field <= this.to;
    }
}


class FieldRule {

    private final String name;
    private final List<Range> ranges;

    FieldRule(final String name, final List<Range> ranges) {
        this.name = name;
        this.ranges = Collections.unmodifiableList(ranges);
    }

    private static FieldRule fromString(final String ruleToParse) {
        Matcher m = Parser.parse(ruleToParse, "^(?<name>.+): (?<range1>\\d+-\\d+) or (?<range2>\\d+-\\d+)$");
        List<Range> ranges = new ArrayList<>();
        ranges.add(Range.fromString(m.group("range1")));
        ranges.add(Range.fromString(m.group("range2")));
        return new FieldRule(m.group("name"), ranges);
    }

    static List<FieldRule> fromList(final List<String> rulesToParse) {
        return rulesToParse.stream().map(FieldRule::fromString).collect(Collectors.toList());
    }

    boolean inRange(final int field) {
        boolean inRange = false;
        for (Range range : this.ranges)
            inRange = inRange || range.inRange(field);
        return inRange;
    }
}


class Ticket {

    private final List<Integer> fields;

    Ticket(final List<Integer> fields) {
        this.fields = Collections.unmodifiableList(fields);
    }

    static Ticket fromString(final String fieldsToParse) {
        List<Integer> fields = Arrays.stream(fieldsToParse.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        return new Ticket(fields);
    }

    static List<Ticket> fromList(final List<String> ticketsToParse) {
        return ticketsToParse.stream().map(Ticket::fromString).collect(Collectors.toList());
    }

    int errorRate(final List<FieldRule> fieldRules) {
        int errorRate = 0;
        for (int field: this.fields) {
            boolean inRange = false;
            for (FieldRule rule: fieldRules)
                inRange = inRange || rule.inRange(field);
            if (!inRange) errorRate += field;
        }
        return errorRate;
    }
}


class TicketScanner {

    private final List<FieldRule> fieldRules;
    private final Ticket myTicket;
    private final List<Ticket> otherTickets;

    TicketScanner(final List<FieldRule> fieldRules, final Ticket myTicket, final List<Ticket> otherTickets) {
        this.fieldRules = Collections.unmodifiableList(fieldRules);
        this.myTicket = myTicket;
        this.otherTickets = Collections.unmodifiableList(otherTickets);
    }

    private static List<List<String>> readFileSections(final String filename) {
        List<List<String>> fileSections = new ArrayList<>();
        List<String> section = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.length() != 0) {
                    section.add(line);
                } else {
                    fileSections.add(section);
                    section = new ArrayList<>();
                }
            }
            fileSections.add(section);
        } catch (FileNotFoundException fnf) {
            throw new IllegalArgumentException("Unable to read ticket file!", fnf);
        }
        return fileSections;
    }

    static TicketScanner fromFile(final String filename) {
        List<List<String>> fileSections = readFileSections(filename);
        List<FieldRule> fieldRules = FieldRule.fromList(fileSections.get(0));
        Ticket myTicket = Ticket.fromString(fileSections.get(1).get(1));
        List<Ticket> otherTickets = Ticket.fromList(fileSections.get(2).subList(1, fileSections.get(2).size()));
        return new TicketScanner(fieldRules, myTicket, otherTickets);
    }

    int errorRate() {
        return this.otherTickets.stream().mapToInt(t -> t.errorRate(this.fieldRules)).sum();
    }

    Map<String, Integer> classifyFields() {
        return new HashMap<>();
    }
}


public class Advent2020Day16 {

    public static void testTickerScannerErrorRate() {
        int expectedRate = 71;
        TicketScanner scanner = TicketScanner.fromFile("2020/day16/test16a.txt");
        int actualRate = scanner.errorRate();
        assert actualRate == expectedRate : String.format("Expected ticket error rate to be %d not %d!%n", expectedRate, actualRate);
    }

    public static void testTickerScannerFieldClassifier() {
        TicketScanner scanner = TicketScanner.fromFile("2020/day16/test16b.txt");
        Map<String, Integer> myFields = scanner.classifyFields();
        assert myFields.get("class") == 12 : "Expected 'class' field to be 12!";
        assert myFields.get("row") == 11 : "Expected 'row' field to be 11!";
        assert myFields.get("seat") == 13 : "Expected 'seat' field to be 13!";
    }

    public static void main(final String[] args) {
        testTickerScannerErrorRate();
        TicketScanner scanner = TicketScanner.fromFile("2020/day16/input16.txt");
        System.out.printf("Day 16, Part 1, ticket error rate is %s\n", scanner.errorRate());
        testTickerScannerFieldClassifier();
    }
}
