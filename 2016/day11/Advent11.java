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

    static State newState(final State state, final int elevator, final int newElevator, final int componentAt, final String component) {
        String[] newFloors = state.floors.clone();
        newFloors[elevator] =
                newFloors[elevator].substring(0, componentAt) + ".." + newFloors[elevator].substring(componentAt + 2);
        newFloors[newElevator] =
                newFloors[newElevator].substring(0, componentAt) + component + newFloors[newElevator].substring(componentAt + 2);
        return new State(newFloors, newElevator);
    }

    boolean finalState() {
        return !this.floors[this.floors.length - 1].contains("..");
    }

    boolean elevatorOnGround() {
        return this.elevator == 0;
    }

    boolean elevatorOnTop() {
        return this.elevator == this.floors.length - 1;
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

class StateSpaceSearch {

    private final List<State> history = new ArrayList<>();

    StateSpaceSearch() {}

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

    private void moveComponents(final State state, final int elevator, final int newElevator) {
        String oldFloor = state.floors[elevator];
        String newFloor = state.floors[newElevator];
        for (int i = 0; i < oldFloor.length() / 3; i++) {
            int component1At = i * 3;
            String component1 = oldFloor.substring(component1At, component1At + 2);
            if (component1.equals("..")) continue;
            // Microchip moved to the same floor as an incompatible Generator OR Generator moved to the same floor as an incompatible Microchip.
            if (this.validGeneratorMove(component1, newFloor) || this.validMicrochipMove(component1, newFloor)) {
                State newState1 = State.newState(state, elevator, newElevator, component1At, component1);
                this.begin(newState1);
                for (int j = i; j < oldFloor.length() / 3; j++) {
                    int component2At = j * 3;
                    String component2 = oldFloor.substring(component2At, component2At + 2);
                    if (component2.equals("..") || component2.equals(component1)) continue;
                    // Microchip and Generator must have the same element to be move together.
                    if (component1.charAt(1) != component2.charAt(1) && component1.charAt(0) != component2.charAt(0)) continue;
                    // Microchip moved to the same floor as an incompatible Generator OR Generator moved to the same floor as an incompatible Microchip.
                    String newFloor1 = newState1.floors[newElevator];
                    if (component1.charAt(0) == component2.charAt(0) ||
                            this.validGeneratorMove(component2, newFloor1) ||
                            this.validMicrochipMove(component2, newFloor1)) {
                        State newState2 = State.newState(newState1, elevator, newElevator, component2At, component2);
                        this.begin(newState2);
                    }
                }
            }
        }
    }

    void begin(final State state) {
        if (this.isNewState(state) && !state.finalState()) {
            if (!state.elevatorOnGround()) this.moveComponents(state, state.elevator, state.elevator - 1);
            if (!state.elevatorOnTop()) this.moveComponents(state,state.elevator, state.elevator + 1);
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
        StateSpaceSearch column = new StateSpaceSearch();
        column.begin(initial);
        column.dump();
    }


    private static void part1Column() {
        String[] floors = new String[4];
        floors[3] = ".. .. .. .. .. .. .. .. .. .. ";
        floors[2] = ".. .. .. .. .. .. .. .. .. .. ";
        floors[1] = ".. .. .. OM .. PM .. .. .. .. ";
        floors[0] = "CG CM OG .. PG .. RG RM TG TM ";
        State initial = new State(floors, 0);
        StateSpaceSearch column = new StateSpaceSearch();
        column.begin(initial);
        column.dump();
    }

    public static void main(final String[] args) {
        testColumn();
//        part1Column();
    }

}
