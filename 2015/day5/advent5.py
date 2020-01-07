def contains_three_vowels(string):
    vowel_count = 0
    for v in 'aeiou':
        vowel_count += string.count(v)
    return vowel_count > 2


def repeating_letter_with_separator(string, separator):
    separator += 1
    for i in range(len(string) - separator):
        if string[i] == string[i + separator]:
            return True
    return False


def does_not_contain(string):
    return not('ab' in string or 'cd' in string or 'pq' in string or 'xy' in string)


def step1_is_nice_string(string):
    return contains_three_vowels(string) and \
           repeating_letter_with_separator(string, 0) and \
           does_not_contain(string)


def process_strings(filename, is_nice_string):
    nice_strings = 0
    with open(filename) as fh:
        for string in fh:
            if is_nice_string(string):
                nice_strings += 1
    return nice_strings


def simple_test_step1(string, expected_nice):
    nice = step1_is_nice_string(string)
    assert nice == expected_nice, 'Expected string {0} to be {1} but was {2}!'.format(string, expected_nice, nice)


def step1_count_nice_strings(filename):
    nice_strings = process_strings(filename, step1_is_nice_string)
    print('Day 5, Step 1 nice strings found {0}.'.format(nice_strings))


def contains_non_overlapping_pair(string):
    for i in range(len(string) - 4):
        pair = string[i:i + 2]
        if string.find(pair, i + 2) != -1:
            return True
    return False


def step2_is_nice_string(string):
    return contains_non_overlapping_pair(string) and \
           repeating_letter_with_separator(string, 1)


def simple_test_step2(string, expected_nice):
    nice = step2_is_nice_string(string)
    assert nice == expected_nice, 'Expected string {0} to be {1} but was {2}!'.format(string, expected_nice, nice)


def step2_count_nice_strings(filename):
    nice_strings = process_strings(filename, step2_is_nice_string)
    print('Day 5, Step 2 nice strings found {0}.'.format(nice_strings))


def main():
    simple_test_step1('ugknbfddgicrmopn', True)
    simple_test_step1('aaa', True)
    simple_test_step1('jchzalrnumimnmhp', False)
    simple_test_step1('haegwjzuvuyypxyu', False)
    simple_test_step1('dvszwmarrgswjxmb', False)
    step1_count_nice_strings('input5.txt')
    simple_test_step2('qjhvhtzxzqqjkmpb', True)
    simple_test_step2('xxyxx', True)
    simple_test_step2('uurcxstgmygtbstg', False)
    simple_test_step2('ieodomkazucvgmuy', False)
    step2_count_nice_strings('input5.txt')


if __name__ == '__main__':
    main()
