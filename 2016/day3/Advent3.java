package day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;


final class TriangleDataValidator {

    private int validTriangles = 0;

    TriangleDataValidator() {}

    private void validateTriangle(final int a, final int b, final int c) {
        if (a + b > c && a + c > b && b + c > a) {
            this.validTriangles++;
        }
    }

    void validateDataByRow(final String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(this::validateData);
        }
    }

    void validateDataByColumn(final String filename) {

    }

    void validateData(final String triangleData) {
        Scanner scan = new Scanner(triangleData);
        int a = scan.nextInt();
        int b = scan.nextInt();
        int c = scan.nextInt();
        validateTriangle(a, b, c);
    }

    int getValidTriangles() {
        return this.validTriangles;
    }
}


final class Advent3 {

    static int countValidTrianglesByRow(final String filename) throws IOException {
        TriangleDataValidator validator = new TriangleDataValidator();
        validator.validateDataByRow(filename);
        return validator.getValidTriangles();
    }

    static void testValidateTrianglesByRow(final String testFileName) throws IOException {
        final int expectedValidTriangles = 3;
        int validTriangles = countValidTrianglesByRow(testFileName);
        assert validTriangles == expectedValidTriangles:
                String.format("Expect to find %d valid triangles but was %d!", expectedValidTriangles, validTriangles);
    }

    static int countValidTrianglesByColumn(final String filename) throws IOException {
        TriangleDataValidator validator = new TriangleDataValidator();
        validator.validateDataByColumn(filename);
        return validator.getValidTriangles();
    }

    static void testValidateTrianglesByColumn(final String testFileName) throws IOException {
        final int expectedValidTriangles = 6;
        int validTriangles = countValidTrianglesByColumn(testFileName);
        assert validTriangles == expectedValidTriangles:
                String.format("Expect to find %d valid triangles but was %d!", expectedValidTriangles, validTriangles);
    }

    public static void main(final String[] args) throws IOException {
        String testFileName = "2016/day3/test3a.txt";
        testValidateTrianglesByRow(testFileName);
        int validTriangles1 = countValidTrianglesByRow("2016/day3/input3.txt");
        System.out.println(String.format("Day 3, Part 1, there are %d valid triangles.", validTriangles1));
        testValidateTrianglesByColumn(testFileName);
        int validTriangles2 = countValidTrianglesByColumn("2016/day3/input3.txt");
        System.out.println(String.format("Day 3, Part 2, there are %d valid triangles.", validTriangles2));
    }

}
