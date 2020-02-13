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
        return String.valueOf(hexChars);
    }

    private static String hackPassword(final String doorId, final boolean sequential) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        char[] password = new char[] {'*', '*', '*', '*', '*', '*', '*', '*'};
        int index = 0;
        int position;
        char replace_with;
        String hash;
        for (int i = 0; i < password.length; i++) {
            do {
                String toHash = doorId + index++;
                md.update(toHash.getBytes());
                byte[] digest = md.digest();
                hash = bytesToHex(digest);
                if (sequential) {
                    position = i;
                    replace_with = hash.charAt(5);
                } else {
                    position = hash.charAt(5) - '0';
                    replace_with = hash.charAt(6);
                }
            } while (!hash.startsWith("00000") || (hash.startsWith("00000") && (position < 0 || position > 7 || password[position] != '*')));
            password[position] = replace_with;
            System.out.println(String.valueOf(password));
        }
        return String.valueOf(password);
    }

    private static void testHackPasswordSequential() throws NoSuchAlgorithmException {
        String doorId = "abc";
        String expectedPassword = "18f47a30";
        String password = hackPassword(doorId, true);
        assert password.equals(expectedPassword) : String.format("Expected password '%s' but was '%s'!", expectedPassword, password);
    }

    private static void testHackPasswordPositional() throws NoSuchAlgorithmException {
        String doorId = "abc";
        String expectedPassword = "05ace8e3";
        String password = hackPassword(doorId, false);
        assert password.equals(expectedPassword) : String.format("Expected password '%s' but was '%s'!", expectedPassword, password);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        testHackPasswordSequential();
        String inputDoorId = "abbhdwsy";
        String password1 = hackPassword(inputDoorId, true);
        System.out.println(String.format("Day 5, Part 1 the password is '%s'.", password1));
        testHackPasswordPositional();
        String password2 = hackPassword(inputDoorId, false);
        System.out.println(String.format("Day 5, Part 2 the password is '%s'.", password2));
    }

}
