def find_best_location(filename):
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
