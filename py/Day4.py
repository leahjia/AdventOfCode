with open('../input/day4.txt') as file:
    lines = [[int(a), int(b)] for line in file.readlines()
             for pair in line.strip().split(',') for a, b in [pair.split('-')]]

part1, part2 = 0, 0
for i in range(0, len(lines), 2):
    pair = [lines[i], lines[i + 1]]
    pair.sort(key=lambda a: (a[0], -a[1]))
    if pair[0][1] >= pair[1][1]:
        part1 = part1 + 1
    if pair[0][1] >= pair[1][0]:
        part2 = part2 + 1

print("Part I:", part1, "| Part II:", part2)
