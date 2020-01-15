def find_shortest_distance(filename):
    pass


def test_find_shortest_route(filename, expected_shortest_distance):
    shortest_distance = find_shortest_distance(filename)
    assert shortest_distance == expected_shortest_distance, \
        'Expected to find shortest distance of {0} but was {1}'.format(expected_shortest_distance, shortest_distance)


def main():
    test_find_shortest_route('test9a.txt', 602)


if __name__ == '__main__':
    main()
