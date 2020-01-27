def animate_lightgrid(filename, steps):
    return 0


def test_animate_lightgrid(filename, steps, expected_lights_on):
    lights_on = animate_lightgrid(filename, steps)
    assert lights_on == expected_lights_on, \
        'Expected to find {0} lights on after {1} steps but was {2}!'.format(expected_lights_on, steps, lights_on)


def main() -> None:
    test_animate_lightgrid('test18a.txt', 4, 4)


if __name__ == '__main__':
    main()
