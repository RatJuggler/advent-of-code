def read_module_list(filename):
    modules = []
    with open('input1.txt') as fh:
        while True:
            module_mass = fh.readline()
            if not module_mass:
                break
            module_mass = module_mass.rstrip('\n')
            if module_mass.isdigit():
                modules.append(int(module_mass))
            else:
                raise Exception('Expected module mass to be an unsigned integer, found "{0}".'.format(module_mass))
    return modules


def calculate_fuel(modules):
    total_fuel = 0
    for module_mass in modules:
        total_fuel += (module_mass // 3) - 2
    return total_fuel


def calculate_fuel_plus_fuel(modules):
    total_fuel = 0
    for module_mass in modules:
        module_fuel = (module_mass // 3) - 2
        while module_fuel > 0:
            total_fuel += module_fuel
            module_fuel = (module_fuel // 3) - 2
    return total_fuel


def main(filename):
    modules = read_module_list(filename)
    total_fuel = calculate_fuel(modules)
    print("Fuel requirement for supplied modules = {0}".format(total_fuel))
    total_fuel_plus_fuel = calculate_fuel_plus_fuel(modules)
    print("Fuel requirement for supplied modules (incl fuel for fuel) = {0}".format(total_fuel_plus_fuel))


if __name__ == '__main__':
    main('input1.txt')
