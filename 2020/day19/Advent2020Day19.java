package day19;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


class Rule {

    private final String rule;

    Rule(final String rule) {
        this.rule = rule;
    }

    static Rule fromString(final String ruleToParse) {
        return new Rule(ruleToParse);
    }

    static List<Rule> fromList(final List<String> ticketsToParse) {
        return ticketsToParse.stream()
                .map(Rule::fromString)
                .collect(Collectors.toList());
    }
}


class MessageValidator {

    private final List<Rule> rules;
    private final List<String> messages;

    MessageValidator(final List<Rule> rules, final List<String> messages) {
        this.rules = rules;
        this.messages = messages;
    }

    private static List<List<String>> readFileSections(final String filename) {
        List<List<String>> fileSections = new ArrayList<>();
        List<String> section = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.length() != 0) {
                    section.add(line);
                } else {
                    fileSections.add(section);
                    section = new ArrayList<>();
                }
            }
            fileSections.add(section);
        } catch (FileNotFoundException fnf) {
            throw new IllegalArgumentException("Unable to read message file!", fnf);
        }
        return fileSections;
    }

    static MessageValidator fromFile(final String filename) {
        List<List<String>> fileSections = readFileSections(filename);
        List<Rule> rules = Rule.fromList(fileSections.get(0));
        List<String> messages = fileSections.get(1);
        return new MessageValidator(rules, messages);
    }

    int validate() {
        return 0;
    }
}


public class Advent2020Day19 {

    private static void testMessageValidator() {
        int expectedMessages = 2;
        MessageValidator validator = MessageValidator.fromFile("2020/day19/test19a.txt");
        int actualMessages = validator.validate();
        assert actualMessages == expectedMessages :
                String.format("Expected valid messages to be %d not %d!%n", expectedMessages, actualMessages);
    }

    public static void main(final String[] args) {
        testMessageValidator();
    }
}
