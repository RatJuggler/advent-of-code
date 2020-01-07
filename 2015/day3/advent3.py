from itertools import zip_longest


def move_and_visit(position, direction, visited_houses):
    x, y = position[0], position[1]
    x += 1 if direction == '>' else -1 if direction == '<' else 0
    y += 1 if direction == '^' else -1 if direction == 'v' else 0
    new_position = (x, y)
    house_visited = visited_houses.get(new_position)
    if house_visited is None:
        visited_houses[new_position] = True
    return new_position, visited_houses


def follow_directions(directions):
    position = (0, 0)
    visited_houses = {position: True}
    for direction in directions:
        position, visited_houses = move_and_visit(position, direction, visited_houses)
    return len(visited_houses)


def simple_test_step1(directions, expected_houses_visited):
    houses_visited = follow_directions(directions)
    assert houses_visited == expected_houses_visited, \
        'Expected Santa to visit {0} houses but was {1}!'.format(expected_houses_visited, houses_visited)


def load_directions(filename):
    with open(filename) as f:
        return f.readline()


def step1_count_houses_visited(filename):
    directions = load_directions(filename)
    houses_visited = follow_directions(directions)
    print('Day 3, Step 1 houses visited by Santa is {0}'.format(houses_visited))


def grouper(iterable, n, fillvalue=None):
    # Collect data into fixed-length chunks or blocks
    # grouper('ABCDEFG', 3, 'x') --> ABC DEF Gxx"
    args = [iter(iterable)] * n
    return zip_longest(*args, fillvalue=fillvalue)


def santa_and_robo_santa_follow_directions(directions):
    santa_position = (0, 0)
    robo_santa_position = (0, 0)
    visited_houses = {santa_position: True}
    for direction in grouper(directions, 2):
        santa_position, visited_houses = move_and_visit(santa_position, direction[0], visited_houses)
        robo_santa_position, visited_houses = move_and_visit(robo_santa_position, direction[1], visited_houses)
    return len(visited_houses)


def simple_test_step2(directions, expected_houses_visited):
    houses_visited = santa_and_robo_santa_follow_directions(directions)
    assert houses_visited == expected_houses_visited, \
        'Expected Santa and Robo-Santa to visit {0} houses but was {1}!'.format(expected_houses_visited, houses_visited)


def step2_count_houses_visited(filename):
    directions = load_directions(filename)
    houses_visited = santa_and_robo_santa_follow_directions(directions)
    print('Day 3, Step 2 houses visited by Santa and Robo-Santa is {0}'.format(houses_visited))


def main():
    simple_test_step1('>', 2)
    simple_test_step1('^>v<', 4)
    simple_test_step1('^v^v^v^v^v', 2)
    step1_count_houses_visited('input3.txt')
    simple_test_step2('^v', 3)
    simple_test_step2('^>v<', 3)
    simple_test_step2('^v^v^v^v^v', 11)
    step2_count_houses_visited('input3.txt')


if __name__ == '__main__':
    main()
