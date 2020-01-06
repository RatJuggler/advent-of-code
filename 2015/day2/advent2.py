def calculate_paper_required(present_dimensions):
    return 0


def simple_test_step1(present_dimensions, expected_paper_required):
    paper_required = calculate_paper_required(present_dimensions)
    assert paper_required == expected_paper_required, \
        'Expected to need {0} sq ft of wrapping paper for a present of dimensions {1} but calculated {2}!'\
            .format(expected_paper_required, present_dimensions, paper_required)


def main():
    simple_test_step1('2x3x4', 58)
    simple_test_step1('1x1x10', 43)


if __name__ == '__main__':
    main()
