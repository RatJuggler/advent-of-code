def valid_password(password):
    return password == password


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
    test_valid_password('hijklmmn', False)
    test_valid_password('abbceffg', False)
    test_valid_password('abbcegjk', True)
    test_next_password('abcdefgh', 'abcdffaa')
    test_next_password('ghijklmn', 'ghjaabcc')


if __name__ == '__main__':
    main()
