map = []
with open('../input/day3.txt') as file:
    map = [line.strip() for line in file.readlines()]

def get_digit(ch):
    return ord(ch) - ord('a') + 1 if ord(ch) >= ord('a') else ord(ch) - ord('A') + 27

def part1():
    ct = 0
    for line in map:
        mid = len(line) // 2
        for ch in line[:mid]:
            if ch in line[mid:]:
                ct += get_digit(ch)
                break
    return ct

print("part I:", part1())

def part2():
    ct = 0
    for line1, line2, line3 in zip(map[::3], map[1::3], map[2::3]):
        for ch in line1:
            if ch in line2 and ch in line3:
                ct += get_digit(ch)
                break
    return ct

print("part II:", part2())
