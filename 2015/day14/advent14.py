import re


class Reindeer:

    def __init__(self, name, speed, duration, rest):
        self.name = name
        self.speed = speed
        self.duration = duration
        self.rest = rest
        self.distance = None
        self.flying = None
        self.resting = None
        self.points = None

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

    def into_the_starting_gate(self):
        self.distance = 0
        self.flying = 0
        self.resting = 0
        self.points = 0

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


class Racer:

    def __init__(self, duration):
        self.duration = duration

    @staticmethod
    def __load_the_starting_gate(reindeer):
        for a_reindeer in reindeer:
            a_reindeer.into_the_starting_gate()

    @staticmethod
    def __winning_distance(reindeer):
        winning_distance = 0
        for a_reindeer in reindeer:
            if a_reindeer.distance > winning_distance:
                winning_distance = a_reindeer.distance
        return winning_distance

    def run_distance_race(self, reindeer):
        self.__load_the_starting_gate(reindeer)
        for i in range(self.duration):
            for a_reindeer in reindeer:
                a_reindeer.race()
        return self.__winning_distance(reindeer)

    @staticmethod
    def __award_points(reindeer):
        leaders_distance = 0
        for a_reindeer in reindeer:
            if a_reindeer.distance >= leaders_distance:
                leaders_distance = a_reindeer.distance
        for a_reindeer in reindeer:
            if a_reindeer.distance == leaders_distance:
                a_reindeer.points += 1

    @staticmethod
    def __winning_points(reindeer):
        winning_points = 0
        for a_reindeer in reindeer:
            if a_reindeer.points > winning_points:
                winning_points = a_reindeer.points
        return winning_points

    def run_points_race(self, reindeer):
        self.__load_the_starting_gate(reindeer)
        for i in range(self.duration):
            for a_reindeer in reindeer:
                a_reindeer.race()
            self.__award_points(reindeer)
        return self.__winning_points(reindeer)


def load_reindeer(filename):
    reindeer = []
    with open(filename) as fh:
        for line in fh:
            reindeer.append(Reindeer.from_line(line))
    return reindeer


def find_winning_distance(filename, race_duration):
    reindeer = load_reindeer(filename)
    for a_reindeer in reindeer:
        print(a_reindeer)
    return Racer(race_duration).run_distance_race(reindeer)


def find_winning_points(filename, race_duration):
    reindeer = load_reindeer(filename)
    for a_reindeer in reindeer:
        print(a_reindeer)
    return Racer(race_duration).run_points_race(reindeer)


def test_winning_distance(filename, race_duration, expected_distance):
    distance = find_winning_distance(filename, race_duration)
    assert distance == expected_distance, \
        'Expected to find winning distance of {0} km but was {1}!'.format(expected_distance, distance)


def test_winning_points(filename, race_duration, expected_points):
    points = find_winning_points(filename, race_duration)
    assert points == expected_points, \
        'Expected to find winning points to be {0} but was {1}!'.format(expected_points, points)


def main():
    test_winning_distance('test14a.txt', 1000, 1120)
    race_duration = 2503
    winning_distance = find_winning_distance('input14.txt', race_duration)
    print('Day 14, Step 1 winning distance after {0} seconds is {1}.'.format(race_duration, winning_distance))
    test_winning_points('test14a.txt', 1000, 689)


if __name__ == '__main__':
    main()
