package day13;


class Office {

    final char[][] space;

    Office(final int seed, final int sizeX, final int sizeY) {
        this.space = new char[sizeY][sizeX];
        for (int y = 0; y < sizeY; y++) {
            char[] strip = new char[sizeX];
            for (int x = 0; x < sizeX; x++) {
                int find = (x * x) + (3 * x) + (2 * x * y) + y + (y * y) + seed;
                String binary = Integer.toBinaryString(find);
                long ones = binary.chars().filter(ch -> ch == '1').count();
                if (ones % 2 == 0)
                    strip[x] = '.';
                else
                    strip[x] = '#';
            }
            this.space[y] = strip;
        }
    }

    int findPathTo(final int toX, final int toY) {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.space.length * this.space[0].length);
        StringBuilder row1 = new StringBuilder().append("   ");
        StringBuilder row2 = new StringBuilder().append("   ");
        for (int x = 0; x < this.space[0].length; x++) {
            row1.append(x / 10);
            row2.append(x % 10);
        }
        sb.append(row1).append('\n').append(row2).append('\n');
        for (int y = 0; y < this.space.length; y++) {
            sb.append(String.format("%02d ", y)).append(this.space[y]).append('\n');
        }
        return sb.toString();
    }
}


public class Advent13 {


    private static void part2() {
    }

    private static void part1() {
        Office office = new Office(1358, 50, 50);
        System.out.println(office.toString());
        System.out.printf("Part 1, steps taken = %s\n", office.findPathTo(31, 39));
    }

    private static void test() {
        Office office = new Office(10, 10, 7);
        System.out.println(office.toString());
        int expectedSteps = 11;
        int actualSteps = office.findPathTo(7, 4);
        assert actualSteps == expectedSteps : String.format("Expected to take '%s' steps but was '%s'!", expectedSteps, actualSteps);
    }

    public static void main(final String[] args) {
        test();
        part1();
        part2();
    }

}
