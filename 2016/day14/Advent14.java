package day14;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class KeyGenerator {

    private final String salt;

    KeyGenerator(final String salt) {
        this.salt = salt;
    }

    private boolean tripleFound(final String hash) {
        String pattern = "(?<triple>.)\\1{2}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(hash);
        return m.find();
    }

    private String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1));
        }
        return sb.toString();
    }

    int findTriple() throws NoSuchAlgorithmException {
        int index = -1;
        String hash = "";
        do {
            index++;
            hash = md5(this.salt + index);
            System.out.println(index + " " + hash);
        } while (!this.tripleFound(hash));
        return index;
    }
}


public class Advent14 {

    private static void part2() {
    }

    private static void part1() {
    }

    private static void test() throws NoSuchAlgorithmException {
        int expectedIndex = 18;
        KeyGenerator generator = new KeyGenerator("abc");
        int indexFound = generator.findTriple();
        assert indexFound == expectedIndex : String.format("Expected index to be '%s' but was '%s'!", expectedIndex, indexFound);
    }

    public static void main(final String[] args) throws NoSuchAlgorithmException {
        test();
        part1();
        part2();
    }
}
