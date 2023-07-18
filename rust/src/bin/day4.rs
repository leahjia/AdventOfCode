use nom::character::complete::newline;
use nom::multi::separated_list1;
use nom::sequence::separated_pair;
use nom::{bytes::complete::tag, character::complete, *};
use std::fs;
use std::ops::RangeInclusive;

lazy_static::lazy_static! {
    static ref FILE:String = fs::read_to_string("../input/day4.txt").unwrap();
}

fn main() {
    println!("{}{}", "Expected: 524, result: ", part1(&FILE));
    println!("{}{}", "Expected: 798, result: ", part2(&FILE));
}

fn pair_of_ranges(input: &str) -> IResult<&str, (RangeInclusive<u32>, RangeInclusive<u32>)> {
    let (input, ((start1, end1), (start2, end2))) = separated_pair(
        separated_pair(complete::u32, tag("-"), complete::u32),
        tag(","),
        separated_pair(complete::u32, tag("-"), complete::u32),
    )(input)?;
    Ok((input, ((start1..=end1), (start2..=end2))))
}

// return custom result type
fn sections(input: &str) -> IResult<&str, Vec<(RangeInclusive<u32>, RangeInclusive<u32>)>> {
    let (input, ranges) = separated_list1(newline, pair_of_ranges)(input)?;
    Ok((input, ranges))
}

fn part1(input: &str) -> String {
    let (_, sections) = sections(input).unwrap();
    sections
        .iter()
        .filter(|(a, b)| {
            a.clone().into_iter().all(|n| b.contains(&n))
                || b.clone().into_iter().all(|n| a.contains(&n))
        })
        .count()
        .to_string()
}

fn part2(input: &str) -> String {
    // only difference is all vs any
    let (_, sections) = sections(input).unwrap();
    sections
        .iter()
        .filter(|(a, b)| {
            a.clone().into_iter().any(|n| b.contains(&n))
                || b.clone().into_iter().any(|n| a.contains(&n))
        })
        .count()
        .to_string()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_test() {
        assert_eq!(part1(&FILE), "524");
    }

    #[test]
    fn part2_test() {
        assert_eq!(part2(&FILE), "798");
    }
}
