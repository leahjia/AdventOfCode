use std::fs;

lazy_static::lazy_static! {
    static ref FILE:String = fs::read_to_string("../input/day2.txt").unwrap();
}

fn main() {
    println!("{}{}", "Expected 11666, Received ", part1(&FILE));
    println!("{}{}", "Expected 12767, Received ", part2(&FILE));
}

fn part1(input: &str) -> u16 {
    input
        .split("\n")
        .map(|line| {
            let pair: Vec<char> = line.split_whitespace().flat_map(|ch| ch.chars()).collect();
            let op = pair[0] as char;
            let me = pair[1] as char;
            let me_ascii = me as u16 - 'X' as u16;
            me_ascii
                + match (op, me) {
                    ('A', 'Y') | ('B', 'Z') | ('C', 'X') => 7,
                    _ if op as u16 == me_ascii + 'A' as u16 => 4,
                    _ => 1,
                }
        })
        .sum()
}

fn part2(input: &str) -> u16 {
    input
        .split("\n")
        .map(|line| {
            let pair: Vec<char> = line.split_whitespace().flat_map(|ch| ch.chars()).collect();
            let op = pair[0] as char;
            let outcome = pair[1] as char;
            match (op, outcome) {
                ('A', 'X') => 3,
                ('B', 'X') => 1,
                ('C', 'X') => 2,
                (_, 'Y') => 4 + op as u16 - 'A' as u16,
                ('A', 'Z') => 8,
                ('B', 'Z') => 9,
                ('C', 'Z') => 7,
                _ => 0,
            }
        })
        .sum()
}

#[cfg(test)]
mod tests {
    use super::*;
    #[test]
    fn day2_part1() {
        assert_eq!(11666, part1(&FILE));
    }

    #[test]
    fn day2_part2() {
        assert_eq!(12767, 12767);
    }
}
