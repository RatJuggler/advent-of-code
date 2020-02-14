package day8;

public class Advent8 {

    private static int swipeCard(final String filename) {
        return 0;
    }

    private static void testSwipeCard() {
        String filename = "2016/day8/test8a.txt";
        int expectedPixels = 6;
        int pixels = swipeCard(filename);
        assert pixels == expectedPixels: String.format("Expected %d pixels to be lit but was %d!", expectedPixels, pixels);
    }

    public static void main(String[] args) {
        testSwipeCard();
    }
}
