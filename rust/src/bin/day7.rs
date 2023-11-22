use crate::tree_module::TreeNode;
use std::{cmp, fs, sync::Mutex, u32::MAX};

mod tree_module {
    use std::collections::HashMap;

    #[derive(Debug)]
    pub struct TreeNode {
        pub val: u32,
        pub branch: HashMap<String, TreeNode>,
    }

    impl TreeNode {
        pub fn new() -> TreeNode {
            TreeNode { val: 0, branch: HashMap::new() }
        }
    }

    #[derive(Debug)]
    pub struct Tree {
        pub root: TreeNode,
    }

    impl Tree {
        pub fn new() -> Tree {
            Tree { root: TreeNode::new() }
        }
    }
}

lazy_static::lazy_static! {
    static ref FILE: String = fs::read_to_string("../input/day7.txt").unwrap();
    static ref SUM: Mutex<u32> = Mutex::new(0);
}

fn main() {
    let mut lines = FILE.lines();
    let mut tree = tree_module::Tree::new();
    tree.root.val = traverse(&mut tree.root, &mut lines);
    println!("{}{}", "day 7 part I:  1297683 - ", SUM.lock().unwrap());

    let target = 30000000 - (70000000 - tree.root.val);
    let mut curr = tree.root.val;
    println!("{}{}", "day 7 part II: 5756764 - ", dfs(&tree.root, &mut curr, target));
}

fn traverse(root: &mut TreeNode, lines: &mut std::str::Lines) -> u32 {
    while let Some(line) = lines.next() {
        if line.starts_with("$ ls") {
            continue;
        }
        let parts: Vec<&str> = line.split_whitespace().collect();
        if line.starts_with("$ cd") {
            if line == "$ cd /" {
                continue;
            }
            let dir = parts[2];
            if dir == ".." {
                return root.val;
            }
            let branch = root.branch.entry(dir.to_string()).or_insert(TreeNode::new());
            let new_val = traverse(branch, lines); // this was causing issue
            root.val += new_val;
            if new_val <= 100000 {
                *SUM.lock().unwrap() += new_val;
            }
        } else if !line.starts_with("dir") {
            let file_name = parts[1].to_string();
            let file_size: u32 = parts[0].parse().unwrap();
            let mut new_node = TreeNode::new();
            new_node.val = file_size;
            root.branch.insert(file_name, new_node);
            root.val += file_size;
        }
    }
    root.val
}

fn dfs(root: &TreeNode, curr: &mut u32, target: u32) -> u32 {
    if root.branch.is_empty() || root.val < target {
        return MAX;
    }
    *curr = cmp::min(*curr, root.val);
    for node in root.branch.values() {
        *curr = cmp::min(*curr, dfs(node, curr, target));
    }
    *curr
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::tree_module::Tree;
    use lazy_static::lazy_static;
    use std::sync::Mutex;

    lazy_static! {
        static ref TREE: Mutex<Tree> = Mutex::new(Tree::new());
    }

    #[test]
    fn day7_test_part1() {
        let mut tree = TREE.lock().unwrap();
        tree.root.val = traverse(&mut tree.root, &mut FILE.lines());
        assert_eq!(*SUM.lock().unwrap(), 1297683)
    }

    #[test]
    fn day7_test_part2() {
        // need to lock it when accessing a Mutex in a multi-threaded context
        //  to ensure exclusive access and prevent data races
        //  probably should do that for main as well
        let target = 30000000 - (70000000 - TREE.lock().unwrap().root.val);
        let mut curr = TREE.lock().unwrap().root.val;
        assert_eq!(dfs(&TREE.lock().unwrap().root, &mut curr, target), 5756764)
    }
}
