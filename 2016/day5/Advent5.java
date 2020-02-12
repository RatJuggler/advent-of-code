package day5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Advent5 {

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    private static String bytesToHex(final byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static String hackPasswordSequential(final String doorId) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        StringBuilder password = new StringBuilder("********");
        int index = 0;
        int position;
        String hash;
        for (int i = 0; i < 8; i++) {
            do {
                String toHash = doorId + index++;
                md.update(toHash.getBytes());
                byte[] digest = md.digest();
                hash = bytesToHex(digest);
                position = i;
            } while (!hash.startsWith("00000") || (hash.startsWith("00000") && (position < 0 || position > 7 || password.charAt(position) != '*')));
            System.out.println(hash);
            password.replace(position, position + 1, String.valueOf(hash.charAt(5)));
        }
        return password.toString();
    }

    private static String hackPasswordPositional(final String doorId) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        StringBuilder password = new StringBuilder("********");
        int index = 0;
        int position;
        String hash;
        for (int i = 0; i < 8; i++) {
            do {
                String toHash = doorId + index++;
                md.update(toHash.getBytes());
                byte[] digest = md.digest();
                hash = bytesToHex(digest);
                position = hash.charAt(5) - '0';
            } while (!hash.startsWith("00000") || (hash.startsWith("00000") && (position < 0 || position > 7 || password.charAt(position) != '*')));
            System.out.println(hash);
            password.replace(position, position + 1, String.valueOf(hash.charAt(6)));
        }
        return password.toString();
    }

    private static void testHackPasswordSequential() throws NoSuchAlgorithmException {
        String doorId = "abc";
        String expectedPassword = "18f47a30";
        String password = hackPasswordSequential(doorId);
        assert password.equals(expectedPassword) : String.format("Expected password '%s' but was '%s'!", expectedPassword, password);
    }

    private static void testHackPasswordPositional() throws NoSuchAlgorithmException {
        String doorId = "abc";
        String expectedPassword = "05ace8e3";
        String password = hackPasswordPositional(doorId);
        assert password.equals(expectedPassword) : String.format("Expected password '%s' but was '%s'!", expectedPassword, password);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        testHackPasswordSequential();
        String password1 = hackPasswordSequential("abbhdwsy");
        System.out.println(String.format("Day 5, Part 1 the password is '%s'.", password1));
        testHackPasswordPositional();
        String password2 = hackPasswordPositional("abbhdwsy");
        System.out.println(String.format("Day 5, Part 2 the password is '%s'.", password2));
    }

}
