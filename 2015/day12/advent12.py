def sum_array(array):
    return 0


def test_sum_arrays(array, expected_sum):
    array_sum = sum_array(array)
    assert array_sum == expected_sum, \
        'Expected sum of array {0} to be {1} but was {2}'.format(array, expected_sum, array_sum)


def main():
    test_sum_arrays('[1,2,3]', 6)
    test_sum_arrays('[[[3]]]', 3)
    test_sum_arrays('[]', 0)


if __name__ == '__main__':
    main()
