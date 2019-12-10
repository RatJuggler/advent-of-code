def load_asteroid_map(filename):
    asteroid_map = ''
    with open(filename) as fh:
        while True:
            map_line = fh.readline()
            if not map_line:
                break
            asteroid_map += map_line
    return asteroid_map


def convert_map_to_coords(asteroid_map):
    coords = []
    x = 0
    y = 0
    for c in asteroid_map:
        if c == '#':
            coords.append((x, y))
        elif c == '\n':
            y += 1
            x = -1
        elif c != '.':
            raise Exception('Unknown map character {0}!'.format(c))
        x += 1
    return coords


def find_best_location(filename):
    asteroid_map = load_asteroid_map(filename)
    print(asteroid_map)
    asteroid_coords = convert_map_to_coords(asteroid_map)
    print(asteroid_coords)
    return (0,0), 0


def test(filename, expected_location, expected_detected):
    location, detected = find_best_location(filename)
    assert location == expected_location, \
        'Expected to find location {0} but found {1}!'.format(expected_location, location)
    assert detected == expected_detected, \
        'Expected to detect {0} asteroids but detected {1}!'.format(expected_detected, detected)


def main():
    test('test10a.txt', (3, 4), 8)
    test('test10b.txt', (5, 8), 33)
    test('test10c.txt', (1, 2), 35)
    test('test10d.txt', (6, 3), 41)
    test('test10e.txt', (11, 13), 210)

    location, detected = find_best_location('test10e.txt')
    print('Day 10 Step 1, best location {1} detects {1} asteroids'.format(location, detected))


if __name__ == '__main__':
    main()
