class SanatasList:

    def __init__(self, entries):
        self.entries = entries

    @classmethod
    def from_file(cls, filename):
        entries = []
        with open(filename) as fh:
            for line in fh:
                entries.append(line.strip())
        return SanatasList(entries)

    def code_string_lengths(self):
        total_length = 0
        for line in self.entries:
            total_length += len(line)
        return total_length

    def in_memory_string_lengths(self):
        total_length = 0
        for line in self.entries:
            total_length += len(eval(line))
        return total_length


def string_length_difference(filename):
    santas_list = SanatasList.from_file(filename)
    return santas_list.code_string_lengths() - santas_list.in_memory_string_lengths()


def test_string_count_difference(filename, expected_difference):
    difference = string_length_difference(filename)
    assert difference == expected_difference, \
        'Expected difference to be {0} but was {1}!'.format(expected_difference, difference)


def main():
    test_string_count_difference('test8a.txt', 12)
    print('Day 8, Step 1 difference = {0}'.format((string_length_difference('input8.txt'))))


if __name__ == '__main__':
    main()
