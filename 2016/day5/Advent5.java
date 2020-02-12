package day5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Advent5 {

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    private static String hackPassword(String doorId) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        StringBuilder password = new StringBuilder();
        int index = 0;
        String hash = null;
        for (int i = 0; i < 8; i++) {
            do {
                String toHash = doorId + index;
                index++;
                md.update(toHash.getBytes());
                byte[] digest = md.digest();
                hash = bytesToHex(digest);
            } while (!hash.startsWith("00000"));
            System.out.println(hash);
            password.append(hash.charAt(5));
        }
        return password.toString();
    }

    private static void testHackPassword() throws NoSuchAlgorithmException {
        String doorId = "abc";
        String expectedPassword = "18f47a30";
        String password = hackPassword(doorId);
        assert password.equals(expectedPassword) : String.format("Expected password '%s' but was '%s'!", expectedPassword, password);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        testHackPassword();
    }

}
