def determine_sue(filename: str) -> int:
    return 0


def main() -> None:
    sue_number = determine_sue('input16.txt')
    print('Day 16, Step 1 Aunt Sue number {0} sent the gift.'.format(sue_number))


if __name__ == '__main__':
    main()
