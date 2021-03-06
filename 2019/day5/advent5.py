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


def simple_test(int_code, inputs, expected_output):
    processor = IntcodeProcessor(int_code)
    output = processor.run(inputs)
    assert output == expected_output, \
        'For {0} expect output of {0} but got {1}!'.format(int_code, expected_output, output)


def simple_tests():
    # Input => Output
    simple_test('3,0,4,0,99', [777], [777])
    simple_test('1002,4,3,4,33', [], [])
    # Input == 8 => 1 else 0 (position mode)
    int_code = '3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8'
    simple_test(int_code, [8], [1])
    simple_test(int_code, [13], [0])
    # Input < 8 => 1 else 0 (position mode)
    int_code = '3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8'
    simple_test(int_code, [3], [1])
    simple_test(int_code, [8], [0])
    simple_test(int_code, [13], [0])
    # Input == 8 => 1 else 0 (immediate mode)
    int_code = '3,3,1108,-1,8,3,4,3,99'
    simple_test(int_code, [8], [1])
    simple_test(int_code, [13], [0])
    # Input < 8 => 1 else 0 (immediate mode)
    int_code = '3,3,1107,-1,8,3,4,3,99'
    simple_test(int_code, [3], [1])
    simple_test(int_code, [8], [0])
    simple_test(int_code, [13], [0])


def complex_test(filename, inputs, expected_output):
    processor = IntcodeProcessor.from_file(filename)
    output = processor.run(inputs)
    assert output == expected_output, \
        'For {0} expect output of {1} but got {2}!'.format(filename, expected_output, output)


def main():
    simple_tests()

    complex_test('test5a.txt', [3], [999])
    complex_test('test5a.txt', [8], [1000])
    complex_test('test5a.txt', [11], [1001])

    step1_processor = IntcodeProcessor.from_file('input5.txt')
    step1_outputs = step1_processor.run([1])
    print("Step 1 Outputs = {0}".format(step1_outputs))
    step2_processor = IntcodeProcessor.from_file('input5.txt')
    step2_outputs = step2_processor.run([5])
    print("Step 2 Outputs = {0}".format(step2_outputs))


if __name__ == '__main__':
    main()
