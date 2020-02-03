from typing import List, Tuple
import itertools


def load_package_weights(filename: str) -> List[int]:
    packages = []
    with open(filename) as fh:
        for line in fh:
            packages.append(int(line))
    return packages


def determine_sleigh_loading_combinations(packages: List[int], number_of_groups: int) -> List[List[Tuple[int]]]:
    # We must have a number of groups of packages with a minimum of one package per group.
    max_group_size = len(packages) - (number_of_groups - 1)
    # Each group must sum to an equal fraction of the total packages.
    group_sum = sum(packages) // number_of_groups
    print('Group sizes from 1-{0} must sum to {1}'.format(max_group_size, group_sum))
    # This gives us all the possible combinations of group sizes.
    group_sizes = [lengths for lengths
                   in itertools.combinations_with_replacement(range(1, max_group_size + 1), number_of_groups)
                   if sum(lengths) == len(packages)]
    # We just want the minimum size from each group (to go in the passenger compartment).
    min_group_sizes = set([min(sizes) for sizes in group_sizes])
    print('Minimum group sizes: ', min_group_sizes)
    # Then we want the present combinations for each minimum size which sum to the required group total.
    sleigh_loadings = []
    for size in min_group_sizes:
        group = [group for group
                 in itertools.combinations(packages, size)
                 if sum(group) == group_sum]
        print('Group size {0} has {1} package combinations.'.format(size, len(group)))
        if group:
            sleigh_loadings.append(group)
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
    quantum_entanglement = find_best_sleigh_loading('input24.txt', 3)
    print('Day 24, Step 1 best quantum entanglement for Group 1 packages is {0}.'.format(quantum_entanglement))
    test_find_best_sleigh_loading('test24a.txt', 4, 44)
    quantum_entanglement = find_best_sleigh_loading('input24.txt', 4)
    print('Day 24, Step 2 best quantum entanglement for Group 1 packages is {0}.'.format(quantum_entanglement))


if __name__ == '__main__':
    main()
