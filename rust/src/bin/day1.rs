use std::fs;

lazy_static::lazy_static! {static ref FILE:String = fs::read_to_string("../input/day1.txt").unwrap();}

fn part1(input: &str) -> u32 {
    // split into chunks, get sum of each chunk, then get max of each sum
    input.split("\n\n").map(|chunk| chunk.lines().map(|line| line.parse::<u32>().unwrap()).sum()).max().unwrap()
}

fn part2(input: &str) -> u32 {
    // sort by desc, then get sum of top 3
    let mut sums = input.split("\n\n").map(|chunk| chunk.lines().map(|line| line.parse::<u32>().unwrap()).sum::<u32>()).collect::<Vec<_>>();
    sums.sort_by(|a, b| b.cmp(a));
    sums.iter().take(3).sum()
}

fn main() {
    println!("{}{}", "Expect 69206, Result: ", part1(&FILE));
    println!("{}{}", "Expect 197400, Result: ", part2(&FILE));
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn day1_part1() {
        assert_eq!(69206, part1(&FILE))
    }

    #[test]
    fn day1_part2() {
        assert_eq!(197400, part2(&FILE))
    }
}
