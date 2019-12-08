def split_layers(layer_data, layer_size):
    layers = []
    layer_start = 0
    while layer_start < len(layer_data):
        layer = layer_data[layer_start:layer_start + layer_size]
        layers.append(layer)
        layer_start += layer_size
    return layers


def load_layers(filename, width, height):
    with open(filename) as fh:
        layer_data = fh.readline().rstrip('\n')
    return split_layers(layer_data, width * height)


def find_layer_with_fewest(layers, digit):
    lowest_found = 99999999
    layer_found = None
    for layer in layers:
        digit_count = 0
        for c in layer:
            if c == digit:
                digit_count += 1
        if digit_count < lowest_found:
            lowest_found = digit_count
            layer_found = layer
    return layer_found


def count_digits(layer, digit):
    count = 0
    for c in layer:
        if c == digit:
            count += 1
    return count


def main():
    layers = load_layers('input8.txt', 25, 6)
    layer = find_layer_with_fewest(layers, '0')
    count1 = count_digits(layer, '1')
    count2 = count_digits(layer, '2')
    result = count1 * count2
    print('Step 1 result, {0} * {1} = {2}'.format(count1, count2, result))


if __name__ == '__main__':
    main()
