import itertools


class IntcodeProcessor:

    def __init__(self, int_code):
        self.memory = [int(x) for x in int_code.split(',')]
        for i in range(len(self.memory), 2048):
            self.memory.append(0)
        self.instruction_pointer = 0
        self.relative_base = 0

    @classmethod
    def from_file(cls, filename):
        int_code = ''
        with open(filename) as csv_file:
            for line in csv_file:
                int_code += line.rstrip('\n')
        return IntcodeProcessor(int_code)

    def read(self, address):
        if address > len(self.memory):
            raise Exception('Read address {0} out of range of {1}!'.format(address, len(self.memory)))
        return self.memory[address]

    def write(self, address, value):
        if address > len(self.memory):
            raise Exception('Write address {0} out of range of {1}!'.format(address, len(self.memory)))
        self.memory[address] = value

    def next_instruction(self):
        read = self.read(self.instruction_pointer)
        self.instruction_pointer += 1
        return read

    def get_instruction_pointer(self):
        return self.instruction_pointer

    def set_instruction_pointer(self, instruction_pointer):
        self.instruction_pointer = instruction_pointer

    def adjust_relative_base(self, adjust_by):
        self.relative_base += adjust_by

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
        # 0 => Position Mode, 1 => Immediate Mode, 2 => Relative Mode
        if mode == 0:
            parameter = self.read(parameter)
        elif mode == 2:
            parameter = self.read(self.relative_base + parameter)
        elif mode != 1:
            raise Exception("Unknown parameter mode {0}!".format(mode))
        return parameter, parameter_modes

    def write_parameter(self, parameter_modes, value):
        mode, parameter_modes = self.determine_mode(parameter_modes)
        parameter = self.next_instruction()
        if mode == 0:
            self.write(parameter, value)
        elif mode == 2:
            self.write(self.relative_base + parameter, value)
        else:
            raise Exception("Unexpected mode {0} for write parameter!".format(mode))

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
            elif opcode == 9:
                # RelativeBase += P1
                parameter1, parameter_modes = self.read_parameter(parameter_modes)
                self.adjust_relative_base(parameter1)
            else:
                raise Exception('Unknown opcode {0}!'.format(opcode))


def simple_test(int_code, inputs, expected_outputs):
    processor = IntcodeProcessor(int_code)
    outputs = processor.run(inputs)
    assert outputs == expected_outputs


def tests():
    int_code = '109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99'
    simple_test(int_code, [], [int(x) for x in int_code.split(',')])
    simple_test('1102,34915192,34915192,7,4,7,99,0', [], [1219070632396864])
    simple_test('104,1125899906842624,99', [], [1125899906842624])


def main():
    tests()

    step1_processor = IntcodeProcessor.from_file('input9.txt')
    step1_outputs = step1_processor.run([1])
    print("Step 1 Outputs = {0}".format(step1_outputs))


if __name__ == '__main__':
    main()
