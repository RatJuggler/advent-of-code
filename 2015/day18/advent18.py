from copy import deepcopy
from typing import List


class LightGrid:

    def __init__(self, grid: List[List[int]]) -> None:
        self.grid = grid
        self.grid_size = len(grid)
        self.stuck_lights = []

    @classmethod
    def from_file(cls, filename: str):
        grid = []
        with open(filename) as fh:
            for line in fh:
                grid_row = []
                for c in line.strip():
                    grid_row.append(0 if c == '.' else 1)
                grid.append(grid_row)
        return LightGrid(grid)

    def set_stuck_light(self, x: int, y: int, stuck_state: int) -> None:
        self.grid[y][x] = stuck_state
        self.stuck_lights.append((x, y))

    def set_light(self, x: int, y: int, new_light) -> None:
        if (y, x) not in self.stuck_lights:
            self.grid[y][x] = new_light

    def get_light(self, x: int, y: int) -> int:
        if x < 0 or y < 0:
            return 0
        if x >= self.grid_size or y >= self.grid_size:
            return 0
        return self.grid[y][x]

    def next_light_state(self, x: int, y: int) -> int:
        surrounding_lights = 0
        for sy in range(y - 1, y + 2):
            for sx in range(x - 1, x + 2):
                if sx != x or sy != y:
                    surrounding_lights += self.get_light(sx, sy)
        if self.get_light(x, y) == 1:
            return 1 if surrounding_lights in (2, 3) else 0
        else:
            return 1 if surrounding_lights == 3 else 0

    def count_lights_on(self) -> int:
        lights_lit = 0
        for grid_row in self.grid:
            for light in grid_row:
                lights_lit += light
        return lights_lit


def animate_light_grid(filename: str, steps: int, add_stuck_lights: bool) -> int:
    light_grid = LightGrid.from_file(filename)
    if add_stuck_lights:
        light_grid.set_stuck_light(0, 0, 1)
        light_grid.set_stuck_light(light_grid.grid_size - 1, 0, 1)
        light_grid.set_stuck_light(0, light_grid.grid_size - 1, 1)
        light_grid.set_stuck_light(light_grid.grid_size - 1, light_grid.grid_size - 1, 1)
    for i in range(steps):
        new_light_grid = deepcopy(light_grid)
        for y in range(light_grid.grid_size):
            for x in range(light_grid.grid_size):
                new_light_grid.set_light(x, y, light_grid.next_light_state(x, y))
        light_grid = new_light_grid
    return light_grid.count_lights_on()


def test_animate_light_grid(filename: str, add_stuck_lights: bool, steps: int, expected_lights_on: int) -> None:
    lights_on = animate_light_grid(filename, steps, add_stuck_lights)
    assert lights_on == expected_lights_on, \
        'Expected to find {0} lights on after {1} steps but was {2}!'.format(expected_lights_on, steps, lights_on)


def main() -> None:
    test_animate_light_grid('test18a.txt', False, 4, 4)
    steps = 100
    light_count = animate_light_grid('input18.txt', steps, False)
    print('Day 18, Step 1 after {0} steps there are {1} lights on.'.format(steps, light_count))
    test_animate_light_grid('test18a.txt', True, 5, 17)
    light_count = animate_light_grid('input18.txt', steps, True)
    print('Day 18, Step 2 with stuck lights after {0} steps there are {1} lights on.'.format(steps, light_count))


if __name__ == '__main__':
    main()
