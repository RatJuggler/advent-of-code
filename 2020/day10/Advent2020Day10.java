package day10;


public class Advent2020Day10 {

    private static int findDifferencesProduct(final String filename) {
        return 0;
    }

    private static void testDifferencesProduct(final String filename, final int expectedDifferencesProduct) {
        long differencesProduct = findDifferencesProduct(filename);
        assert differencesProduct == expectedDifferencesProduct :
                String.format("Expected to find difference product of '%s' but was '%s'!", expectedDifferencesProduct, differencesProduct);
    }

    public static void main(final String[] args) {
        testDifferencesProduct("2020/day10/test10a.txt", 35);
        testDifferencesProduct("2020/day10/test10b.txt", 220);
    }
}
