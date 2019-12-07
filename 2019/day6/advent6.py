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
        while True:
            orbiting_object = fh.readline()
            if not orbiting_object:
                break
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


def locate_orbitee(orbits, orbiter):
    for orbitee in orbits:
        orbiters = orbits.get(orbitee)
        # print(orbitee, orbiters)
        if orbiters is not None and orbiter in orbiters:
            return orbitee


def make_next_transfer(orbits, visited, transfer_count, to_orbit, find_to):
    orbiters = orbits.get(to_orbit)
    if orbiters is not None:
        if find_to in orbiters:
            return transfer_count
        else:
            transfer_count += 1
            for orbiter in orbiters:
                if orbiter not in visited:
                    print('Moving from {0} to {1}'.format(to_orbit, orbiter))
                    transfer_count = make_next_transfer(orbits, visited, transfer_count, orbiter, find_to)
            visited.append(to_orbit)
            back_orbitee = locate_orbitee(orbits, to_orbit)
            print('Moving back to {0}, having visited {1}'.format(back_orbitee, visited))
            return make_next_transfer(orbits, visited, transfer_count, back_orbitee, find_to)
    visited.append(to_orbit)
    return transfer_count


def count_transfers(orbits, from_orbit, to_orbit):
    find_from = locate_orbitee(orbits, from_orbit)
    find_to = locate_orbitee(orbits, to_orbit)
    return make_next_transfer(orbits, ['YOU'], 1, find_from, find_to)


def load_orbits_and_count_transfers(filename, from_orbit, to_orbit):
    orbits = load_orbiting_objects(filename)
    return count_transfers(orbits, from_orbit, to_orbit)


def main():
    checksum = load_orbits_and_checksum('test6a.txt')
    assert(checksum == 42)
    checksum = load_orbits_and_checksum('input6.txt')
    print("Step 1 Orbit Checksum is {0}".format(checksum))
    transfers = load_orbits_and_count_transfers('test6b.txt', 'YOU', 'SAN')
    print(transfers)
    assert(transfers == 4)
    transfers = load_orbits_and_count_transfers('test6c.txt', 'YOU', 'SAN')
    print(transfers)
    assert (transfers == 4)
#    transfers = load_orbits_and_count_transfers('input6.txt', 'YOU', 'SAN')
    print("Step 2 Orbit Transfers required is {0}".format(transfers))


if __name__ == '__main__':
    main()
