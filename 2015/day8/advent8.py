def character_count_difference(filename):
    return 0


def test_character_count_difference(filename, expected_difference):
    difference = character_count_difference(filename)
    assert difference == expected_difference, \
        'Expected difference to be {0} but was {1}!'.format(expected_difference, difference)


def main():
    test_character_count_difference('test8a.py', 12)


if __name__ == '__main__':
    main()
