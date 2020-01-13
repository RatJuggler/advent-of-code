import re


class Instruction:

    def __init__(self, wire, p1, op, p2, signal):
        self.wire = wire
        self.p1 = p1
        self.op = op
        self.p2 = p2
        self.signal = signal

    @classmethod
    def from_line(cls, line):
        wire, p1, op, p2, signal = cls.decode_instruction(line)
        return Instruction(wire, p1, op, p2, signal)

    @staticmethod
    def decode_instruction(line):
        regex = r'(?P<value>\d+)?((?P<p1>\S+)? ?(?P<op>AND|OR|LSHIFT|RSHIFT|NOT) ?(?P<p2>\S+))? -> (?P<wire>\S+)'
        matches = re.match(regex, line)
        wire = matches.group('wire')
        value = matches.group('value')
        signal = None if value is None else int(value)
        p1 = matches.group('p1')
        op = matches.group('op')
        p2 = matches.group('p2')
        return wire, p1, op, p2, signal

    def __repr__(self):
        return "Instruction({0}: {1} {2} {3} = {4})".format(self.wire, self.p1, self.op, self.p2, self.signal)

    def evaluate(self, circuit):
        if not self.signal:
            return self.signal
        return self.signal


def load_circuit(filename):
    circuit = {}
    with open(filename) as fh:
        for line in fh:
            instruction = Instruction.from_line(line)
            circuit[instruction.wire] = instruction
    return circuit


def evaluate_circuit(filename):
    circuit = load_circuit(filename)
    for instruction in circuit.values():
        instruction.evaluate(circuit)
        print(instruction)
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
