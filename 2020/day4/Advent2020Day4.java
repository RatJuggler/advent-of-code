package day4;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


abstract class FieldValidator {

    final String data;

    FieldValidator(final String data) {
        this.data = data;
    }

    static boolean validateRange(final String data, final int from, final int to) {
        try {
            int value = Integer.parseInt(data);
            return value >= from && value <= to;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    abstract boolean validate();
}

class ByrValidator extends FieldValidator {

    ByrValidator(String data) {
        super(data);
    }

    @Override
    boolean validate() {
        return validateRange(this.data, 1920, 2002);
    }
}

class IyrValidator extends FieldValidator {

    IyrValidator(String data) {
        super(data);
    }

    @Override
    boolean validate() {
        return validateRange(this.data, 2010, 2020);
    }
}

class EyrValidator extends FieldValidator {

    EyrValidator(String data) {
        super(data);
    }

    @Override
    boolean validate() {
        return validateRange(this.data, 2020, 2030);
    }
}

class HgtValidator extends FieldValidator {

    HgtValidator(String data) {
        super(data);
    }

    @Override
    boolean validate() {
        String units = this.data.substring(this.data.length() - 2);
        String value = this.data.substring(0, this.data.length() - 2);
        if ("cm".equals(units))
            return validateRange(value,150, 193);
        else if ("in".equals(units))
            return validateRange(value, 59, 76);
        else
            return false;
    }
}

class HclValidator extends FieldValidator {

    HclValidator(String data) {
        super(data);
    }

    @Override
    boolean validate() {
        String pattern = "^#[0-9a-f]{6}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(this.data);
        return m.matches();
    }
}

class EclValidator extends FieldValidator {

    EclValidator(String data) {
        super(data);
    }

    @Override
    boolean validate() {
        return this.data.length() == 3 && !this.data.contains("|") && "amb|blu|brn|gry|grn|hzl|oth".contains(this.data);
    }
}

class PidValidator extends FieldValidator {

    PidValidator(String data) {
        super(data);
    }

    @Override
    boolean validate() {
        String pattern = "^[0-9]{9}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(this.data);
        return m.matches();
    }
}

class CidValidator extends FieldValidator {

    CidValidator(String data) {
        super(data);
    }

    @Override
    boolean validate() {
        return true;
    }
}

class FieldValidatorFactory {

    static FieldValidator createValidator(final String field, final String data) {
        if ("byr".equalsIgnoreCase(field))
            return new ByrValidator(data);
        else if ("iyr".equalsIgnoreCase(field))
            return new IyrValidator(data);
        else if ("eyr".equalsIgnoreCase(field))
            return new EyrValidator(data);
        else if ("hgt".equalsIgnoreCase(field))
            return new HgtValidator(data);
        else if ("hcl".equalsIgnoreCase(field))
            return new HclValidator(data);
        else if ("ecl".equalsIgnoreCase(field))
            return new EclValidator(data);
        else if ("pid".equalsIgnoreCase(field))
            return new PidValidator(data);
        else if ("cid".equalsIgnoreCase(field))
            return new CidValidator(data);
        else
            throw new IllegalArgumentException("Unknown field: " + field);
    }
}


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

    private static void testFieldValidator(final String field, final String data, final boolean expected) {
        assert FieldValidatorFactory.createValidator(field, data).validate() == expected :
                String.format("Expected field \"%s\" data \"%s\" to be %s!", field, data, expected ? "valid" : "invalid");
    }

    private static void testPart2FieldValidations() {
        testFieldValidator("byr", "2002", true);
        testFieldValidator("byr", "2003", false);
        testFieldValidator("byr", "1920", true);
        testFieldValidator("byr", "1919", false);
        testFieldValidator("iyr", "2010", true);
        testFieldValidator("iyr", "2009", false);
        testFieldValidator("iyr", "2020", true);
        testFieldValidator("iyr", "2021", false);
        testFieldValidator("eyr", "2020", true);
        testFieldValidator("eyr", "2019", false);
        testFieldValidator("eyr", "2030", true);
        testFieldValidator("eyr", "2031", false);
        testFieldValidator("hgt", "60in", true);
        testFieldValidator("hgt", "190cm", true);
        testFieldValidator("hgt", "190in", false);
        testFieldValidator("hgt", "190", false);
        testFieldValidator("hcl", "#123abc", true);
        testFieldValidator("hcl", "#123abz", false);
        testFieldValidator("hcl", "123abc", false);
        testFieldValidator("ecl", "brn", true);
        testFieldValidator("ecl", "wat", false);
        testFieldValidator("pid", "000000001", true);
        testFieldValidator("pid", "0123456789", false);
        testFieldValidator("cid", "", true);
        testFieldValidator("cid", "gb", true);
        testFieldValidator("cid", "99", true);
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
        testPart2FieldValidations();
        testPart2PassportValidator();
        assert countValidPassports("2020/day4/test4b.txt") == 4 : "Expected valid passport count to be 4!";
    }
}
