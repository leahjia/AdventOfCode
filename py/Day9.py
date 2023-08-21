directions = {'R': [1, 0], 'U': [0, 1], 'L': [-1, 0], 'D': [0, -1]}
visit = {}

def move(steps):
    T, H = [[0, 0] for _ in range(2)]
    new_set = {T[1]}
    visit[T[0]] = new_set
    res = []
    for step in steps:
        dx, dy, x_diff, y_diff = step[0], step[1], H[0] - T[0], H[1] - T[1]
        if abs(x_diff + dx) == 2 or abs(y_diff + dy) == 2:
            if x_diff == 0 or y_diff == 0:
                res.append([dx, dy])
                T[0] += dx
                T[1] += dy
            elif x_diff + dx == 0:
                res.append([0, y_diff])
                T[1] = H[1]
            elif y_diff + dy == 0:
                res.append([x_diff, 0])
                T[0] = H[0]
            else:
                res.append([x_diff, y_diff])
                T = H.copy()
            new_set = visit.get(T[0], set())
            new_set.add(T[1])
            visit[T[0]] = new_set
        H[0] += dx
        H[1] += dy
    return res

def n_tails(steps, tail):
    for _ in range(tail):
        steps = move(steps)
    count = 0
    for set in visit.values():
        count += len(set)
    return count

def main():
    steps = []
    with open('../input/day9.txt') as file:
        for line in file.readlines():
            direct, times = line.split(" ")
            for _ in range(int(times)):
                steps.append(directions[direct])
    print(n_tails(steps, 1))
    print(n_tails(steps, 9))

if __name__ == "__main__":
    main()
