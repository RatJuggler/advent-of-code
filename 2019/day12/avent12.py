import re


class Moon:

    def __init__(self, x, y, z):
        self.x = int(x)
        self.y = int(y)
        self.z = int(z)

    @classmethod
    def from_string(cls, moon_coords):
        r = re.compile(r'-?\d+')
        coords = r.findall(moon_coords)
        return Moon(coords[0], coords[1], coords[2])

    def __repr__(self):
        return 'pos=<x={0}, y={1}, z={2}>'.format(self.x, self.y, self.z)


def load_moon_start_positions(filename):
    moons = []
    with open(filename) as fh:
        for moon_start in fh:
            moons.append(Moon.from_string(moon_start))
    return moons


def main():
    moons = load_moon_start_positions('test12a.txt')
    for moon in moons:
        print(moon)


if __name__ == '__main__':
    main()
