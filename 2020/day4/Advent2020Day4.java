package day4;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
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
                "iyr:2011 ecl:brn hgt:59in", false);
    }

    private static int countValidPassports(final String filename) throws IOException {
        Path path = Paths.get(filename);
        BufferedReader reader = Files.newBufferedReader(path);
        int validPassports = 0;
        StringBuffer passport = new StringBuffer();
        String line = reader.readLine();
        while (line != null) {
            if ("".equals(line)) {
                if (PassportValidator.create(passport.toString()).validate()) validPassports++;
                passport = new StringBuffer();
            }
            passport.append(' ').append(line);
            line = reader.readLine();
        }
        if (PassportValidator.create(passport.toString()).validate()) validPassports++;
        return validPassports;
    }

    public static void main(final String[] args) throws IOException {
        testPart1PassportValidator();
        assert countValidPassports("2020/day4/test4a.txt") == 2 : "Expected valid passport count to be 2!";
    }
    }
