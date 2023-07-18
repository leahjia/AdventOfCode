import java.io.*;
import java.util.*;

public class Day4 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day4.txt"));
        int part1 = 0;
        int part2 = 0;
        while (in.hasNextLine()) {
            String[] line = in.nextLine().split("[,-]");
            int[][] pair = {{Integer.parseInt(line[0]), Integer.parseInt(line[1])}, {Integer.parseInt(line[2]), Integer.parseInt(line[3])}};
            // start in ASC, end in DESC
            Arrays.sort(pair, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);
            if (pair[0][1] >= pair[1][1]) part1++;
            if (pair[0][1] >= pair[1][0]) part2++;
        }
        System.out.println("Part 1: " + part1 + "\nPart 2: " + part2);
    }
}
