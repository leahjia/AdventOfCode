use std::{cmp::Ordering, fs, str::FromStr};

fn main() {
    let file = fs::read_to_string("../input/day2.txt").unwrap();
    println!("{}{}", "Expect: 11666, result: ", part1(&file));
    println!("{}{}", "Expect: 12767, result: ", part2(&file));
}

#[derive(PartialEq, Copy, Clone)]
enum Move {
    Rock = 1,
    Paper = 2,
    Scissors = 3,
}

// for part 1 comparison
// implement partial ordering - returns an option for ordering
// reason: equality/comparison in a loop
// comment: approach more complex than it could be
impl PartialOrd for Move {
    fn partial_cmp(&self, other: &Self) -> Option<std::cmp::Ordering> {
        if self == &Move::Scissors && other == &Move::Rock {
            Some(Ordering::Less)
        } else if self == &Move::Rock && other == &Move::Scissors {
            Some(Ordering::Greater)
        } else {
            Some((*self as u8).cmp(&(*other as u8)))
        }
    }
}

impl FromStr for Move {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "A" | "X" => Ok(Move::Rock),
            "B" | "Y" => Ok(Move::Paper),
            "C" | "Z" => Ok(Move::Scissors),
            _ => Err("Invalid move:".to_string()),
        }
    }
}

pub fn part1(input: &str) -> String {
    let res: u32 = input
        .lines()
        .map(|line| {
            let moves: Vec<Move> = line
                .split(" ")
                .map(|s| s.parse::<Move>().unwrap())
                .collect();
            match moves[0].partial_cmp(&moves[1]) {
                Some(Ordering::Equal) => 3 + moves[1] as u32,
                Some(Ordering::Less) => 6 + moves[1] as u32,
                Some(Ordering::Greater) => moves[1] as u32,
                None => {
                    panic!("not comparable moves")
                }
            }
        })
        .sum();
    res.to_string()
}

// diff in part 2 - comparison rule
pub fn part2(input: &str) -> String {
    let res: u32 = input
        .lines()
        .map(|line| {
            let moves: Vec<&str> = line.split(" ").collect();
            let op = moves[0].parse::<Move>().unwrap();
            match moves[1] {
                "X" => {
                    let me = match op {
                        Move::Rock => Move::Scissors,
                        Move::Paper => Move::Rock,
                        Move::Scissors => Move::Paper,
                    };
                    me as u32
                }
                "Y" => 3 + op as u32,
                "Z" => {
                    let me = match op {
                        Move::Rock => Move::Paper,
                        Move::Paper => Move::Scissors,
                        Move::Scissors => Move::Rock,
                    };
                    6 + me as u32
                }
                _ => {
                    panic!("What is this move")
                }
            }
        })
        .sum();
    res.to_string()
}
