from typing import Dict, List, Tuple
import re


class Sue:

    def __init__(self, number: int, attributes: Dict[str, int]) -> None:
        self.number = number
        self.attributes = attributes

    @classmethod
    def from_line(cls, line: str):
        regex = r'Sue (?P<sue>\d+)|(?P<attr>\S+): (?P<qty>\d+)'
        matches = re.finditer(regex, line)
        number = 0
        attributes = {}
        for match in matches:
            if match.group('sue'):
                number = int(match.group('sue'))
            else:
                attribute = match.group('attr')
                quantity = int(match.group('qty'))
                attributes[attribute] = quantity
        return Sue(number, attributes)

    def __repr__(self) -> str:
        return "Sue{0}{1}".format(self.number, self.attributes)


def load_sues(filename: str) -> List[Sue]:
    sues = []
    with open(filename) as fh:
        for line in fh:
            sues.append(Sue.from_line(line))
    return sues


def determine_sue(filename: str, known: Dict[str, int]) -> int:
    sues = load_sues(filename)
    sues_found = []
    for sue in sues:
        print(sue)
        for attribute in sue.attributes:
            if sue.attributes[attribute] != known[attribute]:
                break
        else:
            sues_found.append(sue)
    print(sues_found)
    return sues_found[0].number


def main() -> None:
    known = {'children': 3, 'cats': 7, 'samoyeds': 2, 'pomeranians': 3, 'akitas': 0,
             'vizslas': 0, 'goldfish': 5, 'trees': 3, 'cars': 2, 'perfumes': 1}
    sue_number = determine_sue('input16.txt', known)
    print('Day 16, Step 1 Aunt Sue number {0} sent the gift.'.format(sue_number))


if __name__ == '__main__':
    main()
