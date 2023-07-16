import heapq

with open('../input/day1.txt') as file:
    lines = [line.strip() for line in file.readlines()]

# part 1 - find single max
MAX = 0
list = []
sum = 0
for item in lines:
    if item == "":
        MAX = max(MAX, sum)
        list.append(-sum)
        sum = 0
    else:
        sum += int(item)

# part 2 - top three
heapq.heapify(list)
res = 0
for i in range(3):
    res += heapq.heappop(list)

print("part 1 max:", MAX)
print("part 2 maxes:", -res)
