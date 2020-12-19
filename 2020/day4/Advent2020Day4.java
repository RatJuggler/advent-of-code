package day4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


interface Validator {
    boolean validate(final String data);
}


class RangeValidator implements Validator {

    private final int from;
    private final int to;

    RangeValidator(final int from, final int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean validate(final String data) {
        try {
            int value = Integer.parseInt(data);
            return value >= this.from && value <= this.to;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}


class ByUnitsValidator implements Validator {

    private final Map<String, Validator> unitValidators;

    ByUnitsValidator(final Map<String, Validator> unitValidators) {
        this.unitValidators = unitValidators;
    }

    @Override
    public boolean validate(String data) {
        String units = data.substring(data.length() - 2);
        String value = data.substring(0, data.length() - 2);
        return this.unitValidators.containsKey(units) && this.unitValidators.get(units).validate(value);
    }
}


class PatternValidator implements Validator {

    private final String pattern;

    PatternValidator(final String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean validate(final String data) {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(data);
        return m.matches();
    }
}


class PassthroughValidator implements Validator {

    private final boolean result;

    PassthroughValidator(final boolean result) {
        this.result = result;
    }

    @Override
    public boolean validate(String data) {
        return result;
    }
}


class FieldValidator {

    private final String data;
    private final Validator validator;

    FieldValidator(final String data, final Validator validator) {
        this.data = data;
        this.validator = validator;
    }

    boolean validate() {
        return this.validator.validate(this.data);
    }
}


class FieldValidatorFactory {

    private static final Validator BYR_VALIDATOR = new RangeValidator(1920, 2002);
    private static final Validator IYR_VALIDATOR = new RangeValidator(2010, 2020);
    private static final Validator EYR_VALIDATOR = new RangeValidator(2020, 2030);
    private static final Validator HCL_VALIDATOR = new PatternValidator("^#[0-9a-f]{6}$");
    private static final Validator ECL_VALIDATOR = new PatternValidator("^(amb|blu|brn|gry|grn|hzl|oth)$");
    private static final Validator PID_VALIDATOR = new PatternValidator("^[0-9]{9}$");
    private static final Validator PASS_VALIDATOR = new PassthroughValidator(true);
    private static final Validator FAIL_VALIDATOR = new PassthroughValidator(false);
    private static final Validator HGT_VALIDATOR;
    static {
        Map<String, Validator> unitValidators = new HashMap<>();
        unitValidators.put("cm", new RangeValidator(150, 193));
        unitValidators.put("in", new RangeValidator(59, 76));
        HGT_VALIDATOR = new ByUnitsValidator(unitValidators);
    }

    static FieldValidator createValidator(final String field, final String data) {
        switch (field.toLowerCase()) {
            case "byr":
                return new FieldValidator(data, BYR_VALIDATOR);
            case "iyr":
                return new FieldValidator(data, IYR_VALIDATOR);
            case "eyr":
                return new FieldValidator(data, EYR_VALIDATOR);
            case "hgt":
                return new FieldValidator(data, HGT_VALIDATOR);
            case "hcl":
                return new FieldValidator(data, HCL_VALIDATOR);
            case "ecl":
                return new FieldValidator(data, ECL_VALIDATOR);
            case "pid":
                return new FieldValidator(data, PID_VALIDATOR);
            case "cid":
                return new FieldValidator(data, PASS_VALIDATOR);
        }
        throw new IllegalArgumentException("Unknown field: " + field);
    }
}


abstract class PassportValidator {

    private static final String[] FIELD_NAMES = {"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};

    private final Map<String, FieldValidator> fields;

    PassportValidator(final Map<String, FieldValidator> fields) {
        this.fields = fields;
    }

    boolean validateFieldsPresent() {
        return Arrays.stream(FIELD_NAMES).allMatch(this.fields::containsKey);
    }

    boolean validateFields() {
        return this.fields.size() == this.fields.values().stream().filter(FieldValidator::validate).count();
    }

    abstract boolean validate();
}


class Part1PassportValidator extends PassportValidator {

    Part1PassportValidator(Map<String, FieldValidator> fields) {
        super(fields);
    }

    @Override
    boolean validate() {
        return this.validateFieldsPresent();
    }
}


class Part2PassportValidator extends PassportValidator {

    Part2PassportValidator(Map<String, FieldValidator> fields) {
        super(fields);
    }

    @Override
    boolean validate() {
        return this.validateFieldsPresent() && this.validateFields();
    }
}


class PassportValidatorFactory {

    private PassportValidatorFactory() {}

    private static Matcher parsePassport(final String passport) {
        String pattern = "(?<field>\\w+):(?<data>\\S+)";
        Pattern r = Pattern.compile(pattern);
        return r.matcher(passport);
    }

    private static Map<String, FieldValidator> createFieldValidator(final String passport) {
        Matcher m = parsePassport(passport);
        Map<String, FieldValidator> fields = new HashMap<>();
        while (m.find()) {
            String field = m.group("field");
            String data = m.group("data");
            fields.put(field, FieldValidatorFactory.createValidator(field, data));
        }
        return fields;
    }

    static PassportValidator createValidator(final String type, final String passport) {
        Map<String, FieldValidator> fields = createFieldValidator(passport);
        if (fields.size() == 0)
            throw new IllegalStateException("No fields found in passport: " + passport);
        if ("Part1".equalsIgnoreCase(type))
            return new Part1PassportValidator(fields);
        else if ("Part2".equalsIgnoreCase(type))
            return new Part2PassportValidator(fields);
        else
            throw new IllegalArgumentException("Unknown type: " + type);
    }
}


public class Advent2020Day4 {

    private static void testPassportValidator(final String type, final String passport, final boolean expected) {
        assert PassportValidatorFactory.createValidator(type, passport).validate() == expected :
                String.format("Expected %s validation for passport \"%s\" to be %s!", type, passport, expected ? "valid" : "invalid");
    }

    private static void testPart1PassportValidator() {
        testPassportValidator("Part1", "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\n" +
                "byr:1937 iyr:2017 cid:147 hgt:183cm", true);
        testPassportValidator("Part1", "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\n" +
                "hcl:#cfa07d byr:1929", false);
        testPassportValidator("Part1", "hcl:#ae17e1 iyr:2013\n" +
                "eyr:2024\n" +
                "ecl:brn pid:760753108 byr:1931\n" +
                "hgt:179cm", true);
        testPassportValidator("Part1", "hcl:#cfa07d eyr:2025 pid:166559648\n" +
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
        testPassportValidator("Part2", "eyr:1972 cid:100\n" +
                "hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926", false);
        testPassportValidator("Part2", "iyr:2019\n" +
                "hcl:#602927 eyr:1967 hgt:170cm\n" +
                "ecl:grn pid:012533040 byr:1946", false);
        testPassportValidator("Part2", "hcl:dab227 iyr:2012\n" +
                "ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277", false);
        testPassportValidator("Part2", "hgt:59cm ecl:zzz\n" +
                "eyr:2038 hcl:74454a iyr:2023\n" +
                "pid:3556412378 byr:2007", false);
        testPassportValidator("Part2", "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980\n" +
                "hcl:#623a2f", true);
        testPassportValidator("Part2", "eyr:2029 ecl:blu cid:129 byr:1989\n" +
                "iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm", true);
        testPassportValidator("Part2", "hcl:#888785\n" +
                "hgt:164cm byr:2001 iyr:2015 cid:88\n" +
                "pid:545766238 ecl:hzl\n" +
                "eyr:2022", true);
        testPassportValidator("Part2", "iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719", true);
    }

    private static int countValidPassports(final String type, final String filename) throws FileNotFoundException {
        int validPassports = 0;
        StringBuilder passport = new StringBuilder();
        Scanner s = new Scanner(new File(filename));
        while (s.hasNext()) {
            String line = s.nextLine();
            if ("".equals(line)) {
                if (PassportValidatorFactory.createValidator(type, passport.toString()).validate()) validPassports++;
                passport = new StringBuilder();
            } else {
                passport.append(' ').append(line);
            }
        }
        if (PassportValidatorFactory.createValidator(type, passport.toString()).validate()) validPassports++;
        return validPassports;
    }

    public static void main(final String[] args) throws FileNotFoundException {
        testPart1PassportValidator();
        assert countValidPassports("Part1", "2020/day4/test4a.txt") == 2 : "Expected valid passport count to be 2!";
        System.out.printf("Day 4, part 1, number of valid passports is %d.%n", countValidPassports("Part1", "2020/day4/input4.txt"));
        testPart2FieldValidations();
        testPart2PassportValidator();
        assert countValidPassports("Part2", "2020/day4/test4b.txt") == 4 : "Expected valid passport count to be 4!";
        System.out.printf("Day 4, part 2, number of valid passports is %d.%n", countValidPassports("Part2", "2020/day4/input4.txt"));
    }
}
