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


def plan_seating(people):
    most_happiness = 0
    places = len(people)
    for seating in itertools.permutations(people, places):
        total_happiness = 0
        for i in range(places):
            person_a = seating[i]
            person_b = seating[0] if i + 1 == places else seating[i + 1]
            total_happiness += people.get(person_a).get(person_b) + people.get(person_b).get(person_a)
        print(seating, total_happiness)
        if total_happiness > most_happiness:
            most_happiness = total_happiness
    return most_happiness


def find_happiest_seating_plan(filename):
    people = load_seating(filename)
    for person in people:
        print(person, people[person])
    return plan_seating(people)


def add_me(people):
    me = 'Me'
    people[me] = {}
    for person in people:
        people[me][person] = 0
        people[person][me] = 0
    return people


def find_happiest_seating_plan_with_me(filename):
    people = load_seating(filename)
    people = add_me(people)
    for person in people:
        print(person, people[person])
    return plan_seating(people)


def test_plan_seating(filename, expected_happiness):
    most_happiness = find_happiest_seating_plan(filename)
    assert most_happiness == expected_happiness, \
        'Expected happiness of {0} for {1} but was {2}!'.format(expected_happiness, filename, most_happiness)


def main():
    test_plan_seating('test13a.txt', 330)
    most_happiness = find_happiest_seating_plan('input13.txt')
    print('Day 13, Step 1 most happiness could be {0}'.format(most_happiness))
    most_happiness = find_happiest_seating_plan_with_me('input13.txt')
    print('Day 13, Step 2 most happiness with me could be {0}'.format(most_happiness))


if __name__ == '__main__':
    main()
