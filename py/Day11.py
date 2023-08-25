import heapq

class Monkey:
    def __init__(self, number, items, operation, multiplier, test, next, inspected):
        self.number = number
        self.items = items
        self.operation = operation
        self.multiplier = multiplier
        self.test = test
        self.next = next
        self.inspected = inspected

map = {}
mod = 1

def main():
    global mod
    num = 0
    with open('../input/day11.txt') as file:
        while file.readline():
            items = [int(n) for n in file.readline().strip().split(": ")[1].split(", ")]
            heapq.heapify(items)
            operation, multiplier = file.readline().strip().split("= old ")[1].split(" ")
            test = int(file.readline().strip().split(" ")[-1])
            mod *= test
            next = [int(file.readline().strip().split(" ")[-1]) for _ in range(2)]
            map[num], num = Monkey(num, items, operation, multiplier, test, next, 0), num + 1
            file.readline()

    for _ in range(10000):
        inspect()
    print("25935263541 =", two_most_active())

def two_most_active():
    pq = []
    heapq.heapify(pq)
    [heapq.heappop(pq) if len(pq) > 2 else heapq.heappush(pq, monkey.inspected) for monkey in map.values()]
    return heapq.heappop(pq) * heapq.heappop(pq)

def inspect():
    for monkey in map.values():
        while monkey.items:
            item = operate(heapq.heappop(monkey.items), monkey.operation, monkey.multiplier) % mod
            heapq.heappush(map[monkey.next[item % monkey.test != 0]].items, item)
            monkey.inspected += 1

def operate(item, operation, multiplier):
    var = item if multiplier == "old" else int(multiplier)
    return item * var if operation == "*" else item + var

if __name__ == "__main__":
    main()
