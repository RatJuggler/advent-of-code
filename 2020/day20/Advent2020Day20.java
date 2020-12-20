package day20;


class ImageAssembler {

    ImageAssembler() {}

    static ImageAssembler fromFile(final String filename) {
        return new ImageAssembler();
    }

    long assemble() {
        return 0;
    }
}


public class Advent2020Day20 {

    private static void testImageAssembler() {
        long expectedCornerProduct = 20899048083289L;
        ImageAssembler assembler = ImageAssembler.fromFile("2020/day20/input20.txt");
        long actualCornerProduct = assembler.assemble();
        assert actualCornerProduct == expectedCornerProduct :
                String.format("Expected assembled corner product to be %d not %d!%n", expectedCornerProduct, actualCornerProduct);
    }

    public static void main(final String[] args) {
        testImageAssembler();
    }
}
