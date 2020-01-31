from typing import Callable, List


def build_code_sheet(size: int, start_code: int, next_code: Callable[[int], int]) -> List[List[int]]:
    code_sheet = []
    code_sequence = start_code
    for i in range(1, size + 1):
        column = 0
        code_sheet.append([])
        for row in range(i, 0, -1):
            code_sheet[row - 1].append(code_sequence)
            code_sequence = next_code(code_sequence)
            column += 1
    return code_sheet


def test_code_sheet_layout(size: int) -> None:
    code_sheet = build_code_sheet(size, 1, lambda x: x + 1)
    for sheet_row in code_sheet:
        print(sheet_row)
    assert code_sheet[3][1] == 12, 'Expect row 4, column 2 to be 12 but was {0}!'.format(code_sheet[3][1])
    assert code_sheet[0][4] == 15, 'Expect row 1, column 5 to be 15 but was {0}!'.format(code_sheet[0][5])


def test_code_sheet_calculation(size: int ) -> None:
    code_sheet = build_code_sheet(size, 20151125, lambda x: (x * 252533) % 33554393)
    for sheet_row in code_sheet:
        print(sheet_row)
    assert code_sheet[3][1] == 32451966, 'Expect row 4, column 2 to be 32451966 but was {0}!'.format(code_sheet[3][1])
    assert code_sheet[0][4] == 10071777, 'Expect row 1, column 5 to be 10071777 but was {0}!'.format(code_sheet[0][5])


def main() -> None:
    test_code_sheet_layout(6)
    test_code_sheet_calculation(6)
    code_sheet = build_code_sheet(3019, 20151125, lambda x: (x * 252533) % 33554393)
    print('Day 24, Step 1 code at row 3010, column 3019 is {0}'.format(code_sheet[3009][3018]))


if __name__ == '__main__':
    main()
