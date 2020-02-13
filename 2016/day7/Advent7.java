package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


class IPValidator {

    private final String prenet;
    private final String hypernet;
    private final String postnet;

    IPValidator(final String ip) {
        String pattern = "(?<prenet>[a-z]+)\\[(?<hypernet>[a-z]+)](?<postnet>[a-z]+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(ip);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse IP: " + ip);
        }
        this.prenet = m.group("prenet");
        this.hypernet = m.group("hypernet");
        this.postnet = m.group("postnet");
    }

    private boolean hasABBA(final String segment) {
        assert segment.length() > 3: "ABBA only supported for segments of >= 4 characters.";
        for (int i = 0; i < segment.length() - 3; i++) {
            char[] pair = segment.substring(i, i + 2).toCharArray();
            if (pair[0] != pair[1]) {
                String reversePair = String.valueOf(pair[1]) + pair[0];
                if (segment.substring(i + 2, i + 4).equals(reversePair)) {
                    System.out.println(segment);
                    return true;
                }
            }
        }
        return false;
    }

    boolean supportsTLS() {
        return !hasABBA(this.hypernet) && (hasABBA(this.prenet) || hasABBA(this.postnet));
    }

    @Override
    public String toString() {
        return String.format("IPValidator{prenet='%s', hypernet='%s', postnet='%s'}", this.prenet, this.hypernet, this.postnet);
    }
}


public class Advent7 {

    private static Long countTLSSupportedIPs(final String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return stream.map(IPValidator::new)
                    .filter(IPValidator::supportsTLS)
                    .count();
        }
    }

    private static void testSupportsTLS(final String ip, final boolean expectedSupports) {
        IPValidator validator = new IPValidator(ip);
        System.out.println(validator);
        boolean supports = validator.supportsTLS();
        assert supports == expectedSupports:
                String.format("Expected support to be '%s' but was '%s'!", expectedSupports, supports);
    }

    public static void main(final String[] args) throws IOException {
        testSupportsTLS("abba[mnop]qrst", true);
        testSupportsTLS("abcd[bddb]xyyx", false);
        testSupportsTLS("aaaa[qwer]tyui", false);
        testSupportsTLS("ioxxoj[asdfgh]zxcvbn", true);
        testSupportsTLS("tyui[asdfgh]abcd[bddb]ioxxoj", true);
        long tlsSupportedIPs = countTLSSupportedIPs("2016/day7/input7.txt");
        System.out.println(String.format("Day 7, Part 1 number of IPs support TLS is %d.", tlsSupportedIPs));
    }

}
