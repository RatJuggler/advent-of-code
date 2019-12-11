class IntcodeProcessor:

    def __init__(self, int_code):
        self.memory = [int(x) for x in int_code.split(',')]
        for i in range(len(self.memory), 2048):
            self.memory.append(0)
        self.instruction_pointer = 0
        self.opcode = 0
        self.parameter_modes = 0
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

    def is_halted(self):
        return self.opcode == 99

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
            elif self.opcode == 9:
                # RelativeBase += P1
                parameter1, self.parameter_modes = self.read_parameter(self.parameter_modes)
                self.adjust_relative_base(parameter1)
            else:
                raise Exception('Unknown opcode {0}!'.format(self.opcode))


def move_robot(x, y, heading, turn):
    if turn not in (0, 1):
        raise Exception('Unknown turn type {0}!'.format(turn))
    if heading == 'N':
        if turn == 0:
            heading = 'W'
            x -= 1
        else:
            heading = 'E'
            x += 1
    elif heading == 'E':
        if turn == 0:
            heading = 'N'
            y += 1
        else:
            heading = 'S'
            y -= 1
    elif heading == 'S':
        if turn == 0:
            heading = 'E'
            x += 1
        else:
            heading = 'W'
            x -= 1
    elif heading == 'W':
        if turn == 0:
            heading = 'S'
            y -= 1
        else:
            heading = 'N'
            y += 1
    else:
        raise Exception('Unknown heading {0}!'.format(heading))
    return x, y, heading


def run_paint_robot(start_colour):
    x = 100
    y = 100
    heading = 'N'
    painted = {(x, y): start_colour}
    robot = IntcodeProcessor.from_file('input11.txt')
    while not robot.is_halted():
        current_colour = painted.get((x, y))
        if current_colour is None:
            current_colour = 0
        output = robot.run([current_colour])
        if len(output) != 2:
            raise Exception('Two outputs expected from paint robot!')
        new_colour = output[0]
        painted[(x, y)] = new_colour
        turn = output[1]
        x, y, heading = move_robot(x, y, heading, turn)
    return painted


def step1_count_colours():
    painted = run_paint_robot(0)  # Start on black
    print("Day 11, Step 1 panels painted = {0}".format(len(painted)))


def step2_show_output():
    painted = run_paint_robot(1)  # Start on white
    print("Day 11, Step 2 panels painted = {0}".format(len(painted)))


def main():
    step1_count_colours()
    step2_show_output()


if __name__ == '__main__':
    main()
