class SanatasList:

    def __init__(self, entries):
        self.entries = entries

    @classmethod
    def from_file(cls, filename):
        entries = []
        with open(filename) as fh:
            for line in fh:
                entries.append(line)
        return SanatasList(entries)

    def code_string_lengths(self):
        total_length = 0
        for line in self.entries:
            total_length += len(line)
        return total_length

    def in_memory_string_lengths(self):
        total_length = 0
        for line in self.entries:
            total_length += len(line)
        return total_length


def string_length_difference(filename):
    list = SanatasList.from_file(filename)
    return list.code_string_lengths() - list.in_memory_string_lengths()


def test_string_count_difference(filename, expected_difference):
    difference = string_length_difference(filename)
    assert difference == expected_difference, \
        'Expected difference to be {0} but was {1}!'.format(expected_difference, difference)


def main():
    test_string_count_difference('test8a.txt', 12)


if __name__ == '__main__':
    main()
