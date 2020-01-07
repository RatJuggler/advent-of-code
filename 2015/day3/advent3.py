def follow_directions(directions):
    x, y = 0, 0
    houses_visited = 1
    for c in directions:
        x += 1 if c == '>' else -1 if c == '<' else 0
        y += 1 if c == '^' else -1 if c == 'v' else 0
        houses_visited += 1
    return houses_visited


def simple_test_step1(directions, expected_houses_visited):
    houses_visited = follow_directions(directions)
    assert houses_visited == expected_houses_visited, \
        'Expected to visit {0} houses but was {1}!'.format(expected_houses_visited, houses_visited)


def main():
    simple_test_step1('>', 2)
    simple_test_step1('^>v<', 4)
    simple_test_step1('^v^v^v^v^v', 2)


if __name__ == '__main__':
    main()
