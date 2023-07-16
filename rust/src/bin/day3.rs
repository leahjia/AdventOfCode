#![feature(iter_array_chunks)]
use std::{collections::HashMap, fs};

fn main() {
    let file = fs::read_to_string("../input/day3.txt").unwrap();
    println!("{}{}", "Expect: 7967, result: ", part1(&file));
    println!("{}{}", "Expect: 2716, result: ", part2(&file));
}

// get all the ranges for letters; collect tuples into a hashmap
// (chain returns an interator)
lazy_static::lazy_static! {
    static ref ALL_LETTERS: HashMap<char, usize> =
        ('a'..='z').chain('A'..='Z')
        .enumerate()
        .map(|(i, ch)| (ch, i + 1))
        .collect::<HashMap<char, usize>>();
}

pub fn part1(input: &str) -> String {
    let res: usize = input
        .lines()
        .map(|line| {
            let (first_half, second_half) = line.split_at(line.len() / 2);
            // returns an option because find could fail
            let common = first_half
                .chars()
                .find(|ch| second_half.contains(*ch))
                .unwrap();
            // looking up in the dictionary; could also fail
            ALL_LETTERS.get(&common).unwrap()
        })
        .sum();

    res.to_string()
}

pub fn part2(input: &str) -> String {
    let res = input
        .lines()
        .array_chunks::<3>()
        .map(|[a, b, c]| {
            let common = a
                .chars()
                .find(|ch| b.contains(*ch) && c.contains(*ch))
                .unwrap();
            ALL_LETTERS.get(&common).unwrap()
        })
        .sum::<usize>();
    res.to_string()
}
