from typing import List
import itertools


class Item:

    def __init__(self, name: str, cost: int, damage: int, armour: int) -> None:
        self.name = name
        self.cost = cost
        self.damage = damage
        self.armour = armour


class Character:

    def __init__(self, name: str, hp: int, damage: int = 0, armour: int = 0) -> None:
        self.name = name
        self.hp = hp
        self.damage = damage
        self.armour = armour
        self.gold_spent = 0

    def add_item(self, item: Item):
        self.damage += item.damage
        self.armour += item.armour
        self.gold_spent += item.cost

    def __repr__(self) -> str:
        return '{0}(HP:{1}, Damage:{2}, Armour:{3})'.format(self.name, self.hp, self.damage, self.armour)


def weapons_for_sale() -> List[Item]:
    return [Item('Dagger',      8, 4, 0),
            Item('Shortsword', 10, 5, 0),
            Item('Warhammer',  25, 6, 0),
            Item('Longsword',  40, 7, 0),
            Item('Greataxe',   74, 8, 0)]


def armour_for_sale() -> List[Item]:
    return [Item('None',        0, 0, 0),
            Item('Leather',    13, 0, 1),
            Item('Chainmail',  31, 0, 2),
            Item('Splintmail', 53, 0, 3),
            Item('Bandedmail', 75, 0, 4),
            Item('Platemail', 102, 0, 5)]


def rings_for_sale() -> List[Item]:
    return [Item('None',        0, 0, 0),
            Item('None',        0, 0, 0),
            Item('Damage +1',  25, 1, 0),
            Item('Damage +2',  50, 2, 0),
            Item('Damage +3', 100, 3, 0),
            Item('Defense +1', 20, 0, 1),
            Item('Defense +2', 40, 0, 2),
            Item('Defense +3', 80, 0, 3)]


def fight(hero: Character, boss: Character) -> int:
    hero_damage = 1 if boss.armour > hero.damage else hero.damage - boss.armour + 1
    boss_damage = 1 if hero.armour > boss.damage else boss.damage - hero.armour + 1
    while hero.hp > 0 and boss.hp > 0:
        hero.hp -= boss_damage
        boss.hp -= hero_damage
    return 1 if boss.hp <= 0 else 2


def prepare_and_fight() -> int:
    lowest_cost_win = None
    for weapon in weapons_for_sale():
        for armour in armour_for_sale():
            for rings in itertools.permutations(rings_for_sale(), 2):
                hero = Character('Hero', 100)
                hero.add_item(weapon)
                hero.add_item(armour)
                hero.add_item(rings[0])
                hero.add_item(rings[1])
                boss = Character('Boss', 109, 8, 2)
                print('{0} vs {1}'.format(hero, boss))
                winner = fight(hero, boss)
                print('{0} wins!'.format(hero.name if winner == 1 else boss.name))
                if winner == 1 and (not lowest_cost_win or hero.gold_spent < lowest_cost_win):
                    lowest_cost_win = hero.gold_spent
    return lowest_cost_win


def test_fight():
    test_hero = Character('Hero', 8, 5, 5)
    test_boss = Character('Boss', 12, 7, 2)
    winner = fight(test_hero, test_boss)
    assert winner == 1, 'Expected hero to win!'


def main() -> None:
    test_fight()
    lowest_cost_win = prepare_and_fight()
    print('Day 21, Step 1 lowest cost win cost {0} gold.'.format(lowest_cost_win))


if __name__ == '__main__':
    main()
