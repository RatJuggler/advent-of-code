def evaluate_circuit(filename):
    return {}


def test_evaluate_circuit(filename, expected_signals):
    signals = evaluate_circuit(filename)
    assert signals == expected_signals, 'Expected circuit to produce {0} but was {1}'.format(expected_signals, signals)


def main():
    test_evaluate_circuit('test7a.txt',
                          {'d': 72, 'e': 507, 'f': 492, 'g': 114, 'h': 65412, 'i': 65079, 'x': 123, 'y': 456})
    signals = evaluate_circuit('input7.txt')
    print(signals)


if __name__ == '__main__':
    main()
