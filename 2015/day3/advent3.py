from itertools import zip_longest
from typing import Dict, Iterable, Tuple
Position = Tuple[int, int]


def move_and_visit(position: Position, direction: str, visited_houses: Dict[Position, bool]) -> Position:
    x, y = position[0], position[1]
    x += 1 if direction == '>' else -1 if direction == '<' else 0
    y += 1 if direction == '^' else -1 if direction == 'v' else 0
    new_position = (x, y)
    house_visited = visited_houses.get(new_position)
    if house_visited is None:
        visited_houses[new_position] = True
    return new_position


def follow_directions(directions: str) -> int:
    position = (0, 0)
    visited_houses = {position: True}
    for direction in directions:
        position = move_and_visit(position, direction, visited_houses)
    return len(visited_houses)


def simple_test_step1(directions: str, expected_houses_visited: int) -> None:
    houses_visited = follow_directions(directions)
    assert houses_visited == expected_houses_visited, \
        'Expected Santa to visit {0} houses but was {1}!'.format(expected_houses_visited, houses_visited)


def load_directions(filename: str):
    with open(filename) as f:
        return f.readline()


def step1_count_houses_visited(filename: str) -> None:
    directions = load_directions(filename)
    houses_visited = follow_directions(directions)
    print('Day 3, Step 1 houses visited by Santa is {0}'.format(houses_visited))


def grouper(iterable: Iterable[str], n: int, fill_value: str = None) -> Iterable[str]:
    # Collect data into fixed-length chunks or blocks
    # grouper('ABCDEFG', 3, 'x') --> ABC DEF Gxx"
    args = [iter(iterable)] * n
    return zip_longest(*args, fillvalue=fill_value)


def santa_and_robo_santa_follow_directions(directions: str) -> int:
    santa_position = (0, 0)
    robo_santa_position = (0, 0)
    visited_houses = {santa_position: True}
    for direction in grouper(directions, 2):
        santa_position = move_and_visit(santa_position, direction[0], visited_houses)
        robo_santa_position = move_and_visit(robo_santa_position, direction[1], visited_houses)
    return len(visited_houses)


def simple_test_step2(directions: str, expected_houses_visited: int) -> None:
    houses_visited = santa_and_robo_santa_follow_directions(directions)
    assert houses_visited == expected_houses_visited, \
        'Expected Santa and Robo-Santa to visit {0} houses but was {1}!'.format(expected_houses_visited, houses_visited)


def step2_count_houses_visited(filename: str) -> None:
    directions = load_directions(filename)
    houses_visited = santa_and_robo_santa_follow_directions(directions)
    print('Day 3, Step 2 houses visited by Santa and Robo-Santa is {0}'.format(houses_visited))


def main() -> None:
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
