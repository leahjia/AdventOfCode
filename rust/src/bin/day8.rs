use std::{fs, sync::Mutex};

lazy_static::lazy_static! {
    static ref FILE:String = fs::read_to_string("../input/day8.txt").unwrap();
    static ref DIRECT:[(isize, isize); 4] = [(1, 0), (0, 1), (-1, 0), (0, -1)];
    static ref ROW: Mutex<usize> = Mutex::new(0);
    static ref COL: Mutex<usize> = Mutex::new(0);
}

fn main() {
    let lines = FILE.lines();
    let mut grid: Vec<Vec<i32>> = Vec::new();
    for line in lines {
        grid.push(line.chars().filter_map(|c| c.to_digit(10)).map(|d| d as i32).collect());
    }
    *ROW.lock().unwrap() = grid.len();
    *COL.lock().unwrap() = grid[0].len();
    println!("{}{}", "day 8 part I: 1543 - ", visible(grid));
    println!("{}{}", "day 8 part II: 595080 - ", scenicScore());
}

fn scenicScore() -> i32 {
    -1
}

fn score_in_direct() -> i32 {
    -1
}

fn visible(grid: Vec<Vec<i32>>) -> usize {
    let row = *ROW.lock().unwrap();
    let col = *COL.lock().unwrap();
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

    lazy_static! {
        static ref FILE: String = fs::read_to_string("../input/day8.txt").unwrap();
    }

    #[test]
    fn day8_test_part1() {
        assert_eq!(1543, visible());
    }

    #[test]
    fn day8_test_part2() {
        assert_eq!(595080, score_in_direct());
    }
}
