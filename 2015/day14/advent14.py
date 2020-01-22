import re


class Reindeer:

    def __init__(self, name, speed, duration, rest):
        self.name = name
        self.speed = speed
        self.duration = duration
        self.rest = rest

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
        speed = matches.group('speed')
        duration = int(matches.group('duration'))
        rest = int(matches.group('rest'))
        return name, speed, duration, rest

    def __repr__(self):
        return "Reindeer(Name: {0}, Speed: {1} km/s, Duration: {2}, Rest: {3})"\
            .format(self.name, self.speed, self.duration, self.rest)


def load_reindeer(filename):
    reindeer = []
    with open(filename) as fh:
        for line in fh:
            reindeer.append(Reindeer.from_line(line))
    return reindeer


def find_winning_distance(filename, after):
    reindeer = load_reindeer(filename)
    for a_reindeer in reindeer:
        print(a_reindeer)
    return 0


def test_winning_distance(filename, after, expected_distance):
    distance = find_winning_distance(filename, after)
    assert distance == expected_distance, \
        'Expected to find winning distance of {0} but was {1}'.format(expected_distance, distance)


def main():
    test_winning_distance('test14a.txt', 1000, 1120)


if __name__ == '__main__':
    main()
