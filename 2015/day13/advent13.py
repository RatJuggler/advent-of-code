import re
import itertools


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


def load_seating(filename):
    people = {}
    with open(filename) as fh:
        for line in fh:
            person_a, person_b, happiness = decode_line(line)
            person = people.get(person_a)
            if person is None:
                person = {}
            person[person_b] = happiness
            people[person_a] = person
    return people


def plan_seating(filename):
    people = load_seating(filename)
    for person in people:
        print(person, people[person])
    most_happiness = 0
    with_seating = None
    for seating in itertools.permutations(people, len(people)):
        print(seating)
        total_happiness = 0
        places = len(seating)
        for i in range(places):
            person = seating[i]
            next_to = seating[i + 1] if i < places - 1 else seating[0]
            total_happiness += people.get(person).get(next_to)
            total_happiness += people.get(next_to).get(person)
        if total_happiness > most_happiness:
            most_happiness = total_happiness
            with_seating = seating
    return most_happiness, with_seating


def test_plan_seating(filename, expected_happiness):
    most_happiness, with_seating = plan_seating(filename)
    assert most_happiness == expected_happiness, \
        'Expected happiness of {0} for {1} but was {2}!'.format(expected_happiness, filename, most_happiness)


def main():
    test_plan_seating('test13a.txt', 330)


if __name__ == '__main__':
    main()
