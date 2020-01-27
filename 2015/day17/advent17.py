def container_combinations(to_store, containers):
    pass


def test_container_combination(to_store, containers, expected_combinations):
    combinations = container_combinations(to_store, containers)
    assert combinations == expected_combinations, \
        'Expected to find {0} combinations of containers to store {1}l but found {2}!'\
            .format(expected_combinations, to_store, combinations)


def main() -> None:
    test_container_combination(25, [20, 15, 10, 5, 5], 4)


if __name__ == '__main__':
    main()
