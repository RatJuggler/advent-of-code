package day14;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class HashCache {

    private final String salt;
    private final Deque<String> cache;
    private int cacheIndex;
    private int nextIndex;

    private static String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1));
        }
        return sb.toString();
    }

    HashCache(final String salt, final int size) throws NoSuchAlgorithmException {
        this.salt = salt;
        this.cache = new ArrayDeque<>(size);
        for (int i = 0; i < size; i++) {
            this.cache.add(HashCache.md5(salt + i));
        }
        this.cacheIndex = this.cache.size();
        this.nextIndex = -1;
    }

    String nextHash() throws NoSuchAlgorithmException {
        this.cache.add(HashCache.md5(salt + this.cacheIndex));
        this.cacheIndex++;
        this.nextIndex++;
        return this.cache.pollFirst();
    }

    int index() {
        return this.nextIndex;
    }
}

class KeyGenerator {

    private final HashCache hashCache;

    KeyGenerator(final HashCache hashCache) {
        this.hashCache = hashCache;
    }

    private boolean tripleFound(final String hash) {
        String pattern = "(?<triple>.)\\1{2}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(hash);
        return m.find();
    }

    int findTriple() throws NoSuchAlgorithmException {
        String hash;
        do {
            hash = this.hashCache.nextHash();
        } while (!this.tripleFound(hash));
        return this.hashCache.index();
    }
}


public class Advent14 {

    private static void part2() {
    }

    private static void part1() {
    }

    private static void test() throws NoSuchAlgorithmException {
        int expectedIndex = 18;
        HashCache hashCache = new HashCache("abc", 1000);
        KeyGenerator generator = new KeyGenerator(hashCache);
        int indexFound = generator.findTriple();
        assert indexFound == expectedIndex : String.format("Expected index to be '%s' but was '%s'!", expectedIndex, indexFound);
    }

    public static void main(final String[] args) throws NoSuchAlgorithmException {
        test();
        part1();
        part2();
    }
}
