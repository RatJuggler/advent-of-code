def find_distinct_molecules(filename):
    return 0


def test_find_distinct_molecules(filename: str, expected_distinct_molecules: int) -> None:
    distinct_molecules = find_distinct_molecules(filename)
    assert distinct_molecules == expected_distinct_molecules, \
        'Expected to find {0} distinct molecules but found {1}!'.format(expected_distinct_molecules, distinct_molecules)


def main() -> None:
    test_find_distinct_molecules('test19a.txt', 4)
    test_find_distinct_molecules('test19b.txt', 7)


if __name__ == '__main__':
    main()
