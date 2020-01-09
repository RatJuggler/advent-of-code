import re


def create_light_grid():
    light_grid = []
    for y in range(1000):
        grid_row = []
        for x in range(1000):
            grid_row.append(0)
        light_grid.append(grid_row)
    return light_grid


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
    x_range = x_finish - x_start
    y_start = int(corner1[1])
    y_finish = int(corner2[1])
    y_range = y_finish - y_start
    return x_start, x_range, y_start, y_range


def toggle_lights(light_grid, corner1, corner2):
    x_start, x_range, y_start, y_range = decode_coords(corner1, corner2)
    for y in range(y_start, y_start + y_range):
        for x in range(x_start, x_start + x_range):
            current = light_grid[y][x]
            light_grid[y][x] = 1 if current == 0 else 1


def set_lights(light_grid, corner1, corner2, light):
    x_start, x_range, y_start, y_range = decode_coords(corner1, corner2)
    for y in range(y_start, y_start + y_range):
        for x in range(x_start, x_start + x_range):
            light_grid[y][x] = light


def decode_and_apply_instruction(light_grid, instruction):
    action, corner1, corner2 = decode_instruction(instruction)
    if action == 'toggle':
        toggle_lights(light_grid, corner1, corner2)
    elif action == 'turn on':
        set_lights(light_grid, corner1, corner2, 1)
    elif action == 'turn off':
        set_lights(light_grid, corner1, corner2, 0)


def apply_instructions(light_grid, filename):
    with open(filename) as fh:
        for instruction in fh:
            decode_and_apply_instruction(light_grid, instruction)
            return


def count_lights(light_grid):
    lights_lit = 0
    for grid_row in light_grid:
        for light in grid_row:
            lights_lit += light
    return lights_lit


def display_grid(light_grid):
    display = ''
    for grid_row in light_grid:
        for light in grid_row:
            display += 'X' if light == 1 else ' '
        display += '\n'
    print(display)


def main():
    light_grid = create_light_grid()
    apply_instructions(light_grid, 'input6.txt')
    display_grid(light_grid)
    lights_lit = count_lights(light_grid)
    print('Day 6, Step 1 lights lit = {0}'.format(lights_lit))


if __name__ == '__main__':
    main()
