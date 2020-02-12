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

    private enum PasswordCoding {
        SEQUENTIAL,
        POSITIONAL
    }

    private static String hackPassword(final String doorId, final PasswordCoding coding) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        StringBuilder password = new StringBuilder();
        int index = 0;
        String hash;
        for (int i = 0; i < 8; i++) {
            do {
                String toHash = doorId + index;
                index++;
                md.update(toHash.getBytes());
                byte[] digest = md.digest();
                hash = bytesToHex(digest);
            } while (!hash.startsWith("00000"));
            System.out.println(hash);
            if (coding == PasswordCoding.SEQUENTIAL) {
                password.append(hash.charAt(5));
            } else {

            }
        }
        return password.toString();
    }

    private static void testHackPassword(final String expectedPassword, final PasswordCoding coding) throws NoSuchAlgorithmException {
        String doorId = "abc";
        String password = hackPassword(doorId, coding);
        assert password.equals(expectedPassword) : String.format("Expected password '%s' but was '%s'!", expectedPassword, password);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        testHackPassword("18f47a30", PasswordCoding.SEQUENTIAL);
        String password = hackPassword("abbhdwsy", PasswordCoding.SEQUENTIAL);
        System.out.println(String.format("Day 5, Part 1 the password is '%s'.", password));
        testHackPassword("05ace8e3", PasswordCoding.POSITIONAL);
    }

}
