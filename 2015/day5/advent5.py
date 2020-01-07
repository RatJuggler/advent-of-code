def is_nice_string(string):
    return True


def simple_test_step1(string, expected_nice):
    nice = is_nice_string(string)
    assert nice == expected_nice, 'Expected string {0} to be {1} but was {2}!'.format(string, expected_nice, nice)


def step1_count_nice_strings(filename):
    nice_strings = 0
    with open(filename) as fh:
        for string in fh:
            if is_nice_string(string):
                nice_strings += 1
    print('Day 5, Step nice strings found {0}.'.format(nice_strings))


def main():
    simple_test_step1('ugknbfddgicrmopn', True)
    simple_test_step1('aaa', True)
    simple_test_step1('jchzalrnumimnmhp', False)
    simple_test_step1('haegwjzuvuyypxyu', False)
    simple_test_step1('dvszwmarrgswjxmb', False)
    step1_count_nice_strings('input5.txt')


if __name__ == '__main__':
    main()
