package day7;


public class Advent7 {

    private static boolean supportsTLS(final String ip) {
        return true;
    }

    private static void testSupportsTLS(final String ip, final boolean expectedSupports) {
        boolean supports = supportsTLS(ip);
        assert supports == expectedSupports: String.format("Expected support to be '%s' but was '%s'!", expectedSupports, supports);
    }

    public static void main(final String[] args) {
        testSupportsTLS("abba[mnop]qrst", true);
        testSupportsTLS("abcd[bddb]xyyx", false);
        testSupportsTLS("aaaa[qwer]tyui", false);
        testSupportsTLS("ioxxoj[asdfgh]zxcvbn", true);
    }

}
