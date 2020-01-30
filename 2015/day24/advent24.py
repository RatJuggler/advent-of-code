from typing import List
import itertools


def load_package_weights(filename: str) -> List[int]:
    packages = []
    with open(filename) as fh:
        for line in fh:
            packages.append(int(line))
    return packages


def calculate_quantum_entanglement(packages: List[int]) -> int:
    quantum_entanglement = 1
    for package in packages:
        quantum_entanglement *= package
    return quantum_entanglement


def determine_sleigh_loading_combinations(packages: List[int]) -> List[List[List[int]]]:
    sleigh_loadings = []
    # We must have three groups of packages with a minimum of one package per group.
    for i in range(1, len(packages) - 1):
        for group1 in itertools.combinations(packages, i):
            sum_group1 = sum(group1)
            group1_remainder = [package for package in packages if package not in group1]
            for j in range(1, len(group1_remainder)):
                for group2 in [group for group in itertools.combinations(group1_remainder, j) if sum(group) == sum_group1]:
                    group3 = [package for package in packages if package not in group1 and package not in group2]
                    if sum(group3) == sum_group1:
                        sleigh_loadings.append([group1, group2, group3])
    return sleigh_loadings


def determine_quantum_entanglements(sleigh_loadings: List[List[List[int]]], minimum_group1: int) -> List[int]:
    quantum_entanglements = []
    for loading in sleigh_loadings:
        if len(loading[0]) == minimum_group1:
            quantum_entanglements.append(calculate_quantum_entanglement(loading[0]))
    return quantum_entanglements


def find_best_sleigh_loading(filename: str) -> int:
    packages = load_package_weights(filename)
    sleigh_loadings = determine_sleigh_loading_combinations(packages)
    minimum_group1 = min([len(loadings[0]) for loadings in sleigh_loadings])
    quantum_entanglements = determine_quantum_entanglements(sleigh_loadings, minimum_group1)
    return min(quantum_entanglements)


def test_find_best_sleigh_loading(filename: str, expected_quantum_entanglement: int) -> None:
    quantum_entanglement = find_best_sleigh_loading(filename)
    assert quantum_entanglement == expected_quantum_entanglement, \
        'Expect a quantum entanglement of {0} but was {1}!'.format(expected_quantum_entanglement, quantum_entanglement)


def main() -> None:
    test_find_best_sleigh_loading('test24a.txt', 99)
    quantum_entanglement = find_best_sleigh_loading('input24.txt')
    print('Day 24, Step 1 best quantum entanglement for Group 1 packages is {0}.'.format(quantum_entanglement))


if __name__ == '__main__':
    main()
