use std::{collections::HashSet, fs};

lazy_static::lazy_static! {
    static ref FILE:String = fs::read_to_string("../input/day3.txt").unwrap();
}

fn main() {
    println!("Part I: {}", split_lines(&FILE));
}

fn split_lines(input: &str) -> u16 {
    input.split("\n").map(|line| {
        compare_lines(&line.chars().take(line.len() / 2).collect(), &line.chars().skip(line.len() / 2).collect())
    }).sum()
}

fn compare_lines(a: &HashSet<char>, b: &HashSet<char>) -> u16 {
    a.intersection(b).cloned().map(|ch| if ch.is_ascii_uppercase() { ch as u16 - 38 } else { ch as u16 - 96 }).sum()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn day3_test1() {
        assert_eq!(7967, part1(&FILE))
    }
}
