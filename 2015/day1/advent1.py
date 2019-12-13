def follow_directions(directions):
    return 0


def simple_test(directions, expected_floor):
    floor = follow_directions(directions)
    assert floor == expected_floor, 'Expected to be on floor {0} but now on floor {1}!'.format(expected_floor, floor)


def main():
    simple_test('(())', 0)
    simple_test('()()', 0)
    simple_test('(((', 3)
    simple_test('(()(()(', 3)
    simple_test('))(((((', 3)
    simple_test('())', -1)
    simple_test('))(', -1)
    simple_test(')))', -3)
    simple_test(')())())', -3)


if __name__ == '__main__':
    main()
