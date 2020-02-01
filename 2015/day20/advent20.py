from typing import List
import math


def infinite_delivery_step1(find_presents: int) -> int:
    house_number = 1
    house_presents = 0
    while house_presents < find_presents:
        house_number += 1
        # 1st Elf (1) plus <house_number>th Elf (n)
        house_presents = 10 + (house_number * 10)
        for elf in range(2, int(math.sqrt(house_number)) + 1):
            if house_number % elf == 0:
                # Factor!
                house_presents += elf * 10
                # And check for reverse.
                if house_number // elf != elf:
                    house_presents += (house_number // elf) * 10
        print(house_number, house_presents)
    return house_number


def visit_house(elf: int, visits: List[int]):
    if visits[elf] < 50:
        visits[elf] += 1
        return elf * 11
    return 0


def infinite_delivery_step2(find_presents: int) -> int:
    house_number = 1
    house_presents = 0
    visits = [0] * (find_presents // 11)
    while house_presents < find_presents:
        house_number += 1
        # 1st Elf plus <house_number>th Elf
        house_presents = visit_house(1, visits) + visit_house(house_number, visits)
        for elf in range(2, int(math.sqrt(house_number)) + 1):
            if house_number % elf == 0:
                # Factor!
                house_presents += visit_house(elf, visits)
                # And check for reverse.
                if house_number // elf != elf:
                    house_presents += visit_house(house_number // elf, visits)
        print(house_number, house_presents)
    return house_number


def test_infinite_delivery_step1(find_presents: int, expected_house: int) -> None:
    house = infinite_delivery_step1(find_presents)
    assert house == expected_house, \
        'Day 20, Step 1 expected to find house {0} but found {1}!'.format(expected_house, house)


def test_infinite_delivery_step2(find_presents: int, expected_house: int) -> None:
    house = infinite_delivery_step2(find_presents)
    assert house == expected_house, \
        'Day 20, Step 2 expected to find house {0} but found {1}!'.format(expected_house, house)


def main() -> None:
    test_infinite_delivery_step1(150, 8)
    house = infinite_delivery_step1(29000000)   # 665280
    print('Day 20, Step 1 first house to receive 29000000 presents is {0}.'.format(house))
    test_infinite_delivery_step2(160, 8)
    house = infinite_delivery_step2(29000000)   # 705600
    print('Day 20, Step 2 first house to receive 29000000 presents is {0}.'.format(house))


if __name__ == '__main__':
    main()
