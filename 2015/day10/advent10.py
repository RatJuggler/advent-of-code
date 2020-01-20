def look_and_say(look):
    pass


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
