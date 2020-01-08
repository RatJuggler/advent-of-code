def create_light_grid():
    pass


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
