package day19;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


abstract class MatchRule {

    private final String id;

    MatchRule(final String id) {
        this.id = id;
    }

    String getId() {
        return this.id;
    }

    abstract boolean match(final String str, final int i, final Map<String, MatchRule> matchRules);
}


class MatchRuleLetter extends MatchRule {

    private final char letter;

    MatchRuleLetter(final String id, final char letter) {
        super(id);
        this.letter = letter;
    }

    boolean match(final String str, final int i, final Map<String, MatchRule> matchRules) {
        return str.charAt(i) == this.letter;
    }
}


class MatchRuleSubRules extends MatchRule {

    private final List<String[]> subRules;

    MatchRuleSubRules(final String id, final List<String[]> subRules) {
        super(id);
        this.subRules = subRules;
    }

    boolean match(final String str, final int i, final Map<String, MatchRule> matchRules) {
        boolean subRulesMatch = false;
        for (String[] subRule: this.subRules) {
            int current = i;
            boolean rulesMatch = true;
            for (String id: subRule)
                rulesMatch = rulesMatch && matchRules.get(id).match(str, current++, matchRules);
            subRulesMatch = subRulesMatch || rulesMatch;
        }
        return subRulesMatch;
    }
}


class MatchRuleFactory {

    private MatchRuleFactory() {}

    private static Matcher letterMatchRule(final String definition) {
        String pattern = "^\"(?<letter>[ab])\"$";
        Pattern r = Pattern.compile(pattern);
        return r.matcher(definition);
    }

    private static MatchRuleSubRules createMarchSubRule(final String[] definition) {
        String[] subRules = definition[1].split(" \\| ");
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

    private final Map<String, MatchRule> matchRules;

    MatchRules(final Map<String, MatchRule> matchRules) {
        this.matchRules = Collections.unmodifiableMap(matchRules);
    }

    static MatchRules fromList(final List<String> matchDefinitions) {
        Map<String, MatchRule> definitions = matchDefinitions.stream()
                .map(MatchRuleFactory::createMatchRule)
                .collect(Collectors.toMap(MatchRule::getId, Function.identity()));
        return new MatchRules(definitions);
    }

    boolean match(final String str) {
        return this.matchRules.get("0").match(str, 0, matchRules);
    }
}


class MessageMatcher {

    private final MatchRules matchRules;
    private final List<String> messages;

    MessageMatcher(final MatchRules matchRules, final List<String> messages) {
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

    static MessageMatcher fromFile(final String filename) {
        List<List<String>> fileSections = readFileSections(filename);
        MatchRules rules = MatchRules.fromList(fileSections.get(0));
        List<String> messages = fileSections.get(1);
        return new MessageMatcher(rules, messages);
    }

    long countMatches() {
        return this.messages.stream().filter(this.matchRules::match).count();
    }
}


public class Advent2020Day19 {

    private static void testMessageMatcher() {
        long expectedMatches = 2;
        MessageMatcher matcher = MessageMatcher.fromFile("2020/day19/test19a.txt");
        long actualMatches = matcher.countMatches();
        assert actualMatches == expectedMatches :
                String.format("Expected messages matched to be %d not %d!%n", expectedMatches, actualMatches);
    }

    public static void main(final String[] args) {
        testMessageMatcher();
        MessageMatcher matcher = MessageMatcher.fromFile("2020/day19/input19.txt");
        System.out.printf("Day 19, part 1, messages matched is %d.%n", matcher.countMatches());
    }
}
