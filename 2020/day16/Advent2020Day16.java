package day16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class Range {

    final int from;
    final int to;

    Range(final int from, final int to) {
        this.from = from;
        this.to = to;
    }
}


class FieldRule {

    final String name;
    final List<Range> ranges;

    FieldRule(final String name, final List<Range> ranges) {
        this.name = name;
        this.ranges = Collections.unmodifiableList(ranges);
    }
}


class Ticket {

    final List<Integer> fields;

    Ticket(final List<Integer> fields) {
        this.fields = Collections.unmodifiableList(fields);
    }
}


class TicketScanner {

    final List<FieldRule> fieldRules;
    final Ticket myTicket;
    final List<Ticket> otherTickets;

    TicketScanner(final List<FieldRule> fieldRules, final Ticket myTicket, final List<Ticket> otherTickets) {
        this.fieldRules = Collections.unmodifiableList(fieldRules);
        this.myTicket = myTicket;
        this.otherTickets = Collections.unmodifiableList(otherTickets);
    }

    static TicketScanner fromFile(final String filename) {
        List<FieldRule> fieldRules = new ArrayList<>();
        List<Integer> myFields = new ArrayList<>();
        Ticket myTicket = new Ticket(myFields);
        List<Ticket> otherTickets = new ArrayList<>();
        return new TicketScanner(fieldRules, myTicket, otherTickets);
    }

    int errorRate() {
        return 0;
    }
}


public class Advent2020Day16 {

    public static void testTickerScannerErrorRate() {
        int expectedRate = 71;
        TicketScanner scanner = TicketScanner.fromFile("2020/day16/test16a.txt");
        int actualRate = scanner.errorRate();
        assert actualRate == expectedRate : String.format("Expected memory sum to be %d not %d!%n", expectedRate, actualRate);
    }

    public static void main(final String[] args) {
        testTickerScannerErrorRate();
    }
}
