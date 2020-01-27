from typing import List


class LightGrid:

    def __init__(self, grid: List[List[int]]) -> None:
        self.grid = grid
        self.grid_size = len(grid)

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

    def set_light(self, x: int, y: int, new_light) -> None:
        self.grid[y][x] = new_light

    def get_light(self, x: int, y: int) -> int:
        if x < 0 or y < 0:
            return 0
        if x >= self.grid_size or y >= self.grid_size:
            return 0
        return self.grid[y][x]

    def next_light_state(self, x: int, y: int) -> int:
        current_light = 0
        for sy in range(y - 1, y + 2):
            for sx in range(x - 1, x + 2):
                if sx != x or sy != y:
                    current_light += self.get_light(sx, sy)
        if self.get_light(x, y) == 1:
            return 1 if current_light in (2, 3) else 0
        else:
            return 1 if current_light == 3 else 0

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
        for y in range(light_grid.grid_size):
            for x in range(light_grid.grid_size):
                new_light_grid.set_light(x, y, light_grid.next_light_state(x, y))
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
