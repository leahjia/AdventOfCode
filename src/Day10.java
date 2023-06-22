import java.io.*;
import java.util.*;

public class Day10 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day10.txt"));
        List<String> commands = new ArrayList<>();
        while (in.hasNext()) commands.add(in.nextLine());
        System.out.println("part 1: " + CPU(commands));
    }
    
    private static int CPU(List<String> list) {
        int cycle = 1;
        int X = 1;
        int sum = 0;
        for (String s : list) {
            String[] curr = s.split(" ");
            cycle++;
            switch (curr[0]) {
                case "noop":
                    if (cycle % 40 == 20) sum += cycle * X;
                    break;
                case "addx":
                    if (cycle % 40 == 20) sum += cycle * X;
                    X += Integer.parseInt(curr[1]);
                    cycle++;
                    if (cycle % 40 == 20) sum += cycle * X;
                    break;
            }
        }
        return sum;
    }
}
