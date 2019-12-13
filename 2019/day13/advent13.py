from day13.intcode_processor import IntcodeProcessor
import curses


def count_block_tiles(output):
    block_tiles = 0
    for tile_index in range(2, len(output), 3):
        block_tiles += int(output[tile_index] == 2)
    return block_tiles


def step1_count_block_tiles():
    game = IntcodeProcessor.from_file('input13.txt')
    output = game.run([])
    print('Day 13, Step 1 block tiles output = {0}'.format(count_block_tiles(output)))


def display_screen(output, screen):
    display_chars = ' @X=O?'
    score = 0
    bat_at = 0
    ball_at = 0
    for tile_index in range(0, len(output), 3):
        x, y, tile = output[tile_index:tile_index+3]
        if x == -1:
            score = tile
            screen.addstr(0, 0, '{0} '.format(score))
        else:
            if tile == 3:
                bat_at = x
            elif tile == 4:
                ball_at = x
            elif tile > 4:
                tile = 5
            screen.addch(y, x, display_chars[tile])
    return score, ball_at - bat_at


def game_loop(screen):
    game = IntcodeProcessor.from_file('input13.txt')
    game.write(0, 2)  # Set to free play.
    high_score = 0
    joystick = [0]
    while not game.is_halted():
        output = game.run(joystick)
        score, move_to = display_screen(output, screen)
        if score > high_score:
            high_score = score
        screen.refresh()
        screen.timeout(10)
        c = screen.getch()
        screen.addstr(0, 20, ' Key: {0} '.format(c))
        if c == 122:  # Z key
            joystick = [-1]
        elif c == 120:  # X key
            joystick = [1]
        else:
            joystick = [0] if move_to == 0 else [1] if move_to > 0 else [-1]
    return high_score


def step2_play_game():
    screen = curses.initscr()
    curses.noecho()
    curses.curs_set(0)
    try:
        high_score = game_loop(screen)
    finally:
        curses.curs_set(1)
        curses.echo()
        curses.endwin()
    print('Day 13, Step 2 highest score = {0}'.format(high_score))


def main():
    step1_count_block_tiles()
    step2_play_game()


if __name__ == '__main__':
    main()
