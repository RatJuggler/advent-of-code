def plan_seating(filename):
    happiness = 0
    return happiness


def test_plan_seating(filename, expected_happiness):
    happiness = plan_seating(filename)
    assert happiness == expected_happiness, \
        'Expected happiness of {0} for {1} but was {2}!'.format(expected_happiness, filename, happiness)


def main():
    test_plan_seating('test13a.txt', 330)


if __name__ == '__main__':
    main()
