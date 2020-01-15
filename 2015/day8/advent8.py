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

    def string_lengths(self, calc_length):
        total_length = 0
        for line in self.entries:
            total_length += calc_length(line)
        return total_length

    def code_string_lengths(self):
        return self.string_lengths(lambda s: len(s))

    def in_memory_string_lengths(self):
        return self.string_lengths(lambda s: len(eval(s)))

    def encoded_string_length(self):
        return self.string_lengths(
            lambda s: len(s) + 4 + (2 * (s.count('\"') - 2)) + (s.count('\\') + 2 - s.count('\"')))


def step1_string_length_difference(filename):
    santas_list = SanatasList.from_file(filename)
    return santas_list.code_string_lengths() - santas_list.in_memory_string_lengths()


def step2_string_length_difference(filename):
    santas_list = SanatasList.from_file(filename)
    return santas_list.encoded_string_length() - santas_list.code_string_lengths()


def test_test1_string_count_difference(filename, expected_difference):
    difference = step1_string_length_difference(filename)
    assert difference == expected_difference, \
        'Expected difference to be {0} but was {1}!'.format(expected_difference, difference)


def test_test2_string_count_difference(filename, expected_difference):
    difference = step2_string_length_difference(filename)
    assert difference == expected_difference, \
        'Expected difference to be {0} but was {1}!'.format(expected_difference, difference)


def main():
    test_test1_string_count_difference('test8a.txt', 12)
    print('Day 8, Step 1 difference = {0}'.format((step1_string_length_difference('input8.txt'))))
    test_test2_string_count_difference('test8a.txt', 19)
    print('Day 8, Step 2 difference = {0}'.format((step2_string_length_difference('input8.txt'))))


if __name__ == '__main__':
    main()
