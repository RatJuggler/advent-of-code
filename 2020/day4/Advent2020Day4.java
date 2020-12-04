package day4;


class PassportValidator {

    PassportValidator() {}

    static PassportValidator create(final String passport) {
        return new PassportValidator();
    }

    boolean validate() {
        return false;
    }
}


public class Advent2020Day4 {

    private static void testPasportValidator(final String passport, final boolean expected) {
        assert PassportValidator.create(passport).validate() == expected :
                String.format("Expected passport \"%s\" to be %s!", passport, expected ? "valid" : "invalid");
    }

    private static void testPart1PassportValidator() {
        testPasportValidator("ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\n" +
                "byr:1937 iyr:2017 cid:147 hgt:183cm", true);
        testPasportValidator("iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\n" +
                "hcl:#cfa07d byr:1929", false);
        testPasportValidator("hcl:#ae17e1 iyr:2013\n" +
                "eyr:2024\n" +
                "ecl:brn pid:760753108 byr:1931\n" +
                "hgt:179cm", true);
        testPasportValidator("hcl:#cfa07d eyr:2025 pid:166559648\n" +
                "iyr:2011 ecl:brn hgt:59in", true);
    }

    private static int countValidPassports(final String filename) {
        return 0;
    }

    public static void main(final String[] args) {
        testPart1PassportValidator();
        assert countValidPassports("2020/day4/test4a.txt") == 2 : "Expected valid passport count to be 2!";
    }
}
