def create_light_grid():
    light_grid = []
    for y in range(1000):
        grid_row = []
        for x in range(1000):
            grid_row.append(0)
        light_grid.append(grid_row)
    return light_grid


def apply_instructions(light_grid, param):
    pass


def count_lights(light_grid):
    pass


def main():
    light_grid = create_light_grid()
    apply_instructions(light_grid, 'input6.txt')
    lights_lit = count_lights(light_grid)
    print('Day 6, Step 1 lights lit = {0}'.format(lights_lit))


if __name__ == '__main__':
    main()
