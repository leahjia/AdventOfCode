layers = []
moves = []
processLayers = True

with open('../input/day5.txt') as file:
    for line in file.readlines():
        if line == "\n":
            processLayers = False
        elif processLayers:
            layers.append(line.strip("\n"))
        else:
            moves.append(line.strip("\n"))

labels = layers.pop()
index = {}
for i in range(len(labels)):
    if labels[i] != " ":
        index[int(labels[i])] = i

numOfStacks = int(labels[len(labels) - 1])
listOfStacks = [[] for _ in range(numOfStacks + 1)]
copyListStacks = [[] for _ in range(numOfStacks + 1)]
for stack in layers[::-1]:
    for label, idx in index.items():
        if idx < len(stack) and stack[idx] != " ":
            listOfStacks[label].append(stack[idx])
            copyListStacks[label].append(stack[idx])  # part 2

for move in moves:
    nums = [int(str) for str in move.split() if str.isdigit()]
    times = nums[0]
    src = nums[1]
    dest = nums[2]
    tempStacks = []  # for part 2
    for time in range(times):
        listOfStacks[dest].append(listOfStacks[src].pop())
        tempStacks.append(copyListStacks[src].pop())  # part 2
    copyListStacks[dest].extend(reversed(tempStacks))  # part 2

topLayer = ""
topLayerII = ""
for stack, stackII in zip(listOfStacks, copyListStacks):
    if len(stack):
        topLayer += stack.pop()
    if len(stackII):
        topLayerII += stackII.pop()

print("Part I: ", topLayer)
print("Part II:", topLayerII)
