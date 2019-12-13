def follow_directions(directions):
    floor = 0
    for c in directions:
        floor += 1 if c == '(' else -1 if c == ')' else 0
    return floor


def simple_test_step1(directions, expected_floor):
    floor = follow_directions(directions)
    assert floor == expected_floor, 'Expected to be on floor {0} but now on floor {1}!'.format(expected_floor, floor)


def load_directions(filename):
    with open(filename) as f:
        return f.readline()


def step1_load_and_follow_directions(filename):
    directions = load_directions(filename)
    floor = follow_directions(directions)
    print('Day 1, Step 1 directions lead to floor {0}'.format(floor))


def main():
    simple_test_step1('(())', 0)
    simple_test_step1('()()', 0)
    simple_test_step1('(((', 3)
    simple_test_step1('(()(()(', 3)
    simple_test_step1('))(((((', 3)
    simple_test_step1('())', -1)
    simple_test_step1('))(', -1)
    simple_test_step1(')))', -3)
    simple_test_step1(')())())', -3)
    step1_load_and_follow_directions('input1.txt')


if __name__ == '__main__':
    main()
