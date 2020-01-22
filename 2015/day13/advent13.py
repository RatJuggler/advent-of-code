import re
import itertools


class HappinessNextTo:

    def __init__(self, person_a, person_b, happiness):
        self.person_a = person_a
        self.person_b = person_b
        self.happiness = happiness

    @classmethod
    def from_line(cls, line):
        person_a, person_b, happiness = cls.decode_line(line)
        return HappinessNextTo(person_a, person_b, happiness)

    @staticmethod
    def decode_line(line):
        regex = r'^(?P<person_a>\S+) would (?P<difference>\S+) (?P<happiness>\d+) ' \
                r'happiness units by sitting next to (?P<person_b>\S+)\.'
        matches = re.match(regex, line)
        person_a = matches.group('person_a')
        person_b = matches.group('person_b')
        difference = matches.group('difference')
        happiness = int(matches.group('happiness'))
        if difference == 'lose':
            happiness = -happiness
        return person_a, person_b, happiness

    def __repr__(self):
        return "Happiness({0} next to {1} = {2})".format(self.person_a, self.person_b, self.happiness)


def load_seating(filename):
    people = {}
    with open(filename) as fh:
        for line in fh:
            next_to = HappinessNextTo.from_line(line)
            person = people.get(next_to.person_a)
            if person is None:
                person = []
            person.append(next_to)
            people[next_to.person_a] = person
    return people


def plan_seating(filename):
    people = load_seating(filename)
    happiness = 0
    for person in people:
        print(person, people[person])
    for seating in itertools.permutations(people, len(people)):
        print(seating)
    return happiness


def test_plan_seating(filename, expected_happiness):
    happiness = plan_seating(filename)
    assert happiness == expected_happiness, \
        'Expected happiness of {0} for {1} but was {2}!'.format(expected_happiness, filename, happiness)


def main():
    test_plan_seating('test13a.txt', 330)


if __name__ == '__main__':
    main()
