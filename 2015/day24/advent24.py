from typing import List


def load_package_weights(filename: str) -> List[int]:
    packages = []
    with open(filename) as fh:
        for line in fh:
            packages.append(int(line))
    return packages


def find_best_sleigh_loading(filename: str) -> int:
    packages = load_package_weights(filename)
    for package in packages:
        print(package)
    return 0


def test_find_best_sleigh_loading(filename: str, expected_quantum_entanglement: int) -> None:
    quantum_entanglement = find_best_sleigh_loading(filename)
    assert quantum_entanglement == expected_quantum_entanglement, \
        'Expect a quantum entanglement of {0} but was {1}!'.format(expected_quantum_entanglement, quantum_entanglement)


def main() -> None:
    test_find_best_sleigh_loading('test24a.txt', 99)


if __name__ == '__main__':
    main()
