def visible():
    top, bottom, left, right = [[[0] * col for _ in range(row)] for _ in range(4)]
    top[0] = bottom[0] = left[0] = right[0] = matrix[0]
    top[-1] = bottom[-1] = left[-1] = right[-1] = matrix[-1]

    for r in range(row):
        top[r][0] = bottom[r][0] = left[r][0] = right[r][0] = matrix[r][0]
        top[r][-1] = bottom[r][-1] = left[r][-1] = right[r][-1] = matrix[r][-1]

    for r in range(1, row - 1):
        for c in range(1, col - 1):
            top[r][c] = max(matrix[r][c], top[r - 1][c])
            left[r][c] = max(matrix[r][c], left[r][c - 1])
            lr, lc = row - r - 1, col - c - 1
            bottom[lr][c] = max(matrix[lr][c], bottom[lr + 1][c])
            right[r][lc] = max(matrix[r][lc], right[r][lc + 1])

    res = (row + col) * 2 - 4
    for r in range(1, row - 1):
        for c in range(1, col - 1):
            if matrix[r][c] > min(top[r - 1][c], left[r][c - 1], bottom[r + 1][c], right[r][c + 1]):
                res += 1

    return res

def score_in_direction(r, c, dr, dc):
    og = matrix[r][c]
    score = 0
    while 0 <= (r := r + dr) < row and 0 <= (c := c + dc) < col:
        score += 1
        if matrix[r][c] >= og:
            break
    return score

def scenic_score():
    directions = [[1, 0], [0, 1], [-1, 0], [0, -1]]
    res = 0
    for r in range(1, row - 1):
        for c in range(1, col - 1):
            score = 1
            for d in directions:
                score *= score_in_direction(r, c, d[0], d[1])
            res = max(res, score)
    return res

with open('../input/day8.txt') as file:
    matrix = [[int(ch) for ch in line.strip()] for line in file.readlines()]
    row, col = len(matrix), len(matrix[0])
