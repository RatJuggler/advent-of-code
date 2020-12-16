package day16;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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


class RangeRule {

    private final int from;
    private final int to;

    RangeRule(final int from, final int to) {
        this.from = from;
        this.to = to;
    }

    static RangeRule fromString(final String rangeToParse) {
        Matcher m = Parser.parse(rangeToParse, "^(?<from>\\d+)-(?<to>\\d+)$");
        int from = Integer.parseInt(m.group("from"));
        int to = Integer.parseInt(m.group("to"));
        return new RangeRule(from, to);
    }

    boolean inRange(final int field) {
        return this.from <= field && field <= this.to;
    }
}


class FieldDefinition {

    final String name;
    private final List<RangeRule> rangeRules;

    FieldDefinition(final String name, final List<RangeRule> rangeRules) {
        this.name = name;
        this.rangeRules = Collections.unmodifiableList(rangeRules);
    }

    static FieldDefinition fromString(final String definitionToParse) {
        Matcher m = Parser.parse(definitionToParse, "^(?<name>.+): (?<range1>\\d+-\\d+) or (?<range2>\\d+-\\d+)$");
        List<RangeRule> rangeRules = new ArrayList<>();
        rangeRules.add(RangeRule.fromString(m.group("range1")));
        rangeRules.add(RangeRule.fromString(m.group("range2")));
        return new FieldDefinition(m.group("name"), rangeRules);
    }

    boolean inRange(final int field) {
        return this.rangeRules.stream()
                .anyMatch(r -> r.inRange(field));
    }
}


class FieldDefinitions {

    private final List<FieldDefinition> definitions;

    FieldDefinitions(final List<FieldDefinition> definitions) {
        this.definitions = Collections.unmodifiableList(definitions);
    }

    static FieldDefinitions fromList(final List<String> defintionsToParse) {
        List<FieldDefinition> definitions = defintionsToParse.stream()
                .map(FieldDefinition::fromString)
                .collect(Collectors.toList());
        return new FieldDefinitions(definitions);
    }

    boolean nonMatch(final int fieldValue) {
        return this.definitions.stream()
                .noneMatch(r -> r.inRange(fieldValue));
    }

    private List<String> names() {
        return this.definitions.stream()
                .map(r -> r.name)
                .collect(Collectors.toList());
    }

    private List<FieldDefinition> definitions(final List<String> names) {
        return this.definitions.stream()
                .filter(r -> names.contains(r.name))
                .collect(Collectors.toList());
    }

    Map<String, Integer> classifyFields(final List<Ticket> tickets) {
        Map<String, Integer> classifiedFields = new HashMap<>();
        List<String> fieldsNotClassified = this.names();
        while (fieldsNotClassified.size() > 0) {
            for (FieldDefinition fieldDefinition : this.definitions(fieldsNotClassified)) {
                List<Integer> validFields = IntStream.range(0, this.definitions.size()).boxed().collect(Collectors.toList());
                for (Ticket ticket : tickets) {
                    List<Integer> ticketValidFields = ticket.validFields(fieldDefinition, classifiedFields.values());
                    validFields = validFields.stream()
                            .filter(ticketValidFields::contains)
                            .collect(Collectors.toList());
                }
                if (validFields.size() == 1) {
                    classifiedFields.put(fieldDefinition.name, validFields.get(0));
                    fieldsNotClassified.remove(fieldDefinition.name);
                }
            }
        }
        return classifiedFields;
    }
}


class Ticket {

    private final List<Integer> fieldValues;

    Ticket(final List<Integer> fieldValues) {
        this.fieldValues = Collections.unmodifiableList(fieldValues);
    }

    static Ticket fromString(final String ticketToParse) {
        List<Integer> fields = Arrays.stream(ticketToParse.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return new Ticket(fields);
    }

    static List<Ticket> fromList(final List<String> ticketsToParse) {
        return ticketsToParse.stream()
                .map(Ticket::fromString)
                .collect(Collectors.toList());
    }

    int errorRate(final FieldDefinitions fieldDefinitions) {
        return this.fieldValues.stream()
                .filter(fieldDefinitions::nonMatch)
                .mapToInt(Integer::intValue).sum();
    }

    List<Integer> validFields(final FieldDefinition fieldDefinition, final Collection<Integer> alreadyClassified) {
        return IntStream.range(0, this.fieldValues.size())
                .filter(i -> !alreadyClassified.contains(i))
                .filter(i -> fieldDefinition.inRange(this.fieldValues.get(i)))
                .boxed().collect(Collectors.toList());
    }

    long fieldValueProduct(final List<Integer> fields) {
        return fields.stream()
                .map((this.fieldValues::get))
                .mapToLong(Long::valueOf)
                .reduce(1, (a, v) -> a * v);
    }
}


class TicketScanner {

    private final FieldDefinitions fieldDefinitions;
    private final Ticket myTicket;
    private final List<Ticket> otherTickets;

    TicketScanner(final FieldDefinitions fieldDefinitions, final Ticket myTicket, final List<Ticket> otherTickets) {
        this.fieldDefinitions = fieldDefinitions;
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
        FieldDefinitions fieldDefinitions = FieldDefinitions.fromList(fileSections.get(0));
        Ticket myTicket = Ticket.fromString(fileSections.get(1).get(1));
        List<Ticket> otherTickets = Ticket.fromList(fileSections.get(2).subList(1, fileSections.get(2).size()));
        return new TicketScanner(fieldDefinitions, myTicket, otherTickets);
    }

    int totalErrorRate() {
        return this.otherTickets.stream()
                .mapToInt(t -> t.errorRate(this.fieldDefinitions))
                .sum();
    }

    long myDepartureFieldProduct() {
        List<Integer> fields = this.classifyFields().entrySet().stream()
                .filter(e -> e.getKey().startsWith("departure"))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        return this.myTicket.fieldValueProduct(fields);
    }

    Map<String, Integer> classifyFields() {
        List<Ticket> validTickets = this.otherTickets.stream()
                .filter(t -> t.errorRate(this.fieldDefinitions) == 0)
                .collect(Collectors.toList());
        return this.fieldDefinitions.classifyFields(validTickets);
    }
}

// class: 0-1 or 4-19
// row: 0-5 or 8-19
// seat: 0-13 or 16-19
//
// your ticket:
// 11,12,13
//
// nearby tickets:
// 3,9,18
// 15,1,5
// 5,14,9

// class
// 3,9,18 -> 1,2
// 15,1,5 -> 0,1,2
// 5,14,9 -> 0,1,2

// row
// 3,9,18 -> 0,1,2
// 15,1,5 -> 0,1,2
// 5,14,9 -> 0,1,2

// seat
// 3,9,18 -> 0,1,2
// 15,1,5 -> 1,2
// 5,14,9 -> 0,2

// seat => 2

// class
// 3,9,x  -> 1
// 15,1,x -> 0,1
// 5,14,x -> 0,1

// row
// 3,9,x  -> 0,1
// 15,1,x -> 0,1
// 5,14,x -> 0,1

// class = 0

// row = 1

public class Advent2020Day16 {

    public static void testTickerScannerErrorRate() {
        int expectedRate = 71;
        TicketScanner scanner = TicketScanner.fromFile("2020/day16/test16a.txt");
        int actualRate = scanner.totalErrorRate();
        assert actualRate == expectedRate : String.format("Expected ticket error rate to be %d not %d!%n", expectedRate, actualRate);
    }

    public static void testTickerScannerClassifyFields() {
        TicketScanner scanner = TicketScanner.fromFile("2020/day16/test16b.txt");
        Map<String, Integer> myFields = scanner.classifyFields();
        assert myFields.get("class") == 1 : "Expected 'class' field to be 1!";
        assert myFields.get("row") == 0 : "Expected 'row' field to be 0!";
        assert myFields.get("seat") == 2 : "Expected 'seat' field to be 2!";
    }

    public static void main(final String[] args) {
        testTickerScannerErrorRate();
        testTickerScannerClassifyFields();
        TicketScanner scanner = TicketScanner.fromFile("2020/day16/input16.txt");
        System.out.printf("Day 16, Part 1, ticket error rate is %s\n", scanner.totalErrorRate());
        System.out.printf("Day 16, Part 2, my departure field product is %s\n", scanner.myDepartureFieldProduct());
    }
}
