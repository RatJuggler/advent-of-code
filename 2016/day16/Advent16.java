package day16;


public class Advent16 {

    private static String generateData(final String input, final int required) {
        String a = input;
        while (a.length() < required) {
            String b = new StringBuilder(a).reverse().toString();
            b = b.replace('0', '*').replace('1', '0').replace('*', '1');
            a = a + '0' + b;
        }
        return a.substring(0, required);
    }

    private static String checksum(final String data) {
        String checksum = data;
        while (checksum.length() % 2 == 0) {
            StringBuilder newChecksum = new StringBuilder();
            for (int i = 0; i < checksum.length(); i += 2) {
                String pair = checksum.substring(i, i + 2);
                if (pair.charAt(0) == pair.charAt(1))
                    newChecksum.append('1');
                else
                    newChecksum.append('0');
            }
            checksum = newChecksum.toString();
        }
        return checksum;
    }

    private static void part2() {
        String data = generateData("01110110101001000", 35651584);
        String checksum = checksum(data);
        System.out.printf("Day 16, Part 2 checksum is %s.%n", checksum);
    }

    private static void part1() {
        String data = generateData("01110110101001000", 272);
        String checksum = checksum(data);
        System.out.printf("Day 16, Part 1 checksum is %s.%n", checksum);
    }

    private static void test() {
        String expected = "01100";
        String data = generateData("10000", 20);
        String actual = checksum(data);
        assert actual.equals(expected) : String.format("Expected checksum of '%s' but was '%s'!", expected, actual);
    }

    private static void testChecksum(String data, String expected) {
        String actual = checksum(data);
        assert actual.equals(expected) : String.format("Expected checksum '%s' but was '%s'!", expected, actual);
    }

    private static void testGenerate(final String input, final int required, final String expected) {
        String actual = generateData(input, required);
        assert actual.equals(expected) : String.format("Expected data '%s' but was '%s'!", expected, actual);
    }

    public static void main(String[] args) {
        testGenerate("1", 3, "100");
        testGenerate("0", 3, "001");
        testGenerate("11111", 11, "11111000000");
        testGenerate("111100001010", 25, "1111000010100101011110000");
        testChecksum("110010110100", "100");
        test();
        part1();
        part2();
    }

}
