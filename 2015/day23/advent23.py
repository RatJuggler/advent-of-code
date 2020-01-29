from typing import Callable, List, Tuple


class Computer:

    def __init__(self, program: List[str]) -> None:
        self._program = program
        self._a = None
        self._b = None

    @classmethod
    def from_file(cls, filename):
        program = []
        with open(filename) as program_file:
            for line in program_file:
                program.append(line.strip())
        return Computer(program)

    def list(self) -> None:
        for instruction in self._program:
            print(instruction)

    @staticmethod
    def decode_instruction(instruction) -> [str, List[str]]:
        operation, parameters = instruction.split(' ', 1)
        return operation, parameters.split(', ')

    def register_update(self, register: str, update: Callable[[int], int]) -> None:
        setattr(self, register, update(getattr(self, register)))

    def run(self, a_start: int, b_start: int) -> Tuple[int, int]:
        self._a = a_start
        self._b = b_start
        instruction_pointer = 0
        while instruction_pointer < len(self._program):
            instruction = self._program[instruction_pointer]
            instruction_pointer += 1
            operation, parameters = self.decode_instruction(instruction)
            register = None if operation == 'jmp' else '_' + parameters[0]
            if operation == 'hlf':
                self.register_update(register, lambda x: x // 2)
            elif operation == 'tpl':
                self.register_update(register, lambda x: x * 3)
            elif operation == 'inc':
                self.register_update(register, lambda x: x + 1)
            elif operation == 'jmp':
                instruction_pointer += int(parameters[0]) - 1
            elif operation == 'jie':
                if getattr(self, register) % 2 == 0:
                    instruction_pointer += int(parameters[1]) - 1
            elif operation == 'jio':
                if getattr(self, register) == 1:
                    instruction_pointer += int(parameters[1]) - 1
            else:
                raise Exception('Unknown operation {0}!'.format(operation))
        return self._a, self._b


def test_computer(filename: str, expected_a: int, expected_b: int) -> None:
    computer = Computer.from_file(filename)
    a, b = computer.run(0, 0)
    assert a == expected_a, 'Expected register "a" to contain {0} but was {1}!'.format(expected_a, a)
    assert b == expected_b, 'Expected register "b" to contain {0} but was {1}!'.format(expected_b, b)


def main() -> None:
    test_computer('test23a.txt', 2, 0)
    computer = Computer.from_file('input23.txt')
    a, b = computer.run(0, 0)
    print('Day 23, Step 1 registers after running input with 0, 0: a = {0}, b = {1}'.format(a, b))
    a, b = computer.run(1, 0)
    print('Day 23, Step 2 registers after running input with 1, 0: a = {0}, b = {1}'.format(a, b))


if __name__ == '__main__':
    main()
