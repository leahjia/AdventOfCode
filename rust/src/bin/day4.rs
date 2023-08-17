use std::fs;

use itertools::Itertools;

lazy_static::lazy_static! {
    static ref FILE:String = fs::read_to_string("../input/day4.txt").unwrap();
}

fn main() {
    println!("{}{:?}", "Day 4 ", day4(&FILE));
}

fn day4(input: &str) -> Vec<u16> {
    let mut part1: u16 = 0;
    let mut part2: u16 = 0;
    for line in input.lines() {
        let pair: Vec<Vec<u16>> = line.split(",").map(|pair| pair.split("-").map(|num| num.parse().unwrap()).collect()).sorted_by(|a: &Vec<u16>, b| a[0].cmp(&b[0]).then(b[1].cmp(&a[1]))).collect();
        part1 += (pair[0][1] >= pair[1][1]) as u16;
        part2 += (pair[0][1] >= pair[1][0]) as u16;
    }
    vec![part1, part2]
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn day4_part1_test() {
        assert_eq!(day4(&FILE), vec!(524, 798));
    }
}
