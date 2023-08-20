import sys

class TreeNode:
    def __init__(self):
        self.val = 0
        self.branch = {}

class Tree:
    def __init__(self):
        self.root = TreeNode()

directories = []

def traverse(root):
    while file:
        line = file.readline().strip("\n")
        if not line:
            break
        if line.startswith("$ ls"):
            continue
        parts = line.split(" ")
        if line.startswith("$ cd"):
            dir = parts[2]
            if dir == "..":
                return root.val
            root.branch[dir] = TreeNode()
            new_val = traverse(root.branch[dir])
            root.val += new_val
            if new_val <= 100000:
                directories.append(root.branch[dir])
        elif not line.startswith("dir"):
            file_name = parts[1]
            file_size = int(parts[0])
            new_node = TreeNode()
            new_node.val = file_size
            root.branch[file_name] = new_node
            root.val += file_size
    return root.val

def dfs(root, curr):
    if len(root.branch) == 0 or root.val < target:
        return sys.maxsize
    curr = min(curr, root.val)
    curr = min(min(curr, dfs(node, curr)) for node in root.branch.values())
    return curr

with open('../input/day7.txt') as file:
    tree = Tree()
    file.readline()
    tree.root.val = traverse(tree.root)

    count = sum(dir.val for dir in directories)
    target = 30000000 - (70000000 - tree.root.val)
    target_needed = dfs(tree.root, tree.root.val)
