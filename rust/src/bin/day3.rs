use std::{collections::HashSet, fs};
use itertools::Itertools;

lazy_static::lazy_static! {
    static ref FILE:String = fs::read_to_string("../input/day3.txt").unwrap();
}

fn main() {
    println!("Part I: {}", split_lines(&FILE));
    println!("Part II: {}", bulk_lines(&FILE));
}

fn split_lines(input: &str) -> u16 {
    input.split("\n").map(|line| {
        compare_lines(&line.chars().take(line.len() / 2).collect(), &line.chars().skip(line.len() / 2).collect())
    }).sum()
}

fn bulk_lines(input: &str) -> u16 {
    input.split("\n").chunks(3).into_iter().map(|line| {
        let sets: Vec<HashSet<char>> = line.map(|l| l.chars().collect()).collect();
        compare_three_lines(&sets[0], &sets[1], &sets[2])
    }).sum()
}

fn compare_lines(a: &HashSet<char>, b: &HashSet<char>) -> u16 {
    a.intersection(b).cloned().map(|ch| if ch.is_ascii_uppercase() { ch as u16 - 38 } else { ch as u16 - 96 }).sum()
}

fn compare_three_lines(a: &HashSet<char>, b: &HashSet<char>, c: &HashSet<char>) -> u16 {
    a.intersection(b).cloned().collect::<HashSet<_>>().intersection(c).cloned().map(|ch| if ch.is_ascii_uppercase() { ch as u16 - 38 } else { ch as u16 - 96 }).sum()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn day3_test1() {
        assert_eq!(7967, split_lines(&FILE))
    }

    #[test]
    fn day3_test2() {
        assert_eq!(2716, bulk_lines(&FILE))
    }}
