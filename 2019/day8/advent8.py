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


def merge_layer(image, layer):
    new_image = ''
    for j in range(len(image)):
        pixel = image[j]
        if image[j] == '2':
            pixel = layer[j]
        new_image += pixel
    return new_image


def decode_image(layers):
    final_image = layers[0]
    for i in range(1, len(layers)):
        final_image = merge_layer(final_image, layers[i])
    return final_image


def display_image(image, width, height):
    display = ''
    for y in range(height):
        for x in range(width):
            pixel = image[y * width + x]
            if pixel == '2':
                raise Exception('Unexpected transparent pixel!')
            display += 'X' if pixel == '1' else ' '
        display += '\n'
    return display


def main():
    layers = load_layers('input8.txt', 25, 6)
    layer = find_layer_with_fewest(layers, '0')
    count1 = count_digits(layer, '1')
    count2 = count_digits(layer, '2')
    result = count1 * count2
    print('Step 1 result, {0} * {1} = {2}'.format(count1, count2, result))
    image = decode_image(layers)
    display = display_image(image, 25, 6)
    print('Step 2 image = \n{0}'.format(display))


if __name__ == '__main__':
    main()
