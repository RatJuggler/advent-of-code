import re


def create_light_grid():
    grid = []
    for y in range(1000):
        grid_row = []
        for x in range(1000):
            grid_row.append(0)
        grid.append(grid_row)
    return grid


def decode_instruction(instruction):
    regex = r'(\D+) (\d+,\d+)'
    matches = re.findall(regex, instruction)
    action = matches[0][0]
    regex = r'(\d+)'
    corner1 = re.findall(regex, matches[0][1])
    corner2 = re.findall(regex, matches[1][1])
    return action, corner1, corner2


def decode_coords(corner1, corner2):
    x_start = int(corner1[0])
    x_finish = int(corner2[0])
    x_range = x_finish - x_start + 1
    y_start = int(corner1[1])
    y_finish = int(corner2[1])
    y_range = y_finish - y_start + 1
    return x_start, x_range, y_start, y_range


def toggle_lights(grid, corner1, corner2):
    x_start, x_range, y_start, y_range = decode_coords(corner1, corner2)
    for y in range(y_start, y_start + y_range):
        for x in range(x_start, x_start + x_range):
            current = grid[y][x]
            grid[y][x] = 1 if current == 0 else 1


def set_lights(grid, corner1, corner2, light):
    x_start, x_range, y_start, y_range = decode_coords(corner1, corner2)
    for y in range(y_start, y_start + y_range):
        for x in range(x_start, x_start + x_range):
            grid[y][x] = light


def decode_and_apply_instruction(grid, instruction):
    action, corner1, corner2 = decode_instruction(instruction)
    if action == 'toggle':
        toggle_lights(grid, corner1, corner2)
    elif action == 'turn on':
        set_lights(grid, corner1, corner2, 1)
    elif action == 'turn off':
        set_lights(grid, corner1, corner2, 0)


def apply_instructions(grid, filename):
    with open(filename) as fh:
        for instruction in fh:
            decode_and_apply_instruction(grid, instruction)


def count_lights(grid):
    lights_lit = 0
    for grid_row in grid:
        for light in grid_row:
            lights_lit += light
    return lights_lit


def display_grid(grid):
    display = ''
    for grid_row in grid:
        for light in grid_row:
            display += 'X' if light == 1 else ' '
        display += '\n'
    print(display)


def light_grid(filename):
    grid = create_light_grid()
    apply_instructions(grid, filename)
    display_grid(grid)
    return count_lights(grid)


def step1_simple_test(filename, expected_lights_lit):
    lights_lit = light_grid(filename)
    assert lights_lit == expected_lights_lit, \
        'Expected {0} lights to be lit but found {1}!'.format(expected_lights_lit, lights_lit)


def main():
    step1_simple_test('test6a.txt', 9)
    step1_simple_test('test6b.txt', 4)
    lights_lit = light_grid('input6.txt')
    print('Day 6, Step 1 lights lit = {0}'.format(lights_lit))


if __name__ == '__main__':
    main()
