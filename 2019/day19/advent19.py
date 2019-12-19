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
            columns_affected = 0
        else:
            columns_affected = count_affected(drone_control, x, y, size)
        print('Found {0}'.format(columns_affected))
        total_affected += columns_affected
        y += 1
    print('Day 19, Step 1 points affected found = {0}'.format(total_affected))


def step2_find_closest_square(size):
    drone_control = IntcodeProcessor.from_file('input19.txt')
    x = 0
    y = 0
    columns_affected = 0
    while columns_affected < size:
        print('Scanning row {0} from {1}...'.format(y, x), end='')
        x = scan_for_affected(drone_control, x - 1, y, 1000)
        if x == -1:
            columns_affected = 0
        else:
            columns_affected = count_affected(drone_control, x, y, 1000)
        print('Found {0}'.format(columns_affected))
        y += 1


def main():
    # visualise(50)
    # step1_points_affected_by_tractor_beam(50)
    step2_find_closest_square(10)


if __name__ == '__main__':
    main()
