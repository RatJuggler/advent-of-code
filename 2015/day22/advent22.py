from abc import ABC, abstractmethod
from collections import namedtuple
from typing import Dict, List
import sys
CastSpell = namedtuple('CastSpell', 'duration spell_nbr')


def log(message: str) -> None:
    if False:
        print(message)


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
    def turn(self, lowest_mana_spent: int) -> bool:
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
        self.mana_spent = 0
        self.spell_book = spell_book
        self.spells_to_cast = spells_to_cast
        self.next_spell_to_cast = 0
        self.cast_spells = []
        self.damage = 0

    def spell_already_in_progress(self, spell_to_cast_nbr: int) -> bool:
        for cast_spell in self.cast_spells:
            if cast_spell.spell_nbr == spell_to_cast_nbr:
                return True
        return False

    def turn(self, lowest_mana_spent: int) -> bool:
        if self.mana_spent > lowest_mana_spent:
            return False
        if self.next_spell_to_cast >= len(self.spells_to_cast):
            log('{0} has no more spells to cast!'.format(self.name))
            return False
        spell_to_cast_nbr = self.spells_to_cast[self.next_spell_to_cast]
        spell_to_cast = self.spell_book[spell_to_cast_nbr]
        if self.spell_already_in_progress(spell_to_cast_nbr):
            log('{0} already has spell {1} active!'.format(self.name, spell_to_cast.name))
            return False
        if spell_to_cast.cost > self.mana:
            log('{0} fails to cast {1}, insufficient mana!'.format(self.name, spell_to_cast.name))
            self.hp = 0  # If you cannot afford to cast any spell, you loose.
            return False
        self.mana -= spell_to_cast.cost
        self.mana_spent += spell_to_cast.cost
        log('{0} casts {1}!'.format(self.name, spell_to_cast.name))
        if spell_to_cast.duration > 0:
            self.cast_spells.append(CastSpell(spell_to_cast.duration, spell_to_cast_nbr))
        else:
            log('{0} applies {1} instantly!'.format(spell_to_cast.name, spell_to_cast.effects))
            self.apply_effects(spell_to_cast.effects)
        self.next_spell_to_cast += 1
        return True

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
        cast_spells = []
        for cast_spell in self.cast_spells:
            duration = cast_spell.duration - 1
            spell = self.spell_book[cast_spell.spell_nbr]
            self.apply_effects(spell.effects)
            log('{0} applies {1}, duration is now {2}.'.format(spell.name, spell.effects, duration))
            if duration > 0:
                cast_spells.append(CastSpell(duration, cast_spell.spell_nbr))
            else:
                self.remove_effects(spell.effects)
                log('{0} wears off!'.format(spell.name))
        self.cast_spells = cast_spells

    def __repr__(self) -> str:
        return super().__repr__() + ', Mana: {0}'.format(self.mana)


class Fighter(Character):

    def __init__(self, name: str, hp: int, weapon_damage: int) -> None:
        super().__init__(name, hp)
        self.weapon_damage = weapon_damage

    def turn(self, lowest_mana_spent: int) -> bool:
        self.damage = self.weapon_damage
        log('{0} attacks with {1} damage!'.format(self.name, self.damage))
        return True

    def apply_current_effects(self) -> None:
        # Fighter could inflict bleeding damage which lasts for several turns but currently has no such effects.
        self.damage = 0

    def __repr__(self) -> str:
        return super().__repr__()


def fight(hero: Character, boss: Character, lowest_mana_spent: int) -> int:
    while True:
        log('-- Hero turn --')
        log('{0} vs {1}'.format(hero, boss))
        hero.apply_current_effects()
        boss.apply_current_effects()
        if not hero.turn(lowest_mana_spent) or \
                boss.dead_after_damage(hero.damage) or \
                hero.dead_after_damage(boss.damage):
            break
        log('-- Boss turn --')
        log('{0} vs {1}'.format(hero, boss))
        hero.apply_current_effects()
        boss.apply_current_effects()
        if not boss.turn(lowest_mana_spent) or \
                boss.dead_after_damage(hero.damage) or \
                hero.dead_after_damage(boss.damage):
            break
    winner = 1 if boss.hp <= 0 else 2
    log('{0} wins!'.format(hero.name if winner == 1 else boss.name))
    return winner


def spells_available() -> List[Spell]:
    return [Spell('Magic Missile', 53, 0, {'damage': 4}),
            Spell('Drain', 73, 0, {'damage': 2, 'hp': 2}),
            Spell('Shield', 113, 6, {'armour': 7}),
            Spell('Poison', 173, 6, {'damage': 3}),
            Spell('Recharge', 229, 5, {'mana': 101})]
#

def test_fight1() -> None:
    # Hero casts: Poison, Magic Missile
    test_hero = Mage('Hero', 10, 250, spells_available(), [3, 0])
    test_boss = Fighter('Boss', 13, 8)
    winner = fight(test_hero, test_boss, sys.maxsize)
    assert winner == 1, 'Expected hero to win!'
    assert test_hero.hp == 2 and test_hero.armour == 0 and test_hero.mana == 24


def test_fight2() -> None:
    # Hero casts: Recharge, Shield, Drain, Poison, Magic Missile
    test_hero = Mage('Hero', 10, 250, spells_available(), [4, 2, 1, 3, 0])
    test_boss = Fighter('Boss', 14, 8)
    winner = fight(test_hero, test_boss, sys.maxsize)
    assert winner == 1, 'Expected hero to win!'
    assert test_hero.hp == 1 and test_hero.armour == 0 and test_hero.mana == 114


def next_spells_to_cast(spells_to_cast: List[int], spell_idx: int, nbr_of_spells: int) -> int:
    spells_to_cast[spell_idx] += 1
    if spells_to_cast[spell_idx] >= nbr_of_spells:
        spells_to_cast[spell_idx] = 0
        if spell_idx > 0:
            return next_spells_to_cast(spells_to_cast, spell_idx - 1, nbr_of_spells)
        spells_to_cast.append(0)
        print('Now trying {0} permutations of spells!'.format(len(spells_to_cast)))
    return len(spells_to_cast) - 1


def main() -> None:
    test_fight1()
    test_fight2()
    spells_to_cast = [-1]
    lowest_mana_spent = sys.maxsize
    spell_idx = 0
    while True:
        spell_idx = next_spells_to_cast(spells_to_cast, spell_idx, len(spells_available()))
        # print(spells_to_cast)
        hero = Mage('Hero', 50, 500, spells_available(), spells_to_cast)
        boss = Fighter('Boss', 58, 9)
        winner = fight(hero, boss, lowest_mana_spent)
        if winner == 1 and hero.mana_spent < lowest_mana_spent:
            lowest_mana_spent = hero.mana_spent
            print('Lowest mana spent = {0} {1}'.format(lowest_mana_spent, spells_to_cast))


if __name__ == '__main__':
    main()
