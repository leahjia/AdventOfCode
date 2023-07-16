// use rust::process_day1;
use std::fs;

fn day1(input: &str) -> String {
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

fn main() {
    let path = "../input/day1_sample.txt";
    let file = fs::read_to_string(path).unwrap();
    println!("{}{}", "result: ", day1(&file));
}

#[cfg(test)]
mod test {
    use super::*;

    #[test]
    fn day1_test1() {
        let path = "../input/day1_sample.txt";
        let file = fs::read_to_string(path).unwrap();
        let res = day1(&file);
        print!("{}", res);
        assert_eq!(res, "24000");
    }
}
