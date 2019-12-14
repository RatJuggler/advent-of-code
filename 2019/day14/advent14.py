class Chemical:

    def __init__(self, name, quantity):
        self.name = name
        self.quantity = int(quantity)

    def __repr__(self):
        return '{0} {1}'.format(self.quantity, self.name)


class Reaction:

    def __init__(self, produces, requires):
        self.produces = produces
        self.requires = requires

    def __repr__(self):
        return 'Production of {0} requires {1}'.format(self.produces, self.requires)


class Production:

    def __init__(self, of_chemical):
        self.of_chemical = of_chemical
        self.produced = 0
        self.consumed = 0

    def __repr__(self):
        return 'Chemical: {0} Produced: {1} Consumed: {2}'.format(self.of_chemical, self.produced, self.consumed)


def parse_reaction_line(reaction_line):
    produces_indicator = reaction_line.find('=>')
    input_chemicals = reaction_line[:produces_indicator].split(',')
    output_chemical = reaction_line[produces_indicator + 3:]
    return input_chemicals, output_chemical


def parse_chemical_details(chemical_details):
    quantity, name = chemical_details.strip().split(' ')
    return Chemical(name, quantity)


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
            produces = parse_chemical_details(output_chemical)
            reactions[produces.name] = Reaction(produces, parse_input_chemicals(input_chemicals))
    return reactions


def analyse_production_requirements(required, reactions, production_requirements):
    print('{0} required'.format(required))
    current_requirement = production_requirements.get(required.name)
    if not current_requirement:
        current_requirement = Production(required.name)
    current_requirement.consumed += required.quantity
    production_requirements[required.name] = current_requirement
    if required.name == 'ORE':
        return production_requirements
    if current_requirement.consumed <= current_requirement.produced:
        print('Requirement met! Now using {0} out of a total production of {1}'
              .format(current_requirement.consumed, current_requirement.produced))
    else:
        reaction = reactions.get(required.name)
        if not reaction:
            raise Exception('No reaction found to produce chemical {0}!'.format(required.name))
        production_multiplier = 0
        while current_requirement.consumed > current_requirement.produced:
            print('Requirements not met, increase production of {0} by {1}'
                  .format(required.name, reaction.produces.quantity))
            current_requirement.produced += reaction.produces.quantity
            production_multiplier += 1
        production_requirements[required.name] = current_requirement
        for multiplier in range(production_multiplier):
            for requires in reaction.requires:
                production_requirements = \
                    analyse_production_requirements(requires, reactions, production_requirements)
    return production_requirements


def load_and_analyse_reactions_for_step1(filename):
    reactions = load_reactions(filename)
    required = Chemical('FUEL', 1)
    production_required = analyse_production_requirements(required, reactions, {})
    print(production_required)
    return production_required.get('ORE').consumed


def test_ore_required(filename, expected_ore_required):
    ore_required = load_and_analyse_reactions_for_step1(filename)
    assert ore_required == expected_ore_required, \
        'Expected to need {0} ore but actually used {1}!'.format(expected_ore_required, ore_required)


def load_and_analyse_reactions_for_step2(filename, ore_used):
    reactions = load_reactions(filename)
    return 0


def test_ore_produces(filename, ore_used, expected_fuel_produced):
    fuel_produced = load_and_analyse_reactions_for_step2(filename, ore_used)
    assert fuel_produced == expected_fuel_produced, \
        'Expected to produce {0} fuel but actually produced {1}!'.format(expected_fuel_produced, fuel_produced)


def main():
    test_ore_required('test14a.txt', 31)
    test_ore_required('test14b.txt', 165)
    test_ore_required('test14c.txt', 13312)
    test_ore_required('test14d.txt', 180697)
    test_ore_required('test14e.txt', 2210736)
    ore_required = load_and_analyse_reactions_for_step1('input14.txt')
    print('Day 14, Step 1 - Ore required to produce 1 Fuel is {0}'.format(ore_required))

    test_ore_produces('test14c.txt', 13312, 82892753)
    test_ore_produces('test14d.txt', 180697, 5586022)
    test_ore_produces('test14e.txt', 2210736, 460664)
    fuel_produced = load_and_analyse_reactions_for_step2('input14.txt', 1000000000000)
    print('Day 14, Step 2 - Ore required to produce 1 Fuel is {0}'.format(fuel_produced))


if __name__ == '__main__':
    main()
