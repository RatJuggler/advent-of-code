package day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class State {

    final String[] floors;
    final int elevator;
    int steps;

    State(final String[] floors, final int elevator, final int steps) {
        this.floors = floors;
        this.elevator = elevator;
        this.steps = steps;
    }

    static State newState(final State state, final int elevator, final int newElevator, final int componentAt, final String component, final int steps) {
        String[] newFloors = state.floors.clone();
        newFloors[elevator] =
                newFloors[elevator].substring(0, componentAt) + ".." + newFloors[elevator].substring(componentAt + 2);
        newFloors[newElevator] =
                newFloors[newElevator].substring(0, componentAt) + component + newFloors[newElevator].substring(componentAt + 2);
        return new State(newFloors, newElevator, steps);
    }

    boolean isFinalState() {
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
        StringBuilder column = new StringBuilder().append("Steps: ").append(this.steps).append('\n');
        for (int i = this.floors.length; i > 0; i--) {
            column.append('F').append(i).append(i - 1 == this.elevator ? " E " : " . ").append(this.floors[i - 1]).append('\n');
        }
        return column.toString();
    }
}

class StateSpaceSearch {

    private final Map<Integer, State> history = new HashMap<>();
    private int minimumSteps = Integer.MAX_VALUE;

    StateSpaceSearch() {}

    boolean isNewState(final State newState) {
        if (newState.steps > this.minimumSteps) return false;
        if (newState.isFinalState()) {
            if (newState.steps < this.minimumSteps) this.minimumSteps = newState.steps;
            return false;
        }
        State found = history.get(newState.hashCode());
        if (found == null) {
            this.history.put(newState.hashCode(), newState);
        } else {
            if (newState.steps >= found.steps) return false;
            found.steps = newState.steps;
        }
        return true;
    }

    private boolean validGeneratorMove(final String component, final String floor) {
        return component.charAt(1) == 'G' && (!floor.contains("M") || floor.contains(component.charAt(0) + "M"));
    }

    private boolean validMicrochipMove(final String component, final String floor) {
        return component.charAt(1) == 'M' && (!floor.contains("G") || floor.contains(component.charAt(0) + "G"));
    }

    private List<State> componentMoves(final State state, final int elevator, final int newElevator) {
        List<State> nextStates = new ArrayList<>();
        String oldFloor = state.floors[elevator];
        String newFloor1 = state.floors[newElevator];
        for (int i = 0; i < oldFloor.length(); i += 3) {
            String component1 = oldFloor.substring(i, i + 2);
            // Ignore empty components.
            if (component1.equals("..")) continue;
            // Microchip moved to the same floor as an incompatible Generator OR Generator moved to the same floor as an incompatible Microchip.
            if (this.validGeneratorMove(component1, newFloor1) || this.validMicrochipMove(component1, newFloor1)) {
                State newState1 = State.newState(state, elevator, newElevator, i, component1, state.steps + 1);
                nextStates.add(newState1);
                for (int j = i; j < oldFloor.length(); j += 3) {
                    String component2 = oldFloor.substring(j, j + 2);
                    // Ignore empty components.
                    if (component2.equals("..")) continue;
                    // Microchip and Generator must have the same element to be moved together.
                    if (component1.charAt(1) != component2.charAt(1) && component1.charAt(0) != component2.charAt(0)) continue;
                    // Microchip moved to the same floor as an incompatible Generator OR Generator moved to the same floor as an incompatible Microchip.
                    String newFloor2 = newState1.floors[newElevator];
                    if (component1.charAt(0) == component2.charAt(0) ||
                            this.validGeneratorMove(component2, newFloor2) || this.validMicrochipMove(component2, newFloor2)) {
                        State newState2 = State.newState(newState1, elevator, newElevator, j, component2, newState1.steps);
                        nextStates.add(newState2);
                    }
                }
            }
        }
        return nextStates;
    }

    void begin(final State state) {
        List<State> newStates = new ArrayList<>();
        if (this.isNewState(state)) {
            if (!state.elevatorOnGround()) newStates.addAll(this.componentMoves(state, state.elevator, state.elevator - 1));
            if (!state.elevatorOnTop()) newStates.addAll(this.componentMoves(state, state.elevator, state.elevator + 1));
        }
        if (state.isFinalState()) {
            System.out.println(state);
            System.out.println(this.minimumSteps);
        }
        for (State nextState : newStates) {
            this.begin(nextState);
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
        State initial = new State(floors, 0, 0);
        StateSpaceSearch column = new StateSpaceSearch();
        column.begin(initial);
    }


    private static void part1Column() {
        String[] floors = new String[4];
        floors[3] = ".. .. .. .. .. .. .. .. .. .. ";
        floors[2] = ".. .. .. .. .. .. .. .. .. .. ";
        floors[1] = ".. .. .. OM .. PM .. .. .. .. ";
        floors[0] = "CG CM OG .. PG .. RG RM TG TM ";
        State initial = new State(floors, 0, 0);
        StateSpaceSearch column = new StateSpaceSearch();
        column.begin(initial);
    }

    public static void main(final String[] args) {
        testColumn();
        part1Column();
    }

}
