from typing import Callable, Dict, List, Tuple
import re


class Distance:

    def __init__(self, from_location: str, to_location: str, distance: int) -> None:
        self.from_location = from_location
        self.to_location = to_location
        self.distance = distance

    @classmethod
    def from_line(cls, line: str):
        regex = r'(?P<from_location>\S+) to (?P<to_location>\S+) = (?P<distance>\d+)'
        matches = re.match(regex, line)
        from_location = matches.group('from_location')
        to_location = matches.group('to_location')
        distance = int(matches.group('distance'))
        return Distance(from_location, to_location, distance)

    @staticmethod
    def from_reversed(distance):
        return Distance(distance.to_location, distance.from_location, distance.distance)

    def __repr__(self) -> str:
        return "Distance({0} to {1} = {2})".format(self.from_location, self.to_location, self.distance)


class Locations:

    def __init__(self, locations: Dict[str, List[Distance]]) -> None:
        self.locations = locations

    def add_location_distance(self, distance: Distance) -> None:
        distances = self.locations.get(distance.from_location)
        if distances is None:
            distances = []
            self.locations[distance.from_location] = distances
        distances.append(distance)

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

    def list(self) -> None:
        for location in self.locations:
            print(location, ':', self.locations[location])

    def total_distances(self, visited_locations: List[str], current_distance: int) -> List[Tuple[List[str], int]]:
        results = []
        last_visited = visited_locations[-1]
        for distance in self.locations[last_visited]:
            if distance.to_location not in visited_locations:
                visited_locations.append(distance.to_location)
                current_distance += distance.distance
                if len(visited_locations) != len(self.locations):
                    results.extend(self.total_distances(visited_locations, current_distance))
                else:
                    results.append((visited_locations.copy(), current_distance))
                visited_locations.pop()
                current_distance -= distance.distance
        return results

    def total_locations(self) -> List[Tuple[List[str], int]]:
        total_distances = []
        for start_location in self.locations:
            total_distances.extend(self.total_distances([start_location], 0))
        return total_distances


def get_total_distances(filename: str) -> List[Tuple[List[str], int]]:
    locations = Locations.from_file(filename)
    locations.list()
    return locations.total_locations()


def find_distance(filename: str, comparison: Callable[[int, int], bool]) -> int:
    results = get_total_distances(filename)
    distance_found = None
    distance_results = None
    for result in results:
        if distance_found is None or comparison(result[1], distance_found):
            distance_found = result[1]
            distance_results = []
        if result[1] == distance_found:
            distance_results.append(result[0])
    print(distance_found, distance_results)
    return distance_found


def find_shortest_distance(filename: str) -> int:
    return find_distance(filename, lambda x, y: x < y)


def test_find_shortest_route(filename: str, expected_shortest_distance: int) -> None:
    shortest_distance = find_shortest_distance(filename)
    assert shortest_distance == expected_shortest_distance, \
        'Expected to find shortest distance of {0} but was {1}'.format(expected_shortest_distance, shortest_distance)


def main() -> None:
    test_find_shortest_route('test9a.txt', 605)
    print('Day 9, Step 1 shortest route = {0}'.format(find_shortest_distance('input9.txt')))
    print('Day 9, Step 2 longest route = {0}'.format(find_distance('input9.txt', lambda x, y: x > y)))


if __name__ == '__main__':
    main()
