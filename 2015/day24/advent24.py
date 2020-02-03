from typing import List, Tuple
import itertools


def load_package_weights(filename: str) -> List[int]:
    packages = []
    with open(filename) as fh:
        for line in fh:
            packages.append(int(line))
    return packages


def group_overlap(group: Tuple[int], previous_group: Tuple[int]):
    for package in group:
        if package in previous_group:
            return True
    return False


def groups_overlap(group: Tuple[int], previous_group1: Tuple[int], previous_group2: Tuple[int]):
    for package in group:
        if package in previous_group1 or package in previous_group2:
            return True
    return False


def determine_sleigh_loading_combinations(packages: List[int]) -> List[List[Tuple[int]]]:
    sleigh_loadings = []
    # We must have three groups of packages with a minimum of one package per group.
    max_group_size = len(packages) - 2
    # This gives us all the possible combinations of group sizes.
    group_sizes = [lengths for lengths in itertools.combinations_with_replacement(range(1, max_group_size + 1), 3)
                   if sum(lengths) == len(packages)]
    # Each group must sum to a third of the total packages.
    group_sum = sum(packages) // 3
    print('Group sizes from 1-{0} must sum to {1}'.format(max_group_size, group_sum))
    groups = []
    for group_size in range(1, max_group_size + 1):
        # Every combination of presents which sums to the total.
        group = [group for group in itertools.combinations(packages, group_size)
                 if sum(group) == group_sum]
        groups.append(group)
        if not group:
            group_sizes = [sizes for sizes in group_sizes if group_size not in sizes]
    for sizes in group_sizes:
        for size in sizes:
            print(size, 'x', len(groups[size - 1]), ' ', end='')
        print('')
        for group1 in [group for group in groups[sizes[0] - 1]]:
            for group2 in [group for group in groups[sizes[1] - 1]
                           if not group_overlap(group, group1)]:
                for group3 in [group for group in groups[sizes[2] - 1]
                               if not groups_overlap(group, group1, group2)]:
                    sleigh_loadings.append([group1, group2, group3])
    return sleigh_loadings


def calculate_quantum_entanglement(packages: Tuple[int]) -> int:
    quantum_entanglement = 1
    for package in packages:
        quantum_entanglement *= package
    return quantum_entanglement


def determine_quantum_entanglements(sleigh_loadings: List[List[Tuple[int]]], minimum_group1: int) -> List[int]:
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
