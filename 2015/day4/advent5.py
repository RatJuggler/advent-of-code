import hashlib


def find_number(secret_key, for_hash_beginning):
    number = 1
    while True:
        full_key = secret_key + str(number)
        md5 = hashlib.md5(full_key.encode('utf-8')).hexdigest()
        if md5[:len(for_hash_beginning)] == for_hash_beginning:
            break
        number += 1
    return str(number)


def simple_test_step1(secret_key, expected_number):
    number_found = find_number(secret_key, '00000')
    assert number_found == expected_number, \
        'Expected number {0} for secret key {1} but found {2}!'.format(expected_number, secret_key, number_found)


def step1_find_number(secret_key):
    number_found = find_number(secret_key, '00000')
    print('Day 4, Step 1 number for secret key {0} is {1}.'.format(secret_key, number_found))


def step2_find_number(secret_key):
    number_found = find_number(secret_key, '000000')
    print('Day 4, Step 2 number for secret key {0} is {1}.'.format(secret_key, number_found))


def main():
    simple_test_step1('abcdef', '609043')
    simple_test_step1('pqrstuv', '1048970')
    step1_find_number('iwrupvqb')
    step2_find_number('iwrupvqb')


if __name__ == '__main__':
    main()
