package day11;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class Column {

    private static final List<Column> history = new ArrayList<>();

    private final String[] floors;
    private final int elevator;

    Column(final String[] floors, final int elevator) {
        this.floors = floors;
        this.elevator = elevator;
    }

    static boolean newState(final Column newColumn) {
        for (Column column : Column.history) {
            if (newColumn.equals(column)) return false;
        }
        Column.history.add(newColumn);
        return true;
    }

    static void dump() {
        for (Column column : Column.history) {
            System.out.println(column);
        }
    }
    private boolean validMove(final String component, final String floor, final char componentMoved, final String otherComponent) {
        return component.charAt(1) == componentMoved && (!floor.contains(otherComponent) || floor.contains(component.charAt(0) + otherComponent));
    }

    private void moveComponent(final int newElevator) {
        String oldFloor = this.floors[this.elevator];
        String newFloor = this.floors[newElevator];
        for (int i = 0; i < (oldFloor.length() - 4) / 3; i++) {
            int componentAt = 5 + (i * 3);
            String component = oldFloor.substring(componentAt, componentAt + 2);
            if (component.equals("..")) continue;
            // Microchip moved to the same floor as an incompatible Generator OR Generator moved to the same floor as an incompatible Microchip.
            if (this.validMove(component, newFloor, 'M', "G") ||
                    this.validMove(component, newFloor, 'G', "M")) {
                String[] newFloors = this.floors.clone();
                newFloors[this.elevator] = oldFloor.substring(0, 3) + "." + oldFloor.substring(4, componentAt) + ".." + oldFloor.substring(componentAt + 2);
                newFloors[newElevator] = newFloor.substring(0, 3) + "E" + newFloor.substring(4, componentAt) + component + newFloor.substring(componentAt + 2);
                Column newColumn = new Column(newFloors, newElevator);
                if (Column.newState(newColumn)) newColumn.move();
            }
        }
    }

    void move() {
        if (this.floors[this.floors.length - 1].contains("..")) {
            if (this.elevator > 0) this.moveComponent(this.elevator - 1);
            if (this.elevator < this.floors.length - 1) this.moveComponent(this.elevator + 1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return elevator == column.elevator &&
                Arrays.equals(floors, column.floors);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(elevator);
        result = 31 * result + Arrays.hashCode(floors);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder column = new StringBuilder();
        for (int i = this.floors.length; i > 0; i--) {
            column.append(this.floors[i - 1]).append('\n');
        }
        return column.toString();
    }
}

public class Advent11 {

    private static void testColumn() {
        String[] floors = new String[4];
        floors[3] = "F4 . .. .. .. ..";
        floors[2] = "F3 . .. .. LG ..";
        floors[1] = "F2 . HG .. .. ..";
        floors[0] = "F1 E .. HM .. LM";
        Column column = new Column(floors, 0);
        if (Column.newState(column)) column.move();
        Column.dump();
    }


    private static void part1Column() {
        String[] floors = new String[4];
        floors[3] = "F4 . .. .. .. .. .. .. .. .. .. ..";
        floors[2] = "F3 . .. .. .. .. .. .. .. .. .. ..";
        floors[1] = "F2 . .. .. .. OM .. PM .. .. .. ..";
        floors[0] = "F1 E CG CM OG .. PG .. RG RM TG TM";
        Column column = new Column(floors, 0);
        if (Column.newState(column)) column.move();
        Column.dump();
    }

    public static void main(final String[] args) {
        testColumn();
//        part1Column();
    }

}
