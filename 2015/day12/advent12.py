def sum_array(array_list: list, ignore_red):
    array_sum = 0
    for element in array_list:
        if type(element) is list:
            array_sum += sum_array(element, ignore_red)
        elif type(element) is dict:
            array_sum += sum_object(element, ignore_red)
        elif type(element) is int:
            array_sum += element
    return array_sum


def sum_object(object_dict: dict, ignore_red):
    object_sum = 0
    if ignore_red and "red" in object_dict.values():
        return object_sum
    for element in object_dict.values():
        if type(element) is dict:
            object_sum += sum_object(element, ignore_red)
        elif type(element) is list:
            object_sum += sum_array(element, ignore_red)
        elif type(element) is int:
            object_sum += element
    return object_sum


def sum_string(string, ignore_red=False):
    eval_string = eval(string)
    if type(eval_string) is list:
        return sum_array(eval_string, ignore_red)
    elif type(eval_string) is dict:
        return sum_object(eval_string, ignore_red)
    else:
        return -1


def sum_string_from_file(filename):
    with open(filename) as fh:
        file_string = fh.readline()
    return sum_string(file_string)


def test_sum_array(array_string: str, expected_sum):
    array_list = eval(array_string)
    array_sum = sum_array(array_list, False)
    assert array_sum == expected_sum, \
        'Expected sum of array {0} to be {1} but was {2}'.format(array_string, expected_sum, array_sum)


def test_sum_object(object_string: str, expected_sum):
    object_dict = eval(object_string)
    object_sum = sum_object(object_dict, False)
    assert object_sum == expected_sum, \
        'Expected sum of object {0} to be {1} but was {2}'.format(object_string, expected_sum, object_sum)


def test_sum_string(string: str, expected_sum, ignore_red=False):
    string_sum = sum_string(string, ignore_red)
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
    test_sum_string('[1,{"c":"red","b":2},3]', 4, True)
    test_sum_string('{"d":"red","e":[1,2,3,4],"f":5}', 0, True)
    test_sum_string('[1,"red",5]', 6, True)


if __name__ == '__main__':
    main()
