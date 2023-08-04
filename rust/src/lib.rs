use anyhow::{anyhow, Result};
use std::str::FromStr;

pub fn read_line<T>(path: &str) -> Result<Vec<T>>
where
    T: FromStr,
{
    Ok(std::fs::read_to_string(path).map_err(|err| anyhow!("Failed to read file: {}", err))?.lines().filter_map(|line| line.parse::<T>().ok()).collect())
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_reads() {
        let result: Result<Vec<String>> = read_line("../input/day2.txt");
        assert!(result.is_ok());
    }
}
