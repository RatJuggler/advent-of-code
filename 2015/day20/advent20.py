def infinite_delivery(presents: int) -> int:
    return 0


def test_infinite_delivery(presents: int, expected_house: int) -> None:
    house = infinite_delivery(presents)
    assert house == expected_house, \
        'Day 20, Step 1 expected to find house {0} but found {1}!'.format(expected_house, house)


def main() -> None:
    test_infinite_delivery(150, 8)


if __name__ == '__main__':
    main()
