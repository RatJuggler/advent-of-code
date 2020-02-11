package day3;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


final class TriangleDataValidator {

    private int validTriangles = 0;

    TriangleDataValidator() {}

    private int[] parseRowData(final String row) {
        int[] rowData = new int[3];
        Scanner scan = new Scanner(row);
        rowData[0] = scan.nextInt();
        rowData[1] = scan.nextInt();
        rowData[2] = scan.nextInt();
        return rowData;
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
