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


def test_look_and_say(look, expected_say):
    say = look_and_say(look)
    assert say == expected_say, \
        'Expected to say {0} for {1} but was {2}'.format(expected_say, look, say)


def main():
    test_look_and_say('1', '11')
    test_look_and_say('11', '21')
    test_look_and_say('21', '1211')
    test_look_and_say('1211', '111221')
    test_look_and_say('111221', '312211')


if __name__ == '__main__':
    main()
