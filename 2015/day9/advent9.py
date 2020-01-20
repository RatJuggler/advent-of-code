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
        distance = int(matches.group('distance'))
        return from_location, to_location, distance

    @staticmethod
    def from_reversed(distance):
        return Distance(distance.to_location, distance.from_location, distance.distance)

    def __repr__(self):
        return "Distance({0} to {1} = {2})".format(self.from_location, self.to_location, self.distance)


class Distances:

    def __init__(self, distances):
        self.distances = distances

    def get_distances(self):
        return self.distances

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

    def get_locations(self):
        return self.locations

    def list(self):
        for location in self.locations:
            print(location, ':', self.locations[location])

    def total_distances(self, visited_locations, current_distance):
        results = []
        last_visited = visited_locations[-1]
        from_distances = self.locations[last_visited]
        for distance in from_distances.get_distances():
            if distance.to_location not in visited_locations:
                visited_locations.append(distance.to_location)
                current_distance += distance.distance
                if len(visited_locations) != len(self.locations):
                    results.extend(self.total_distances(visited_locations, current_distance))
                else:
                    results.append([visited_locations.copy(), current_distance])
                visited_locations.pop()
                current_distance -= distance.distance
        return results


def find_shortest_distance(filename):
    locations = Locations.from_file(filename)
    locations.list()
    results = []
    for start_location in locations.get_locations():
        results.extend(locations.total_distances([start_location], 0))
    print(results)
    shortest_distance = None
    for result in results:
        if shortest_distance is None or result[1] < shortest_distance[0][1]:
            shortest_distance = [result]
        if result[1] == shortest_distance[0][1]:
            shortest_distance.append(result)
    print(shortest_distance)
    return shortest_distance[0][1]


def test_find_shortest_route(filename, expected_shortest_distance):
    shortest_distance = find_shortest_distance(filename)
    assert shortest_distance == expected_shortest_distance, \
        'Expected to find shortest distance of {0} but was {1}'.format(expected_shortest_distance, shortest_distance)


def main():
    test_find_shortest_route('test9a.txt', 605)
    print('Day 9, Step 1 shortest route = {0}'.format(find_shortest_distance('input9.txt')))


if __name__ == '__main__':
    main()
