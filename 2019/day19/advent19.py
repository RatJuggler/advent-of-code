from day19.intcode_processor import IntcodeProcessor


def visualise(size):
    drone_control = IntcodeProcessor.from_file('input19.txt')
    affected = 0
    for y in range(size):
        for x in range(size):
            drone_control.reset()
            output = drone_control.run([x, y])
            c = '.'
            if output[0] == 1:
                c = '#'
                affected += 1
            print(c, end='')
        print(y, affected)


def move_drone(drone_control, x, y):
    drone_control.reset()
    output = drone_control.run([x, y])
    return output[0]


def scan_for_affected(drone_control, x, y, max_columns):
    drone_status = 0
    while drone_status == 0 and x <= max_columns:
        x += 1
        drone_status = move_drone(drone_control, x, y)
    return -1 if x > max_columns else x


def count_affected(drone_control, x_from, y, max_columns):
    affected = 0
    x = 0
    drone_status = 1
    while drone_status == 1 and x_from + x < max_columns:
        affected += 1
        x += 1
        drone_status = move_drone(drone_control, x_from + x, y)
    return affected


def step1_points_affected_by_tractor_beam(size):
    drone_control = IntcodeProcessor.from_file('input19.txt')
    x = 0
    y = 0
    total_affected = 0
    while y < size:
        print('Scanning row {0} from {1}...'.format(y, x), end='')
        x = scan_for_affected(drone_control, x - 1, y, size)
        if x == -1:
            x = 0
            columns_affected = 0
        else:
            columns_affected = count_affected(drone_control, x, y, size)
        print('Found {0}'.format(columns_affected))
        total_affected += columns_affected
        y += 1
    print('Day 19, Step 1 points affected found = {0}'.format(total_affected))


def square_test(rows_affected, size):
    if len(rows_affected) < size:
        return True
    square_first_row = rows_affected[0]
    first_row_end = square_first_row[0] + square_first_row[2]
    square_last_row = rows_affected[size - 1]
    last_row_start = square_last_row[0]
    return first_row_end - last_row_start < size


def step2_find_closest_square(size):
    drone_control = IntcodeProcessor.from_file('input19.txt')
    x = 0
    y = 0
    max_columns = 10000
    rows_affected = []
    while square_test(rows_affected, size):
        print('Scanning row {0} from {1}...'.format(y, x), end='')
        x = scan_for_affected(drone_control, x - 1, y, max_columns)
        if x == -1:
            x = 0
            columns_affected = 0
        else:
            columns_affected = count_affected(drone_control, x, y, max_columns)
        print('Found {0} from {1}'.format(columns_affected, x))
        if columns_affected >= size:
            rows_affected.append((x, y, columns_affected))
            if len(rows_affected) > size:
                rows_affected.pop(0)
        y += 1
    print(rows_affected)
    first_row = rows_affected[0]
    value = first_row[0] * 10000 + first_row[1]
    print('Day 19, Step 2 square size {0} starts at ({1}, {2}) giving value {3}'
          .format(size, first_row[0], first_row[1], value))


def main():
    # visualise(50)
    # step1_points_affected_by_tractor_beam(50)
    step2_find_closest_square(100)


if __name__ == '__main__':
    main()
