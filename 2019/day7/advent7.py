import itertools


class IntcodeProcessor:

    def __init__(self, int_code):
        self.memory = [int(x) for x in int_code.split(',')]
        self.instruction_pointer = 0

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
        while True:
            instruction = self.next_instruction()
            opcode, parameter_modes = self.decode_instruction(instruction)
            if opcode == 99:
                # End
                return outputs
            elif opcode == 1:
                # P3 = P1 + P2
                parameter1, parameter_modes = self.read_parameter(parameter_modes)
                parameter2, parameter_modes = self.read_parameter(parameter_modes)
                result = parameter1 + parameter2
                self.write_parameter(parameter_modes, result)
            elif opcode == 2:
                # P3 = P1 * P2
                parameter1, parameter_modes = self.read_parameter(parameter_modes)
                parameter2, parameter_modes = self.read_parameter(parameter_modes)
                result = parameter1 * parameter2
                self.write_parameter(parameter_modes, result)
            elif opcode == 3:
                # Input
                self.write_parameter(parameter_modes, inputs.pop(0))
            elif opcode == 4:
                # Output
                parameter1, parameter_modes = self.read_parameter(parameter_modes)
                outputs.append(parameter1)
            elif opcode == 5:
                # If P1 != 0 InstructionPointer = P2
                parameter1, parameter_modes = self.read_parameter(parameter_modes)
                parameter2, parameter_modes = self.read_parameter(parameter_modes)
                if parameter1 != 0:
                    self.set_instruction_pointer(parameter2)
            elif opcode == 6:
                # If P1 == 0 InstructionPointer = P2
                parameter1, parameter_modes = self.read_parameter(parameter_modes)
                parameter2, parameter_modes = self.read_parameter(parameter_modes)
                if parameter1 == 0:
                    self.set_instruction_pointer(parameter2)
            elif opcode == 7:
                # P3 = P1 < P2
                parameter1, parameter_modes = self.read_parameter(parameter_modes)
                parameter2, parameter_modes = self.read_parameter(parameter_modes)
                result = int(parameter1 < parameter2)
                self.write_parameter(parameter_modes, result)
            elif opcode == 8:
                # P3 = P1 == P2
                parameter1, parameter_modes = self.read_parameter(parameter_modes)
                parameter2, parameter_modes = self.read_parameter(parameter_modes)
                result = int(parameter1 == parameter2)
                self.write_parameter(parameter_modes, result)
            else:
                raise Exception('Unknown opcode {0}!'.format(opcode))


def run_amplifiers(filename, inputs):
    outputs = [0]
    for phase in inputs:
        amplifier = IntcodeProcessor.from_file(filename)
        amplifier_input = [phase, outputs[0]]
        outputs = amplifier.run(amplifier_input)
    return outputs


def run_amplifiers_with_feedback(filename, inputs):
    outputs = [0]
    for phase in inputs:
        amplifier = IntcodeProcessor.from_file(filename)
        amplifier_input = [phase, outputs[0]]
        outputs = amplifier.run(amplifier_input)
    return outputs


def scan_phases(filename):
    maximum_signal = 0
    for phases in itertools.permutations('01234'):
        inputs = [int(x) for x in list(phases)]
        outputs = run_amplifiers(filename, inputs)
        if outputs[0] > maximum_signal:
            maximum_signal = outputs[0]
            print(inputs, maximum_signal)
    return maximum_signal


def scan_phases_with_feedback(filename):
    maximum_signal = 0
    for phases in itertools.permutations('56789'):
        inputs = [int(x) for x in list(phases)]
        outputs = run_amplifiers_with_feedback(filename, inputs)
        if outputs[0] > maximum_signal:
            maximum_signal = outputs[0]
            print(inputs, maximum_signal)
    return maximum_signal


def test(filename, inputs, expected_outputs):
    outputs = run_amplifiers(filename, inputs)
    assert outputs == expected_outputs


def test_with_feedback(filename, inputs, expected_outputs):
    output = run_amplifiers_with_feedback(filename, inputs)
    assert output == expected_outputs


def tests():
    test('test7a.txt', [4, 3, 2, 1, 0], [43210])
    test('test7b.txt', [0, 1, 2, 3, 4], [54321])
    test('test7c.txt', [1, 0, 4, 3, 2], [65210])

#    test_with_feedback('test7d.txt', [9,8,7,6,5], [139629729])
#    test_with_feedback('test7e.txt', [9,7,8,5,6], [18216])


def find_highest_signal():
    highest_signal = scan_phases('input7.txt')
    print('Step 1 - Highest signal = {0}'.format(highest_signal))


def find_highest_signal_with_feedback():
    highest_signal = scan_phases_with_feedback('input7.txt')
    print('Step 2 - Highest signal = {0}'.format(highest_signal))


def main():
    tests()
    find_highest_signal()
#    find_highest_signal_with_feedback()


if __name__ == '__main__':
    main()
