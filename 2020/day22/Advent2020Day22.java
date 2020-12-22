package day22;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Player {

    final String name;
    private final List<Integer> deck;
    private final List<Integer> deckHistory = new ArrayList<>();

    Player(final String name, final List<Integer> deck) {
        this.name = name;
        this.deck = deck;
    }

    Integer getCard() {
        return this.deck.remove(0);
    }

    void addCards(final List<Integer> cards) {
        this.deckHistory.add(this.deck.hashCode());
        this.deck.addAll(cards);
    }

    boolean stillHoldingCards() {
        return this.deck.size() > 0;
    }

    int score() {
        return IntStream.range(0, this.deck.size()).map(i -> this.deck.get(i) * (this.deck.size() - i)).sum();
    }

    boolean repeatDeck() {
        return this.deckHistory.contains(this.deck.hashCode());
    }

    boolean hasAtLeast(final int number) {
        return this.deck.size() >= number;
    }

    List<Integer> copyDeck(final int size) {
        return new ArrayList<>(this.deck.subList(0, size));
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
                List<Integer> startingDeck = new ArrayList<>();
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
        return new CombatGame(players);
    }

    static CombatGame recursive(final CombatGame game, final List<Integer> deckSizes) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < deckSizes.size(); i++) {
            Player currentPlayer = game.players.get(i);
            List<Integer> newDeck = currentPlayer.copyDeck(deckSizes.get(i));
            players.add(new Player(currentPlayer.name, newDeck));
        }
        return new CombatGame(players);
    }

    private List<Integer> drawRoundCards() {
        return this.players.stream().map(Player::getCard).collect(Collectors.toList());
    }

    private int findRoundWinner(final List<Integer> round) {
        return round.indexOf(Collections.max(round));
    }

    private Integer findGameWinner() {
        List<Integer> winner = new ArrayList<>();
        for (int i = 0; i < this.players.size(); i++) {
            if (this.players.get(i).stillHoldingCards()) winner.add(i);
        }
        if (winner.size() > 1)
            return null;
        else
            return winner.get(0);
    }

    private boolean recursiveRound(final List<Integer> round) {
        return IntStream.range(0, round.size()).allMatch(i -> this.players.get(i).hasAtLeast(round.get(i)));
    }

    private List<Integer> recursiveResults(final List<Integer> round, final int roundWinner) {
        List<Integer> results = new ArrayList<>();
        results.add(round.remove(roundWinner));
        results.addAll(round);
        return results;
    }

    int play() {
        Integer winner = null;
        while (winner == null) {
            List<Integer> round = this.drawRoundCards();
            int roundWinner = this.findRoundWinner(round);
            round.sort(Collections.reverseOrder());
            this.players.get(roundWinner).addCards(round);
            winner = this.findGameWinner();
        }
        return this.players.get(winner).score();
    }

    private int recursivePlay() {
        Integer winner = null;
        while (winner == null) {
            boolean repeatedDeck = this.players.stream().anyMatch(Player::repeatDeck);
            if (repeatedDeck) {
                winner = 0;
                continue;
            }
            List<Integer> round = this.drawRoundCards();
            int roundWinner;
            if (this.recursiveRound(round)) {
                CombatGame recursiveGame = CombatGame.recursive(this, round);
                roundWinner = recursiveGame.recursivePlay();
                round = this.recursiveResults(round, roundWinner);
            } else {
                roundWinner = this.findRoundWinner(round);
                round.sort(Collections.reverseOrder());
            }
            this.players.get(roundWinner).addCards(round);
            winner = this.findGameWinner();
        }
        return winner;
    }

    int playRecursive() {
        int winner = this.recursivePlay();
        return this.players.get(winner).score();
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
