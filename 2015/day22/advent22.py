from abc import ABC, abstractmethod
from typing import Dict, List


class Spell:

    def __init__(self, name: str, cost: int, duration: int, effects: Dict[str: int]) -> None:
        self.name = name
        self.cost = cost
        self.duration = duration
        self.effects = effects


class Character(ABC):

    def __init__(self, name: str, hp: int, armour: int = 0) -> None:
        self.name = name
        self.hp = hp
        self.armour = armour

    @abstractmethod
    def turn(self, light: int) -> int:
        pass

    def __repr__(self) -> str:
        return '{0}(HP:{1}, Armour:{2})'.format(self.name, self.hp, self.armour)


class Mage(Character):

    def __init__(self, name: str, hp: int, mana: int) -> None:
        super().__init__(name, hp)
        self.mana = mana


class Fighter(Character):

    def __init__(self, name: str, hp: int, damage: int) -> None:
        super().__init__(name, hp)
        self.damage = damage


def spells() -> List[Spell]:
    return [Spell('Magic Missile', 53, 0, {'damage': -4}),
            Spell('Drain', 73, 0, {'damage': -2, 'hp': 2}),
            Spell('Shield', 113, 6, {'armour': 7}),
            Spell('Poison', 173, 6, {'hp': -3}),
            Spell('Recharge', 229, 5, {'mana': 101})]


def fight(hero: Character, boss: Character) -> int:
    print('{0} vs {1}'.format(hero, boss))
    hero_damage = 1 if boss.armour > hero.damage else hero.damage - boss.armour + 1
    boss_damage = 1 if hero.armour > boss.damage else boss.damage - hero.armour + 1
    while hero.hp > 0 and boss.hp > 0:
        hero.hp -= boss_damage
        boss.hp -= hero_damage
    winner = 1 if boss.hp <= 0 else 2
    print('{0} wins!'.format(hero.name if winner == 1 else boss.name))
    return winner


def test_fight() -> None:
    test_hero = Mage('Hero', 10, 250)
    test_boss = Fighter('Boss', 13, 8)
    winner = fight(test_hero, test_boss)
    assert winner == 1, 'Expected hero to win!'


def main() -> None:
    test_fight()


if __name__ == '__main__':
    main()
