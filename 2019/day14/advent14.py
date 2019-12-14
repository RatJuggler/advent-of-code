def parse_reaction_line(reaction_line):
    produces_indicator = reaction_line.find('=>')
    input_chemicals = reaction_line[:produces_indicator].split(',')
    output_chemical = reaction_line[produces_indicator + 3:]
    return input_chemicals, output_chemical


def parse_chemical_details(chemical_details):
    return chemical_details.strip().split(' ')


def parse_input_chemicals(input_chemicals):
    inputs = []
    for chemical_details in input_chemicals:
        inputs.append((parse_chemical_details(chemical_details)))
    return inputs


def load_reactions(filename):
    reactions = {}
    with open(filename) as f:
        for reaction_line in f:
            input_chemicals, output_chemical = parse_reaction_line(reaction_line.rstrip('\n'))
            output_quantity, output_name = parse_chemical_details(output_chemical)
            reactions[output_name] = (output_quantity, parse_input_chemicals(input_chemicals))
        return reactions


def analyse_reactions(chemical_required, reactions):
    produces_quantity = reactions[chemical_required][0]
    requires_chemicals = reactions[chemical_required][1]
    print('{0} {1} requires {2}'.format(produces_quantity, chemical_required, requires_chemicals))
    for requires_chemical in requires_chemicals:
        if requires_chemical[1] != 'ORE':
            analyse_reactions(requires_chemical[1], reactions)


def load_and_analyse_reactions(filename):
    reactions = load_reactions(filename)
    analyse_reactions('FUEL', reactions)
    return 0


def test_ore_required(filename, expected_ore_required):
    ore_required = load_and_analyse_reactions(filename)
    assert ore_required == expected_ore_required, \
        'Expected to need {0} ore but actually used {1}!'.format(expected_ore_required, ore_required)


def main():
    test_ore_required('test14a.txt', 31)
    test_ore_required('test14b.txt', 165)
    test_ore_required('test14c.txt', 13312)
    test_ore_required('test14d.txt', 180697)
    test_ore_required('test14e.txt', 22107136)
    ore_required = load_and_analyse_reactions('input14.txt')
    print('Day 14, Step 1 - Ore required to produce 1 Fuel is {0}'.format(ore_required))


if __name__ == '__main__':
    main()
