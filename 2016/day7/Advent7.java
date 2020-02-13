package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


class IPValidator {

    private final List<String> net = new ArrayList<>();
    private final List<String> hypernet = new ArrayList<>();

    IPValidator(final String ip) {
        String pattern = "((?<net>[a-z]+)|\\[(?<hypernet>[a-z]+)])";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(ip);
        while (m.find()) {
            String net = m.group("net");
            if (net != null) {
                this.net.add(net);
            }
            String hypernet = m.group("hypernet");
            if (hypernet != null) {
                this.hypernet.add(hypernet);
            }
        }
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
        for (String hypernet: this.hypernet) {
            if (hasABBA(hypernet)) {
                return false;
            }
        }
        for (String net: this.net) {
            if (hasABBA(net)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("IPValidator{net='%s', hypernet='%s'}", this.net, this.hypernet);
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
        testSupportsTLS("tyui[asdfgh]abcd[bddb]ioxxoj", false);
        long tlsSupportedIPs = countTLSSupportedIPs("2016/day7/input7.txt");
        System.out.println(String.format("Day 7, Part 1 number of IPs support TLS is %d.", tlsSupportedIPs));
    }

}
