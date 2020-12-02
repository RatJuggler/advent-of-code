package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


class PasswordValidator {

    final int min;
    final int max;
    final String c;
    final String password;

    PasswordValidator(final int min, final int max, final String c, final String password) {
        this.min = min;
        this.max = max;
        this.c = c;
        this.password = password;
    }

    static PasswordValidator fromPasswordLine(final String line) {
        String pattern = "^(?<min>\\d+)-(?<max>\\d+) (?<c>\\w): (?<password>\\w+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse password line: " + line);
        }
        int min = Integer.parseInt(m.group("min"));
        int max = Integer.parseInt(m.group("max"));
        String c = m.group("c");
        String password = m.group("password");
        return new PasswordValidator(min, max, c, password);
    }

    boolean validate() {
        String pattern = String.format("(%s)", this.c);
        Pattern r = Pattern.compile(pattern);
        long found = r.matcher(this.password).results().count();
        return found >= this.min && found <= this.max;
    }

    public String toString() {
        return String.format("{min = %d, max = %d, c = %s, password = %s}", this.min, this.max, this.c, this.password);
    }
}


public class Advent2020Day2 {

    private static void testPasswordValidator() {
        String testLine1 = "1-3 a: abcde";
        assert PasswordValidator.fromPasswordLine(testLine1).validate() :
                String.format("Expected password in line \"%s\" to be valid!", testLine1);
        String testLine2 = "1-3 b: cdefg";
        assert !PasswordValidator.fromPasswordLine(testLine2).validate() :
                String.format("Expected password in line \"%s\" to be invalid!", testLine2);
        String testLine3 = "2-9 c: ccccccccc";
        assert PasswordValidator.fromPasswordLine(testLine3).validate() :
                String.format("Expected password in line \"%s\" to be valid!", testLine3);
    }

    private static long countValidPasswords(final String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.map(PasswordValidator::fromPasswordLine).filter(PasswordValidator::validate).count();
        }
    }

    public static void main(final String[] args) throws IOException {
        testPasswordValidator();
        assert countValidPasswords("2020/day2/test2a.txt") == 2 : "Expected valid password count to be 2!";
        System.out.printf("Day 1, part 1, number of valid passwords is %d.%n", countValidPasswords("2020/day2/input2.txt"));
    }

}
