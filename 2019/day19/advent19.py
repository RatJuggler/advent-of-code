from day19.intcode_processor import IntcodeProcessor


def step1_points_affected_by_tractor_beam():
    affected = 0
    for y in range(50):
        for x in range(50):
            drone_control = IntcodeProcessor.from_file('input19.txt')
            output = drone_control.run([x, y])
            c = '.'
            if output[0] == 1:
                c = '#'
                affected += 1
            print(c, end='')
        print()
    print('Day 19, Step points affected found = {0}'.format(affected))


def main():
    step1_points_affected_by_tractor_beam()


if __name__ == '__main__':
    main()
