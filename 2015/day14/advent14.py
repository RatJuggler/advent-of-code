import re


class Reindeer:

    def __init__(self, name, speed, duration, rest):
        self.name = name
        self.speed = speed
        self.duration = duration
        self.rest = rest
        self.distance = 0
        self.flying = 0
        self.resting = 0

    @classmethod
    def from_line(cls, line):
        name, speed, duration, rest = cls.decode_line(line)
        return Reindeer(name, speed, duration, rest)

    @staticmethod
    def decode_line(line):
        regex = r'(?P<name>\S+) can fly (?P<speed>\d+) km\/s for (?P<duration>\d+) seconds, ' \
                r'but then must rest for (?P<rest>\d+) seconds\.'
        matches = re.match(regex, line)
        name = matches.group('name')
        speed = int(matches.group('speed'))
        duration = int(matches.group('duration'))
        rest = int(matches.group('rest'))
        return name, speed, duration, rest

    def race(self):
        if self.resting > 0:
            self.resting -= 1
        else:
            self.flying += 1
            self.distance += self.speed
            if self.flying == self.duration:
                self.flying = 0
                self.resting = self.rest
        print(self.name, self.distance, self.flying, self.resting)

    def __repr__(self):
        return "Reindeer(Name: {0}, Speed: {1} km/s, Duration: {2}, Rest: {3})"\
            .format(self.name, self.speed, self.duration, self.rest)


def load_reindeer(filename):
    reindeer = []
    with open(filename) as fh:
        for line in fh:
            reindeer.append(Reindeer.from_line(line))
    return reindeer


def find_winning_distance(filename, after_time):
    reindeer = load_reindeer(filename)
    for a_reindeer in reindeer:
        print(a_reindeer)
    for i in range(after_time):
        for a_reindeer in reindeer:
            a_reindeer.race()
    winning_distance = 0
    for a_reindeer in reindeer:
        if a_reindeer.distance > winning_distance:
            winning_distance = a_reindeer.distance
    return winning_distance


def test_winning_distance(filename, after, expected_distance):
    distance = find_winning_distance(filename, after)
    assert distance == expected_distance, \
        'Expected to find winning distance of {0} km but was {1}!'.format(expected_distance, distance)


def main():
    test_winning_distance('test14a.txt', 1000, 1120)
    race_duration = 2503
    winning_distance = find_winning_distance('input14.txt', race_duration)
    print('Day 14, Step 1 winning distance after {0} seconds is {1}.'.format(race_duration, winning_distance))


if __name__ == '__main__':
    main()
