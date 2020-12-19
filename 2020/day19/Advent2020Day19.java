package day19;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


class MatchRule {

    private String id;

    MatchRule(final String id) {
        this.id = id;
    }

    String getId() {
        return this.id;
    }
}


class MatchRuleLetter extends MatchRule {

    private final char letter;

    MatchRuleLetter(final String id, final char letter) {
        super(id);
        this.letter = letter;
    }

    boolean match(final char letter) {
        return this.letter == letter;
    }
}


class MatchRuleSubRules extends MatchRule {

    private final List<String[]> subRules;

    MatchRuleSubRules(final String id, final List<String[]> subRules) {
        super(id);
        this.subRules = subRules;
    }

    boolean match() {
        return false;
    }
}


class MatchRuleFactory {

    private MatchRuleFactory() {}

    private static Matcher letterMatchRule(final String definition) {
        String pattern = "^'(?<letter>[ab])'$";
        Pattern r = Pattern.compile(pattern);
        return r.matcher(definition);
    }

    private static MatchRuleSubRules createMarchSubRule(final String[] definition) {
        String[] subRules = definition[1].split("\\|");
        List<String[]> matchRules = new ArrayList<>();
        for (String subRule: subRules) {
            matchRules.add(subRule.split(" "));
        }
        return new MatchRuleSubRules(definition[0], matchRules);
    }

    static MatchRule createMatchRule(final String matchRuleDefinition) {
        String[] definition = matchRuleDefinition.split(": ");
        Matcher letterRule = letterMatchRule(definition[1]);
        if (letterRule.find())
            return new MatchRuleLetter(definition[0], letterRule.group("letter").charAt(0));
        else
            return createMarchSubRule(definition);
    }
}


class MatchRules {

    private final List<MatchRule> matchRules;

    MatchRules(final List<MatchRule> matcheRules) {
        this.matchRules = Collections.unmodifiableList(matcheRules);
    }

    static MatchRules fromList(final List<String> matchDefinitions) {
        List<MatchRule> definitions = matchDefinitions.stream()
                .map(MatchRuleFactory::createMatchRule)
                .collect(Collectors.toList());
        return new MatchRules(definitions);
    }
}


class MessageValidator {

    private final MatchRules matchRules;
    private final List<String> messages;

    MessageValidator(final MatchRules matchRules, final List<String> messages) {
        this.matchRules = matchRules;
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
        MatchRules rules = MatchRules.fromList(fileSections.get(0));
        List<String> messages = fileSections.get(1);
        return new MessageValidator(rules, messages);
    }

    int validate() {
        int valid = 0;
        return valid;
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
