def read_file(file):
    layers = []
    moves = []
    process_layers = True

    with open(file) as file:
        for line in file.readlines():
            if line == "\n":
                process_layers = False
            elif process_layers:
                layers.append(line.strip("\n"))
            else:
                moves.append(line.strip("\n"))
    return layers, moves

def process_stacks(layers):
    labels = layers.pop()
    index = {}
    for i in range(len(labels)):
        if labels[i] != " ":
            index[int(labels[i])] = i

    num_stacks = int(labels[len(labels) - 1])
    list_stacks = [[] for _ in range(num_stacks + 1)]
    list_stacks_2 = [[] for _ in range(num_stacks + 1)]
    for stack in layers[::-1]:
        for label, idx in index.items():
            if idx < len(stack) and stack[idx] != " ":
                list_stacks[label].append(stack[idx])
                list_stacks_2[label].append(stack[idx])  # part 2
    return list_stacks, list_stacks_2

def execute_moves(moves, list_stacks, list_stacks_2):
    for move in moves:
        nums = [int(str) for str in move.split() if str.isdigit()]
        times = nums[0]
        src = nums[1]
        dest = nums[2]
        temp_stacks = []  # for part 2
        for _ in range(times):
            list_stacks[dest].append(list_stacks[src].pop())
            temp_stacks.append(list_stacks_2[src].pop())  # part 2
        list_stacks_2[dest].extend(reversed(temp_stacks))  # part 2

def get_top_layers(list_stacks, list_stacks_2):
    top_layer = ""
    top_layer_2 = ""
    for stack, stackII in zip(list_stacks, list_stacks_2):
        if len(stack):
            top_layer += stack.pop()
        if len(stackII):
            top_layer_2 += stackII.pop()
    return top_layer, top_layer_2

layers, moves = read_file('../input/day5.txt')
list_stacks, list_stacks_2 = process_stacks(layers)
execute_moves(moves, list_stacks, list_stacks_2)
top_layer, top_layer_2 = get_top_layers(list_stacks, list_stacks_2)

print("Part I: ", top_layer)
print("Part II:", top_layer_2)
