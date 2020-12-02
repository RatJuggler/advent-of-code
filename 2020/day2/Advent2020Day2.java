package day2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


class PasswordValidator {

    static class PasswordLine {

        final int min;
        final int max;
        final String c;
        final String password;

        PasswordLine(final int min, final int max, final String c, final String password) {
            this.min = min;
            this.max = max;
            this.c = c;
            this.password = password;
        }

        String getRegExRule() {
            return String.format("(%s){%d,%d}", this.c, this.min, this.max);
        }

        public String toString() {
            return String.format("{min = %d, max = %d, c = %s, password = %s}", this.min, this.max, this.c, this.password);
        }
    }

    PasswordValidator() {}

    private PasswordLine parsePasswordLine(final String line) {
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
        return new PasswordLine(min, max, c, password);
    }

    private boolean checkRule(final String pattern, final String password) {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(password);
        return m.find();
    }

    boolean validate(final String passwordLine) {
        PasswordLine details = this.parsePasswordLine(passwordLine);
        return this.checkRule(details.getRegExRule(), details.password);
    }
}


public class Advent2020Day2 {

    private static void testPasswordValidator() {
        PasswordValidator validator = new PasswordValidator();
        String testLine1 = "1-3 a: abcde";
        assert validator.validate(testLine1) : String.format("Expected password in line \"%s\" to be valid!", testLine1);
        String testLine2 = "1-3 b: cdefg";
        assert !validator.validate(testLine2) : String.format("Expected password in line \"%s\" to be invalid!", testLine2);
        String testLine3 = "2-9 c: ccccccccc";
        assert validator.validate(testLine3) : String.format("Expected password in line \"%s\" to be valid!", testLine3);
    }

    public static void main(final String[] args) {
        testPasswordValidator();
    }

}
