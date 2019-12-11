import random


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
    return coords


def obscures_asteroid(a, b, c):
    cross_product = (c[1] - a[1]) * (b[0] - a[0]) - (c[0] - a[0]) * (b[1] - a[1])
    if cross_product != 0:
        return False
    dot_product = (c[0] - a[0]) * (b[0] - a[0]) + (c[1] - a[1]) * (b[1] - a[1])
    if dot_product < 0:
        return False
    squared_length_ba = (b[0] - a[0]) * (b[0] - a[0]) + (b[1] - a[1]) * (b[1] - a[1])
    if dot_product > squared_length_ba:
        return False
    return True


def can_see_asteroid(from_base, asteroid, other_asteroids):
    for other_asteroid in other_asteroids:
        if other_asteroid == from_base or other_asteroid == asteroid:
            continue
        if obscures_asteroid(from_base, asteroid, other_asteroid):
            return False
    return True


def detect_asteroids(asteroid_coords, from_base):
    detected = 0
    for asteroid in asteroid_coords:
        if asteroid == from_base:
            continue
        if can_see_asteroid(from_base, asteroid, asteroid_coords):
            detected += 1
    return detected


def find_best_base(asteroid_coords):
    maximum_detected = 0
    best_base = None
    for base in asteroid_coords:
        asteroids_detected = detect_asteroids(asteroid_coords, base)
        # print('{0} observable count = {1}'.format(base, asteroids_detected))
        if asteroids_detected > maximum_detected:
            maximum_detected = asteroids_detected
            best_base = base
    return maximum_detected, best_base


def analyse_map(filename):
    asteroid_map = load_asteroid_map(filename)
    asteroid_coords = convert_map_to_coords(asteroid_map)
    return find_best_base(asteroid_coords)


def test_analyse_map(filename, expected_location, expected_detected):
    detected, location = analyse_map(filename)
    assert location == expected_location, \
        'For map {0} expected to find location {1} but found {2}!'.format(filename, expected_location, location)
    assert detected == expected_detected, \
        'For map {0} expected to detect {0} asteroids but detected {1}!'.format(filename, expected_detected, detected)


def vapourise_asteroids(filename, from_base):
    asteroid_map = load_asteroid_map(filename)
    asteroid_coords = convert_map_to_coords(asteroid_map)
    vapourised_asteroids = []
    while asteroid_coords:
        vapourise = random.randrange(len(asteroid_coords))
        vapourise_asteroid = asteroid_coords[vapourise]
        vapourised_asteroids.append(vapourise_asteroid)
        asteroid_coords.remove(vapourise_asteroid)
    return vapourised_asteroids


def test_vapourise(filename, from_base, sample_results):
    vapourised = vapourise_asteroids(filename, from_base)
    for result in sample_results:
        expected_coords = sample_results.get(result)
        actual_coords = vapourised[result]
        assert actual_coords == expected_coords, \
            "Expected asteroid {0} to be vapourised at {1} but was at {2}!"\
            .format(result, expected_coords, actual_coords)


def main():
    test_analyse_map('test10a.txt', (3, 4), 8)
    test_analyse_map('test10b.txt', (5, 8), 33)
    test_analyse_map('test10c.txt', (1, 2), 35)
    test_analyse_map('test10d.txt', (6, 3), 41)
    test_analyse_map('test10e.txt', (11, 13), 210)

    test_vapourise('test10e.txt', (11, 13),
                   {1: (11, 12), 2: (12, 1), 3: (12, 2),
                    10: (12, 8), 20: (16, 0), 50: (16, 9),
                    100: (10, 16), 199: (9, 6), 200: (8, 2),
                    201: (10, 9), 299: (11, 1)})

    detected_asteroids, best_base = analyse_map('input10.txt')
    print('Day 10 Step 1, best location {0} detects {1} asteroids'.format(best_base, detected_asteroids))
    vapourised = vapourise_asteroids('input10.txt', best_base)
    th200 = vapourised[200]
    print('Day 10 Step 2, 200th vapourised asteroid at {0}, x * 100 + y => {1}'
          .format(th200, th200[0] * 100 + th200[1]))


if __name__ == '__main__':
    main()
