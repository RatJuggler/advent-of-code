class Memory:

    def __init__(self, int_code):
        self.memory = [int(x) for x in int_code.split(',')]
        self.instruction_pointer = 0

    @classmethod
    def from_file(cls, filename):
        int_code = ''
        with open(filename) as csv_file:
            for line in csv_file:
                int_code += line.rstrip('\n')
        return Memory(int_code)

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


def decode_instruction(instruction):
    opcode = instruction % 100
    modes = instruction // 100
    return opcode, modes


def decode_parameter(memory, parameter_modes):
    mode = parameter_modes % 10
    parameter_modes = parameter_modes // 10
    parameter = memory.next_instruction()
    if mode == 0:
        parameter = memory.read(parameter)
    elif mode != 1:
        raise Exception("Unknown parameter mode {0}!".format(mode))
    return parameter, parameter_modes


def run(memory, inputs):
    instruction_pointer = 0
    outputs = []
    while True:
        instruction = memory.next_instruction()
        opcode, parameter_modes = decode_instruction(instruction)
        if opcode == 99:
            # End
            return outputs
        elif opcode == 1:
            # P3 = P1 + P2
            parameter1, parameter_modes = decode_parameter(memory, parameter_modes)
            parameter2, parameter_modes = decode_parameter(memory, parameter_modes)
            result = parameter1 + parameter2
            if parameter_modes != 0:
                raise Exception("Unexpected immediate mode for opcode 1 result!")
            parameter3 = memory.next_instruction()
            memory.write(parameter3, result)
        elif opcode == 2:
            # P3 = P1 * P2
            parameter1, parameter_modes = decode_parameter(memory, parameter_modes)
            parameter2, parameter_modes = decode_parameter(memory, parameter_modes)
            result = parameter1 * parameter2
            if parameter_modes != 0:
                raise Exception("Unexpected immediate mode for opcode 2 result!")
            parameter3 = memory.next_instruction()
            memory.write(parameter3, result)
        elif opcode == 3:
            # Input
            parameter1 = memory.next_instruction()
            memory.write(parameter1, inputs.pop(0))
        elif opcode == 4:
            # Output
            parameter1, parameter_modes = decode_parameter(memory, parameter_modes)
            outputs.append(parameter1)
        elif opcode == 5:
            # If P1 != 0 InstructionPointer = P2
            parameter1, parameter_modes = decode_parameter(memory, parameter_modes)
            parameter2, parameter_modes = decode_parameter(memory, parameter_modes)
            if parameter1 != 0:
                memory.set_instruction_pointer(parameter2)
        elif opcode == 6:
            # If P1 == 0 InstructionPointer = P2
            parameter1, parameter_modes = decode_parameter(memory, parameter_modes)
            parameter2, parameter_modes = decode_parameter(memory, parameter_modes)
            if parameter1 == 0:
                memory.set_instruction_pointer(parameter2)
        elif opcode == 7:
            # P3 = P1 < P2
            parameter1, parameter_modes = decode_parameter(memory, parameter_modes)
            parameter2, parameter_modes = decode_parameter(memory, parameter_modes)
            parameter3 = memory.next_instruction()
            memory.write(parameter3, int(parameter1 < parameter2))
        elif opcode == 8:
            # P3 = P1 == P2
            parameter1, parameter_modes = decode_parameter(memory, parameter_modes)
            parameter2, parameter_modes = decode_parameter(memory, parameter_modes)
            parameter3 = memory.next_instruction()
            memory.write(parameter3, int(parameter1 == parameter2))
        else:
            raise Exception('Unknown opcode {0} at address {1}!'.format(opcode, instruction_pointer))


def simple_test(int_code, inputs, expected_outputs):
    memory = Memory(int_code)
    outputs = run(memory, inputs)
    assert outputs == expected_outputs


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


def complex_test(filename, inputs, expected_outputs):
    memory = Memory.from_file(filename)
    outputs = run(memory, inputs)
    assert outputs == expected_outputs


def main():
    simple_tests()

    complex_test('test5a.txt', [3], [999])
    complex_test('test5a.txt', [8], [1000])
    complex_test('test5a.txt', [11], [1001])

    step1_memory = Memory.from_file('input5.txt')
    step1_outputs = run(step1_memory, [1])
    print("Step 1 Outputs = {0}".format(step1_outputs))
    step2_memory = Memory.from_file('input5.txt')
    step2_outputs = run(step2_memory, [5])
    print("Step 2 Outputs = {0}".format(step2_outputs))


if __name__ == '__main__':
    main()
