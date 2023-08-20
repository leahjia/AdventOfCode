from collections import deque

def read_file(file):
    layers, moves, process_layers = [], [], True
    with open(file) as file:
        for line in file.readlines():
            if line == "\n":
                process_layers = False
            else:
                (layers if process_layers else moves).append(line.strip("\n"))
    return layers, moves

def process_stacks():
    labels = layers.pop()
    index = {int(label): idx for idx, label in enumerate(labels) if label != " "}
    stacks = [deque() for _ in range(int(labels[len(labels) - 1]) + 1)]
    for stack in layers[::-1]:
        for label, idx in index.items():
            if idx < len(stack) and stack[idx] != " ":
                stacks[label].append(stack[idx])
    return stacks

def execute_moves(stacks, all_at_once):
    for move in moves:
        nums = [int(str) for str in move.split() if str.isdigit()]
        times, src, dest, temp_stacks = nums[0], nums[1], nums[2], []
        for _ in range(times):
            temp_stacks.append(stacks[src].pop()) if all_at_once else stacks[dest].append(stacks[src].pop())
        stacks[dest].extend(reversed(temp_stacks))  # part 2
    return stacks

def get_top_layers(stacks):
    return "".join(stack.pop() if stack else "" for stack in stacks)

layers, moves = read_file('../input/day5.txt')
list_stacks = process_stacks()
print("Part I: ", get_top_layers(execute_moves([deque(stack) for stack in list_stacks], False)))
print("Part II:", get_top_layers(execute_moves(list_stacks, True)))
