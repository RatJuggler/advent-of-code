def find_best_ingredient_score(filename):
    with open(filename) as fh:
        for line in fh:
            print(line.strip())
    return 0, []


def test_find_best_ingredient_score(filename, expected_score, expected_teaspoons):
    best_score, teaspoons = find_best_ingredient_score(filename)
    assert best_score == expected_score, \
        'Expected to get a best score of {0} but was {1}!'.format(expected_score, best_score)
    assert teaspoons == expected_teaspoons, \
        'Expected to use {0} teaspoons but was {1}!'.format(expected_teaspoons, teaspoons)


def main():
    test_find_best_ingredient_score('test15a.txt', 62842880, [44, 56])
    best_score, teaspoons = find_best_ingredient_score('input15.txt')
    print('Day 15, Step 1 best ingredient score is {0} using {1} teaspoons.'.format(best_score, teaspoons))


if __name__ == '__main__':
    main()
