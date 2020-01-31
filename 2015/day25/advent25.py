from typing import List


def build_blank_code_sheet(size: int) -> List[List[int]]:
    code_sheet = []
    for row in range(1, size + 1):
        sheet_row = []
        for column in range(1, size - row + 2):
            sheet_row.append(column)
        code_sheet.append(sheet_row)
    return code_sheet


def test_code_sheet_layout(size: int) -> None:
    code_sheet = build_blank_code_sheet(size)
    for row in code_sheet:
        print(row)


def main() -> None:
    test_code_sheet_layout(6)


if __name__ == '__main__':
    main()
