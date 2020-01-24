from abc import ABC, abstractmethod
from typing import List, Type
import re


class Instruction(ABC):

    def __init__(self, action: str, corner1: List[int], corner2: List[int]) -> None:
        self.action = action
        self.corner1 = corner1
        self.corner2 = corner2

    @classmethod
    @abstractmethod
    def from_line(cls, line: str):
        pass

    @staticmethod
    def decode_instruction(line: str) -> [str, List[int], List[int]]:
        regex = r'(?P<action>\D+) (?P<coord1>\d+,\d+) through (?P<coord2>\d+,\d+)'
        matches = re.match(regex, line)
        action = matches.group('action')
        regex = r'(\d+)'
        corner1 = [int(x) for x in re.findall(regex, matches.group('coord1'))]
        corner2 = [int(x) for x in re.findall(regex, matches.group('coord2'))]
        return action, corner1, corner2

    def decode_coords(self) -> [int, int, int, int]:
        x_range = self.corner2[0] - self.corner1[0] + 1
        y_range = self.corner2[1] - self.corner1[1] + 1
        return self.corner1[0], x_range, self.corner1[1], y_range

    @abstractmethod
    def apply_action(self, light: int) -> int:
        pass


class Step1Instruction(Instruction):

    @classmethod
    def from_line(cls, line: str) -> Instruction:
        action, corner1, corner2 = cls.decode_instruction(line)
        return Step1Instruction(action, corner1, corner2)

    def apply_action(self, light: int) -> int:
        if self.action == 'toggle':
            return 1 if light == 0 else 0
        elif self.action == 'turn on':
            return 1
        elif self.action == 'turn off':
            return 0


class Step2Instruction(Instruction):

    @classmethod
    def from_line(cls, line: str) -> Instruction:
        action, corner1, corner2 = cls.decode_instruction(line)
        return Step2Instruction(action, corner1, corner2)

    def apply_action(self, light: int) -> int:
        if self.action == 'toggle':
            return light + 2
        elif self.action == 'turn on':
            return light + 1
        elif self.action == 'turn off':
            return light - 1 if light > 0 else 0


class LightGrid:

    def __init__(self, grid_size: int) -> None:
        self.grid = []
        for y in range(grid_size):
            grid_row = [0] * grid_size
            self.grid.append(grid_row)

    def set_lights(self, instruction: Instruction) -> None:
        x_start, x_range, y_start, y_range = instruction.decode_coords()
        for y in range(y_start, y_start + y_range):
            for x in range(x_start, x_start + x_range):
                self.grid[y][x] = instruction.apply_action(self.grid[y][x])

    def count_brightness(self) -> int:
        lights_lit = 0
        for grid_row in self.grid:
            for light in grid_row:
                lights_lit += light
        return lights_lit


def apply_instructions(grid: LightGrid, filename: str, instruction_type: Type[Instruction]) -> None:
    with open(filename) as fh:
        for line in fh:
            instruction = instruction_type.from_line(line)
            grid.set_lights(instruction)


def light_grid(filename: str, instruction_type: Type[Instruction]) -> int:
    grid = LightGrid(1000)
    apply_instructions(grid, filename, instruction_type)
    return grid.count_brightness()


def step1_simple_test(filename: str, expected_lights_lit: int) -> None:
    lights_lit = light_grid(filename, Step1Instruction)
    assert lights_lit == expected_lights_lit, \
        'Expected {0} lights to be lit but found {1}!'.format(expected_lights_lit, lights_lit)


def step2_simple_test(filename: str, expected_lights_lit: int) -> None:
    lights_lit = light_grid(filename, Step2Instruction)
    assert lights_lit == expected_lights_lit, \
        'Expected brightness to be {0} but found {1}!'.format(expected_lights_lit, lights_lit)


def main() -> None:
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
