package day4;


public class Advent4 {

    private static boolean isARealRoom(String room) {
        return false;
    }

    private static void testIsARealRoom(final String room, final boolean expectedRealRoom) {
        boolean realRoom = isARealRoom(room);
        assert realRoom == expectedRealRoom: String.format("Expected result %s but was %s!", expectedRealRoom, realRoom);
    }

    public static void main(final String[] args) {
        testIsARealRoom("aaaaa-bbb-z-y-x-123[abxyz]", true);
        testIsARealRoom("a-b-c-d-e-f-g-h-987[abcde]", true);
        testIsARealRoom("not-a-real-room-404[oarel]", true);
        testIsARealRoom("totally-real-room-200[decoy]", false);
    }

}
