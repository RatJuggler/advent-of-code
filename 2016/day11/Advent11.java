package day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Floor {

    private final int number;
    private final List<String> contains;

    Floor(final int number, final int capacity) {
        this.number = number;
        this.contains = new ArrayList<>(capacity);
    }

    public void addComponent(String component) {
        this.contains.add(component);
    }

    @Override
    public String toString() {
        return "Floor-" + number + "{contains=" + contains + '}';
    }

}

class Column {

    private final Floor[] floors;

    Column(final int floors, final int capacity) {
        this.floors = new Floor[floors];
        for (int i = 0; i < floors; i++) {
            this.floors[i] = new Floor(i, capacity);
        }
    }

    void addComponent(final int floor, final String component) {
        this.floors[floor].addComponent(component);
    }

    @Override
    public String toString() {
        return "Column{" +
                "floors=" + Arrays.toString(floors) +
                '}';
    }
}

public class Advent11 {

    private static void testColumn() {
        Column column = new Column(4, 4);
        column.addComponent(0, "HM");
        column.addComponent(0, "LM");
        column.addComponent(1, "HG");
        column.addComponent(2, "LG");
        System.out.println(column);
    }

    private static void part1Column() {
        Column column = new Column(4, 4);
        column.addComponent(0, "POG");
        column.addComponent(0, "TG");
        column.addComponent(0, "TM");
        column.addComponent(0, "PRG");
        column.addComponent(0, "RG");
        column.addComponent(0, "RM");
        column.addComponent(0, "CG");
        column.addComponent(0, "CM");
        column.addComponent(1, "POM");
        column.addComponent(1, "PRM");
        System.out.println(column);
    }

    public static void main(final String[] args) {
        testColumn();
        part1Column();
    }

}
