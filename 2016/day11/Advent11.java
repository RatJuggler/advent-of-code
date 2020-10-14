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
        for (int i = 0; i < oldFloor.length() / 3; i++) {
            int component1At = i * 3;
            String component1 = oldFloor.substring(component1At, component1At + 2);
            if (component1.equals("..")) continue;
            // Microchip moved to the same floor as an incompatible Generator OR Generator moved to the same floor as an incompatible Microchip.
            if (this.validMove(component1, newFloor, 'M', "G") ||
                    this.validMove(component1, newFloor, 'G', "M")) {
                String[] newFloors1 = this.floors.clone();
                String oldFloor1 = oldFloor.substring(0, component1At) + ".." + oldFloor.substring(component1At + 2);
                String newFloor1 = newFloor.substring(0, component1At) + component1 + newFloor.substring(component1At + 2);
                newFloors1[this.elevator] = oldFloor1;
                newFloors1[newElevator] = newFloor1;
                Column newColumn1 = new Column(newFloors1, newElevator);
                if (Column.newState(newColumn1)) newColumn1.move();
                for (int j = 0; j < oldFloor1.length() / 3; j++) {
                    int component2At = j * 3;
                    String component2 = oldFloor1.substring(component2At, component2At + 2);
                    if (component2.equals("..")) continue;
                    // Microchip and Generator must have the same element to be move together.
                    if (component1.charAt(1) != component2.charAt(1) && component1.charAt(0) != component2.charAt(0)) continue;
                    // Microchip moved to the same floor as an incompatible Generator OR Generator moved to the same floor as an incompatible Microchip.
                    if (component1.charAt(0) == component2.charAt(0) ||
                            this.validMove(component2, newFloor, 'M', "G") ||
                            this.validMove(component2, newFloor, 'G', "M")) {
                        String[] newFloors2 = newFloors1.clone();
                        String oldFloor2 = oldFloor1.substring(0, component2At) + ".." + oldFloor1.substring(component2At + 2);
                        String newFloor2 = newFloor1.substring(0, component2At) + component2 + newFloor1.substring(component2At + 2);
                        newFloors2[this.elevator] = oldFloor2;
                        newFloors2[newElevator] = newFloor2;
                        Column newColumn2 = new Column(newFloors2, newElevator);
                        if (Column.newState(newColumn2)) newColumn2.move();
                    }
                }
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
            column.append('F').append(i).append(i - 1 == this.elevator ? " E " : " . ").append(this.floors[i - 1]).append('\n');
        }
        return column.toString();
    }
}

public class Advent11 {

    private static void testColumn() {
        String[] floors = new String[4];
        floors[3] = ".. .. .. .. ";
        floors[2] = ".. .. LG .. ";
        floors[1] = "HG .. .. .. ";
        floors[0] = ".. HM .. LM ";
        Column column = new Column(floors, 0);
        if (Column.newState(column)) column.move();
        Column.dump();
    }


    private static void part1Column() {
        String[] floors = new String[4];
        floors[3] = ".. .. .. .. .. .. .. .. .. .. ";
        floors[2] = ".. .. .. .. .. .. .. .. .. .. ";
        floors[1] = ".. .. .. OM .. PM .. .. .. .. ";
        floors[0] = "CG CM OG .. PG .. RG RM TG TM ";
        Column column = new Column(floors, 0);
        if (Column.newState(column)) column.move();
        Column.dump();
    }

    public static void main(final String[] args) {
        testColumn();
//        part1Column();
    }

}
