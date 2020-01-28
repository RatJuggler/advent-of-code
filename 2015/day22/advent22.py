class Character:

    def __init__(self, name: str, hp: int, mana: int, damage: int = 0, armour: int = 0) -> None:
        self.name = name
        self.hp = hp
        self.mana = mana
        self.damage = damage
        self.armour = armour


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
    test_hero = Character('Hero', 10, 250)
    test_boss = Character('Boss', 13, 0, 8)
    winner = fight(test_hero, test_boss)
    assert winner == 1, 'Expected hero to win!'


def main() -> None:
    test_fight()


if __name__ == '__main__':
    main()
