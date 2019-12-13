import curses


class IntcodeProcessor:

    def __init__(self, int_code):
        self.memory = [int(x) for x in int_code.split(',')]
        for i in range(len(self.memory), 4096):
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


def count_block_tiles(output):
    block_tiles = 0
    for tile_index in range(2, len(output), 3):
        if output[tile_index] == 2:
            block_tiles += 1
    return block_tiles


def step1_count_block_tiles():
    game = IntcodeProcessor.from_file('input13.txt')
    output = game.run([])
    print('Day 13, Step 1 block tiles output = {0}'.format(count_block_tiles(output)))


def display_screen(output, screen):
    for tile_index in range(2, len(output), 3):
        tile = output[tile_index]
        x = output[tile_index - 2]
        y = output[tile_index - 1]
        if x == -1:
            screen.addstr(0, 0, 'Score: {0} '.format(tile))
        else:
            if tile == 0:
                display = ' '
            elif tile == 1:
                display = '@'
            elif tile == 2:
                display = 'X'
            elif tile == 3:
                display = '='
            elif tile == 4:
                display = 'o'
            else:
                display = '?'
            screen.addch(y, x, display)


def step2_play_game():
    game = IntcodeProcessor.from_file('input13.txt')
    game.write(0, 2)  # Set to free play.
    screen = curses.initscr()
    curses.noecho()
    curses.curs_set(0)
    screen.nodelay(True)
    joystick = [0]
    while not game.is_halted():
        output = game.run(joystick)
        display_screen(output, screen)
        screen.refresh()
        screen.timeout(500)
        c = screen.getch()
        screen.addstr(0, 20, 'Key: {0} '.format(c))
        if c == 122:  # Z key
            joystick = [-1]
        elif c == 120:  # X key
            joystick = [1]
        else:
            joystick = [0]
    screen.nodelay(False)
    curses.curs_set(1)
    curses.echo()
    curses.endwin()


def main():
    step1_count_block_tiles()
    step2_play_game()


if __name__ == '__main__':
    main()
