def parse_look(look):
    groups = []
    group = None
    for c in look:
        if group is None:
            group = c
        elif c in group:
            group += c
        else:
            groups.append(group)
            group = c
    groups.append(group)
    return groups


def look_and_say(look):
    groups = parse_look(look)
    say = ''
    for group in groups:
        say += str(len(group))
        say += group[0]
    return say


def iterate_look_and_say(look, iterations):
    say = None
    for i in range(iterations):
        say = look_and_say(look)
        look = say
    return say


def test_look_and_say(look, expected_say):
    say = look_and_say(look)
    assert say == expected_say, \
        'Expected to say {0} for {1} but was {2}'.format(expected_say, look, say)


def test_iterate_look_and_say(look, iterations, expected_say):
    say = iterate_look_and_say(look, iterations)
    assert say == expected_say, \
        'Expected to say {0} for {1} but was {2}'.format(expected_say, look, say)


def main():
    test_look_and_say('1', '11')
    test_look_and_say('11', '21')
    test_look_and_say('21', '1211')
    test_look_and_say('1211', '111221')
    test_look_and_say('111221', '312211')
    test_iterate_look_and_say('1', 5, '312211')
    step1_look = '3113322113'
    step1_iterations = 40
    step1_say_length = len(iterate_look_and_say(step1_look, step1_iterations))
    print('Day 10, Step 1 look {0} after {1} iterations is {2} characters long!'
          .format(step1_look, step1_iterations, step1_say_length))


if __name__ == '__main__':
    main()
