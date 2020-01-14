import re


class Instruction:

    def __init__(self, wire, p1, op, p2):
        self.wire = wire
        self.p1 = p1
        self.op = op
        self.p2 = p2
        self.signal = None

    @classmethod
    def from_line(cls, line):
        wire, p1, op, p2 = cls.decode_instruction(line)
        return Instruction(wire, p1, op, p2)

    @staticmethod
    def decode_instruction(line):
        regex = r'(?P<p1>\S+)? ?((?P<op>AND|OR|LSHIFT|RSHIFT|NOT) ?(?P<p2>\S+))? -> (?P<wire>\S+)'
        matches = re.match(regex, line)
        wire = matches.group('wire')
        p1 = matches.group('p1')
        op = matches.group('op')
        p2 = matches.group('p2')
        return wire, p1, op, p2

    def __repr__(self):
        return "Instruction({0}: {1} {2} {3} = {4})".format(self.wire, self.p1, self.op, self.p2, self.signal)

    @staticmethod
    def get_parameter(circuit, p):
        if not p:
            return p
        if p.isnumeric():
            return int(p)
        return circuit.get_wire_instruction(p).evaluate(circuit)

    def evaluate(self, circuit):
        if self.signal:
            return self.signal
        p1_value = self.get_parameter(circuit, self.p1)
        p2_value = self.get_parameter(circuit, self.p2)
        if self.op == 'NOT':
            self.signal = ~ p2_value
            if self.signal < 0:
                self.signal += 2**16
        elif self.op == 'AND':
            self.signal = p1_value & p2_value
        elif self.op == 'OR':
            self.signal = p1_value | p2_value
        elif self.op == 'LSHIFT':
            self.signal = p1_value << p2_value
        elif self.op == 'RSHIFT':
            self.signal = p1_value >> p2_value
        else:
            self.signal = p1_value
        return self.signal


class Circuit:

    def __init__(self, instructions):
        self.instructions = instructions

    @classmethod
    def from_file(cls, filename):
        instructions = {}
        with open(filename) as fh:
            for line in fh:
                instruction = Instruction.from_line(line)
                instructions[instruction.wire] = instruction
        return Circuit(instructions)

    def get_wire_instruction(self, wire):
        return self.instructions[wire]

    def evaluate(self):
        for instruction in self.instructions.values():
            instruction.evaluate(self)

    def get_signals(self):
        return {k: self.instructions[k].signal for k in sorted(self.instructions)}


def test_evaluate_circuit(filename, expected_signals):
    circuit = Circuit.from_file(filename)
    circuit.evaluate()
    signals = circuit.get_signals()
    assert signals == expected_signals, 'Expected circuit to produce {0} but was {1}'.format(expected_signals, signals)


def main():
    test_evaluate_circuit('test7a.txt',
                          {'a': 123, 'b': 456, 'd': 72, 'e': 507, 'f': 492,
                           'g': 114, 'h': 65412, 'i': 65079, 'x': 123, 'y': 456})
    circuit = Circuit.from_file('input7.txt')
    circuit.evaluate()
    signal_a = circuit.get_wire_instruction('a').signal
    print('Day 7, Step 1 signal for wire "a" = {0}'.format(signal_a))


if __name__ == '__main__':
    main()
