package day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class State {

    final String[] floors;
    final int elevator;

    State(final String[] floors, final int elevator) {
        this.floors = floors;
        this.elevator = elevator;
    }

    static State newState(final State currentState, final int newElevator, final int componentAt, final String component) {
        String[] newFloors = currentState.floors.clone();
        newFloors[currentState.elevator] =
                newFloors[currentState.elevator].substring(0, componentAt) + ".." + newFloors[currentState.elevator].substring(componentAt + 2);
        newFloors[newElevator] =
                newFloors[newElevator].substring(0, componentAt) + component + newFloors[newElevator].substring(componentAt + 2);
        return new State(newFloors, newElevator);
    }

    boolean finalState() {
        return !this.floors[this.floors.length - 1].contains("..");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return this.elevator == state.elevator && Arrays.equals(this.floors, state.floors);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.elevator);
        result = 31 * result + Arrays.hashCode(this.floors);
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

class Column {

    private final List<State> history = new ArrayList<>();

    boolean isNewState(final State newState) {
        for (State state : this.history) {
            if (newState.equals(state)) return false;
        }
        this.history.add(newState);
        return true;
    }

    void dump() {
        for (State state : this.history) {
            System.out.println(state);
        }
    }

    private boolean validGeneratorMove(final String component, final String floor) {
        return component.charAt(1) == 'G' && (!floor.contains("M") || floor.contains(component.charAt(0) + "M"));
    }

    private boolean validMicrochipMove(final String component, final String floor) {
        return component.charAt(1) == 'M' && (!floor.contains("G") || floor.contains(component.charAt(0) + "G"));
    }

    private void moveComponent(final State state, final int newElevator) {
        String oldFloor = state.floors[state.elevator];
        String newFloor = state.floors[newElevator];
        for (int i = 0; i < oldFloor.length() / 3; i++) {
            int component1At = i * 3;
            String component1 = oldFloor.substring(component1At, component1At + 2);
            if (component1.equals("..")) continue;
            // Microchip moved to the same floor as an incompatible Generator OR Generator moved to the same floor as an incompatible Microchip.
            if (this.validGeneratorMove(component1, newFloor) || this.validMicrochipMove(component1, newFloor)) {
                State newState1 = State.newState(state, newElevator, component1At, component1);
                if (this.isNewState(newState1)) this.move(newState1);
                String oldFloor1 = newState1.floors[newState1.elevator];
                for (int j = 0; j < oldFloor1.length() / 3; j++) {
                    int component2At = j * 3;
                    String component2 = oldFloor1.substring(component2At, component2At + 2);
                    if (component2.equals("..")) continue;
                    // Microchip and Generator must have the same element to be move together.
                    if (component1.charAt(1) != component2.charAt(1) && component1.charAt(0) != component2.charAt(0)) continue;
                    // Microchip moved to the same floor as an incompatible Generator OR Generator moved to the same floor as an incompatible Microchip.
                    if (component1.charAt(0) == component2.charAt(0) ||
                            this.validGeneratorMove(component2, newFloor) ||
                            this.validMicrochipMove(component2, newFloor)) {
                        State newState2 = State.newState(newState1, newElevator, component2At, component2);
                        if (this.isNewState(newState2)) this.move(newState2);
                    }
                }
            }
        }
    }

    void move(final State state) {
        if (!state.finalState()) {
            if (state.elevator > 0) this.moveComponent(state, state.elevator - 1);
            if (state.elevator < state.floors.length - 1) this.moveComponent(state,state.elevator + 1);
        }
    }

}

public class Advent11 {

    private static void testColumn() {
        String[] floors = new String[4];
        floors[3] = ".. .. .. .. ";
        floors[2] = ".. .. LG .. ";
        floors[1] = "HG .. .. .. ";
        floors[0] = ".. HM .. LM ";
        State initial = new State(floors, 0);
        Column column = new Column();
        if (column.isNewState(initial)) column.move(initial);
        column.dump();
    }


    private static void part1Column() {
        String[] floors = new String[4];
        floors[3] = ".. .. .. .. .. .. .. .. .. .. ";
        floors[2] = ".. .. .. .. .. .. .. .. .. .. ";
        floors[1] = ".. .. .. OM .. PM .. .. .. .. ";
        floors[0] = "CG CM OG .. PG .. RG RM TG TM ";
        State initial = new State(floors, 0);
        Column column = new Column();
        if (column.isNewState(initial)) column.move(initial);
        column.dump();
    }

    public static void main(final String[] args) {
        testColumn();
//        part1Column();
    }

}
