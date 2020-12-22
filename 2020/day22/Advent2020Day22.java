package day22;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


class Player {

    final String name;
    private final List<Integer> deck = new ArrayList<>();

    Player(final String name) {
        this.name = name;
    }

    void addCard(final Integer card) {
        this.deck.add(card);
    }

    Integer getCard() {
        return this.deck.remove(0);
    }

    void addCards(final List<Integer> cards) {
        cards.sort(Collections.reverseOrder());
        this.deck.addAll(cards);
    }

    int holdingCards() {
        return this.deck.size();
    }

    int score() {
        int score = 0;
        for (int i = 0; i < this.deck.size(); i++)
            score += this.deck.get(i) * (this.deck.size() - i);
        return score;
    }
}


class CombatGame {

    private final List<Player> players;

    CombatGame(final List<Player> players) {
        this.players = players;
    }

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

    private List<Integer> drawRoundCards() {
        return this.players.stream().map(Player::getCard).collect(Collectors.toList());
    }

    private int findRoundWinner(List<Integer> play) {
        return play.indexOf(Collections.max(play));
    }

    private Player findWinner() {
        List<Player> winner = this.players.stream().filter(p -> p.holdingCards() > 0).collect(Collectors.toList());
        if (winner.size() > 1)
            return null;
        else
            return winner.get(0);
    }

    int play() {
        Player winner = null;
        while (winner == null) {
            List<Integer> round = this.drawRoundCards();
            int roundWinner = this.findRoundWinner(round);
            this.players.get(roundWinner).addCards(round);
            winner = this.findWinner();
        }
        return winner.score();
    }

    int playRecursive() {
        return 0;
    }
}


public class Advent2020Day22 {

    private static int playCombatGame(final String filename) {
        return CombatGame.fromFile(filename).play();
    }

    private static int playRecursiveCombatGame(final String filename) {
        return CombatGame.fromFile(filename).playRecursive();
    }

    private static void testCombat() {
        int expectedScore = 306;
        int actualScore = playCombatGame("2020/day22/test22a.txt");
        assert actualScore == expectedScore : String.format("Expected winning score to be %d not %d!%n", expectedScore, actualScore);
    }

    private static void testRecursiveCombat() {
        int expectedScore = 291;
        int actualScore = playRecursiveCombatGame("2020/day22/test22a.txt");
        assert actualScore == expectedScore : String.format("Expected winning score to be %d not %d!%n", expectedScore, actualScore);
    }

    public static void main(final String[] args) {
        testCombat();
        System.out.printf("Day 22, part 1, winning players score is %d.%n", playCombatGame("2020/day22/input22.txt"));
        testRecursiveCombat();
        System.out.printf("Day 22, part 2, winning players score is %d.%n", playRecursiveCombatGame("2020/day22/input22.txt"));
    }
}
