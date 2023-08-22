# part 1
def cpu(lines):
    cycle, x, sum = 1, 1, 0
    for s in lines:
        curr = s.split(" ")
        cycle += 1
        sum += (cycle % 40 == 20) * cycle * x
        if curr[0] == "addx":
            x += int(curr[1])
            cycle += 1
            sum += (cycle % 40 == 20) * cycle * x
    return sum

# part 2
def crt(lines):
    x, drawing, line = 1, 0, "#"
    for s in lines:
        curr = s.split(" ")
        x, drawing, line = check_position(drawing, line, x)
        if curr[0] == "addx":
            x += int(curr[1])
            x, drawing, line = check_position(drawing, line, x)

def check_position(drawing, line, x):
    drawing += 1
    if drawing == 40:
        print(line)
        line, drawing = "", 0
    line += "#" if x - 1 <= drawing <= x + 1 else "."
    return [x, drawing, line]

def main():
    with open('../input/day10.txt') as file:
        lines = [line.strip() for line in file.readlines()]
        print(cpu(lines))
        crt(lines)

if __name__ == "__main__":
    main()
