from abc import ABC, abstractmethod
from typing import Dict, List


class Spell:

    def __init__(self, name: str, cost: int, duration: int, effects: Dict[str, int]) -> None:
        self.name = name
        self.cost = cost
        self.duration = duration
        self.effects = effects


class Character(ABC):

    def __init__(self, name: str, hp: int) -> None:
        self.name = name
        self.hp = hp
        self.armour = 0
        self.damage = 0

    @abstractmethod
    def turn(self) -> None:
        pass

    @abstractmethod
    def apply_current_effects(self) -> None:
        pass

    def __repr__(self) -> str:
        return '{0} - HP:{1}, Armour:{2}'.format(self.name, self.hp, self.armour)


class Mage(Character):

    def __init__(self, name: str, hp: int, mana: int, spell_book: List[Spell], spells_to_cast: List[int]) -> None:
        super().__init__(name, hp)
        self.mana = mana
        self.spell_book = spell_book
        self.spells_to_cast = spells_to_cast
        self.current_cast = 0
        self.current_effects = []
        self.damage = 0

    def turn(self) -> None:
        self.apply_current_effects()
        cast_spell = self.spell_book[self.spells_to_cast[self.current_cast]]
        print('{0} casts {1}!'.format(self.name, cast_spell.name))
        self.mana -= cast_spell.cost
        if cast_spell.duration > 0:
            self.current_effects.append([cast_spell.duration, cast_spell])
        else:
            print('{0} applies {1} instantly!'.format(cast_spell.name, cast_spell.effects))
            self.apply_effects(cast_spell.effects)
        self.current_cast += 1

    def apply_effects(self, spell_effects: Dict[str, int]) -> None:
        for attribute in spell_effects:
            setattr(self, attribute, getattr(self, attribute) + spell_effects[attribute])

    def apply_current_effects(self) -> None:
        self.damage = 0
        self.armour = 0
        for effects in self.current_effects:
            effects[0] -= 1
            spell = effects[1]
            self.apply_effects(spell.effects)
            print('{0} applies {1}, duration is now {2}!'.format(spell.name, spell.effects, effects[0]))
        self.current_effects = [effects for effects in self.current_effects if effects[0] > 0]

    def __repr__(self) -> str:
        return super().__repr__() + ', Mana: {0}'.format(self.mana)


class Fighter(Character):

    def __init__(self, name: str, hp: int, damage: int) -> None:
        super().__init__(name, hp)
        self.damage = damage

    def turn(self) -> None:
        print('{0} attacks with {1} damage!'.format(self.name, self.damage))

    def apply_current_effects(self) -> None:
        pass

    def __repr__(self) -> str:
        return super().__repr__() + ', Damage: {0}'.format(self.damage)


def spells_available() -> List[Spell]:
    return [Spell('Magic Missile', 53, 0, {'damage': 4}),
            Spell('Drain', 73, 0, {'damage': 2, 'hp': 2}),
            Spell('Shield', 113, 6, {'armour': 7}),
            Spell('Poison', 173, 6, {'damage': 3}),
            Spell('Recharge', 229, 5, {'mana': 101})]


def fight(hero: Character, boss: Character) -> int:
    print('{0} vs {1}'.format(hero, boss))
    while True:
        print('-- Hero turn --')
        hero.turn()
        hero_damage = 1 if boss.armour > hero.damage else hero.damage - boss.armour
        boss.hp -= hero_damage
        print('-- Boss turn --')
        hero.apply_current_effects()
        hero_damage = 1 if boss.armour > hero.damage else hero.damage - boss.armour
        boss.hp -= hero_damage
        print('{0} vs {1}'.format(hero, boss))
        if boss.hp <= 0:
            break
        boss.turn()
        boss_damage = 1 if hero.armour > boss.damage else boss.damage - hero.armour
        hero.hp -= boss_damage
        if hero.hp <= 0:
            break
        print('{0} vs {1}'.format(hero, boss))
    winner = 1 if boss.hp <= 0 else 2
    print('{0} wins!'.format(hero.name if winner == 1 else boss.name))
    return winner


def test_fight() -> None:
    test_hero = Mage('Hero', 10, 250, spells_available(), [3, 0])
    test_boss = Fighter('Boss', 13, 8)
    winner = fight(test_hero, test_boss)
    assert winner == 1, 'Expected hero to win!'


def main() -> None:
    test_fight()


if __name__ == '__main__':
    main()
