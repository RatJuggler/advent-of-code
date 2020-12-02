package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


abstract class PasswordValidator {

    final int min;
    final int max;
    final String c;
    final String password;

    PasswordValidator(final String line) {
        String pattern = "^(?<min>\\d+)-(?<max>\\d+) (?<c>\\w): (?<password>\\w+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse password line: " + line);
        }
        this.min = Integer.parseInt(m.group("min"));
        this.max = Integer.parseInt(m.group("max"));
        this.c = m.group("c");
        this.password = m.group("password");
    }

    abstract boolean validate();

    public String toString() {
        return String.format("{min = %d, max = %d, c = %s, password = %s}", this.min, this.max, this.c, this.password);
    }
}


class SledCompanyPasswordValidator extends PasswordValidator {

    SledCompanyPasswordValidator(final String line) {
        super(line);
    }

    boolean validate() {
        String pattern = String.format("(%s)", this.c);
        Pattern r = Pattern.compile(pattern);
        long found = r.matcher(this.password).results().count();
        return found >= this.min && found <= this.max;
    }
}


class TobogganCompanyPasswordValidator extends PasswordValidator {

    TobogganCompanyPasswordValidator(final String line) {
        super(line);
    }

    boolean validate() {
        return false;
    }
}


class CompanyPasswordValidatorFactory {

    static PasswordValidator createValidator(final String company, final String line) {
        if ("Sled".equalsIgnoreCase(company))
            return new SledCompanyPasswordValidator(line);
        else if ("Toboggan".equalsIgnoreCase(company))
            return new TobogganCompanyPasswordValidator(line);
        else
            throw new IllegalArgumentException("Unknown company: " + company);
    }
}


public class Advent2020Day2 {

    private static void testPasswordValidator(final String company, final String line, final boolean expected) {
        assert CompanyPasswordValidatorFactory.createValidator(company, line).validate() == expected :
                String.format("Expected password in line \"%s\" to be %s!", line, expected ? "valid" : "invalid");
    }

    private static void testSledCompanyPasswordValidator() {
        testPasswordValidator("Sled", "1-3 a: abcde", true);
        testPasswordValidator("Sled", "1-3 b: cdefg", false);
        testPasswordValidator("Sled", "2-9 c: ccccccccc", true);
    }

    private static void testTobogganCompanyPasswordValidator() {
        testPasswordValidator("Toboggan", "1-3 a: abcde", true);
        testPasswordValidator("Toboggan", "1-3 b: cdefg", false);
        testPasswordValidator("Toboggan", "2-9 c: ccccccccc", false);
    }

    private static long countValidPasswords(final String company, final String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.map(line -> CompanyPasswordValidatorFactory.createValidator(company, line))
                    .filter(PasswordValidator::validate)
                    .count();
        }
    }

    public static void main(final String[] args) throws IOException {
        testSledCompanyPasswordValidator();
        assert countValidPasswords("Sled", "2020/day2/test2a.txt") == 2 : "Expected valid password count to be 2!";
        System.out.printf("Day 1, part 1, number of valid passwords is %d.%n", countValidPasswords("Sled", "2020/day2/input2.txt"));
        assert countValidPasswords("Toboggan", "2020/day2/test2a.txt") == 1 : "Expected valid password count to be 1!";
        System.out.printf("Day 1, part 2, number of valid passwords is %d.%n", countValidPasswords("Toboggan", "2020/day2/input2.txt"));
    }

}
