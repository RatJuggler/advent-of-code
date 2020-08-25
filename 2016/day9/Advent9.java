package day9;

public class Advent9 {

    private static String decompress(final String sequence) {
        return sequence;
    }

    private static void testDecompress(final String sequence, final String expectedResult) {
        String decompressed = decompress(sequence);
        assert sequence.equals(expectedResult):
                String.format("Expected decompressed result to be '%s' but was '%s'!", expectedResult, decompressed);
    }

    public static void main(final String[] args) {
        testDecompress("ADVENT", "ADVENT");
        testDecompress("A(1x5)BC", "ABBBBBC");
        testDecompress("(3x3)XYZ", "XYZXYZXYZ");
        testDecompress("A(2x2)BCD(2x2)EFG", "ABCBCDEFEFG");
        testDecompress("(6x1)(1x3)A", "(1x3)A");
        testDecompress("X(8x2)(3x3)ABCY", "X(3x3)ABC(3x3)ABCY");
    }

}
