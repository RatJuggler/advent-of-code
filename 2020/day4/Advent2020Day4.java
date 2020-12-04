package day4;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class PassportValidator {

    private final Map<String, String> fields;

    PassportValidator(final Map<String, String> fields) {
        this.fields = fields;
    }

    static PassportValidator create(final String passport) {
        String pattern = "(?<field>\\w+):(?<data>\\S+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(passport);
        Map<String, String> fields = new HashMap<>();
        while (m.find()) {
            String field = m.group("field");
            String data = m.group("data");
            fields.put(field, data);
        }
        if (fields.size() == 0) {
            throw new IllegalStateException("No fields found in passport: " + passport);
        }
        return new PassportValidator(fields);
    }

    boolean validate() {
        return this.fields.containsKey("byr") &&
                this.fields.containsKey("iyr") &&
                this.fields.containsKey("eyr") &&
                this.fields.containsKey("hgt") &&
                this.fields.containsKey("hcl") &&
                this.fields.containsKey("ecl") &&
                this.fields.containsKey("pid");
    }
}


public class Advent2020Day4 {

    private static void testPassportValidator(final String passport, final boolean expected) {
        assert PassportValidator.create(passport).validate() == expected :
                String.format("Expected passport \"%s\" to be %s!", passport, expected ? "valid" : "invalid");
    }

    private static void testPart1PassportValidator() {
        testPassportValidator("ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\n" +
                "byr:1937 iyr:2017 cid:147 hgt:183cm", true);
        testPassportValidator("iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\n" +
                "hcl:#cfa07d byr:1929", false);
        testPassportValidator("hcl:#ae17e1 iyr:2013\n" +
                "eyr:2024\n" +
                "ecl:brn pid:760753108 byr:1931\n" +
                "hgt:179cm", true);
        testPassportValidator("hcl:#cfa07d eyr:2025 pid:166559648\n" +
                "iyr:2011 ecl:brn hgt:59in", false);
    }

    private static void testPart2PassportValidator() {
        testPassportValidator("eyr:1972 cid:100\n" +
                "hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926", false);
        testPassportValidator("iyr:2019\n" +
                "hcl:#602927 eyr:1967 hgt:170cm\n" +
                "ecl:grn pid:012533040 byr:1946", false);
        testPassportValidator("hcl:dab227 iyr:2012\n" +
                "ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277", false);
        testPassportValidator("hgt:59cm ecl:zzz\n" +
                "eyr:2038 hcl:74454a iyr:2023\n" +
                "pid:3556412378 byr:2007", false);
        testPassportValidator("pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980\n" +
                "hcl:#623a2f", true);
        testPassportValidator("eyr:2029 ecl:blu cid:129 byr:1989\n" +
                "iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm", true);
        testPassportValidator("hcl:#888785\n" +
                "hgt:164cm byr:2001 iyr:2015 cid:88\n" +
                "pid:545766238 ecl:hzl\n" +
                "eyr:2022", true);
        testPassportValidator("iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719", true);
    }

    private static int countValidPassports(final String filename) throws IOException {
        int validPassports = 0;
        StringBuilder passport = new StringBuilder();
        try (Scanner s = new Scanner(new File(filename))) {
            while (s.hasNext()) {
                String line = s.nextLine();
                if ("".equals(line)) {
                    if (PassportValidator.create(passport.toString()).validate()) validPassports++;
                    passport = new StringBuilder();
                }
                passport.append(' ').append(line);
            }
        }
        if (PassportValidator.create(passport.toString()).validate()) validPassports++;
        return validPassports;
    }

    public static void main(final String[] args) throws IOException {
        testPart1PassportValidator();
        assert countValidPassports("2020/day4/test4a.txt") == 2 : "Expected valid passport count to be 2!";
        System.out.printf("Day 4, part 1, number of valid passports is %d.%n", countValidPassports("2020/day4/input4.txt"));
        testPart2PassportValidator();
        assert countValidPassports("2020/day4/test4b.txt") == 4 : "Expected valid passport count to be 4!";
    }
}
