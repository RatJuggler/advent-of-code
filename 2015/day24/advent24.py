from typing import List, Tuple
import itertools


def load_package_weights(filename: str) -> List[int]:
    packages = []
    with open(filename) as fh:
        for line in fh:
            packages.append(int(line))
    return packages


def determine_sleigh_loading_combinations(packages: List[int], number_of_groups: int) -> List[List[Tuple[int]]]:
    # We must have three groups of packages with a minimum of one package per group.
    max_group_size = len(packages) - (number_of_groups - 1)
    # This gives us all the possible combinations of group sizes.
    group_sizes = [lengths
                   for lengths
                   in itertools.combinations_with_replacement(range(1, max_group_size + 1), number_of_groups)
                   if sum(lengths) == len(packages)]
    # Each group must sum to a third of the total packages.
    group_sum = sum(packages) // number_of_groups
    print('Group sizes from 1-{0} must sum to {1}'.format(max_group_size, group_sum))
    groups = []
    for group_size in range(1, max_group_size + 1):
        # Every combination of presents which sums to the total.
        group = [group
                 for group
                 in itertools.combinations(packages, group_size)
                 if sum(group) == group_sum]
        groups.append(group)
        # If no groups of this size sum to the total remove any group combinations which contain it.
        if not group:
            group_sizes = [sizes
                           for sizes
                           in group_sizes
                           if group_size not in sizes]
    valid_sizes = []
    sleigh_loadings = []
    for sizes in group_sizes:
        for size in sizes:
            print(size, 'x', len(groups[size - 1]), ' ', end='')
        print('')
        min_size = min(sizes)
        if min_size not in valid_sizes:
            valid_sizes.append(min_size)
            sleigh_loadings.append(groups[min_size - 1])
    return sleigh_loadings


def calculate_quantum_entanglement(packages: Tuple[int]) -> int:
    quantum_entanglement = 1
    for package in packages:
        quantum_entanglement *= package
    return quantum_entanglement


def determine_quantum_entanglements(sleigh_loadings: List[List[Tuple[int]]]) -> List[int]:
    quantum_entanglements = []
    min_group_size = min([len(loadings[0]) for loadings in sleigh_loadings])
    for loading in sleigh_loadings:
        if len(loading[0]) == min_group_size:
            for load in loading:
                quantum_entanglements.append(calculate_quantum_entanglement(load))
    return quantum_entanglements


def find_best_sleigh_loading(filename: str, number_of_groups: int) -> int:
    packages = load_package_weights(filename)
    sleigh_loadings = determine_sleigh_loading_combinations(packages, number_of_groups)
    quantum_entanglements = determine_quantum_entanglements(sleigh_loadings)
    return min(quantum_entanglements)


def test_find_best_sleigh_loading(filename: str, number_of_groups: int, expected_quantum_entanglement: int) -> None:
    quantum_entanglement = find_best_sleigh_loading(filename, number_of_groups)
    assert quantum_entanglement == expected_quantum_entanglement, \
        'Expect a quantum entanglement of {0} but was {1}!'.format(expected_quantum_entanglement, quantum_entanglement)


def main() -> None:
    test_find_best_sleigh_loading('test24a.txt', 3, 99)
    # quantum_entanglement = find_best_sleigh_loading('input24.txt', 3)
    # print('Day 24, Step 1 best quantum entanglement for Group 1 packages is {0}.'.format(quantum_entanglement))
    test_find_best_sleigh_loading('test24a.txt', 4, 44)
    # quantum_entanglement = find_best_sleigh_loading('input24.txt', 4)
    # print('Day 24, Step 2 best quantum entanglement for Group 1 packages is {0}.'.format(quantum_entanglement))


if __name__ == '__main__':
    main()
