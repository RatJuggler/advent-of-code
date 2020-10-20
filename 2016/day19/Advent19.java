package day19;


class Party {

    private final int elves;
    private final boolean[] hasPresents;

    Party(final int elves) {
        this.elves = elves;
        this.hasPresents = new boolean[elves];
        for (int elf = 0; elf < elves; elf++)
            this.hasPresents[elf] = true;
    }

    int play() {
        int winner = -1;
        while (winner < 0) {
            for (int elf = 0; elf < this.elves; elf++) {
                if (this.hasPresents[elf]) {
                    winner = elf;
                    for (int steal = 1; steal < this.elves; steal++) {
                        int nextElf = (elf + steal) % this.elves;
                        if (this.hasPresents[nextElf]) {
                            this.hasPresents[nextElf] = false;
                            winner = -1;
                            break;
                        }
                    }
                }
            }
        }
        return winner + 1;
    }
}


public class Advent19 {

    private static void part1() {
        Party party = new Party(3005290);
        System.out.printf("Part 1, the winner is elf %s\n", party.play());
    }

    private static void test() {
        int expectedWinner = 3;
        Party party = new Party(5);
        int actualWinner = party.play();
        assert actualWinner == expectedWinner : String.format("Expected winner to be elf '%s' but was elf '%s'!", expectedWinner, actualWinner);
    }

    public static void main(String[] args) {
        test();
        part1();
    }
}
