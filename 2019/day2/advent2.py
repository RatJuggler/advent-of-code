class Intcode:

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

    def run(self):
        while True:
            opcode = self.next_instruction()
            if opcode == 99:
                break
            if opcode == 1:
                parameter1 = self.next_instruction()
                parameter2 = self.next_instruction()
                parameter3 = self.next_instruction()
                result = self.read(parameter1) + self.read(parameter2)
                self.write(parameter3, result)
            elif opcode == 2:
                parameter1 = self.next_instruction()
                parameter2 = self.next_instruction()
                parameter3 = self.next_instruction()
                result = self.read(parameter1) * self.read(parameter2)
                self.write(parameter3, result)
            else:
                raise Exception('Unknown opcode {0}!'.format(opcode))


def run_test(filename):
    memory = Intcode(filename)
    memory.run()
    return memory.read(0)


def run(filename, inputs):
    memory = Intcode(filename)
    memory.write(1, inputs[0])
    memory.write(2, inputs[1])
    memory.run()
    return memory.read(0)


def noun_verb(n):
    for i in range(n):
        for j in range(n):
            yield i, j


def main():
    result = run_test('test2a.txt')
    assert result == 3500
    result = run('input2.txt', [12, 2])
    print('Day 2, Part 1 gravity assist "1202 program alarm" result = {0}'.format(result))
    for noun, verb in noun_verb(100):
        result = run('input2.txt', [noun, verb])
        if result == 19690720:
            print('Day 2, Part 2 gravity assist "1202 program alarm" result 19690720 found!')
            print('100 * {0} + {1} = {2}'.format(noun, verb, 100 * noun + verb))
            break


if __name__ == '__main__':
    main()
