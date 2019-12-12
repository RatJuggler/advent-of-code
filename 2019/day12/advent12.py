import itertools
import re


class Moon:

    def __init__(self, x, y, z):
        self.x = int(x)
        self.y = int(y)
        self.z = int(z)
        self.vel_x = 0
        self.vel_y = 0
        self.vel_z = 0

    @classmethod
    def from_string(cls, moon_coords):
        r = re.compile(r'-?\d+')
        coords = r.findall(moon_coords)
        return Moon(coords[0], coords[1], coords[2])

    def apply_velocity(self):
        self.x += self.vel_x
        self.y += self.vel_y
        self.z += self.vel_z

    def total_energy(self):
        pot = abs(self.x) + abs(self.y) + abs(self.z)
        kin = abs(self.vel_x) + abs(self.vel_y) + abs(self.vel_z)
        return pot * kin

    def __repr__(self):
        return 'pos=<x={0}, y={1}, z={2}>, vel=<x={3}, y={4}, z={5}>'\
            .format(self.x, self.y, self.z, self.vel_x, self.vel_y, self.vel_z)


def load_moon_start_positions(filename):
    moons = []
    with open(filename) as fh:
        for moon_start in fh:
            moons.append(Moon.from_string(moon_start))
    return moons


def output_moon_state(moons, time_step):
    print('After {0} steps:'.format(time_step))
    for moon in moons:
        print(moon)


def gravity(moon1, moon2):
    if moon1.x > moon2.x:
        moon1.vel_x -= 1
        moon2.vel_x += 1
    elif moon2.x > moon1.x:
        moon2.vel_x -= 1
        moon1.vel_x += 1
    if moon1.y > moon2.y:
        moon1.vel_y -= 1
        moon2.vel_y += 1
    elif moon2.y > moon1.y:
        moon2.vel_y -= 1
        moon1.vel_y += 1
    if moon1.z > moon2.z:
        moon1.vel_z -= 1
        moon2.vel_z += 1
    elif moon2.z > moon1.z:
        moon2.vel_z -= 1
        moon1.vel_z += 1


def apply_gravity(moons):
    for combination in itertools.combinations(moons, 2):
        gravity(combination[0], combination[1])


def apply_velocity(moons):
    for moon in moons:
        moon.apply_velocity()


def simulate_motion(moons, time_steps):
    output_moon_state(moons, 0)
    for time_step in range(time_steps):
        apply_gravity(moons)
        apply_velocity(moons)
        output_moon_state(moons, time_step + 1)


def calculate_total_energy(moons):
    total_energy = 0
    for moon in moons:
        total_energy += moon.total_energy()
    return total_energy


def run_simulation(filename, time_steps):
    moons = load_moon_start_positions(filename)
    simulate_motion(moons, time_steps)
    return calculate_total_energy(moons)


def test_total_energy(filename, time_steps, expected_energy):
    total_energy = run_simulation(filename, time_steps)
    assert total_energy == expected_energy, 'Expect total energy of {0} but got {1}!'\
        .format(expected_energy, total_energy)


def get_moons_state(moons):
    state = ''
    for moon in moons:
        state += moon.__repr__()
    return state


def simulate_motion_until_repeat(moons):
    time_steps = 0
    previous_states = []
    moons_state = get_moons_state(moons)
    while moons_state not in previous_states:
        previous_states.append(moons_state)
        apply_gravity(moons)
        apply_velocity(moons)
        moons_state = get_moons_state(moons)
        time_steps += 1
    return time_steps


def test_previous_state(filename, expected_steps):
    moons = load_moon_start_positions(filename)
    time_steps = simulate_motion_until_repeat(moons)
    assert time_steps == expected_steps, 'Expect {0} time steps but got {1}!'.format(expected_steps, time_steps)


def main():
    test_total_energy('test12a.txt', 10, 179)
    test_total_energy('test12b.txt', 100, 1940)

    test_previous_state('test12a.txt', 2772)

    step1_total_energy = run_simulation('input12.txt', 1000)
    print('Day 12 Step 1, total energy = {0}'.format(step1_total_energy))


if __name__ == '__main__':
    main()
