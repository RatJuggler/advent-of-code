from typing import List


class Item:

    def __init__(self, name: str, cost: int, damage: int, armour: int) -> None:
        self.name = name
        self.cost = cost
        self.damage = damage
        self.armour = armour


class Character:

    def __init__(self, hp: int, damage: int, armour: int) -> None:
        self.hp = hp
        self.damage = damage
        self.armour = armour

    def new_items(self) -> None:
        self.damage = 0
        self.armour = 0

    def add_item(self, item: Item):
        self.damage += item.damage
        self.armour += item.armour


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
    return [Item('Damage +1',  25, 1, 0),
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
        print(hero.hp, boss.hp)
    return 1 if boss.hp <= 0 else 2


def prepare_and_fight(hero: Character):
    for weapon in weapons_for_sale():
        for armour in armour_for_sale():
            for rings in rings_for_sale():
                hero.new_items()
                hero.add_item(weapon)
                hero.add_item(armour)
                hero.add_item(rings)


def test_fight():
    test_hero = Character(8, 5, 5)
    test_boss = Character(12, 7, 2)
    winner = fight(test_hero, test_boss)
    assert winner == 1, 'Expected hero to win!'


def main() -> None:
    test_fight()


if __name__ == '__main__':
    main()
