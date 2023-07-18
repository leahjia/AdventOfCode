use nom::{
    bytes::complete::tag, character::complete, character::complete::newline,
    multi::separated_list1, sequence::separated_pair, *,
};
use std::{fs, ops::RangeInclusive};

lazy_static::lazy_static! {
    static ref FILE:String = fs::read_to_string("../input/day4.txt").unwrap();
}

fn main() {}

fn pair_of_ranges(input: &str) -> IResult<&str, (RangeInclusive<u32>, RangeInclusive<u32>)> {
    let (input, ((start1, end1), (start2, end2))) = separated_pair(
        separated_pair(complete::u32, tag("-"), complete::u32),
        tag(","),
        separated_pair(complete::u32, tag("-"), complete::u32),
    )(input)?;
    Ok((input, ((start1..=end1), (start2..=end2))))
}

// custom result type
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
    fn day4_part1_test() {
        assert_eq!(part1(&FILE), "524");
    }

    #[test]
    fn day4_part2_test() {
        assert_eq!(part2(&FILE), "798");
    }
}
