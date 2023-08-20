with open('../input/day6.txt') as file:
    for line in file.readlines():
        l, r = 0, 14
        chars = {}
        for i in range(r):
            chars[line[i]] = chars.get(line[i], 0) + 1

        while len(chars) < 14 and r < len(line):
            chars[line[l]] -= 1
            if chars[line[l]] == 0:
                chars.pop(line[l])
            chars[line[r]] = chars.get(line[r], 0) + 1
            l += 1
            r += 1
