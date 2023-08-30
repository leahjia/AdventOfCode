use std::{collections::HashMap, fs};

struct TreeNode {
    val: u32,
    branch: HashMap<String, TreeNode>,
}

impl TreeNode {
    fn new() -> TreeNode {
        TreeNode { val: 0, branch: HashMap::new() }
    }
}

struct Tree {
    root: TreeNode,
}

impl Tree {
    fn new() -> Tree {
        Tree { root: TreeNode::new() }
    }
}

lazy_static::lazy_static! {
    static ref FILE: String = fs::read_to_string("../input/day7_sample.txt").unwrap();
}
static mut SUM: u32 = 0;

fn main() {
    let mut lines = FILE.lines();
    let mut tree = Tree::new();
    tree.root.val = traverse(&mut tree.root, &mut lines);

    println!("{}{}", "day 7 part I:  1297683 - ", unsafe { SUM });
    println!("{}{}", "day 7 part II: 5756764 - ", dfs(&FILE));
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
            root.branch.entry(dir.to_string()).or_insert(TreeNode::new());
            let new_val = traverse(root, lines);
            root.val += new_val;
            if new_val <= 100000 {
                unsafe {
                    SUM += new_val;
                    dbg!(&SUM);
                }
            }
        } else if !line.starts_with("dir") {
            let file_name = parts[1];
            let file_size: u32 = parts[0].parse().unwrap();
            let mut new_node = TreeNode::new();
            new_node.val = file_size;
            dbg!(&root.val);
            root.branch.insert(file_name.to_string(), new_node);
            root.val += file_size;
            // dbg!(&file_size);
        }
    }
    root.val
}

fn dfs(input: &str) -> u32 {
    4
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn day7_test_part1() {
        // let mut tree = Tree::new();
        // tree.root.val = traverse(&mut tree.root);
        // assert_eq!(&SUM, 1297683)
    }

    #[test]
    fn day7_test_part2() {
        assert_eq!(dfs(&FILE), 5756764)
    }
}
