def load_and_run_reaction(filename):
    return 0


def test_ore_required(filename, expected_ore_required):
    ore_required = load_and_run_reaction(filename)
    assert ore_required == expected_ore_required, \
        'Expected to need {0} ore but actually used {1}!'.format(expected_ore_required, ore_required)


def main():
    test_ore_required('advent14a.txt', 31)
    test_ore_required('advent14b.txt', 165)
    test_ore_required('advent14c.txt', 13312)
    test_ore_required('advent14d.txt', 180697)
    test_ore_required('advent14e.txt', 22107136)
    ore_required = load_and_run_reaction('input14.txt')
    print('Day 14, Step 1 - Ore required to produce 1 Fuel is {0}'.format(ore_required))


if __name__ == '__main__':
    main()
