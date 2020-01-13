import re


class Instruction:

    def __init__(self, wire, op1, operand, op2, signal):
        self.wire = wire
        self.op1 = op1
        self.operand = operand
        self.op2 = op2
        self.signal = signal

    @classmethod
    def from_line(cls, line):
        wire, op1, operand, op2, signal = cls.decode_instruction(line)
        return Instruction(wire, op1, operand, op2, signal)

    @staticmethod
    def decode_instruction(line):
        regex = r'(?P<value>\d+)?((?P<op1>.+)? ?(?P<operand>AND|OR|LSHIFT|RSHIFT|NOT) ?(?P<op2>.+))? -> (?P<wire>.+)'
        matches = re.match(regex, line)
        wire = matches.group('wire')
        value = matches.group('value')
        signal = 0 if value is None else int(value)
        op1 = matches.group('op1')
        operand = matches.group('operand')
        op2 = matches.group('op2')
        return wire, op1, operand, op2, signal

    def __repr__(self):
        return "Instruction({0}: {1} {2} {3} = {4})".format(self.wire, self.op1, self.operand, self.op2, self.signal)


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
