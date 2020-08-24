package day3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;


final class TriangleDataValidator {

    private int validTriangles = 0;

    TriangleDataValidator() {}

    private int[] parseRowData(final String row) {
        Scanner scan = new Scanner(row);
        return new int[] {scan.nextInt(), scan.nextInt(), scan.nextInt()};
    }

    private void validateTriangle(final int a, final int b, final int c) {
        if (a + b > c && a + c > b && b + c > a) {
            this.validTriangles++;
        }
    }

    void validateDataByRow(final String filename) throws IOException {
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            int[] rowData = parseRowData(row);
            validateTriangle(rowData[0], rowData[1], rowData[2]);
        }
    }

    void validateDataByColumn(final String filename) throws IOException {
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLine()) {
            String row1 = scanner.nextLine();
            int[] rowData1 = parseRowData(row1);
            String row2 = scanner.nextLine();
            int[] rowData2 = parseRowData(row2);
            String row3 = scanner.nextLine();
            int[] rowData3 = parseRowData(row3);
            validateTriangle(rowData1[0], rowData2[0], rowData3[0]);
            validateTriangle(rowData1[1], rowData2[1], rowData3[1]);
            validateTriangle(rowData1[2], rowData2[2], rowData3[2]);
        }
    }

    int getValidTriangles() {
        return this.validTriangles;
    }
}


final class Advent3 {

    static long countByRowCondensed(final String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream
                    .map(row -> {Scanner scan = new Scanner(row); return new int[] {scan.nextInt(), scan.nextInt(), scan.nextInt()};})
                    .filter(rowData -> rowData[0] + rowData[1] > rowData[2] && rowData[0] + rowData[2] > rowData[1] && rowData[1] + rowData[2] > rowData[0])
                    .count();
        }
    }

    static int countValidTrianglesByRow(final String filename) throws IOException {
        TriangleDataValidator validator = new TriangleDataValidator();
        validator.validateDataByRow(filename);
        return validator.getValidTriangles();
    }

    static void testValidateTrianglesByRow(final String testFileName) throws IOException {
        final int expectedValidTriangles = 3;
        int validTriangles = countValidTrianglesByRow(testFileName);
        assert validTriangles == expectedValidTriangles:
                String.format("Expected to find %d valid triangles but was %d!", expectedValidTriangles, validTriangles);
        long altValidTriangles = countByRowCondensed(testFileName);
        assert altValidTriangles == expectedValidTriangles:
                String.format("Expected to find %d valid triangles using alt but was %d!", expectedValidTriangles, altValidTriangles);
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
        System.out.printf("Day 3, Part 1, there are %d valid triangles.%n", validTriangles1);
        testValidateTrianglesByColumn(testFileName);
        int validTriangles2 = countValidTrianglesByColumn("2016/day3/input3.txt");
        System.out.printf("Day 3, Part 2, there are %d valid triangles.%n", validTriangles2);
    }

}
