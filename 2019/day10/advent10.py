def load_asteroid_map(filename):
    asteroid_map = []
    with open(filename) as fh:
        for map_line in fh:
            asteroid_map.append(map_line.rstrip('\n'))
    return asteroid_map


def convert_map_to_coords(asteroid_map):
    coords = []
    for y, map_line in enumerate(asteroid_map):
        for x, c in enumerate(map_line):
            if c == '#':
                coords.append((x, y))
            elif c != '.':
                raise Exception('Unknown map character {0}!'.format(c))
    return coords, (len(asteroid_map[0]), len(asteroid_map))


def detect_asteroids(asteroid_coords, map_size, from_base):
    return 0


def find_best_base(asteroid_coords, map_size):
    maximum_detected = 0
    best_base = None
    for base in asteroid_coords:
        asteroids_detected = detect_asteroids(asteroid_coords, map_size, base)
        print('{0} observable count = {1}'.format(base, asteroids_detected))
        if asteroids_detected > maximum_detected:
            maximum_detected = asteroids_detected
            best_base = base
    return maximum_detected, best_base


def analyse_map(filename):
    asteroid_map = load_asteroid_map(filename)
    asteroid_coords, map_size = convert_map_to_coords(asteroid_map)
    return find_best_base(asteroid_coords, map_size)


def test(filename, expected_location, expected_detected):
    detected, location = analyse_map(filename)
    assert location == expected_location, \
        'For map {0} expected to find location {1} but found {2}!'.format(filename, expected_location, location)
    assert detected == expected_detected, \
        'For map {0} expected to detect {0} asteroids but detected {1}!'.format(filename, expected_detected, detected)


def main():
    test('test10a.txt', (3, 4), 8)
    test('test10b.txt', (5, 8), 33)
    test('test10c.txt', (1, 2), 35)
    test('test10d.txt', (6, 3), 41)
    test('test10e.txt', (11, 13), 210)

    detected, location = analyse_map('test10e.txt')
    print('Day 10 Step 1, best location {0} detects {1} asteroids'.format(location, detected))


if __name__ == '__main__':
    main()
