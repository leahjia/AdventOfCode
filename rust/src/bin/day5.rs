use std::{
    collections::{HashMap, VecDeque},
    fs,
};

use itertools::Itertools;
use regex::Regex;

lazy_static::lazy_static! {
    static ref FILE:String = fs::read_to_string("../input/day5.txt").unwrap();
}

fn main() {
    print!("{}{:?}", "Day 5 ", day5(&FILE));
}

fn day5(input: &str) -> Vec<String> {
    let sections = input.split("\n\n").collect_vec();
    let mut layers = sections[0].split("\n").collect();
    let mut stacks = process_stack(&mut layers);
    let moves = sections[1].split("\n").collect_vec();
    let part1 = execute_moves(&mut stacks.clone(), &moves, false);
    let part2 = execute_moves(&mut stacks, &moves, true);
    vec![part1, part2]
}

fn process_stack(layers: &mut VecDeque<&str>) -> HashMap<u32, VecDeque<char>> {
    let mut stacks = HashMap::new();
    let mut labels = HashMap::new();
    for (idx, label) in layers.pop_back().unwrap().chars().enumerate() {
        if label != ' ' {
            labels.insert(label.to_digit(10).unwrap(), idx);
        }
    }
    while let Some(layer) = layers.pop_back() {
        for (label, idx) in labels.iter() {
            let stack = stacks.entry(*label).or_insert_with(VecDeque::new);
            stack.extend(layer.chars().nth(*idx).filter(|&ch| ch != ' '));
        }
    }
    stacks
}

fn execute_moves(stacks: &mut HashMap<u32, VecDeque<char>>, moves: &Vec<&str>, all_at_once: bool) -> String {
    let mut top = String::from("");
    let re = Regex::new(r"move (\d+) from (\d+) to (\d+)").unwrap();
    for line in moves {
        let numbers: Vec<_> = re.captures(line).unwrap().iter().skip(1).map(|num| num.unwrap().as_str().parse::<u32>().unwrap()).collect();
        let mut temp_stack = VecDeque::new();
        for _ in 0..numbers[0] {
            if all_at_once {
                if let Some(item) = stacks.get_mut(&numbers[1]).and_then(|src| src.pop_back()) {
                    temp_stack.push_front(item);
                }
            } else {
                stacks.get_mut(&numbers[1]).and_then(|src| src.pop_back()).and_then(|item| stacks.get_mut(&numbers[2]).map(|dest| dest.push_back(item)));
            }
        }
        stacks.get_mut(&numbers[2]).unwrap().extend(temp_stack);
    }
    for key in 1..stacks.len() + 1 {
        top.push(stacks.get_mut(&(key as u32)).unwrap().pop_back().unwrap());
    }
    top.to_string()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn day5_part1_test() {
        assert_eq!(day5(&FILE), vec!("BWNCQRMDB", "NHWZCBNBF"))
    }
}
