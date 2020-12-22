package day22;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Player {

    final String name;
    final List<Integer> deck = new ArrayList<>();

    Player(final String name) {
        this.name = name;
    }

    void addCard(final Integer card) {
        this.deck.add(card);
    }
}


class CombatGame {

    CombatGame(final List<Player> players) {}

    static CombatGame fromFile(final String filename) {
        List<Player> players = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                Player player = new Player(name);
                while (scanner.hasNextLine()) {
                    String card = scanner.nextLine();
                    if (card.length() == 0) break;
                    player.addCard(Integer.decode(card));
                }
                players.add(player);
            }
        } catch (FileNotFoundException fnf) {
            throw new IllegalArgumentException("Unable to read tiles file!", fnf);
        }
        return new CombatGame(players);
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
