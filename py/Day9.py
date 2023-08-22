direct = {'R': [1, 0], 'L': [-1, 0], 'U': [0, 1], 'D': [0, -1]}
visit = {}

def move(steps):
    head, tail = [0, 0], [0, 0]
    visit[tail[0]] = {tail[1]}
    res = []
    for dx, dy in steps:
        x_diff, y_diff = head[0] - tail[0], head[1] - tail[1]
        if abs(x_diff + dx) == 2 or abs(y_diff + dy) == 2:
            if x_diff == 0 or y_diff == 0:
                res.append([dx, dy])
                tail[0] += dx
                tail[1] += dy
            elif x_diff + dx == 0:
                res.append([0, y_diff])
                tail[1] = head[1]
            elif y_diff + dy == 0:
                res.append([x_diff, 0])
                tail[0] = head[0]
            else:
                res.append([x_diff, y_diff])
                tail = head.copy()
            visit.setdefault(tail[0], set()).add(tail[1])
        head[0] += dx
        head[1] += dy
    return res

def n_tails(steps, tail):
    for _ in range(tail):
        steps = move(steps)
    ct = 0
    for set in visit.values():
        ct += len(set)
    return ct

def main():
    steps = []
    with open('../input/day9.txt') as file:
        for line in file.readlines():
            dir, time = line.strip().split(" ")
            for _ in range(int(time)):
                steps.append(direct[dir])
    print(n_tails(steps, 1))
    print(n_tails(steps, 9))

if __name__ == "__main__":
    main()
