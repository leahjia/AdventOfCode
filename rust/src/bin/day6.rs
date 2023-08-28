use std::{collections::HashMap, fs};

lazy_static::lazy_static! {
    static ref FILE:String = fs::read_to_string("../input/day6.txt").unwrap();
}

fn main() {
    print!("{}{}", "Day 6 ", day6(&FILE));
}

fn day6(input: &str) -> usize {
    let mut l = 0;
    let mut r = 14;
    for line in input.lines() {
        let mut chars: HashMap<char, u16> = HashMap::new();
        for i in 0..r {
            let ch = line.chars().nth(i).unwrap();
            chars.insert(ch, chars.get(&ch).unwrap_or(&0) + 1);
        }
        while chars.len() < 14 && r < line.len() {
            let mut ch = line.chars().nth(l).unwrap();
            *chars.get_mut(&ch).unwrap() -= 1;
            if *chars.get(&ch).unwrap() == 0 {
                chars.remove(&ch);
            }
            ch = line.chars().nth(r).unwrap();
            chars.insert(ch, chars.get(&ch).unwrap_or(&0) + 1);
            l += 1;
            r += 1;
        }
    }
    r
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn day6_test() {
        assert_eq!(day6(&FILE), 3495);
    }
}
