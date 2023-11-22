use std::{fs::File, io};
use std::io::BufRead;
use anyhow::Result;


lazy_static::lazy_static! {
    static ref DIRECT:[(isize, isize); 4] = [(1, 0), (0, 1), (-1, 0), (0, -1)];
}

fn main() -> Result<()> {
    let input  = io::BufReader::new(File::open("../input/day8.txt")?);

    let mut grid: Vec<Vec<i32>> = Vec::new();
    for line in input.lines() {
        grid.push(line?.chars().map(|c| c.to_digit(10).unwrap() as i32).collect());
    }

    let row = grid.len();
    let col = grid[0].len();
    println!("{}{}", "day 8 part I: 1543 - ", visible(&grid, row, col));
    println!("{}{}", "day 8 part II: 595080 - ", scenic_score(&grid, row, col));
    Ok(())
}

fn scenic_score(grid: &Vec<Vec<i32>>, row: usize, col: usize) -> i32 {
    let mut max = 0;
    for r in 1..row - 1 {
        for c in 1..col - 1 {
            let mut score = 1;
            for &(x, y) in DIRECT.iter() {
                score *= score_in_direct(&grid, r as isize, c as isize, x, y, row, col);
            }
            max = max.max(score);
        }
    }
    max
}

fn score_in_direct(grid: &[Vec<i32>], mut r: isize, mut c: isize, x: isize, y: isize, row: usize, col: usize) -> i32 {
    let og = grid[r as usize][c as usize];
    let mut res = 0;
    while {r += x; c += y; r >= 0 && c >= 0 && r < row as isize && c < col as isize} {
        res += 1;
        if grid[r as usize][c as usize] >= og {
            break;
        }
    }
    res
}

fn visible(grid: &Vec<Vec<i32>>, row: usize, col: usize) -> usize {
    let mut top = vec![vec![0; col]; row];
    let mut down = vec![vec![0; col]; row];
    let mut left = vec![vec![0; col]; row];
    let mut right = vec![vec![0; col]; row];

    for r in 0..row {
        for c in 0..col {
            top[r][c] = grid[r][c];
            down[row - r - 1][c] = grid[row - r - 1][c];
            left[r][c] = grid[r][c];
            right[r][col - c - 1] = grid[r][col - c - 1];
        }
    }

    for r in 1..row - 1 {
        for c in 1..col - 1 {
            top[r][c] = grid[r][c].max(top[r - 1][c]);
            left[r][c] = grid[r][c].max(left[r][c - 1]);
            let last_row = row - r - 1;
            let last_col = col - c - 1;
            down[last_row][c] = grid[last_row][c].max(down[last_row + 1][c]);
            right[r][last_col] = grid[r][last_col].max(right[r][last_col + 1]);
        }
    }

    let mut visible = (row + col) * 2 - 4;
    for r in 1..row - 1 {
        for c in 1..col - 1 {
            let curr = grid[r][c];
            if curr > top[r - 1][c] || curr > left[r][c - 1] || curr > down[r + 1][c] || curr > right[r][c + 1] {
                visible += 1;
            }
        }
    }
    visible
}

#[cfg(test)]
mod tests {
    use super::*;
    use lazy_static::lazy_static;
    use std::fs;

    lazy_static! {
        static ref GRID: Vec<Vec<i32>> = fs::read_to_string("../input/day8.txt").unwrap().lines().map(|line| line.chars().map(|ch| ch.to_digit(10).unwrap() as i32).collect()).collect();
    }

    #[test]
    fn day8_test_part1() {
        assert_eq!(1543, visible(&GRID, GRID.len(), GRID[0].len()));
    }

    #[test]
    fn day8_test_part2() {
        assert_eq!(595080, scenic_score(&GRID, GRID.len(), GRID[0].len()));
    }
}
