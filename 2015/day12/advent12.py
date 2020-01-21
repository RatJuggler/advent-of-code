def sum_array(array: list):
    array_sum = 0
    for element in array:
        print(element, type(element))
        if type(element) is list:
            array_sum += sum_array(element)
        else:
            array_sum += element
    return array_sum


def sum_object(object: dict):
    object_sum = 0
    for element in object.values():
        print(element, type(element))
        if type(element) is dict:
            object_sum += sum_object(element)
        else:
            object_sum += element
    return object_sum


def test_sum_array(array_string: str, expected_sum):
    array_list = eval(array_string)
    array_sum = sum_array(array_list)
    assert array_sum == expected_sum, \
        'Expected sum of array {0} to be {1} but was {2}'.format(array_string, expected_sum, array_sum)


def test_sum_object(object_string: str, expected_sum):
    object_dict = eval(object_string)
    object_sum = sum_object(object_dict)
    assert object_sum == expected_sum, \
        'Expected sum of object {0} to be {1} but was {2}'.format(object_string, expected_sum, object_sum)


def main():
    test_sum_array('[1,2,3]', 6)
    test_sum_array('[[[3]]]', 3)
    test_sum_array('[]', 0)
    test_sum_object('{"a":2,"b":4}', 6)
    test_sum_object('{"a":{"b":4},"c":-1}', 3)
    test_sum_object('{}', 0)


if __name__ == '__main__':
    main()
