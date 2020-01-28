from typing import List, Tuple
import re


def from_line(line: str) -> [Tuple[str, str], str]:
    regex = r'(?P<from_atom>\S+) => (?P<to_atom>\S+)|(?P<molecule>\S+)'
    matches = re.finditer(regex, line)
    molecule = ''
    replacement = ()
    for match in matches:
        if match.group('molecule'):
            molecule = match.group('molecule')
        else:
            from_atom = match.group('from_atom')
            to_atom = match.group('to_atom')
            replacement = (from_atom, to_atom)
    return replacement, molecule


def load_replacements_and_molecule(filename: str) -> [List[Tuple[str, str]], str]:
    replacements = []
    with open(filename) as fh:
        for line in fh:
            replacement, molecule = from_line(line)
            if replacement is not ():
                replacements.append(replacement)
    return replacements, molecule


def find_distinct_molecules(filename: str) -> int:
    replacements, molecule = load_replacements_and_molecule(filename)
    print(replacements, molecule)
    new_molecules = {}
    for replacement in replacements:
        occurrence = molecule.find(replacement[0], 0)
        while occurrence != -1:
            new_molecule = molecule[:occurrence] + replacement[1] + molecule[occurrence + len(replacement[0]):]
            new_molecules[new_molecule] = True
            print(molecule, replacement[0], occurrence, new_molecule)
            occurrence = molecule.find(replacement[0], occurrence + 1)
    return len(new_molecules)


def test_find_distinct_molecules(filename: str, expected_distinct_molecules: int) -> None:
    distinct_molecules = find_distinct_molecules(filename)
    assert distinct_molecules == expected_distinct_molecules, \
        'Expected to find {0} distinct molecules but found {1}!'.format(expected_distinct_molecules, distinct_molecules)


def minimum_steps_to_make_molecule(filename: str) -> int:
    replacements, molecule = load_replacements_and_molecule(filename)
    electrons = []
    for replacement in replacements:
        electrons.append(replacement[0])
    print(replacements, electrons, molecule)
    steps = 1
    while molecule not in electrons:
        for replacement in replacements:
            occurrence = molecule.find(replacement[1], 0)
            while occurrence != -1:
                molecule = molecule[:occurrence] + replacement[0] + molecule[occurrence + len(replacement[1]):]
                print(replacement[1], occurrence, molecule)
                steps += 1
                occurrence = molecule.find(replacement[1], occurrence + 1)
    return steps


def test_minimum_steps_to_make_molecule(filename: str, expected_minimum_steps: int) -> None:
    minimum_steps = minimum_steps_to_make_molecule(filename)
    assert minimum_steps == expected_minimum_steps, \
        'Expected to take minimum of {0} steps to make molecule was {1}!'.format(expected_minimum_steps, minimum_steps)


def main() -> None:
    test_find_distinct_molecules('test19a.txt', 4)
    test_find_distinct_molecules('test19b.txt', 7)
    test_find_distinct_molecules('test19c.txt', 2)
    distinct_molecules = find_distinct_molecules('input19.txt')
    print('Day 19, Step 1 found {0} distinct new molecules.'.format(distinct_molecules))
    test_minimum_steps_to_make_molecule('test19a.txt', 3)
    test_minimum_steps_to_make_molecule('test19b.txt', 6)
    test_minimum_steps_to_make_molecule('test19c.txt', 2)


if __name__ == '__main__':
    main()
