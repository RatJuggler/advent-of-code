package day19;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


class MatchContext {

    private final String str;
    private int i = 0;

    MatchContext(final String str) {
        this.str = str;
    }

    boolean hasNextLetter() {
        return this.i < str.length();
    }

    char getLetter() {
        return this.str.charAt(this.i++);
    }

    int getPosition() {
        return this.i;
    }

    void setPosition(final int i) {
        this.i = i;
    }
}


abstract class MatchRule {

    private final String id;

    MatchRule(final String id) {
        this.id = id;
    }

    String getId() {
        return this.id;
    }

    abstract boolean match(final MatchContext context, final Map<String, MatchRule> matchRules);
}


class MatchRuleLetter extends MatchRule {

    private final char letter;

    MatchRuleLetter(final String id, final char letter) {
        super(id);
        this.letter = letter;
    }

    boolean match(final MatchContext context, final Map<String, MatchRule> matchRules) {
        return context.hasNextLetter() && context.getLetter() == this.letter;
    }
}


class MatchRuleSubRules extends MatchRule {

    private final List<String[]> subRules;

    MatchRuleSubRules(final String id, final List<String[]> subRules) {
        super(id);
        this.subRules = subRules;
    }

    boolean match(final MatchContext context, final Map<String, MatchRule> matchRules) {
        int current = context.getPosition();
        for (String[] subRule: this.subRules) {
            context.setPosition(current);
            boolean subRulesMatch = true;
            for (String id: subRule)
                subRulesMatch = subRulesMatch && matchRules.get(id).match(context, matchRules);
            if (subRulesMatch) return true;
        }
        return false;
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
        this.matchRules = matchRules;
    }

    static MatchRules fromList(final List<String> matchDefinitions) {
        Map<String, MatchRule> definitions = matchDefinitions.stream()
                .map(MatchRuleFactory::createMatchRule)
                .collect(Collectors.toMap(MatchRule::getId, Function.identity()));
        return new MatchRules(definitions);
    }

    void replaceRule(final String matchDefinition) {
        MatchRule newRule = MatchRuleFactory.createMatchRule(matchDefinition);
        this.matchRules.put(newRule.getId(), newRule);
    }

    boolean match(final String str) {
        MatchContext context = new MatchContext(str);
        return this.matchRules.get("0").match(context, matchRules) && !context.hasNextLetter();
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

    void replaceRule(final String replacementRule) {
        this.matchRules.replaceRule(replacementRule);
    }

    long countMatches() {
        return this.messages.stream().filter(this.matchRules::match).peek(System.out::println).count();
    }
}


public class Advent2020Day19 {

    private static long matchMessagesWithoutLoops(final String filename) {
        MessageMatcher matcher = MessageMatcher.fromFile(filename);
        return matcher.countMatches();
    }

    private static long matchMessagesWithLoops(final String filename) {
        MessageMatcher matcher = MessageMatcher.fromFile(filename);
        matcher.replaceRule("8: 42 | 42 8");
        matcher.replaceRule("11: 42 31 | 42 11 31");
        return matcher.countMatches();
    }

    private static void testMessageMatcherWithoutLoops(final String filename, final long expectedMatches) {
        long actualMatches = matchMessagesWithoutLoops(filename);
        assert actualMatches == expectedMatches :
                String.format("Expected messages matched without loops to be %d not %d!%n", expectedMatches, actualMatches);
    }

    private static void testMessageMatcherWithLoops(final String filename, final long expectedMatches) {
        long actualMatches = matchMessagesWithLoops(filename);
        assert actualMatches == expectedMatches :
                String.format("Expected messages matched with loops to be %d not %d!%n", expectedMatches, actualMatches);
    }

    public static void main(final String[] args) {
        testMessageMatcherWithoutLoops("2020/day19/test19a.txt", 2);
        testMessageMatcherWithoutLoops("2020/day19/test19b.txt", 3);
        System.out.printf("Day 19, part 1, messages matched is %d.%n", matchMessagesWithoutLoops("2020/day19/input19.txt"));
        testMessageMatcherWithLoops("2020/day19/test19b.txt", 12);
        System.out.printf("Day 19, part 2, messages matched is %d.%n", matchMessagesWithLoops("2020/day19/input19.txt"));
    }
}
