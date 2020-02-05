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

    def dead_after_damage(self, damage: int) -> bool:
        self.hp -= 0 if damage == 0 else 1 if self.armour >= damage else damage - self.armour
        return self.hp <= 0

    @abstractmethod
    def turn(self) -> None:
        pass

    @abstractmethod
    def apply_current_effects(self) -> None:
        pass

    def __repr__(self) -> str:
        return '{0} - HP:{1}, Armour:{2}, Damage: {3}'.format(self.name, self.hp, self.armour, self.damage)


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

    def remove_effects(self, spell_effects: Dict[str, int]) -> None:
        for attribute in spell_effects:
            if attribute in ['damage', 'armour']:
                setattr(self, attribute, getattr(self, attribute) - spell_effects[attribute])

    def apply_current_effects(self) -> None:
        self.damage = 0
        self.armour = 0
        for effects in self.current_effects:
            effects[0] -= 1
            spell = effects[1]
            self.apply_effects(spell.effects)
            print('{0} applies {1}, duration is now {2}!'.format(spell.name, spell.effects, effects[0]))
        current_effects = []
        for effects in self.current_effects:
            if effects[0] > 0:
                current_effects.append(effects)
            else:
                spell = effects[1]
                self.remove_effects(spell.effects)
                print('{0} wears off!'.format(spell.name))
        self.current_effects = current_effects

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
        return super().__repr__()


def fight(hero: Character, boss: Character) -> int:
    while True:
        print('-- Hero turn --')
        print('{0} vs {1}'.format(hero, boss))
        hero.turn()
        if boss.dead_after_damage(hero.damage):
            break
        print('-- Boss turn --')
        print('{0} vs {1}'.format(hero, boss))
        hero.apply_current_effects()
        if boss.dead_after_damage(hero.damage):
            break
        boss.turn()
        if hero.dead_after_damage(boss.damage):
            break
    winner = 1 if boss.hp <= 0 else 2
    print('{0} wins!'.format(hero.name if winner == 1 else boss.name))
    return winner


def spells_available() -> List[Spell]:
    return [Spell('Magic Missile', 53, 0, {'damage': 4}),
            Spell('Drain', 73, 0, {'damage': 2, 'hp': 2}),
            Spell('Shield', 113, 6, {'armour': 7}),
            Spell('Poison', 173, 6, {'damage': 3}),
            Spell('Recharge', 229, 5, {'mana': 101})]


def test_fight1() -> None:
    # Hero casts: Poison, Magic Missile
    test_hero = Mage('Hero', 10, 250, spells_available(), [3, 0])
    test_boss = Fighter('Boss', 13, 8)
    winner = fight(test_hero, test_boss)
    assert winner == 1, 'Expected hero to win!'
    assert test_hero.hp == 2 and test_hero.armour == 0 and test_hero.mana == 24


def test_fight2() -> None:
    # Hero casts: Recharge, Shield, Drain, Poison, Magic Missile
    test_hero = Mage('Hero', 10, 250, spells_available(), [4, 2, 1, 3, 0])
    test_boss = Fighter('Boss', 14, 8)
    winner = fight(test_hero, test_boss)
    assert winner == 1, 'Expected hero to win!'
    assert test_hero.hp == 1 and test_hero.armour == 0 and test_hero.mana == 114


def main() -> None:
    test_fight1()
    test_fight2()
    # hero = Mage('Hero', 50, 500, spells_available(), [???])
    # boss = Fighter('Boss', 58, 9)
    # winner = fight(hero, boss)


if __name__ == '__main__':
    main()
