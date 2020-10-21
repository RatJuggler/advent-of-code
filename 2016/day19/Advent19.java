package day19;

import java.util.LinkedList;
import java.util.List;


class Party {

    private final int elves;

    Party(final int elves) {
        this.elves = elves;
    }

    int play1() {
        final boolean[] hasPresents = new boolean[elves];
        for (int elf = 0; elf < elves; elf++)
            hasPresents[elf] = true;
        int winner = -1;
        while (winner < 0) {
            for (int elf = 0; elf < this.elves; elf++) {
                if (hasPresents[elf]) {
                    winner = elf;
                    for (int steal = 1; steal < this.elves; steal++) {
                        int nextElf = (elf + steal) % this.elves;
                        if (hasPresents[nextElf]) {
                            hasPresents[nextElf] = false;
                            winner = -1;
                            break;
                        }
                    }
                }
            }
        }
        return winner + 1;
    }

    int play2() {
        List<Integer> players = new LinkedList<>();
        for (int player = 0; player < this.elves; player++)
            players.add(player, player + 1);
        int player = 0;
        while (players.size() > 1) {
            int opposite = (player + (players.size() / 2)) % players.size();
            players.remove(opposite);
            if (player == players.size())
                player = 0;
            else
                player++;
            System.out.println(players.size());
        }
        return players.get(0);
    }
}


public class Advent19 {

    private static void part2() {
        Party party = new Party(3005290);
        System.out.printf("Part 2, the winner is elf %s\n", party.play2());
    }

    private static void part1() {
        Party party = new Party(3005290);
        System.out.printf("Part 1, the winner is elf %s\n", party.play1());
    }

    private static void test2() {
        int expectedWinner = 2;
        Party party = new Party(5);
        int actualWinner = party.play2();
        assert actualWinner == expectedWinner : String.format("Expected winner to be elf '%s' but was elf '%s'!", expectedWinner, actualWinner);
    }

    private static void test1() {
        int expectedWinner = 3;
        Party party = new Party(5);
        int actualWinner = party.play1();
        assert actualWinner == expectedWinner : String.format("Expected winner to be elf '%s' but was elf '%s'!", expectedWinner, actualWinner);
    }

    public static void main(String[] args) {
        test1();
        part1();
        test2();
        part2();
    }
}
