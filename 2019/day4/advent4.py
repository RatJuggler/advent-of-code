def decrease_digit(password):
    for i in range(len(password) - 1):
        if password[i + 1] < password[i]:
            return True
    return False


def step1_adjacent_digits(password):
    for i in range(len(password) - 1):
        if password[i] == password[i + 1]:
            return True
    return False


def step2_adjacent_digits(password):
    previous = ''
    adjacent = 1
    for i in range(len(password)):
        if password[i] == previous:
            adjacent += 1
        else:
            if adjacent == 2:
                return True
            adjacent = 1
        previous = password[i]
    return adjacent == 2


def step1_validation(password):
    return step1_adjacent_digits(password) and not decrease_digit(password)


def step2_validation(password):
    return step2_adjacent_digits(password) and not decrease_digit(password)


def validate_passwords(validation_test):
    valid_passwords = 0
    for potential_password in range(134792, 675810+1):
        password_string = str(potential_password)
        if validation_test(password_string):
            valid_passwords += 1
    return valid_passwords


def main():
    assert(step1_adjacent_digits('111123'))
    assert(not step1_adjacent_digits('135679'))
    assert(not decrease_digit('111123'))
    assert(not decrease_digit('135679'))
    assert(decrease_digit('654321'))
    assert(decrease_digit('654421'))
    assert(step1_validation('111111'))
    assert(not step1_validation('223450'))
    assert(not step1_validation('123789'))
    assert(step2_validation('112233'))
    assert(step1_validation('123444'))
    assert(not step2_validation('123444'))
    assert(step2_validation('111122'))

    step1_passwords = validate_passwords(step1_validation)
    print('For Step 1 there are {0} valid passwords.'.format(step1_passwords))
    step2_passwords = validate_passwords(step2_validation)
    print('For Step 2 there are {0} valid passwords.'.format(step2_passwords))


if __name__ == '__main__':
    main()
