import re


class Instruction:

    def __init__(self, wire, connection):
        self.wire = wire
        self.connection = connection

    @classmethod
    def from_line(cls, line):
        wire, connection = cls.decode_instruction(line)
        return Instruction(wire, connection)

    @staticmethod
    def decode_instruction(line):
        regex = r'(.+) -> (.+)'
        matches = re.findall(regex, line)
        wire = matches[0][1]
        connection = matches[0][0]
        return wire, connection

    def __repr__(self):
        return "Instruction({0}, {1})".format(self.wire, self.connection)


def load_circuit(filename):
    circuit = {}
    with open(filename) as fh:
        for line in fh:
            instruction = Instruction.from_line(line)
            circuit[instruction.wire] = instruction
    return circuit


def evaluate_circuit(filename):
    circuit = load_circuit(filename)
    print(circuit)
    return {}


def test_evaluate_circuit(filename, expected_signals):
    signals = evaluate_circuit(filename)
    assert signals == expected_signals, 'Expected circuit to produce {0} but was {1}'.format(expected_signals, signals)


def main():
    test_evaluate_circuit('test7a.txt',
                          {'d': 72, 'e': 507, 'f': 492, 'g': 114, 'h': 65412, 'i': 65079, 'x': 123, 'y': 456})
    signals = evaluate_circuit('input7.txt')
    print(signals)


if __name__ == '__main__':
    main()
