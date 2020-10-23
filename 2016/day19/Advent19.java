package day19;

import java.util.LinkedList;
import java.util.List;


class Party {

    private final int elves;

    Party(final int elves) {
        this.elves = elves;
    }

    int play1() {
        final boolean[] hasPresents = new boolean[this.elves];
        for (int elf = 0; elf < this.elves; elf++)
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

    int oldPlay2() {
        final List<Integer> players = new LinkedList<>();
        for (int elf = 0; elf < this.elves; elf++)
            players.add(elf + 1);
        int player = 0;
        while (players.size() > 1) {
            int opposite = (player + (players.size() / 2)) % players.size();
            players.remove(opposite);
            if (opposite > player)
                player = (player + 1) % players.size();
            else if (player == players.size())
                player = 0;
            if (players.size() % 10000 == 0)
                System.out.println(players.size());
        }
        return players.get(0);
    }

    int play2() {
        final int[] hasPresents = new int[this.elves];
        for (int elf = 0; elf < this.elves; elf++)
            hasPresents[elf] = elf + 1;
        int player = 0;
        int leftInGame = this.elves;
        while (leftInGame > 1) {
            int opposite = (player + (leftInGame / 2)) % leftInGame;
            leftInGame--;
            System.arraycopy(hasPresents, opposite + 1, hasPresents, opposite, leftInGame - opposite);
            if (opposite > player)
                player = (player + 1) % leftInGame;
            else if (player == leftInGame)
                player = 0;
            if (leftInGame % 10000 == 0)
                System.out.println(leftInGame);
        }
        return hasPresents[0];
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
