with open('../input/day2.txt') as file:
    lines = [line.strip() for line in file.readlines()]

map = {'X': 'A', 'Y': 'B', 'Z': 'C'}
res = 0
for line in lines:
    [op, me] = line.split(" ")
    res += ord(me) - ord('X') + 1
    if op == map[me]:
        res += 3
    elif op == 'A' and map[me] == 'B' or op == 'B' and map[me] == 'C' or op == 'C' and map[me] == 'A':
        res += 6

print("part 1:", res)

res = 0
for line in lines:
    [op, outcome] = line.split(" ")
    if outcome == 'X':
        res += 3 if op == 'A' else 0
        res += 1 if op == 'B' else 0
        res += 2 if op == 'C' else 0
    elif outcome == 'Y':
        res += 3 + ord(op) - ord('A') + 1
    else:
        res += 6
        res += 2 if op == 'A' else 0
        res += 3 if op == 'B' else 0
        res += 1 if op == 'C' else 0

print("part 2:", res)
