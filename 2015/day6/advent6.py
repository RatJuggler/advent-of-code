import re


class LightGrid:

    def __init__(self, grid_size):
        self.grid = []
        for y in range(grid_size):
            grid_row = [0] * grid_size
            self.grid.append(grid_row)

    @staticmethod
    def decode_coords(corner1, corner2):
        x_start = int(corner1[0])
        x_finish = int(corner2[0])
        x_range = x_finish - x_start + 1
        y_start = int(corner1[1])
        y_finish = int(corner2[1])
        y_range = y_finish - y_start + 1
        return x_start, x_range, y_start, y_range

    def set_lights(self, corner1, corner2, change_light):
        x_start, x_range, y_start, y_range = self.decode_coords(corner1, corner2)
        for y in range(y_start, y_start + y_range):
            for x in range(x_start, x_start + x_range):
                self.grid[y][x] = change_light(self.grid[y][x])

    def count_lights(self):
        lights_lit = 0
        for grid_row in self.grid:
            for light in grid_row:
                lights_lit += light
        return lights_lit

    def display(self):
        display = ''
        for grid_row in self.grid:
            for light in grid_row:
                display += 'X' if light == 1 else ' '
            display += '\n'
        print(display)


def decode_instruction(instruction):
    regex = r'(\D+) (\d+,\d+)'
    matches = re.findall(regex, instruction)
    action = matches[0][0]
    regex = r'(\d+)'
    corner1 = re.findall(regex, matches[0][1])
    corner2 = re.findall(regex, matches[1][1])
    return action, corner1, corner2


def decode_and_apply_instruction(grid, instruction):
    action, corner1, corner2 = decode_instruction(instruction)
    if action == 'toggle':
        grid.set_lights(corner1, corner2, lambda light: 1 if light == 0 else 0)
    elif action == 'turn on':
        grid.set_lights(corner1, corner2, lambda light: 1)
    elif action == 'turn off':
        grid.set_lights(corner1, corner2, lambda light: 0)


def apply_instructions(grid, filename):
    with open(filename) as fh:
        for instruction in fh:
            decode_and_apply_instruction(grid, instruction)


def light_grid(filename):
    grid = LightGrid(1000)
    apply_instructions(grid, filename)
    grid.display()
    return grid.count_lights()


def step1_simple_test(filename, expected_lights_lit):
    lights_lit = light_grid(filename)
    assert lights_lit == expected_lights_lit, \
        'Expected {0} lights to be lit but found {1}!'.format(expected_lights_lit, lights_lit)


def main():
    step1_simple_test('test6a.txt', 13)
    step1_simple_test('test6b.txt', 999000)
    lights_lit = light_grid('input6.txt')
    print('Day 6, Step 1 lights lit = {0}'.format(lights_lit))


if __name__ == '__main__':
    main()
