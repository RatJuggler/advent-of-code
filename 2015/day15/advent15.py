from typing import List
import re
import itertools


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


def load_ingredients(filename: str) -> List[Ingredient]:
    ingredients = []
    with open(filename) as fh:
        for line in fh:
            ingredients.append(Ingredient.from_line(line))
    return ingredients


def calculate_property_score(ingredients: List[Ingredient], teaspoons: List[int], ingredient_property: str) -> int:
    property_score = 0
    for i in range(len(ingredients)):
        property_score += teaspoons[i] * getattr(ingredients[i], ingredient_property)
    return property_score if property_score > 0 else 0


def calculate_score(ingredients: List[Ingredient], teaspoons: List[int]) -> int:
    assert len(ingredients) == len(teaspoons), \
        'There should be one teaspoon ({0}) of every ingredient ({1})!'.format(len(teaspoons), len(ingredients))
    capacity = calculate_property_score(ingredients, teaspoons, 'capacity')
    durability = calculate_property_score(ingredients, teaspoons, 'durability')
    flavor = calculate_property_score(ingredients, teaspoons, 'flavor')
    texture = calculate_property_score(ingredients, teaspoons, 'texture')
    return capacity * durability * flavor * texture


def calculate_calories(ingredients: List[Ingredient], teaspoons: List[int]) -> int:
    assert len(ingredients) == len(teaspoons), \
        'There should be one teaspoon ({0}) of every ingredient ({1})!'.format(len(teaspoons), len(ingredients))
    calories = calculate_property_score(ingredients, teaspoons, 'calories')
    return calories


def find_best_ingredient_score(filename: str) -> [int, List[int]]:
    ingredients = load_ingredients(filename)
    best_score = 0
    best_teaspoons = []
    teaspoon_sizes = [i + 1 for i in range(99)]
    for teaspoons in [s for s in itertools.permutations(teaspoon_sizes, len(ingredients)) if sum(s) == 100]:
        score = calculate_score(ingredients, list(teaspoons))
        if score > best_score:
            best_score = score
            best_teaspoons = []
        if score == best_score:
            best_teaspoons.append(list(teaspoons))
    return best_score, best_teaspoons[0]


def find_best_ingredient_score_with_calories_of(filename: str, calorie_target: int) -> [int, List[int]]:
    ingredients = load_ingredients(filename)
    best_score = 0
    best_teaspoons = []
    teaspoon_sizes = [i + 1 for i in range(99)]
    for teaspoons in [s for s in itertools.permutations(teaspoon_sizes, len(ingredients)) if sum(s) == 100]:
        if calculate_calories(ingredients, list(teaspoons)) > calorie_target:
            continue
        score = calculate_score(ingredients, list(teaspoons))
        if score > best_score:
            best_score = score
            best_teaspoons = []
        if score == best_score:
            best_teaspoons.append(list(teaspoons))
    return best_score, best_teaspoons[0]


def test_find_best_ingredient_score(filename: str, expected_score: int, expected_teaspoons: List[int]) -> None:
    best_score, best_teaspoons = find_best_ingredient_score(filename)
    assert best_score == expected_score, \
        'Expected to get a best score of {0} but was {1}!'.format(expected_score, best_score)
    assert best_teaspoons == expected_teaspoons, \
        'Expected to use {0} teaspoons but was {1}!'.format(expected_teaspoons, best_teaspoons)


def test_find_best_ingredient_score_with_calories_of(filename: str, calorie_target: int,
                                                     expected_score: int, expected_teaspoons: List[int]):
    best_score, best_teaspoons = find_best_ingredient_score_with_calories_of(filename, calorie_target)
    assert best_score == expected_score, \
        'Expected to get a best score of {0} but was {1}!'.format(expected_score, best_score)
    assert best_teaspoons == expected_teaspoons, \
        'Expected to use {0} teaspoons but was {1}!'.format(expected_teaspoons, best_teaspoons)


def main() -> None:
    test_find_best_ingredient_score('test15a.txt', 62842880, [44, 56])
    best_score, teaspoons = find_best_ingredient_score('input15.txt')
    print('Day 15, Step 1 best ingredient score is {0} using {1} teaspoons.'.format(best_score, teaspoons))
    calorie_target = 500
    test_find_best_ingredient_score_with_calories_of('test15a.txt', 500, 57600000, [40, 60])
    best_score, teaspoons = find_best_ingredient_score_with_calories_of('input15.txt', calorie_target)
    print('Day 15, Step 2 best ingredient score with calories <= {0} is {1} using {2} teaspoons.'
          .format(calorie_target, best_score, teaspoons))


if __name__ == '__main__':
    main()
