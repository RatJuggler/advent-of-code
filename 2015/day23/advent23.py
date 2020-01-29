from typing import List, Tuple


class Computer:

    def __init__(self, program: List[str]):
        self._program = program
        self._a = 0
        self._b = 0

    @classmethod
    def from_file(cls, filename):
        program = []
        with open(filename) as program_file:
            for line in program_file:
                program.append(line.strip())
        return Computer(program)

    def list(self) -> None:
        for line in self._program:
            print(line)

    def run(self) -> Tuple[int, int]:
        return self._a, self._b


def test_computer(filename, expected_a, expected_b):
    computer = Computer.from_file(filename)
    a, b = computer.run()
    assert a == expected_a, 'Expected register "a" to contain {0} but was {1}!'.format(expected_a, a)
    assert b == expected_b, 'Expected register "b" to contain {0} but was {1}!'.format(expected_b, b)


def main() -> None:
    test_computer('test23a.txt', 2, 0)


if __name__ == '__main__':
    main()
