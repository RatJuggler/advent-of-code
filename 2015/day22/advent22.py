from abc import ABC, abstractmethod
from collections import namedtuple
from copy import deepcopy
from typing import Dict
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


SPELL_BOOK = [Spell('Magic Missile', 53, 0, {'damage': 4}),
              Spell('Drain', 73, 0, {'damage': 2, 'hp': 2}),
              Spell('Shield', 113, 6, {'armour': 7}),
              Spell('Poison', 173, 6, {'damage': 3}),
              Spell('Recharge', 229, 5, {'mana': 101})]


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
    def turn(self) -> bool:
        pass

    @abstractmethod
    def apply_current_effects(self) -> None:
        pass

    def __repr__(self) -> str:
        return '{0} - HP:{1}, Armour:{2}, Damage: {3}'.format(self.name, self.hp, self.armour, self.damage)


class Mage(Character):

    def __init__(self, name: str, hp: int, mana: int) -> None:
        super().__init__(name, hp)
        self.mana = mana
        self.mana_spent = 0
        self.spells_cast_history = []
        self.next_spell_to_cast = 0
        self.spells_in_effect = []
        self.damage = 0

    def spell_already_in_progress(self, spell_to_cast_nbr: int) -> bool:
        for spell_in_effect in self.spells_in_effect:
            if spell_in_effect.spell_nbr == spell_to_cast_nbr:
                return True
        return False

    def turn(self) -> bool:
        spell_to_cast = SPELL_BOOK[self.next_spell_to_cast]
        if self.spell_already_in_progress(self.next_spell_to_cast):
            log('{0} already has spell {1} active!'.format(self.name, spell_to_cast.name))
            return True
        if spell_to_cast.cost > self.mana:
            log('{0} is unable to cast {1}, insufficient mana!'.format(self.name, spell_to_cast.name))
            return True
        self.mana -= spell_to_cast.cost
        self.mana_spent += spell_to_cast.cost
        self.spells_cast_history.append(spell_to_cast.name)
        if spell_to_cast.duration == 0:
            log('{0} applies {1} instantly!'.format(spell_to_cast.name, spell_to_cast.effects))
            self.apply_effects(spell_to_cast.effects)
        else:
            log('{0} casts {1}!'.format(self.name, spell_to_cast.name))
            self.spells_in_effect.append(CastSpell(spell_to_cast.duration, self.next_spell_to_cast))
        return False

    def apply_effects(self, spell_effects: Dict[str, int]) -> None:
        for attribute in spell_effects:
            setattr(self, attribute, getattr(self, attribute) + spell_effects[attribute])

    def apply_current_effects(self) -> None:
        # Reset before determining effects.
        self.armour = 0
        self.damage = 0
        spells_in_effect = []
        for spell_in_effect in self.spells_in_effect:
            spell = SPELL_BOOK[spell_in_effect.spell_nbr]
            self.apply_effects(spell.effects)
            duration = spell_in_effect.duration - 1
            log('{0} applies {1}, duration is now {2}.'.format(spell.name, spell.effects, duration))
            if duration == 0:
                log('{0} wears off!'.format(spell.name))
            else:
                spells_in_effect.append(CastSpell(duration, spell_in_effect.spell_nbr))
        self.spells_in_effect = spells_in_effect

    def __repr__(self) -> str:
        return super().__repr__() + ', Mana: {0} {1}'.format(self.mana, self.mana_spent)


class Fighter(Character):

    def __init__(self, name: str, hp: int, weapon_damage: int) -> None:
        super().__init__(name, hp)
        self.weapon_damage = weapon_damage

    def turn(self) -> bool:
        self.damage = self.weapon_damage
        log('{0} attacks with {1} damage!'.format(self.name, self.damage))
        return False

    def apply_current_effects(self) -> None:
        # Fighter could inflict bleeding damage which lasts for several turns but currently has no such effects.
        self.damage = 0

    def __repr__(self) -> str:
        return super().__repr__()


def fight(hero: Character, boss: Character) -> bool:
    log('-- Hero turn --')
    log('{0} vs {1}'.format(hero, boss))
    hero.apply_current_effects()
    boss.apply_current_effects()
    if hero.turn() or boss.dead_after_damage(hero.damage) or hero.dead_after_damage(boss.damage):
        return True
    log('-- Boss turn --')
    log('{0} vs {1}'.format(hero, boss))
    hero.apply_current_effects()
    boss.apply_current_effects()
    if boss.turn() or boss.dead_after_damage(hero.damage) or hero.dead_after_damage(boss.damage):
        return True
    return False


def test_fight1() -> None:
    # Hero casts: Poison, Magic Missile
    test_hero = Mage('Hero', 10, 250)
    test_boss = Fighter('Boss', 13, 8)
    for spell_to_cast_nbr in [3, 0]:
        test_hero.next_spell_to_cast = spell_to_cast_nbr
        fight_completed = fight(test_hero, test_boss)
    assert fight_completed, 'Expected fight to complete!'
    assert test_boss.hp <= 0, 'Expected hero to win!'
    assert test_hero.hp == 2 and test_hero.armour == 0 and test_hero.mana == 24


def test_fight2() -> None:
    # Hero casts: Recharge, Shield, Drain, Poison, Magic Missile
    test_hero = Mage('Hero', 10, 250)
    test_boss = Fighter('Boss', 14, 8)
    for spell_to_cast_nbr in [4, 2, 1, 3, 0]:
        test_hero.next_spell_to_cast = spell_to_cast_nbr
        fight_completed = fight(test_hero, test_boss)
    assert fight_completed, 'Expected fight to complete!'
    assert test_boss.hp <= 0, 'Expected hero to win!'
    assert test_hero.hp == 1 and test_hero.armour == 0 and test_hero.mana == 114


def test_fight3() -> None:
    # Hero casts: Poison, Drain, Recharge, Poison, Shield, Recharge, Poison, Magic Missile, Magic Missile
    test_hero = Mage('Hero', 50, 500)
    test_boss = Fighter('Boss', 58, 9)
    for spell_to_cast_nbr in [3, 1, 4, 3, 2, 4, 3, 0, 0]:
        test_hero.next_spell_to_cast = spell_to_cast_nbr
        fight_completed = fight(test_hero, test_boss)
    assert fight_completed, 'Expected fight to complete!'
    assert test_boss.hp == 0, 'Expected hero to win!'
    assert test_hero.hp == 1 and test_hero.armour == 0 and test_hero.mana == 241 and test_hero.mana_spent == 1269


def next_round(hero: Mage, boss: Character, lowest_mana_spent: int) -> int:
    for spell_to_cast_nbr in range(len(SPELL_BOOK)):
        clone_hero = deepcopy(hero)
        clone_boss = deepcopy(boss)
        clone_hero.next_spell_to_cast = spell_to_cast_nbr
        fight_completed = fight(clone_hero, clone_boss)
        if not fight_completed and clone_hero.mana_spent < lowest_mana_spent:
            log('Subtree down from: {0} {1}'.format(lowest_mana_spent, hero.spells_cast_history))
            mana_spent = next_round(clone_hero, clone_boss, lowest_mana_spent)
            if mana_spent < lowest_mana_spent:
                lowest_mana_spent = mana_spent
            log('Returned to tree: {0} {1}'.format(lowest_mana_spent, hero.spells_cast_history))
        else:
            if clone_boss.hp <= 0:
                print('{0} wins with mana spent = {1} {2}'
                      .format(clone_hero.name, clone_hero.mana_spent, clone_hero.spells_cast_history))
                if clone_hero.mana_spent < lowest_mana_spent:
                    lowest_mana_spent = clone_hero.mana_spent
    return lowest_mana_spent


def search_fights():
    hero = Mage('Hero', 50, 500)
    boss = Fighter('Boss', 58, 9)
    least_mana_spent = next_round(hero, boss, sys.maxsize)
    print('Least mana spent = {0}'.format(least_mana_spent))


def main() -> None:
    # test_fight1()
    # test_fight2()
    # test_fight3()
    search_fights()


if __name__ == '__main__':
    main()
