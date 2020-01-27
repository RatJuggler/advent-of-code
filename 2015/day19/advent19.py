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
            new_molecule = molecule[:occurrence] + replacement[1] + molecule[occurrence + 1:]
            new_molecules[new_molecule] = True
            print(molecule, replacement[0], occurrence, new_molecule)
            occurrence = molecule.find(replacement[0], occurrence + 1)
    return len(new_molecules)


def test_find_distinct_molecules(filename: str, expected_distinct_molecules: int) -> None:
    distinct_molecules = find_distinct_molecules(filename)
    assert distinct_molecules == expected_distinct_molecules, \
        'Expected to find {0} distinct molecules but found {1}!'.format(expected_distinct_molecules, distinct_molecules)


def main() -> None:
    test_find_distinct_molecules('test19a.txt', 4)
    test_find_distinct_molecules('test19b.txt', 7)


if __name__ == '__main__':
    main()
