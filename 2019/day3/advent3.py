def read_wires_file(filename):
    wires_found = []
    with open(filename) as csv_file:
        for line in csv_file:
            wires_found.append([_section for _section in line.rstrip('\n').split(',')])
    if len(wires_found) > 2:
        raise Exception('Expect 2 wires but found {0}!'.format(len(wires_found)))
    return wires_found


def follow_wire(section, x, y):
    direction = section[:1]
    distance = int(section[1:])
    if direction == 'L':
        x -= distance
    elif direction == 'R':
        x += distance
    elif direction == 'U':
        y += distance
    elif direction == 'D':
        y -= distance
    else:
        raise Exception('Unknown direction {0}!'.format(direction))
    return x, y


def convert_to_coords(wire):
    # All wires start from 0,0
    x = 0
    y = 0
    coords = [[x, y]]
    for section in wire:
        x, y = follow_wire(section, x, y)
        coords.append([x, y])
    return coords


def extrapolate_wire_paths(wires):
    paths = []
    for select_wire in range(len(wires)):
        wire = wires[select_wire]
        print("Wire: {0} -> {1} Sections {2}".format(select_wire, len(wire), wire))
        coords = convert_to_coords(wire)
        print(coords)
        paths.append(coords)
    return paths


def find_intersection(p0, p1, p2, p3):
    # This algorithm not the best!
    if p0 == p2 or p0 == p3:
        return p0
    if p1 == p2 or p1 == p3:
        return p1
    s10_x = p1[0] - p0[0]
    s10_y = p1[1] - p0[1]
    s32_x = p3[0] - p2[0]
    s32_y = p3[1] - p2[1]
    denom = s10_x * s32_y - s32_x * s10_y
    if denom == 0:  # collinear
        return None, None
    denom_is_positive = denom > 0
    s02_x = p0[0] - p2[0]
    s02_y = p0[1] - p2[1]
    s_numer = s10_x * s02_y - s10_y * s02_x
    if (s_numer < 0) == denom_is_positive:  # no collision
        return None, None
    t_numer = s32_x * s02_y - s32_y * s02_x
    if (t_numer < 0) == denom_is_positive:  # no collision
        return None, None
    if (s_numer > denom) == denom_is_positive or (t_numer > denom) == denom_is_positive:  # no collision
        return None, None
    # Collision detected!
    t = t_numer / denom
    intersection_point = [p0[0] + (t * s10_x), p0[1] + (t * s10_y)]
    return intersection_point


def find_intersections(wire1, wire2):
    intersections_found = []
    for wire1_position in range(len(wire1) - 1):
        for wire2_position in range(len(wire2) - 1):
            [intersect_x, intersect_y] = find_intersection(
                wire1[wire1_position], wire1[wire1_position + 1],
                wire2[wire2_position], wire2[wire2_position + 1])
            if not (intersect_x is None or intersect_y is None):
                intersection = [[wire1_position, wire2_position], [intersect_x, intersect_y]]
                intersections_found.append(intersection)
                print("({0} -> {1}) intersects ({2} -> {3}) at ({4}, {5})".format(
                    wire1[wire1_position], wire1[wire1_position + 1],
                    wire2[wire2_position], wire2[wire2_position + 1],
                    intersect_x, intersect_y))
    return intersections_found


def find_closest_distance(intersections):
    closest_found = 99999999
    # Ignore intersections at 0,0
    for intersection in intersections[1:]:
        distance = abs(intersection[1][0]) + abs(intersection[1][1])
        print("Intersection ({0}, {1}) distance = {2}".format(intersection[0], intersection[1], distance))
        if distance < closest_found:
            closest_found = distance
    return closest_found


def steps_between(position1, position2):
    return abs(position2[0] - position1[0]) + abs(position2[1] - position1[1])


def find_steps_to_intersection(paths, intersection, wire):
    path = paths[wire]
    to_position = intersection[0][wire]
    steps = 0
    for wire_position in range(to_position):
        position1 = path[wire_position]
        position2 = path[wire_position + 1]
        steps += steps_between(position1, position2)
    steps += steps_between(path[to_position], intersection[1])
    return steps


def find_shortest_steps(paths, intersections):
    shortest_found = 99999999
    # Ignore intersections at 0,0
    for intersection in intersections[1:]:
        wire1_steps = find_steps_to_intersection(paths, intersection, 0)
        wire2_steps = find_steps_to_intersection(paths, intersection, 1)
        total_steps = wire1_steps + wire2_steps
        print("Intersection {0} steps = {1}".format(intersection, total_steps))
        if total_steps < shortest_found:
            shortest_found = total_steps
    return shortest_found


def run(filename):
    details = read_wires_file(filename)
    paths = extrapolate_wire_paths(details)
    intersections = find_intersections(paths[0], paths[1])
    closest = find_closest_distance(intersections)
    print("Closest intersection distance = {0}".format(closest))
    shortest = find_shortest_steps(paths, intersections)
    print("Shortest steps to an intersection = {0}".format(shortest))
    return closest, shortest


def main():
    closest, shortest = run('test3a.txt')
    assert(closest == 159)
    assert(shortest == 610)
    closest, shortest = run('test3b.txt')
    assert(closest == 135)
    assert(shortest == 410)
    run('input3.txt')


if __name__ == '__main__':
    main()
