from intcode_processor import IntcodeProcessor
from random import randint
import curses


def update_position(position, move):
    if move == 1:
        position[1] -= 1
    elif move == 2:
        position[1] += 1
    elif move == 3:
        position[0] -= 1
    elif move == 4:
        position[0] += 1
    return position


def display_position(status, droid_position, move, screen):
    if status == 0:
        wall = update_position([droid_position[0], droid_position[1]], move)
        screen.addch(wall[1], wall[0], '#')
    elif status == 1:
        screen.addch(droid_position[1], droid_position[0], ' ')
        droid_position = update_position(droid_position, move)
        screen.addch(droid_position[1], droid_position[0], 'D')
    elif status == 2:
        oxygen_system = update_position([droid_position[0], droid_position[1]], move)
        screen.addch(oxygen_system[1], oxygen_system[0], '*')
        screen.addstr(0, 20, ' Oxygen System: {0} '.format(oxygen_system))
    return droid_position


def control_loop(screen):
    repair_droid = IntcodeProcessor.from_file('input15.txt')
    droid_position = [40, 25]
    moves_taken = 0
    move = 0
    while not repair_droid.is_halted():
        if move != 0:
            output = repair_droid.run([move])
            droid_position = display_position(output[0], droid_position, move, screen)
            moves_taken += 1
        screen.refresh()
        screen.timeout(10)
        c = screen.getch()
        screen.addstr(0, 0, ' Repair Droid: {0} '.format(droid_position))
        if c == 119:  # W key
            move = 1
        elif c == 115:  # S key
            move = 2
        elif c == 97:  # A key
            move = 3
        elif c == 100:  # D key
            move = 4
        else:
            move = 0
    return moves_taken


def step1_count_moves_to_oxygen_system():
    screen = curses.initscr()
    curses.noecho()
    curses.curs_set(0)
    try:
        moves_taken = control_loop(screen)
    finally:
        curses.curs_set(1)
        curses.echo()
        curses.endwin()
    print('Day 15, Step 1 moves to oxygen system = {0}'.format(moves_taken))


def main():
    step1_count_moves_to_oxygen_system()


if __name__ == '__main__':
    main()
