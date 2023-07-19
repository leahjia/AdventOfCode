use anyhow::Ok;
use core::num::{self, dec2flt::number};
use nom::{
    branch::alt,
    bytes::complete::tag,
    character::complete::{
        self, alpha1, anychar, digit1, line_ending, multispace1, newline, space1,
    },
    multi::{self, many1, separated_list1},
    sequence::{delimited, preceded, separated_pair, terminated},
    IResult,
};
use std::fs;

lazy_static::lazy_static! {
    static ref FILE:String = fs::read_to_string("../input/day5.txt").unwrap();
}

fn main() {
    print!("{}{}", "Expected BWNCQRMDB - Received ", part1(&FILE));
    print!("{}{}", "Expected NHWZCBNBF - Received ", part2(&FILE));
}

fn get_crate(input: &str) -> IResult<&str, Option<&str>> {
    let (input, c) = alt((
        tag("   "),
        delimited(complete::char('['), alpha1, complete::char(']')),
    ))(input)?;

    Ok((
        input,
        match c {
            "   " => None,
            value => Some(value),
        },
    ))
}

fn line(input: &str) -> IResult<&str, Vec<Option<&str>>> {
    Ok(separated_list1(tag(","), get_crate)(input)?)
}

#[derive(Debug)]
struct Move {
    num: u32,
    from: u32,
    to: u32,
}

fn move_crate(input: &str) -> IResult<&str, Move> {
    let (input, _) = tag("move ")(input)?;
    let (input, num) = complete::u32(input)?;
    let (input, _) = tag(" from ")(input)?;
    let (input, from) = complete::u32(input)?;
    let (input, _) = tag(" to")(input)?;
    let (input, to) = complete::u32(input)?;
    Ok((input, Move { num, from, to }))
}

fn crates(input: &str) -> IResult<&str, Vec<Vec<Option<&str>>>> {
    let (input, crates_horizontal) = separated_list1(newline, line)(input)?;
    let (input, _) = newline(input)?;
    let (input, numbers) = preceded(multispace1, digit1)(input)?;
    let (input, _) = multispace1(input)?;
    let (input, _) = newline(input);
    let (input, _) = newline(input);
    let (input, moves)
    Ok((input, crates_horizontal));
}

fn part1(input: &str) -> String {
    let res = "";
    let (_, assignments) = stacks(input).unwrap();
    dbg!(assignments);
    res.to_string()
}

fn part2(input: &str) -> String {
    let res = "";
    res.to_string()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn day5_part1_test() {
        assert_eq!(part1(&FILE), "BWNCQRMDB");
    }

    #[test]
    fn day5_part2_test() {
        assert_eq!(part2(&FILE), "NHWZCBNBF");
    }
}
