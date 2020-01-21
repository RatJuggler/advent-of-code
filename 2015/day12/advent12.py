def sum_array(array_list: list):
    array_sum = 0
    for element in array_list:
        print(element, type(element))
        if type(element) is list:
            array_sum += sum_array(element)
        elif type(element) is dict:
            array_sum += sum_object(element)
        elif type(element) is int:
            array_sum += element
    return array_sum


def sum_object(object_dict: dict):
    object_sum = 0
    for element in object_dict.values():
        print(element, type(element))
        if type(element) is dict:
            object_sum += sum_object(element)
        elif type(element) is list:
            object_sum += sum_array(element)
        elif type(element) is int:
            object_sum += element
    return object_sum


def sum_string(string):
    eval_string = eval(string)
    if type(eval_string) is list:
        return sum_array(eval_string)
    elif type(eval_string) is dict:
        return sum_object(eval_string)
    else:
        return -1


def sum_string_from_file(filename):
    with open(filename) as fh:
        file_string = fh.readline()
    return sum_string(file_string)


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


def test_sum_string(string: str, expected_sum):
    string_sum = sum_string(string)
    assert string_sum == expected_sum, \
        'Expected sum of string {0} to be {1} but was {2}'.format(string, expected_sum, string_sum)


def main():
    test_sum_array('[1,2,3]', 6)
    test_sum_array('[[[3]]]', 3)
    test_sum_array('[]', 0)
    test_sum_object('{"a":2,"b":4}', 6)
    test_sum_object('{"a":{"b":4},"c":-1}', 3)
    test_sum_object('{}', 0)
    test_sum_string('{"a":[-1,1]}', 0)
    test_sum_string('[-1,{"a":1}]', 0)
    file_string_sum = sum_string_from_file('input12.txt')
    print('Day 12, Step sum of file = {0}'.format(file_string_sum))


if __name__ == '__main__':
    main()
