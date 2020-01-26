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


def calculate_capacity(ingredients: List[Ingredient], teaspoons: List[int]) -> int:
    capacity = 0
    for i in range(len(ingredients)):
        capacity += teaspoons[i] * ingredients[i].capacity
    return capacity


def calculate_durability(ingredients: List[Ingredient], teaspoons: List[int]) -> int:
    durability = 0
    for i in range(len(ingredients)):
        durability += teaspoons[i] * ingredients[i].durability
    return durability


def calculate_flavour(ingredients: List[Ingredient], teaspoons: List[int]) -> int:
    flavour = 0
    for i in range(len(ingredients)):
        flavour += teaspoons[i] * ingredients[i].flavor
    return flavour


def calculate_texture(ingredients: List[Ingredient], teaspoons: List[int]) -> int:
    texture = 0
    for i in range(len(ingredients)):
        texture += teaspoons[i] * ingredients[i].texture
    return texture


def calculate_score(ingredients: List[Ingredient], teaspoons: List[int]) -> int:
    assert len(ingredients) == len(teaspoons), \
        'There should be one teaspoon ({0}) of every ingredient ({1})!'.format(len(teaspoons), len(ingredients))
    capacity = calculate_capacity(ingredients, teaspoons)
    durability = calculate_durability(ingredients, teaspoons)
    flavor = calculate_flavour(ingredients, teaspoons)
    texture = calculate_texture(ingredients, teaspoons)
    return capacity * durability * flavor * texture


def find_best_ingredient_score(filename: str):
    ingredients = []
    with open(filename) as fh:
        for line in fh:
            ingredients.append(Ingredient.from_line(line))
    print(ingredients)
    teaspoons = [44, 56]
    score = calculate_score(ingredients, teaspoons)
    print(teaspoons, score)
    return score, teaspoons


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
