import re
from typing import Callable


def count_matches(regex, string):
    return len(re.findall(regex, string))


def contains_three_vowels(string: str) -> bool:
    return count_matches(r'(a|e|i|o|u)', string) > 2


def repeating_letter_with_gap(string: str, gap_size: int) -> bool:
    return count_matches(r'([a-z])[a-z]{{{}}}\1'.format(gap_size), string) > 0


def does_not_contain(string: str) -> bool:
    return count_matches(r'(ab|cd|pq|xy)', string) == 0


def contains_non_overlapping_pair(string: str) -> bool:
    return count_matches(r'([a-z][a-z])[a-z]*?\1', string) > 0


def step1_is_nice_string(string: str) -> bool:
    return contains_three_vowels(string) and \
           repeating_letter_with_gap(string, 0) and \
           does_not_contain(string)


def step2_is_nice_string(string: str) -> bool:
    return contains_non_overlapping_pair(string) and \
           repeating_letter_with_gap(string, 1)


def process_strings(filename: str, is_nice_string: Callable[[str], bool]) -> int:
    nice_strings = 0
    with open(filename) as fh:
        for string in fh:
            if is_nice_string(string):
                nice_strings += 1
    return nice_strings


def simple_test_step1(string: str, expected_nice: bool) -> None:
    nice = step1_is_nice_string(string)
    assert nice == expected_nice, 'Expected string {0} to be {1} but was {2}!'.format(string, expected_nice, nice)


def step1_count_nice_strings(filename: str) -> None:
    nice_strings = process_strings(filename, step1_is_nice_string)
    print('Day 5, Step 1 nice strings found {0}.'.format(nice_strings))


def simple_test_step2(string: str, expected_nice: bool) -> None:
    nice = step2_is_nice_string(string)
    assert nice == expected_nice, 'Expected string {0} to be {1} but was {2}!'.format(string, expected_nice, nice)


def step2_count_nice_strings(filename: str) -> None:
    nice_strings = process_strings(filename, step2_is_nice_string)
    print('Day 5, Step 2 nice strings found {0}.'.format(nice_strings))


def main() -> None:
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
