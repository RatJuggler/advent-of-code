def find_number(secret_key):
    return 0


def simple_test_step1(secret_key, expected_number):
    number_found = find_number(secret_key)
    assert number_found == expected_number, \
        'Expected number {0} for secret key {1} but found {2}!'.format(expected_number, secret_key, number_found)


def step1_find_number(secret_key):
    number_found = find_number(secret_key)
    print('Day 4, Step 1 number for secret key {0} is {1}.'.format(secret_key, number_found))


def main():
    simple_test_step1('abcdef', '609043')
    simple_test_step1('pqrstuv', '1048970')
    step1_find_number('iwrupvqb')


if __name__ == '__main__':
    main()