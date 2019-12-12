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


def apply_gravity():
    pass


def apply_velocity(moons):
    for moon in moons:
        moon.apply_velocity()


def simulate_motion(moons, time_steps):
    for time_step in range(time_steps):
        apply_gravity()
        apply_velocity(moons)
        output_moon_state(moons, time_step)
    return moons


def calculate_total_energy(moons):
    total_energy = 0
    for moon in moons:
        total_energy += moon.total_energy()
    return total_energy


def test(filename, time_steps, expected_energy):
    moons = load_moon_start_positions(filename)
    simulate_motion(moons, time_steps)
    total_energy = calculate_total_energy(moons)
    assert total_energy == expected_energy, 'Expect total energy {0} but got {1}!'.format(expected_energy, total_energy)


def main():
    test('test12a.txt', 10, 179)
    test('test12b.txt', 100, 1940)


if __name__ == '__main__':
    main()
