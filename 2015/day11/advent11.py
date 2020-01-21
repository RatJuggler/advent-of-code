import re


def is_eight_letters(password):
    regex = r'^[a-z]{8}$'
    matches = re.match(regex, password)
    return matches is not None


def contains_iol(password):
    regex = r'[iol]'
    matches = re.findall(regex, password)
    return len(matches) > 0


def contains_sequence(password):
    sequence = 0
    for c in range(len(password) - 1):
        if ord(password[c + 1]) != ord(password[c]) + 1:
            sequence = 0
        elif sequence == 2:
            return True
        else:
            sequence += 1
    return False


def valid_password(password):
    if not is_eight_letters(password):
        return False
    if contains_iol(password):
        return False
    if not contains_sequence(password):
        return False
    return True


def generate_next_password(password):
    return password


def test_valid_password(password, expected_valid):
    valid = valid_password(password)
    assert valid == expected_valid, \
        'Expected password {0} validity to be {1} but was {2}'.format(password, expected_valid, valid)


def test_next_password(password, expected_next_password):
    next_password = generate_next_password(password)
    assert next_password == expected_next_password, \
        'Expected next password for {0} to be {1} but was {2}'.format(password, expected_next_password, next_password)


def main():
    test_valid_password('abcddee', False)
    test_valid_password('12345678', False)
    test_valid_password('hijklmmn', False)
    test_valid_password('abbceffg', False)
    test_valid_password('abbcegjk', False)
    test_valid_password('abcxxkkq', True)
    test_valid_password('abcdffaa', True)
    test_valid_password('ghjaabcc', True)
    test_next_password('abcdefgh', 'abcdffaa')
    test_next_password('ghijklmn', 'ghjaabcc')


if __name__ == '__main__':
    main()
