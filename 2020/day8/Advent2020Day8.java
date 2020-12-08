package day8;

import java.io.IOException;


class HHGC {

    HHGC() {}

    static HHGC fromFile(final String filename) {
        return new HHGC();
    }

    void bootUntilLoop() {
    }

    int getAccumulator() {
        return 0;
    }
}


public class Advent2020Day8 {

    private static void test() throws IOException {
        int expected = 5;
        HHGC hhgc = HHGC.fromFile("2020/day8/test8a.txt");
        hhgc.bootUntilLoop();
        int actual = hhgc.getAccumulator();
        assert actual == expected : String.format("Expected accumulator to be '%s' but was '%s'!", expected, actual);
    }

    public static void main(final String[] args) throws IOException {
        test();
    }
}
