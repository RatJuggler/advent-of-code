package day5;


public class Advent5 {

    private static String hackPassword(String doorId) {
        return doorId;
    }

    private static void testHackPassword() {
        String doorId = "abc";
        String expectedPassword = "18f47a30";
        String password = hackPassword(doorId);
        assert password.equals(expectedPassword) : String.format("Expected password '%s' but was '%s'!", expectedPassword, password);
    }

    public static void main(String[] args) {
        testHackPassword();
    }

}
