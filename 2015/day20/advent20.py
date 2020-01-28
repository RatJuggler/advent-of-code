def infinite_delivery(presents: int) -> int:
    elf_house_visits = [0]
    house_presents = [0]
    number_elves = 0
    house_found = None
    while not house_found:
        for i in range(len(elf_house_visits)):
            elf_house_visits[i] += i
            while len(house_presents) < elf_house_visits[i] + 1:
                house_presents.append(0)
            house_presents[elf_house_visits[i]] += i * 10
            if house_presents[i] >= presents:
                house_found = i
                break
        number_elves += 1
        elf_house_visits.append(number_elves)
        house_presents.append(0)
        house_presents[number_elves] += number_elves * 10
        # print(elf_house_visits, house_presents)
    return house_found


def test_infinite_delivery(presents: int, expected_house: int) -> None:
    house = infinite_delivery(presents)
    assert house == expected_house, \
        'Day 20, Step 1 expected to find house {0} but found {1}!'.format(expected_house, house)


def main() -> None:
    test_infinite_delivery(150, 8)
    house = infinite_delivery(29000000)
    print('Day 20, Step 1 first house to receive 29000000 presents is {0}.'.format(house))


if __name__ == '__main__':
    main()
