package day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;


final class TriangleDataValidator {

    private int dataValidations = 0;
    private int validTriangles = 0;

    TriangleDataValidator() {}

    void validateData(final String triangleData) {
        this.dataValidations++;
        Scanner scan = new Scanner(triangleData);
        int a = scan.nextInt();
        int b = scan.nextInt();
        int c = scan.nextInt();
        if (a + b > c && a + c > b && b + c > a) {
            this.validTriangles++;
        }
    }

    int getDataValidations() {
        return this.dataValidations;
    }

    int getValidTriangles() {
        return this.validTriangles;
    }
}


final class Advent3 {

    static int countValidTriangles(final String filename) throws IOException {
        TriangleDataValidator validator = new TriangleDataValidator();
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(validator::validateData);
        }
        return validator.getValidTriangles();
    }

    public static void main(final String[] args) throws IOException {
        int validTriangles = countValidTriangles("2016/day3/input3.txt");
        System.out.println(String.format("Day 3, Part 1, there are %d valid triangles.", validTriangles));
    }

}
