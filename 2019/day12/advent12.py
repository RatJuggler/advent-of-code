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

    def not_stationary(self):
        return self.vel_x or self.vel_y or self.vel_z

    def __repr__(self):
        return 'pos=<x={0}, y={1}, z={2}>, vel=<x={3}, y={4}, z={5}>'\
            .format(self.x, self.y, self.z, self.vel_x, self.vel_y, self.vel_z)


def load_moon_start_positions(filename):
    with open(filename) as fh:
        return [Moon.from_string(moon_start) for moon_start in fh]


def output_moon_state(moons, time_step):
    print('After {0} steps:'.format(time_step))
    for moon in moons:
        print(moon)


def axis_pull(moon1_axis, moon2_axis):
    return 1 if moon2_axis > moon1_axis else -1 if moon2_axis < moon1_axis else 0


def gravity(moon1, moon2):
    x_axis_pull = axis_pull(moon1.x, moon2.x)
    moon1.vel_x += x_axis_pull
    moon2.vel_x += -x_axis_pull

    y_axis_pull = axis_pull(moon1.y, moon2.y)
    moon1.vel_y += y_axis_pull
    moon2.vel_y += -y_axis_pull

    z_axis_pull = axis_pull(moon1.z, moon2.z)
    moon1.vel_z += z_axis_pull
    moon2.vel_z += -z_axis_pull


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


def stationary_moons(moons):
    for moon in moons:
        if moon.not_stationary() > 0:
            return False
    return True


def simulate_motion_until_repeat(moons):
    time_steps = 0
    # Must cycle to (0,0,0) velocity then back to initial state.
    while True:
        apply_gravity(moons)
        apply_velocity(moons)
        time_steps += 1
        if time_steps % 100000 == 0:
            print(time_steps)
        if stationary_moons(moons):
            break
    return time_steps * 2


def run_simulation_until_repeat(filename):
    moons = load_moon_start_positions(filename)
    return simulate_motion_until_repeat(moons)


def test_previous_state(filename, expected_steps):
    time_steps = run_simulation_until_repeat(filename)
    assert time_steps == expected_steps, 'Expect {0} time steps but got {1}!'.format(expected_steps, time_steps)


def main():
    test_total_energy('test12a.txt', 10, 179)
    test_total_energy('test12b.txt', 100, 1940)

    test_previous_state('test12a.txt', 2772)
    test_previous_state('test12b.txt', 4686774924)

    step1_total_energy = run_simulation('input12.txt', 1000)
    print('Day 12 Step 1, total energy = {0}'.format(step1_total_energy))
    step2_time_steps = run_simulation_until_repeat('input12.txt')
    print('Day 12 Step 2, time steps until repeat = {0}'.format(step2_time_steps))


if __name__ == '__main__':
    main()
