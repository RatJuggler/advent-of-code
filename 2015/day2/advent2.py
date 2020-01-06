import re


def parse_dimensions(dimensions):
    return [int(dimension) for dimension in re.split(r'x', dimensions)]


def process_presents(filename, calculation):
    total_required = 0
    with open(filename) as fh:
        for present_dimensions in fh:
            total_required += calculation(present_dimensions)
    return total_required


def calculate_paper_required(present_dimensions):
    l, w, h = parse_dimensions(present_dimensions)
    side1 = l * w
    side2 = w * h
    side3 = h * l
    return (2 * side1) + (2 * side2) + (2 * side3) + min(side1, side2, side3)


def simple_test_step1(present_dimensions, expected_paper_required):
    paper_required = calculate_paper_required(present_dimensions)
    assert paper_required == expected_paper_required, \
        'Expected to need {0} sq ft of wrapping paper for a present of dimensions {1} but calculated {2}!'\
        .format(expected_paper_required, present_dimensions, paper_required)


def step1_calculate_total_paper_required(filename):
    total_paper_required = process_presents(filename, calculate_paper_required)
    print('Day 2, Step 1 wrapping paper required = {0} sq ft'.format(total_paper_required))


def calculate_ribbon_required(present_dimensions):
    l, w, h = parse_dimensions(present_dimensions)
    face1 = 2 * (l + w)
    face2 = 2 * (w + h)
    face3 = 2 * (h + l)
    return min(face1, face2, face3) + (l * w * h)


def simple_test_step2(present_dimensions, expected_ribbon_required):
    ribbon_required = calculate_ribbon_required(present_dimensions)
    assert ribbon_required == expected_ribbon_required, \
        'Expected to need {0} ft of ribbon for a present of dimensions {1} but calculated {2}!'\
        .format(expected_ribbon_required, present_dimensions, ribbon_required)


def step2_calculate_total_ribbon_required(filename):
    total_ribbon_required = process_presents(filename, calculate_ribbon_required)
    print('Day 2, Step 2 ribbon required = {0} ft'.format(total_ribbon_required))


def main():
    simple_test_step1('2x3x4', 58)
    simple_test_step1('1x1x10', 43)
    step1_calculate_total_paper_required('input2.txt')
    simple_test_step2('2x3x4', 34)
    simple_test_step2('1x1x10', 14)
    step2_calculate_total_ribbon_required('input2.txt')


if __name__ == '__main__':
    main()
