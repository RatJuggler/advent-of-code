def sum_list(array_list: list, ignore_red: bool) -> int:
    list_sum = 0
    for element in array_list:
        if type(element) is list:
            list_sum += sum_list(element, ignore_red)
        elif type(element) is dict:
            if ignore_red and "red" in element.values():
                continue
            list_sum += sum_list(list(element.values()), ignore_red)
        elif type(element) is int:
            list_sum += element
        # elif ignore other types.
    return list_sum


def sum_string(string, ignore_red: bool = False) -> int:
    eval_string = eval(string)
    if type(eval_string) is list:
        return sum_list(eval_string, ignore_red)
    elif type(eval_string) is dict:
        return sum_list([eval_string], ignore_red)
    else:
        return -1


def sum_string_from_file(filename: str, ignore_red: bool = False) -> int:
    with open(filename) as fh:
        file_string = fh.readline()
    return sum_string(file_string, ignore_red)


def test_sum_array(array_string: str, expected_sum: int) -> None:
    array_list = eval(array_string)
    array_sum = sum_list(array_list, False)
    assert array_sum == expected_sum, \
        'Expected sum of array {0} to be {1} but was {2}'.format(array_string, expected_sum, array_sum)


def test_sum_object(object_string: str, expected_sum: int) -> None:
    object_dict = eval(object_string)
    object_sum = sum_list(list(object_dict.values()), False)
    assert object_sum == expected_sum, \
        'Expected sum of object {0} to be {1} but was {2}'.format(object_string, expected_sum, object_sum)


def test_sum_string(string: str, expected_sum: int, ignore_red: bool = False) -> None:
    string_sum = sum_string(string, ignore_red)
    assert string_sum == expected_sum, \
        'Expected sum of string {0} to be {1} but was {2}'.format(string, expected_sum, string_sum)


def main() -> None:
    test_sum_array('[1,2,3]', 6)
    test_sum_array('[[[3]]]', 3)
    test_sum_array('[]', 0)
    test_sum_object('{"a":2,"b":4}', 6)
    test_sum_object('{"a":{"b":4},"c":-1}', 3)
    test_sum_object('{}', 0)
    test_sum_string('{"a":[-1,1]}', 0)
    test_sum_string('[-1,{"a":1}]', 0)
    file_string_sum = sum_string_from_file('input12.txt')
    print('Day 12, Step 1 sum of file = {0}'.format(file_string_sum))
    test_sum_string('[1,{"c":"red","b":2},3]', 4, True)
    test_sum_string('{"d":"red","e":[1,2,3,4],"f":5}', 0, True)
    test_sum_string('[1,"red",5]', 6, True)
    file_string_sum = sum_string_from_file('input12.txt', True)
    print('Day 12, Step 2 sum of file ignoring red = {0}'.format(file_string_sum))


if __name__ == '__main__':
    main()
