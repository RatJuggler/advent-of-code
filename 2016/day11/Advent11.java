package day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;


class State implements Comparable<State> {

    final String[] floors;
    final int elevator;
    int priority;
    int steps;

    State(final String[] floors, final int elevator, final int steps) {
        this.floors = floors;
        this.elevator = elevator;
        this.steps = steps;
        // Number of empty positions on the ground floor (prefer) - Number of empty positions on the top floor (avoid)
        this.priority = (int) (this.floors[0].chars().filter(ch -> ch == '.').count() -
                this.floors[this.floors.length - 1].chars().filter(ch -> ch == '.').count());
    }

    static State newState(final State state, final String newFromFloor, final int newElevator, final String newToFloor) {
        String[] newFloors = state.floors.clone();
        newFloors[state.elevator] = newFromFloor;
        newFloors[newElevator] = newToFloor;
        return new State(newFloors, newElevator, state.steps + 1);
    }

    String getCurrentFloor() {
        return this.floors[this.elevator];
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
        StringBuilder column = new StringBuilder().append("Steps: ").append(this.steps)
                .append(", Priority: ").append(this.priority).append('\n');
        for (int i = this.floors.length; i > 0; i--) {
            column.append('F').append(i).append(i - 1 == this.elevator ? " E " : " . ").append(this.floors[i - 1]).append('\n');
        }
        return column.toString();
    }

    @Override
    public int compareTo(State state) {
        return Integer.compare(this.priority, state.priority);
    }
}


class StateSpaceSearch {

    private final Map<Integer, State> history = new HashMap<>();
    private int minimumSteps = Integer.MAX_VALUE;

    StateSpaceSearch() {}

    private boolean validState(final State state) {
        if (state.steps > this.minimumSteps) return false;
        if (state.isFinalState()) {
            if (state.steps < this.minimumSteps) this.minimumSteps = state.steps;
            return false;
        }
        int hash = state.hashCode();
        State found = history.get(hash);
        if (found == null) {
            this.history.put(hash, state);
        } else {
            if (state.steps >= found.steps) return false;
            found.steps = state.steps;
        }
        return true;
    }

    private List<String> generateElevators(final String fromFloor) {
        final String emptyElevator = ".. ".repeat(fromFloor.length() / 3);
        List<String> elevators = new ArrayList<>();
        for (int i = 0; i < fromFloor.length(); i += 3) {
            String component1 = fromFloor.substring(i, i + 2);
            // Ignore empty components.
            if (component1.equals("..")) continue;
            String elevator1 = emptyElevator.substring(0, i) + component1 + emptyElevator.substring(i + 2);
            elevators.add(elevator1);
            for (int j = i + 3; j < fromFloor.length(); j += 3) {
                String component2 = fromFloor.substring(j, j + 2);
                // Ignore empty components.
                if (component2.equals("..")) continue;
                // Microchip and Generator must have the same element to be moved together.
                if (component1.charAt(1) != component2.charAt(1) && component1.charAt(0) != component2.charAt(0)) continue;
                elevators.add(elevator1.substring(0, j) + component2 + elevator1.substring(j + 2));
            }
        }
        return elevators;
    }

    private boolean validFloor(final String floor) {
        boolean generatorPresent = false;
        boolean unshieldedMicrochipPresent = false;
        for (int i = 1; i < floor.length(); i += 3) {
            if (floor.charAt(i) == 'G') generatorPresent = true;
            if (floor.charAt(i) == 'M' && floor.charAt(i - 3) == '.') unshieldedMicrochipPresent = true;
        }
        return !(generatorPresent && unshieldedMicrochipPresent);
    }

    private String validElevatorMoveFrom(final String elevator, final String fromFloor) {
        String newFromFloor = fromFloor;
        for (int i = 0; i < elevator.length(); i += 3) {
            String moved = elevator.substring(i, i + 2);
            if (moved.equals("..")) continue;
            newFromFloor = newFromFloor.substring(0, i) + ".." + newFromFloor.substring(i + 2);
        }
        if (this.validFloor(newFromFloor))
            return newFromFloor;
        else
            return null;
    }

    private String validElevatorMoveTo(final String elevator, final String toFloor) {
        String newToFloor = toFloor;
        for (int i = 0; i < elevator.length(); i += 3) {
            String moved = elevator.substring(i, i + 2);
            if (moved.equals("..")) continue;
            newToFloor = newToFloor.substring(0, i) + moved + newToFloor.substring(i + 2);
        }
        if (this.validFloor(newToFloor))
            return newToFloor;
        else
            return null;
    }

    private List<State> generateNextStates(final State currentState, List<String> elevators) {
        List<State> nextStates = new ArrayList<>();
        for (String elevator : elevators) {
            String newFromFloor = this.validElevatorMoveFrom(elevator, currentState.getCurrentFloor());
            if (newFromFloor == null) continue;
            if (!currentState.elevatorOnTop()) {
                int up = currentState.elevator + 1;
                String newToFloor = this.validElevatorMoveTo(elevator, currentState.floors[up]);
                if (newToFloor != null)
                    nextStates.add(State.newState(currentState, newFromFloor, up, newToFloor));
            }
            if (!currentState.elevatorOnGround()) {
                int down = currentState.elevator - 1;
                String newToFloor = this.validElevatorMoveTo(elevator, currentState.floors[down]);
                if (newToFloor != null)
                    nextStates.add(State.newState(currentState, newFromFloor, down, newToFloor));
            }
        }
        return nextStates;
    }

    void begin(final State initialState) {
        Queue<State> nextStates = new PriorityQueue<>();
        nextStates.add(initialState);
        while (nextStates.peek() != null) {
            State state = nextStates.poll();
//            System.out.println("Queue: " + nextStates.size() + ", History: " + this.history.size() + ", Steps to solution: " + this.minimumSteps);
//            System.out.println(state);
            if (this.validState(state)) {
                List<String> elevators = this.generateElevators(state.getCurrentFloor());
                nextStates.addAll(this.generateNextStates(state, elevators));
            }
            if (state.isFinalState()) {
                System.out.println("Queue: " + nextStates.size() + ", History: " + this.history.size() + ", Steps to solution: " + this.minimumSteps);
            }
        }
    }

}


public class Advent11 {

    private static void test() {
        String[] floors = new String[4];
        floors[3] = ".. .. .. .. ";
        floors[2] = ".. .. LG .. ";
        floors[1] = "HG .. .. .. ";
        floors[0] = ".. HM .. LM ";
        State initial = new State(floors, 0, 0);
        StateSpaceSearch column = new StateSpaceSearch();
        column.begin(initial);
    }

    private static void part1() {
        String[] floors = new String[4];
        floors[3] = ".. .. .. .. .. .. .. .. .. .. ";
        floors[2] = ".. .. .. .. .. .. .. .. .. .. ";
        floors[1] = ".. .. .. OM .. PM .. .. .. .. ";
        floors[0] = "CG CM OG .. PG .. RG RM TG TM ";
        State initial = new State(floors, 0, 0);
        StateSpaceSearch column = new StateSpaceSearch();
        column.begin(initial);
    }

    private static void part2() {
        String[] floors = new String[4];
        floors[3] = ".. .. .. .. .. .. .. .. .. .. .. .. .. .. ";
        floors[2] = ".. .. .. .. .. .. .. .. .. .. .. .. .. .. ";
        floors[1] = ".. .. .. OM .. PM .. .. .. .. .. .. .. .. ";
        floors[0] = "CG CM OG .. PG .. RG RM TG TM EG EM DG DM ";
        State initial = new State(floors, 0, 0);
        StateSpaceSearch column = new StateSpaceSearch();
        column.begin(initial);
    }

    public static void main(final String[] args) {
        test();
        part1();
        part2();
    }

}
