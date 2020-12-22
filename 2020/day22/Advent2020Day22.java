package day22;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


class Player {

    private final String name;
    private final LinkedList<Integer> deck;
    private final Set<Integer> deckHistory = new HashSet<>();

    Player(final String name, final LinkedList<Integer> deck) {
        this.name = name;
        this.deck = deck;
    }

    Player recursive(final Integer newDeckSize) {
        return new Player(this.name, new LinkedList<>(this.deck.subList(0, newDeckSize)));
    }

    Integer getCard() {
        return this.deck.poll();
    }

    void addCards(final Integer card1, final Integer card2) {
        this.deckHistory.add(this.deck.hashCode());
        this.deck.add(card1);
        this.deck.add(card2);
    }

    boolean hasCards() {
        return this.deck.size() > 0;
    }

    boolean repeatDeck() {
        return this.deckHistory.contains(this.deck.hashCode());
    }

    boolean hasAtLeast(final int number) {
        return this.deck.size() >= number;
    }

    int score() {
        int score = 0;
        for (int i = 0; i < this.deck.size(); i++)
            score += this.deck.get(i) * (this.deck.size() - i);
        return score;
    }
}


class CombatGame {

    private final Player player1;
    private final Player player2;

    CombatGame(final Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    static CombatGame fromFile(final String filename) {
        List<Player> players = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                LinkedList<Integer> startingDeck = new LinkedList<>();
                while (scanner.hasNextLine()) {
                    String card = scanner.nextLine();
                    if (card.length() == 0) break;
                    startingDeck.add(Integer.decode(card));
                }
                players.add(new Player(name, startingDeck));
            }
        } catch (FileNotFoundException fnf) {
            throw new IllegalArgumentException("Unable to read tiles file!", fnf);
        }
        return new CombatGame(players.get(0), players.get(1));
    }

    int play() {
        while (this.player1.hasCards() && this.player2.hasCards()) {
            Integer player1Card = this.player1.getCard();
            Integer player2Card = this.player2.getCard();
            if (player1Card > player2Card) {
                this.player1.addCards(player1Card, player2Card);
            } else {
                this.player2.addCards(player2Card, player1Card);
            }
        }
        return this.player1.hasCards() ? this.player1.score() : this.player2.score();
    }

    private int recursivePlay() {
        while (this.player1.hasCards() && this.player2.hasCards()) {
            if (this.player1.repeatDeck() || this.player2.repeatDeck()) break;
            Integer player1Card = this.player1.getCard();
            Integer player2Card = this.player2.getCard();
            int winner;
            if (this.player1.hasAtLeast(player1Card) && this.player2.hasAtLeast(player2Card)) {
                winner = new CombatGame(this.player1.recursive(player1Card), this.player2.recursive(player2Card)).recursivePlay();
            } else {
                winner = player1Card > player2Card ? 1 : 2;
            }
            if (winner == 1) {
                this.player1.addCards(player1Card, player2Card);
            } else {
                this.player2.addCards(player2Card, player1Card);
            }
        }
        if (this.player1.repeatDeck() || this.player2.repeatDeck()) {
            return 1;
        } else {
            return this.player1.hasCards() ? 1 : 2;
        }
    }

    int playRecursive() {
        return this.recursivePlay() == 1 ? this.player1.score() : this.player2.score();
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
