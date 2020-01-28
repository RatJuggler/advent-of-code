class Character:

    def __init__(self, hp: int, damage: int, armour: int) -> None:
        self.hp = hp
        self.damage = damage
        self.armour = armour


def fight(combatant1: Character, combatant2: Character) -> int:
    cb1_damage = 1 if combatant2.armour > combatant1.damage else combatant1.damage - combatant2.armour + 1
    cb2_damage = 1 if combatant1.armour > combatant2.damage else combatant2.damage - combatant1.armour + 1
    while combatant1.hp > 0 and combatant2.hp > 0:
        combatant1.hp -= cb2_damage
        combatant2.hp -= cb1_damage
        print(combatant1.hp, combatant2.hp)
    return 1 if combatant2.hp <= 0 else 2


def test_fight():
    test_hero = Character(8, 5, 5)
    test_boss = Character(12, 7, 2)
    winner = fight(test_hero, test_boss)
    assert winner == 1, 'Expected hero to win!'


def main() -> None:
    test_fight()


if __name__ == '__main__':
    main()
