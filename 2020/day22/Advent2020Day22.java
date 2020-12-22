package day22;


class CombatGame {

    CombatGame() {}

    static CombatGame fromFile(final String fromFile) {
        return new CombatGame();
    }

    int play() {
        return 0;
    }
}


public class Advent2020Day22 {

    private static int playCombatGame(final String filename) {
        return CombatGame.fromFile(filename).play();
    }

    private static void testCombat() {
        int expectedScore = 306;
        int actualScore = playCombatGame("2020/day22/test22a.txt");
        assert actualScore == expectedScore : String.format("Expected winning score to be %d not %d!%n", expectedScore, actualScore);
    }

    public static void main(final String[] args) {
        testCombat();
        System.out.printf("Day 22, part 1, winning players score is %d.%n", playCombatGame("2020/day22/input22.txt"));
    }
}
