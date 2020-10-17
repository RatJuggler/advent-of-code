package day15;


class Disc {

    private final int positions;
    private int currentPosition;

    Disc(final int positions, final int startPosition) {
        this.positions = positions;
        this.currentPosition = startPosition;
    }

    int turn() {
        this.currentPosition = (this.currentPosition + 1) % this.positions;
        return this.currentPosition;
    }
}


public class Advent15 {

    private static void part1() {
    }

    private static void test() {
        Disc disc = new Disc(3, 0);
        System.out.println(disc.turn());
        System.out.println(disc.turn());
        System.out.println(disc.turn());
        System.out.println(disc.turn());
    }

    public static void main(String[] args) {
        test();
        part1();
    }
}
