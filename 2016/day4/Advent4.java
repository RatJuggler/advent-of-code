package day4;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Advent4 {

    private static boolean isARealRoom(String room) {
        String pattern = "(?<name>[a-z\\-]+)-(?<sectorid>\\d+)\\[(?<checksum>[a-z]+)]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(room);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse room: " + room);
        }
        String name = m.group("name");
        String sectorId = m.group("sectorid");
        String checksum = m.group("checksum");
        System.out.println(String.format("%s -> %s %s %s", room, name, sectorId, checksum));
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
