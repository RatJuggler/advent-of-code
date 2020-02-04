import re


def is_eight_letters(password: str) -> bool:
    regex = r'^[a-z]{8}$'
    matches = re.match(regex, password)
    return matches is not None


def contains_iol(password: str) -> bool:
    regex = r'[iol]'
    matches = re.findall(regex, password)
    return len(matches) > 0


def contains_sequence(password: str) -> bool:
    sequence = 1
    for i in range(len(password) - 1):
        if ord(password[i + 1]) != ord(password[i]) + 1:
            sequence = 1
        elif sequence == 2:
            return True
        else:
            sequence += 1
    return False


def contains_pair(password: str) -> bool:
    regex = r'(?P<pair>[a-z])\1'
    matches = re.findall(regex, password)
    if len(matches) < 2:
        return False
    for match in matches:
        if matches.count(match) < len(matches):
            return True
    return False


def valid_password(password: str) -> bool:
    return is_eight_letters(password) and not contains_iol(password) and \
           contains_sequence(password) and contains_pair(password)


def increment_character(c):
    return 'a' if c == 'z' else chr(ord(c) + 1)


def increment_password(password, i):
    password = password[:i] + increment_character(password[i]) + password[i+1:]
    if password[i] == 'a':
        password = increment_password(password, i - 1)
    return password


def generate_next_password(password):
    while True:
        password = increment_password(password, len(password) - 1)
        if valid_password(password):
            break
    return password


def test_valid_password(password: str, expected_valid: bool) -> None:
    valid = valid_password(password)
    assert valid == expected_valid, \
        'Expected password {0} validity to be {1} but was {2}'.format(password, expected_valid, valid)


def test_next_password(password: str, expected_next_password: str) -> None:
    next_password = generate_next_password(password)
    assert next_password == expected_next_password, \
        'Expected next password for {0} to be {1} but was {2}'.format(password, expected_next_password, next_password)


def generate_password(step: int, password: str) -> None:
    next_password = generate_next_password(password)
    print('Day 11, Step {0} next valid password after {1} is {2}.'.format(step, password, next_password))


def main() -> None:
    test_valid_password('abcddee', False)
    test_valid_password('12345678', False)
    test_valid_password('hijklmmn', False)
    test_valid_password('abbceffg', False)
    test_valid_password('abbcegjk', False)
    test_valid_password('abcxjkmq', False)
    test_valid_password('abcxxkmq', False)
    test_valid_password('abcxxxxx', False)
    test_valid_password('abcxxkkq', True)
    test_valid_password('abcdffaa', True)
    test_valid_password('abccccaa', True)
    test_valid_password('ghjaabcc', True)
    test_next_password('abcdefgh', 'abcdffaa')
    test_next_password('ghijklmn', 'ghjaabcc')
    generate_password(1, 'hepxcrrq')
    generate_password(2, 'hepxxyzz')


if __name__ == '__main__':
    main()
