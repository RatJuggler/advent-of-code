def infinite_delivery(find_presents: int) -> int:
    house_number = 0
    house_presents = 0
    while house_presents < find_presents:
        house_number += 1
        house_presents = 10 + (house_number * 10)
        for elf in range(2, (house_number // 2) + 1):
            if house_number % elf == 0:
                house_presents += elf * 10
        print(house_number, house_presents)
    return house_number


def test_infinite_delivery(find_presents: int, expected_house: int) -> None:
    house = infinite_delivery(find_presents)
    assert house == expected_house, \
        'Day 20, Step 1 expected to find house {0} but found {1}!'.format(expected_house, house)


def main() -> None:
    test_infinite_delivery(150, 8)
    house = infinite_delivery(29000000)
    print('Day 20, Step 1 first house to receive 29000000 presents is {0}.'.format(house))


if __name__ == '__main__':
    main()
