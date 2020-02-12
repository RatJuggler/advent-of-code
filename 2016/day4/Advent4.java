package day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;


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

    private Map<Character, Long> countLetterOccurrences(final String string, final int limit) {
        return string.codePoints()
                .mapToObj(c -> (char) c)
                .filter(c -> String.valueOf(c).matches("[a-z]"))
                .collect(groupingBy(c -> c, counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Character, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    boolean isARealRoom() {
        Map<Character, Long> occurrences = countLetterOccurrences(this.name, this.checksum.length());
        return this.checksum.codePoints().mapToObj(c -> (char) c).allMatch(occurrences::containsKey);
    }

    long sectorIdValue() {
        return Long.parseLong(this.sectorId);
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

    private static Long sumRealRoomSectorIds() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get("2016/day4/input4.txt"))) {
            return stream.map(RoomValidator::new).filter(RoomValidator::isARealRoom).map(RoomValidator::sectorIdValue).reduce(0L, Long::sum);
        }
    }

    private static void testIsARealRoom(final String room, final boolean expectedRealRoom) {
        boolean realRoom = isARealRoom(room);
        assert realRoom == expectedRealRoom: String.format("Expected result %s but was %s!", expectedRealRoom, realRoom);
    }

    public static void main(final String[] args) throws IOException {
        testIsARealRoom("aaaaa-bbb-z-y-x-123[abxyz]", true);
        testIsARealRoom("a-b-c-d-e-f-g-h-987[abcde]", true);
        testIsARealRoom("not-a-real-room-404[oarel]", true);
        testIsARealRoom("totally-real-room-200[decoy]", false);
        System.out.println(String.format("Day 4, Step 1, sum of sector id's for real rooms is %d.", sumRealRoomSectorIds()));
    }

}
