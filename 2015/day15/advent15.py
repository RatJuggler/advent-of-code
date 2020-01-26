from typing import List
import re


class Ingredient:

    def __init__(self, name: str, capacity: int, durability: int, flavor: int, texture: int, calories: int) -> None:
        self.name = name
        self.capacity = capacity
        self.durability = durability
        self.flavor = flavor
        self.texture = texture
        self.calories = calories

    @classmethod
    def from_line(cls, line: str):
        regex = r'(?P<name>\S+): capacity (?P<capacity>-?\d+), durability (?P<durability>-?\d+), ' \
                r'flavor (?P<flavor>-?\d+), texture (?P<texture>-?\d+), calories (?P<calories>-?\d+)'
        matches = re.match(regex, line)
        name = matches.group('name')
        capacity = int(matches.group('capacity'))
        durability = int(matches.group('durability'))
        flavor = int(matches.group('flavor'))
        texture = int(matches.group('texture'))
        calories = int(matches.group('calories'))
        return Ingredient(name, capacity, durability, flavor, texture, calories)

    def __repr__(self) -> str:
        return "{0}(capacity: {1}, durability: {2}, flavor: {3}, texture: {4}, calories: {5})"\
            .format(self.name, self.capacity, self.durability, self.flavor, self.texture, self.calories)


def find_best_ingredient_score(filename: str):
    with open(filename) as fh:
        for line in fh:
            print(Ingredient.from_line(line))
    return 0, []


def test_find_best_ingredient_score(filename: str, expected_score: int, expected_teaspoons: List[int]) -> None:
    best_score, teaspoons = find_best_ingredient_score(filename)
    assert best_score == expected_score, \
        'Expected to get a best score of {0} but was {1}!'.format(expected_score, best_score)
    assert teaspoons == expected_teaspoons, \
        'Expected to use {0} teaspoons but was {1}!'.format(expected_teaspoons, teaspoons)


def main() -> None:
    test_find_best_ingredient_score('test15a.txt', 62842880, [44, 56])
    best_score, teaspoons = find_best_ingredient_score('input15.txt')
    print('Day 15, Step 1 best ingredient score is {0} using {1} teaspoons.'.format(best_score, teaspoons))


if __name__ == '__main__':
    main()
