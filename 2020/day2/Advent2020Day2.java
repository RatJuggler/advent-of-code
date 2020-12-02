package day2;


class PasswordValidator {

    PasswordValidator() {}

    boolean validate(final String passwordLine) {
        return false;
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
