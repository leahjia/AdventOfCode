use nom::{
    branch::alt,
    bytes::complete::tag,
    character::complete::{
        self, alpha1, digit1, multispace1, newline, space1,
    },
    multi::{many1, separated_list1},
    sequence::{delimited, preceded},
    *,
};
use std::fs;

lazy_static::lazy_static! {
    static ref FILE:String = fs::read_to_string("../input/day5.txt").unwrap();
}

fn main() {
    println!("{}{}", "Expected BWNCQRMDB - Received ", part1(&FILE));
    println!("{}{}", "Expected NHWZCBNBF - Received ", part2(&FILE));
}

fn get_crate(input: &str) -> IResult<&str, Option<&str>> {
    let (input, c) = alt((
        tag("   "),
        delimited(complete::char('['), alpha1, complete::char(']'))
    ))(input)?;

    Ok((input, match c {
        "   " => None,
        value => Some(value)
    }))
}

fn line(input: &str) -> IResult<&str, Vec<Option<&str>>> {
    Ok(separated_list1(tag(" "), get_crate)(input)?)
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
    let (input, _) = tag(" to ")(input)?;
    let (input, to) = complete::u32(input)?;
    Ok((
        input,
        Move {
            num,
            from: from - 1,
            to: to - 1,
        },
    ))
}

fn crates(input: &str) -> IResult<&str, (Vec<Vec<&str>>, Vec<Move>)> {
    let (input, crates_horizontal) = separated_list1(newline, line)(input)?;
    let (input, _) = newline(input)?;
    let (input, _numbers) = many1(preceded(multispace1, digit1))(input)?;
    let (input, _) = multispace1(input)?;
    let (input, moves) = separated_list1(newline, move_crate)(input)?;

    let mut crates_vertical: Vec<Vec<Option<&str>>> = vec![];
    for _ in 0..=crates_horizontal.len() {
        crates_vertical.push(vec![]);
    }

    for vec in crates_horizontal.iter().rev() {
        for (i, ch) in vec.iter().enumerate() {
            crates_vertical[i].push(ch.clone())
        }
    }

    // when starting iter, need to collect
    let crates: Vec<Vec<&str>> = crates_vertical
        .iter()
        .map(|vec| vec.iter().filter_map(|v| *v).collect())
        .collect();
    Ok((input, (crates, moves)))
}

fn part1(input: &str) -> String {
    let (_, (mut crates, moves)) = crates(input).unwrap();
    for Move { num, from, to } in moves.iter() {
        let len = crates[*from as usize].len();
        let drained = crates[*from as usize]
            .drain((len - *num as usize)..)
            .rev().collect::<Vec<&str>>();
        for c in drained.iter() {
            crates[*to as usize].push(c);
        }
    }

    let res: String = crates.iter().map(|v| match v.iter().last() {
        Some(c) => c,
        None => ""
    }).collect();
    res
}

fn part2(input: &str) -> String {
    let res = "rando str";
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
