package day4;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


class RoomValidator {

    final String name;
    final String sectorId;
    final String checksum;

    RoomValidator(final String room) {
        String pattern = "(?<name>[a-z\\-]+)-(?<sectorid>\\d+)\\[(?<checksum>[a-z]+)]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(room);
        if (!m.find()) {
            throw new IllegalStateException("Unable to parse room: " + room);
        }
        this.name = m.group("name");
        this.sectorId = m.group("sectorid");
        this.checksum = m.group("checksum");
    }

    boolean isARealRoom() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("RoomValidator{name='%s', sectorId='%s', checksum='%s'}", this.name, this.sectorId, this.checksum);
    }
}

public class Advent4 {

    private static boolean isARealRoom(String room) {
        RoomValidator roomValidator = new RoomValidator(room);
        System.out.println(String.format("%s -> %s", room, roomValidator));
        return roomValidator.isARealRoom();
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
