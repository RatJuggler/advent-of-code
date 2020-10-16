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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.space.length).append("  ");
        for (int x = 0; x < this.space[0].length; x++) {
            sb.append(x);
        }
        sb.append('\n');
        for (int y = 0; y < this.space.length; y++) {
            sb.append(y).append(' ').append(this.space[y]).append('\n');
        }
        return sb.toString();
    }
}


public class Advent13 {


    private static void part2() {
    }

    private static void part1() {
    }

    private static void test() {
        Office office = new Office(10, 10, 7);
        System.out.println(office.toString());
    }

    public static void main(final String[] args) {
        test();
        part1();
        part2();
    }

}
