from typing import List


def build_blank_code_sheet(size: int) -> List[List[int]]:
    code_sheet = []
    for row in range(1, size + 1):
        sheet_row = []
        for column in range(1, size - row + 2):
            sheet_row.append(0)
        code_sheet.append(sheet_row)
    return code_sheet


def test_code_sheet_layout(size: int) -> None:
    code_sheet = build_blank_code_sheet(size)
    for sheet_row in code_sheet:
        print(sheet_row)
    code_sequence = 1
    for i in range(1, size + 1):
        column = 0
        for row in range(i, 0, -1):
            code_sheet[row - 1][column] = code_sequence
            code_sequence += 1
            column += 1
    for sheet_row in code_sheet:
        print(sheet_row)


def main() -> None:
    test_code_sheet_layout(6)


if __name__ == '__main__':
    main()
