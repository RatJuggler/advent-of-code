from abc import ABC, abstractmethod
import re


class LightGrid:

    def __init__(self, grid_size):
        self.grid = []
        for y in range(grid_size):
            grid_row = [0] * grid_size
            self.grid.append(grid_row)

    def set_lights(self, instruction):
        x_start, x_range, y_start, y_range = instruction.decode_coords()
        for y in range(y_start, y_start + y_range):
            for x in range(x_start, x_start + x_range):
                self.grid[y][x] = instruction.apply_action(self.grid[y][x])

    def count_brightness(self):
        lights_lit = 0
        for grid_row in self.grid:
            for light in grid_row:
                lights_lit += light
        return lights_lit


class Instruction(ABC):

    def __init__(self, action, corner1, corner2):
        self.action = action
        self.corner1 = corner1
        self.corner2 = corner2

    @staticmethod
    def decode_instruction(line):
        regex = r'(\D+) (\d+,\d+)'
        matches = re.findall(regex, line)
        action = matches[0][0]
        regex = r'(\d+)'
        corner1 = re.findall(regex, matches[0][1])
        corner2 = re.findall(regex, matches[1][1])
        return action, corner1, corner2

    def decode_coords(self):
        x_start = int(self.corner1[0])
        x_finish = int(self.corner2[0])
        x_range = x_finish - x_start + 1
        y_start = int(self.corner1[1])
        y_finish = int(self.corner2[1])
        y_range = y_finish - y_start + 1
        return x_start, x_range, y_start, y_range

    @abstractmethod
    def apply_action(self, light):
        pass


class Step1Instruction(Instruction):

    @classmethod
    def from_line(cls, line):
        action, corner1, corner2 = cls.decode_instruction(line)
        return Step1Instruction(action, corner1, corner2)

    def apply_action(self, light):
        if self.action == 'toggle':
            return 1 if light == 0 else 0
        elif self.action == 'turn on':
            return 1
        elif self.action == 'turn off':
            return 0


class Step2Instruction(Instruction):

    @classmethod
    def from_line(cls, line):
        action, corner1, corner2 = cls.decode_instruction(line)
        return Step2Instruction(action, corner1, corner2)

    def apply_action(self, light):
        if self.action == 'toggle':
            return light + 2
        elif self.action == 'turn on':
            return light + 1
        elif self.action == 'turn off':
            return light - 1 if light > 0 else 0


def apply_instructions(grid, filename, instruction_type):
    with open(filename) as fh:
        for line in fh:
            instruction = instruction_type.from_line(line)
            grid.set_lights(instruction)


def light_grid(filename, instruction_type):
    grid = LightGrid(1000)
    apply_instructions(grid, filename, instruction_type)
    return grid.count_brightness()


def step1_simple_test(filename, expected_lights_lit):
    lights_lit = light_grid(filename, Step1Instruction)
    assert lights_lit == expected_lights_lit, \
        'Expected {0} lights to be lit but found {1}!'.format(expected_lights_lit, lights_lit)


def step2_simple_test(filename, expected_lights_lit):
    lights_lit = light_grid(filename, Step2Instruction)
    assert lights_lit == expected_lights_lit, \
        'Expected brightness to be {0} but found {1}!'.format(expected_lights_lit, lights_lit)


def main():
    step1_simple_test('test6a.txt', 13)
    step1_simple_test('test6b.txt', 999000)
    lights_lit = light_grid('input6.txt', Step1Instruction)
    print('Day 6, Step 1 lights lit = {0}'.format(lights_lit))
    step2_simple_test('test6a.txt', 13)
    step2_simple_test('test6b.txt', 1002000)
    lights_lit = light_grid('input6.txt', Step2Instruction)
    print('Day 6, Step 2 brightness = {0}'.format(lights_lit))


if __name__ == '__main__':
    main()
