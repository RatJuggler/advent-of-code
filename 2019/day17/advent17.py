from day17.intcode_processor import IntcodeProcessor


def step1_sum_alignment_parameters():
    ascii_view = IntcodeProcessor.from_file('input17.txt')
    output = ascii_view.run([])
    for c in output:
        print(chr(c), end='')


def main():
    step1_sum_alignment_parameters()


if __name__ == '__main__':
    main()
