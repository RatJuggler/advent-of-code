def find_winning_distance(filename, after):
    return 0


def test_winning_distance(filename, after, expected_distance):
    distance = find_winning_distance(filename, after)
    assert distance == expected_distance, \
        'Expected to find winning distance of {0} but was {1}'.format(expected_distance, distance)


def main():
    test_winning_distance('test14a.txt', 1000, 1120)


if __name__ == '__main__':
    main()
