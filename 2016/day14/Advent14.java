package day14;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class HashCache {

    private final String salt;
    private final Deque<String> cache;
    private int cacheIndex;
    private int nextIndex;

    private static String md5(String input) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        byte[] digest = md5.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1));
        }
        return sb.toString();
    }

    HashCache(final String salt, final int size) {
        this.salt = salt;
        this.cache = new ArrayDeque<>(size);
        for (int i = 0; i < size; i++) {
            this.cache.add(HashCache.md5(salt + i));
        }
        this.cacheIndex = this.cache.size();
        this.nextIndex = -1;
    }

    String nextHash() {
        this.cache.add(HashCache.md5(salt + this.cacheIndex));
        this.cacheIndex++;
        this.nextIndex++;
        return this.cache.pollFirst();
    }

    int index() {
        return this.nextIndex;
    }

    Iterator<String> iterator() {
        return this.cache.iterator();
    }
}

class KeyGenerator {

    private final HashCache hashCache;

    KeyGenerator(final HashCache hashCache) {
        this.hashCache = hashCache;
    }

    private boolean quintupleFound(final String hash, final String find) {
        String pattern = "(" + find + ")\\1{4}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(hash);
        return m.find();
    }

    private String tripleFound(final String hash) {
        String pattern = "(?<triple>.)\\1{2}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(hash);
        if (m.find())
            return m.group("triple");
        return null;
    }

    private int findNextKeyIndex() {
        String triple;
        boolean quintupleFound;
        do {
            do {
                String hash = this.hashCache.nextHash();
                triple = this.tripleFound(hash);
            } while (triple == null);
            Iterator<String> cache = this.hashCache.iterator();
            quintupleFound = false;
            while (!quintupleFound && cache.hasNext()) {
                String hash = cache.next();
                quintupleFound = this.quintupleFound(hash, triple);
            }
        } while (!quintupleFound);
        return this.hashCache.index();
    }

    int findKeyIndex(final int number) {
        int keyIndex = 0;
        for (int i = 0; i < number; i++) {
            keyIndex = findNextKeyIndex();
            System.out.println(i + " " + keyIndex);
        }
        return keyIndex;
    }
}


public class Advent14 {

    private static void part2() {
    }

    private static void part1() {
        HashCache hashCache = new HashCache("ahsbgdzn", 1000);
        KeyGenerator generator = new KeyGenerator(hashCache);
        int indexFound = generator.findKeyIndex(64);
        System.out.printf("Day 14, Part 1 index of 64th key is %d.%n", indexFound);
    }

    private static void test1() {
        int expectedIndex = 22728;
        HashCache hashCache = new HashCache("abc", 1000);
        KeyGenerator generator = new KeyGenerator(hashCache);
        int indexFound = generator.findKeyIndex(64);
        assert indexFound == expectedIndex : String.format("Expected index to be '%s' but was '%s'!", expectedIndex, indexFound);
    }

    public static void main(final String[] args) {
        test1();
        part1();
        part2();
    }
}
