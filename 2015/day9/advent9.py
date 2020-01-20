import re


class Distance:

    def __init__(self, from_location, to_location, distance):
        self.from_location = from_location
        self.to_location = to_location
        self.distance = distance

    @classmethod
    def from_line(cls, line):
        from_location, to_location, distance = cls.decode_distance(line)
        return Distance(from_location, to_location, distance)

    @staticmethod
    def decode_distance(line):
        regex = r'(?P<from_location>\S+) to (?P<to_location>\S+) = (?P<distance>\d+)'
        matches = re.match(regex, line)
        from_location = matches.group('from_location')
        to_location = matches.group('to_location')
        distance = matches.group('distance')
        return from_location, to_location, distance

    @staticmethod
    def from_reversed(distance):
        return Distance(distance.to_location, distance.from_location, distance.distance)

    def __repr__(self):
        return "Distance({0} to {1} = {2})".format(self.from_location, self.to_location, self.distance)


class Distances:

    def __init__(self, distances):
        self.distances = distances

    def add_distance(self, distance):
        return self.distances.append(distance)

    def __repr__(self):
        to_string = ''
        for distance in self.distances:
            if len(to_string) > 0:
                to_string += ', '
            to_string += distance.__repr__()
        return to_string


class Locations:

    def __init__(self, locations):
        self.locations = locations

    @classmethod
    def from_file(cls, filename):
        locations = Locations({})
        with open(filename) as fh:
            for line in fh:
                distance = Distance.from_line(line)
                locations.add_location_distance(distance)
                distance = Distance.from_reversed(distance)
                locations.add_location_distance(distance)
        return locations

    def add_location_distance(self, distance):
        distances = self.locations.get(distance.from_location)
        if distances is None:
            distances = Distances([])
            self.locations[distance.from_location] = distances
        distances.add_distance(distance)

    def list(self):
        for distances in self.locations:
            print(distances, ':', self.locations[distances])


def find_shortest_distance(filename):
    locations = Locations.from_file(filename)
    locations.list()
    return 0


def test_find_shortest_route(filename, expected_shortest_distance):
    shortest_distance = find_shortest_distance(filename)
    assert shortest_distance == expected_shortest_distance, \
        'Expected to find shortest distance of {0} but was {1}'.format(expected_shortest_distance, shortest_distance)


def main():
    test_find_shortest_route('test9a.txt', 602)


if __name__ == '__main__':
    main()
