from copy import deepcopy
from typing import List


class LightGrid:

    def __init__(self, grid: List[List[int]]) -> None:
        self.grid = grid

    @classmethod
    def from_empty(cls, grid_size: int):
        grid = []
        for y in range(grid_size):
            grid_row = [0] * grid_size
            grid.append(grid_row)
        return LightGrid(grid)

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

    def size(self) -> int:
        return len(self.grid)

    def animate(self, new_light_grid):
        pass

    def count_lights_on(self) -> int:
        lights_lit = 0
        for grid_row in self.grid:
            for light in grid_row:
                lights_lit += light
        return lights_lit


def animate_light_grid(filename: str, steps: int) -> int:
    light_grid = LightGrid.from_file(filename)
    for i in range(steps):
        new_light_grid = LightGrid.from_empty(light_grid.size())
        light_grid.animate(new_light_grid)
        light_grid = new_light_grid
    return light_grid.count_lights_on()


def test_animate_lightgrid(filename: str, steps: int, expected_lights_on: int) -> None:
    lights_on = animate_light_grid(filename, steps)
    assert lights_on == expected_lights_on, \
        'Expected to find {0} lights on after {1} steps but was {2}!'.format(expected_lights_on, steps, lights_on)


def main() -> None:
    test_animate_lightgrid('test18a.txt', 4, 4)


if __name__ == '__main__':
    main()
