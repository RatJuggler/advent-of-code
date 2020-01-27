from typing import List
import itertools


def load_containers(filename: str) -> List[int]:
    containers = []
    with open(filename) as fh:
        for line in fh:
            containers.append(int(line))
    return containers


def container_combinations(to_store: int, containers: List[int]) -> int:
    combinations = [seq for i in range(len(containers), 0, -1)
                    for seq in itertools.combinations(containers, i)
                    if sum(seq) == to_store]
    print(combinations)
    return len(combinations)


def test_container_combination(to_store: int, filename: str, expected_combinations: int) -> None:
    containers = load_containers(filename)
    combinations = container_combinations(to_store, containers)
    assert combinations == expected_combinations, \
        'Expected to find {0} combinations of containers to store {1}l but found {2}!'\
        .format(expected_combinations, to_store, combinations)


def minimum_container_combinations(to_store: int, containers: List[int]) -> [int, int]:
    return 0, 0


def test_minimum_container_combination(to_store: int, filename: str,
                                       expected_minimum_containers: int, expected_combinations: int) -> None:
    containers = load_containers(filename)
    minimum_containers, combinations = minimum_container_combinations(to_store, containers)
    assert minimum_containers == expected_minimum_containers, \
        'Expected to find minimum of {0} containers needed to store {1}l but found {2}!'\
        .format(expected_minimum_containers, to_store, minimum_containers)
    assert combinations == expected_combinations, \
        'Expected to find {0} combinations of the minimum number of containers to store {1}l but found {2}!'\
        .format(expected_combinations, to_store, combinations)


def main() -> None:
    test_container_combination(25, 'test17a.txt', 4)
    containers = load_containers('input17.txt')
    print('Day 17, Step 1 container combinations found {0}.'.format(container_combinations(150, containers)))
    test_minimum_container_combination(25, 'test17a.txt', 2, 3)


if __name__ == '__main__':
    main()
