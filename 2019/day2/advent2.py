class Memory:

    def __init__(self, filename):
        with open(filename) as csv_file:
            for line in csv_file:
                self.memory = [int(x) for x in line.split(',')]
        self.instruction_pointer = 0

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


def run_intcode(memory):
    while True:
        opcode = memory.next_instruction()
        if opcode == 99:
            break
        if opcode == 1:
            parameter1 = memory.next_instruction()
            parameter2 = memory.next_instruction()
            parameter3 = memory.next_instruction()
            result = memory.read(parameter1) + memory.read(parameter2)
            memory.write(parameter3, result)
        elif opcode == 2:
            parameter1 = memory.next_instruction()
            parameter2 = memory.next_instruction()
            parameter3 = memory.next_instruction()
            result = memory.read(parameter1) * memory.read(parameter2)
            memory.write(parameter3, result)
        else:
            raise Exception('Unknown opcode {0}!'.format(opcode))
    return memory


def run_test(filename):
    memory = Memory(filename)
    memory = run_intcode(memory)
    return memory.read(0)


def run(filename, inputs):
    memory = Memory(filename)
    memory.write(1, inputs[0])
    memory.write(2, inputs[1])
    memory = run_intcode(memory)
    return memory.read(0)


def noun_verb(n):
    for i in range(n):
        for j in range(n):
            yield i, j


def main():
    result = run_test('test2a.txt')      # 3500
    assert result == 3500
    result = run('input2.txt', [12, 2])  # 12490719
    print('Gravity assist "1202 program alarm" result = {0}'.format(result))
    for noun, verb in noun_verb(100):
        result = run('input2.txt', [noun, verb])
        if result == 19690720:
            print('Gravity assist "1202 program alarm" result 19690720 found!')
            print('100 * {0} + {1} = {2}'.format(noun, verb, 100 * noun + verb))
            break


if __name__ == '__main__':
    main()
