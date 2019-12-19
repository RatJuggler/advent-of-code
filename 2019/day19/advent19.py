from day19.intcode_processor import IntcodeProcessor


def step1_points_affected_by_tractor_beam(size):
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
        print(y)
    print('Day 19, Step points affected found = {0}'.format(affected))


def move_drone(x, y):
    drone_control = IntcodeProcessor.from_file('input19.txt')
    output = drone_control.run([x, y])
    return output[0]


def scan_for_affected(x, y):
    drone_status = 0
    while drone_status == 0 and x < 1000:
        drone_status = move_drone(x, y)
        if drone_status == 0:
            x += 1
    return 0 if x == 1000 else x


def count_affected(x_from, y):
    affected = 0
    x = -1
    drone_status = 1
    while drone_status == 1:
        x += 1
        drone_status = move_drone(x_from + x, y)
        if drone_status == 1:
            affected += 1
    return affected


def step2_find_closest_square(size):
    x = 0
    y = -1
    column_affected = 0
    while column_affected < size:
        y += 1
        print('Scanning row {0} from {1}...'.format(y, x), end='')
        x = scan_for_affected(x, y)
        column_affected = count_affected(x, y)
        print('Found {0}'.format(column_affected))
    print(x, y)


def main():
    step1_points_affected_by_tractor_beam(100)
    step2_find_closest_square(10)


if __name__ == '__main__':
    main()
