package day16;


class TicketScanner {

    TicketScanner() {}

    static TicketScanner fromFile(final String filename) {
        return new TicketScanner();
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
