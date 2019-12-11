import itertools


class IntcodeProcessor:

    def __init__(self, int_code):
        self.memory = [int(x) for x in int_code.split(',')]
        self.instruction_pointer = 0
        self.opcode = 0
        self.parameter_modes = 0

    @classmethod
    def from_file(cls, filename):
        int_code = ''
        with open(filename) as csv_file:
            for line in csv_file:
                int_code += line.rstrip('\n')
        return IntcodeProcessor(int_code)

    def read(self, address):
        return self.memory[address]

    def write(self, address, value):
        self.memory[address] = value

    def next_instruction(self):
        read = self.read(self.instruction_pointer)
        self.instruction_pointer += 1
        return read

    def get_instruction_pointer(self):
        return self.instruction_pointer

    def set_instruction_pointer(self, instruction_pointer):
        self.instruction_pointer = instruction_pointer

    def is_halted(self):
        return self.opcode == 99

    @staticmethod
    def decode_instruction(instruction):
        opcode = instruction % 100
        modes = instruction // 100
        return opcode, modes

    @staticmethod
    def determine_mode(parameter_modes):
        mode = parameter_modes % 10
        parameter_modes = parameter_modes // 10
        return mode, parameter_modes

    def read_parameter(self, parameter_modes):
        mode, parameter_modes = self.determine_mode(parameter_modes)
        parameter = self.next_instruction()
        if mode == 0:
            parameter = self.read(parameter)
        elif mode != 1:
            raise Exception("Unknown parameter mode {0}!".format(mode))
        return parameter, parameter_modes

    def write_parameter(self, parameter_modes, value):
        mode, parameter_modes = self.determine_mode(parameter_modes)
        if mode != 0:
            raise Exception("Unexpected mode {0} for write parameter!".format(mode))
        parameter = self.next_instruction()
        self.write(parameter, value)

    def run(self, inputs):
        outputs = []
        restart = self.opcode == 3
        while True:
            if restart:
                restart = False
            else:
                instruction = self.next_instruction()
                self.opcode, self.parameter_modes = self.decode_instruction(instruction)
            if self.opcode == 99:
                # End
                return outputs
            elif self.opcode == 1:
                # P3 = P1 + P2
                parameter1, self.parameter_modes = self.read_parameter(self.parameter_modes)
                parameter2, self.parameter_modes = self.read_parameter(self.parameter_modes)
                result = parameter1 + parameter2
                self.write_parameter(self.parameter_modes, result)
            elif self.opcode == 2:
                # P3 = P1 * P2
                parameter1, self.parameter_modes = self.read_parameter(self.parameter_modes)
                parameter2, self.parameter_modes = self.read_parameter(self.parameter_modes)
                result = parameter1 * parameter2
                self.write_parameter(self.parameter_modes, result)
            elif self.opcode == 3:
                # Input
                if len(inputs) == 0:
                    return outputs
                self.write_parameter(self.parameter_modes, inputs.pop(0))
            elif self.opcode == 4:
                # Output
                parameter1, self.parameter_modes = self.read_parameter(self.parameter_modes)
                outputs.append(parameter1)
            elif self.opcode == 5:
                # If P1 != 0 InstructionPointer = P2
                parameter1, self.parameter_modes = self.read_parameter(self.parameter_modes)
                parameter2, self.parameter_modes = self.read_parameter(self.parameter_modes)
                if parameter1 != 0:
                    self.set_instruction_pointer(parameter2)
            elif self.opcode == 6:
                # If P1 == 0 InstructionPointer = P2
                parameter1, self.parameter_modes = self.read_parameter(self.parameter_modes)
                parameter2, self.parameter_modes = self.read_parameter(self.parameter_modes)
                if parameter1 == 0:
                    self.set_instruction_pointer(parameter2)
            elif self.opcode == 7:
                # P3 = P1 < P2
                parameter1, self.parameter_modes = self.read_parameter(self.parameter_modes)
                parameter2, self.parameter_modes = self.read_parameter(self.parameter_modes)
                result = int(parameter1 < parameter2)
                self.write_parameter(self.parameter_modes, result)
            elif self.opcode == 8:
                # P3 = P1 == P2
                parameter1, self.parameter_modes = self.read_parameter(self.parameter_modes)
                parameter2, self.parameter_modes = self.read_parameter(self.parameter_modes)
                result = int(parameter1 == parameter2)
                self.write_parameter(self.parameter_modes, result)
            else:
                raise Exception('Unknown opcode {0}!'.format(self.opcode))


def create_amplifiers(filename, number_to_create):
    amplifiers = []
    for i in range(number_to_create):
        amplifiers.append(IntcodeProcessor.from_file(filename))
    return amplifiers


def run_amplifiers(amplifiers, phase_sequence):
    output = [0]
    for i in range(len(amplifiers)):
        amplifier = amplifiers[i]
        amplifier_input = [phase_sequence[i], output[0]]
        output = amplifier.run(amplifier_input)
    return output


def test(filename, phase_sequence, expected_output):
    amplifiers = create_amplifiers(filename, len(phase_sequence))
    output = run_amplifiers(amplifiers, phase_sequence)
    assert output == expected_output


def tests():
    test('test7a.txt', [4, 3, 2, 1, 0], [43210])
    test('test7b.txt', [0, 1, 2, 3, 4], [54321])
    test('test7c.txt', [1, 0, 4, 3, 2], [65210])


def run_amplifiers_with_phase(filename, phase_permutation):
    phase_sequence = [int(x) for x in list(phase_permutation)]
    amplifiers = create_amplifiers(filename, len(phase_sequence))
    return run_amplifiers(amplifiers, phase_sequence)


def scan_phases(filename):
    maximum_signal = 0
    for phase_permutation in itertools.permutations('01234'):
        output = run_amplifiers_with_phase(filename, phase_permutation)
        if output[0] > maximum_signal:
            maximum_signal = output[0]
            print(phase_permutation, maximum_signal)
    return maximum_signal


def run_amplifiers_with_feedback(amplifiers, phase_sequence):
    output = [0]
    halted = False
    while not halted:
        halted = True
        for i in range(len(amplifiers)):
            amplifier = amplifiers[i]
            if len(phase_sequence) > 0:
                amplifier_input = [phase_sequence.pop(0), output[0]]
            else:
                amplifier_input = [output[0]]
            output = amplifier.run(amplifier_input)
            halted = halted and amplifier.is_halted()
    return output


def test_with_feedback(filename, phase_sequence, expected_output):
    amplifiers = create_amplifiers(filename, len(phase_sequence))
    output = run_amplifiers_with_feedback(amplifiers, phase_sequence)
    assert output == expected_output


def run_amplifiers_with_feedback_with_phase(filename, phase_permutation):
    phase_sequence = [int(x) for x in list(phase_permutation)]
    amplifiers = create_amplifiers(filename, len(phase_sequence))
    return run_amplifiers_with_feedback(amplifiers, phase_sequence)


def scan_phases_with_feedback(filename):
    maximum_signal = 0
    for phase_permutation in itertools.permutations('56789'):
        output = run_amplifiers_with_feedback_with_phase(filename, phase_permutation)
        if output[0] > maximum_signal:
            maximum_signal = output[0]
            print(phase_permutation, maximum_signal)
    return maximum_signal


def main():
    tests()
    test_with_feedback('test7d.txt', [9, 8, 7, 6, 5], [139629729])
    test_with_feedback('test7e.txt', [9, 7, 8, 5, 6], [18216])

    highest_signal = scan_phases('input7.txt')
    print('Step 1 - Highest signal = {0}'.format(highest_signal))
    highest_signal = scan_phases_with_feedback('input7.txt')
    print('Step 2 - Highest signal = {0}'.format(highest_signal))


if __name__ == '__main__':
    main()
