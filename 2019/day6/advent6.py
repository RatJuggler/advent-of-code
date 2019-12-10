def decode_orbit_definition(orbit_definition):
    orbit_separator = orbit_definition.find(')')
    if orbit_separator == -1:
        raise Exception("Orbit separation character not found in {0}!".format(orbit_definition))
    orbitee = orbit_definition[:orbit_separator]
    orbiter = orbit_definition[orbit_separator+1:]
    return orbitee, orbiter


def add_orbit(orbiting_objects, orbitee, orbiter):
    existing_orbiters = orbiting_objects.get(orbitee)
    if existing_orbiters is None:
        existing_orbiters = [orbiter]
    else:
        existing_orbiters.append(orbiter)
    orbiting_objects[orbitee] = existing_orbiters


def load_orbiting_objects(filename):
    orbiting_objects = {}
    with open(filename) as fh:
        for orbiting_object in fh:
            orbitee, orbiter = decode_orbit_definition(orbiting_object.rstrip('\n'))
            add_orbit(orbiting_objects, orbitee, orbiter)
    return orbiting_objects


def count_orbiters(orbiting_count, orbiting_objects, orbiters):
    checksum = 0
    for orbiter in orbiters:
        # print('Orbit count for {0} = {1}'.format(orbiter, orbiting_count))
        checksum += orbiting_count
        next_orbiters = orbiting_objects.get(orbiter)
        if next_orbiters is not None:
            checksum += count_orbiters(orbiting_count + 1, orbiting_objects, next_orbiters)
    # print('Subtotal at {0} = {1}'.format(orbiters, checksum))
    return checksum


def load_orbits_and_checksum(filename):
    orbiting_objects = load_orbiting_objects(filename)
    return count_orbiters(1, orbiting_objects, orbiting_objects['COM'])


def find_orbiter(orbits, orbiter):
    for orbitee in orbits:
        orbiters = orbits.get(orbitee)
        if orbiter in orbiters:
            return orbitee
    return None


def move_orbit(orbits, transfers, visited, from_orbit, to_orbitee):
    print('At {0} in {1} transfers having visited {2}'.format(from_orbit, transfers, visited))
    if from_orbit not in visited:
        visited.append(from_orbit)
        orbiters = orbits.get(from_orbit)
        if orbiters is not None:
            for orbiter in orbiters:
                if orbiter == to_orbitee:
                    print("Found {0} in {1} transfers!".format(to_orbitee, transfers))
                    exit(0)
                if orbiter not in visited:
                    move_orbit(orbits, transfers + 1, visited, orbiter, to_orbitee)
    return visited


def count_transfers(orbits, from_orbit, to_orbit):
    transfers = 1
    from_orbitee = find_orbiter(orbits, from_orbit)
    to_orbitee = find_orbiter(orbits, to_orbit)
    visited = [from_orbit]
    while from_orbitee != to_orbitee:
        visited = move_orbit(orbits, transfers, visited, from_orbitee, to_orbitee)
        from_orbitee = find_orbiter(orbits, from_orbitee)
        transfers += 1
    return transfers


def load_orbits_and_count_transfers(filename, from_orbit, to_orbit):
    orbits = load_orbiting_objects(filename)
    return count_transfers(orbits, from_orbit, to_orbit)


def main():
    checksum = load_orbits_and_checksum('test6a.txt')
    assert(checksum == 42)
#    transfers = load_orbits_and_count_transfers('test6b.txt', 'YOU', 'SAN')
#    assert(transfers == 4)
#    transfers = load_orbits_and_count_transfers('test6c.txt', 'YOU', 'SAN')
#    assert(transfers == 7)

    checksum = load_orbits_and_checksum('input6.txt')
    print("Step 1 Orbit Checksum is {0}".format(checksum))
    transfers = load_orbits_and_count_transfers('input6.txt', 'YOU', 'SAN')
    print("Step 2 Orbit Transfers required is {0}".format(transfers))


if __name__ == '__main__':
    main()
