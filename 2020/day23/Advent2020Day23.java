package day23;

public class Advent2020Day23 {

    private static String crabCups(final String startingCups, final int moves) {
        return "";
    }

    private static void testCrabCups(final String startingCups, final int moves, final String expectedResult) {
        String actualResult = crabCups(startingCups, moves);
        assert expectedResult.equals(actualResult) :
                String.format("Expected cup ordering to be \"%s\" not \"%s\"!%n", expectedResult, actualResult);
    }

    public static void main(final String[] args) {
        testCrabCups("389125467", 10, "92658374");
        testCrabCups("389125467", 100, "67384529");
        System.out.printf("Day 23, part 1, cup ordering is \"%s\".%n", crabCups("67384529", 100));
    }
}
