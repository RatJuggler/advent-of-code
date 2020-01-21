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
    sequence = 1
    for i in range(len(password) - 1):
        c = password[i]
#        skip = 2 if c in ['h', 'n', 'k'] else 1
        skip = 1
        if ord(password[i + 1]) != ord(c) + skip:
            sequence = 1
        elif sequence == 2:
            return True
        else:
            sequence += 1
    return False


def contains_pair(password):
    regex = r'(?P<pair>[a-z])\1'
    matches = re.findall(regex, password)
    if len(matches) < 2:
        return False
    for match in matches:
        if matches.count(match) < len(matches):
            return True
    return False


def valid_password(password):
    if not is_eight_letters(password):
        return False
    if contains_iol(password):
        return False
    if not contains_sequence(password):
        return False
    return contains_pair(password)


def increment_character(c):
    return 'a' if c == 'z' else chr(ord(c) + 1)


def increment_password(password, i):
    password = password[:i] + increment_character(password[i]) + password[i+1:]
    if password[i] == 'a':
        password = increment_password(password, i - 1)
    return password


def generate_next_password(password):
    while not valid_password(password):
        password = increment_password(password, len(password) - 1)
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
    test_valid_password('abcxjkmq', False)
    test_valid_password('abcxxkmq', False)
    test_valid_password('abcxxxxx', False)
    test_valid_password('abcxxkkq', True)
    test_valid_password('abcdffaa', True)
    test_valid_password('abccccaa', True)
    test_valid_password('ghjaabcc', True)
    test_next_password('abcdefgh', 'abcdffaa')
    test_next_password('ghijklmn', 'ghjaabcc')


if __name__ == '__main__':
    main()
