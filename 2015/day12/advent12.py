def sum_array(array: list):
    array_sum = 0
    for element in array:
        print(element, type(element))
        if type(element) is list:
            array_sum += sum_array(element)
        else:
            array_sum += element
    return array_sum


def test_sum_arrays(array_string: str, expected_sum):
    array_list = eval(array_string)
    array_sum = sum_array(array_list)
    assert array_sum == expected_sum, \
        'Expected sum of array {0} to be {1} but was {2}'.format(array_string, expected_sum, array_sum)


def main():
    test_sum_arrays('[1,2,3]', 6)
    test_sum_arrays('[[[3]]]', 3)
    test_sum_arrays('[]', 0)


if __name__ == '__main__':
    main()
