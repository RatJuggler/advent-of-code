def fft(input_signal, base_pattern, phases):
    return [0]


def test_fft(input_signal, base_pattern, phases, expected_output):
    output = fft(list(input_signal), base_pattern, phases)
    assert output == expected_output, 'Expect output to be {0} but was {1}!'.format(expected_output, output)


def main():
    test_fft('12345678', [0, 1, 0, -1], 4, '01029498')


if __name__ == '__main__':
    main()
