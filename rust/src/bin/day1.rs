// use rust::process_day1;
use std::fs;

fn day1_part1(input: &str) -> String {
    /*
     * split by double line for each elf, then by item, find max
     * need to unwrap after parse
     */
    let res = input
        .split("\n\n")
        .map(|elf| {
            elf.lines()
                .map(|item| item.parse::<u32>().unwrap())
                .sum::<u32>()
        })
        .max()
        .unwrap();
    res.to_string()
}

fn day1_part2(input: &str) -> String {
    let mut res = input
        .split("\n\n")
        .map(|elf| {
            elf.lines()
                .map(|item| item.parse::<u32>().unwrap())
                .sum::<u32>()
        })
        .collect::<Vec<_>>();

    res.sort_by(|a, b| b.cmp(a));
    let sort: u32 = res.iter().take(3).sum();
    sort.to_string()
}

fn main() {
    let path = "../input/day1.txt";
    let file = fs::read_to_string(path).unwrap();
    println!("{}{}", "result: ", day1_part1(&file));
    println!("{}{}", "result: ", day1_part2(&file));
}

#[cfg(test)]
mod test {
    use super::*;
    const PATH: &str = "../input/day1.txt";

    #[test]
    fn part1() {
        let file = fs::read_to_string(PATH).unwrap();
        assert_eq!(day1_part1(&file), "69206");
    }

    #[test]
    fn part2() {
        let file = fs::read_to_string(PATH).unwrap();
        assert_eq!(day1_part2(&file), "197400");
    }
}
