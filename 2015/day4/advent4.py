import hashlib
from time import perf_counter


def find_number(secret_key: str, for_hash_beginning: str) -> str:
    number = 1
    utf8_key = secret_key.encode('utf-8')
    hash_len = len(for_hash_beginning)
    while True:
        full_key = utf8_key + str(number).encode('utf-8')
        md5 = hashlib.md5(full_key).hexdigest()
        if md5[:hash_len] == for_hash_beginning:
            break
        number += 1
    return str(number)


def find_number_timed(secret_key: str, for_hash_beginning: str) -> str:
    t_start = perf_counter()
    number_found = find_number(secret_key, for_hash_beginning)
    t_stop = perf_counter()
    print("Hashing took {0} secs.".format(t_stop - t_start))
    return number_found


def simple_test_step1(secret_key: str, expected_number: str) -> None:
    number_found = find_number(secret_key, '00000')
    assert number_found == expected_number, \
        'Expected number {0} for secret key {1} but found {2}!'.format(expected_number, secret_key, number_found)


def step1_find_number(secret_key: str) -> None:
    number_found = find_number_timed(secret_key, '00000')
    print('Day 4, Step 1 number for secret key {0} is {1}.'.format(secret_key, number_found))


def step2_find_number(secret_key: str) -> None:
    number_found = find_number_timed(secret_key, '000000')
    print('Day 4, Step 2 number for secret key {0} is {1}.'.format(secret_key, number_found))


def main() -> None:
    simple_test_step1('abcdef', '609043')
    simple_test_step1('pqrstuv', '1048970')
    step1_find_number('iwrupvqb')
    step2_find_number('iwrupvqb')


if __name__ == '__main__':
    main()
