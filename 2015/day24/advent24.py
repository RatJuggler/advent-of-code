from typing import List
import itertools


def load_package_weights(filename: str) -> List[int]:
    packages = []
    with open(filename) as fh:
        for line in fh:
            packages.append(int(line))
    return packages


def find_best_sleigh_loading(filename: str) -> int:
    packages = load_package_weights(filename)
    # We must have three groups of packages with a minimum of one package per group.
    for i in range(1, len(packages) - 1):
        for group1 in itertools.combinations(packages, i):
            sum_group1 = sum(group1)
            group1_remainder = [package for package in packages if package not in group1]
            for j in range(1, len(group1_remainder)):
                for group2 in [group for group in itertools.combinations(group1_remainder, j) if sum(group) == sum_group1]:
                    group3 = [package for package in packages if package not in group1 and package not in group2]
                    if sum(group3) == sum_group1:
                        print(group1, group2, group3)
    return 0


def test_find_best_sleigh_loading(filename: str, expected_quantum_entanglement: int) -> None:
    quantum_entanglement = find_best_sleigh_loading(filename)
    assert quantum_entanglement == expected_quantum_entanglement, \
        'Expect a quantum entanglement of {0} but was {1}!'.format(expected_quantum_entanglement, quantum_entanglement)


def main() -> None:
    test_find_best_sleigh_loading('test24a.txt', 99)


if __name__ == '__main__':
    main()
