def follow_directions(directions):
    x, y = 0, 0
    visited_houses = {(x, y): 1}
    houses_visited = 1
    for c in directions:
        x += 1 if c == '>' else -1 if c == '<' else 0
        y += 1 if c == '^' else -1 if c == 'v' else 0
        house = (x, y)
        house_visits = visited_houses.get(house)
        if house_visits is None:
            visited_houses[house] = 1
            houses_visited += 1
        else:
            visited_houses[house] = house_visits + 1
    return houses_visited


def simple_test_step1(directions, expected_houses_visited):
    houses_visited = follow_directions(directions)
    assert houses_visited == expected_houses_visited, \
        'Expected to visit {0} houses but was {1}!'.format(expected_houses_visited, houses_visited)


def load_directions(filename):
    with open(filename) as f:
        return f.readline()


def step1_count_houses_visited(filename):
    directions = load_directions(filename)
    houses_visited = follow_directions(directions)
    print('Day 3, Step 1 houses visited is {0}'.format(houses_visited))


def main():
    simple_test_step1('>', 2)
    simple_test_step1('^>v<', 4)
    simple_test_step1('^v^v^v^v^v', 2)
    step1_count_houses_visited('input3.txt')


if __name__ == '__main__':
    main()