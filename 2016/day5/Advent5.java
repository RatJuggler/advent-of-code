package day5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Advent5 {

    private static String bytesToHex(final byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1));
        }
        return sb.toString();
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
        System.out.printf("Day 5, Part 1 the password is '%s'.%n", password1);
        testHackPasswordPositional();
        String password2 = hackPassword(inputDoorId, false);
        System.out.printf("Day 5, Part 2 the password is '%s'.%n", password2);
    }

}
