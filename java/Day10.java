import java.io.*;
import java.util.*;

public class Day10 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day10.txt"));
        List<String> commands = new ArrayList<>();
        while (in.hasNext()) commands.add(in.nextLine());
        
        System.out.println("part 1: " + CPU(commands)); // 16880
        System.out.println("part 2: ");
        CRT(commands);
    }
    
    static String line;
    static int drawing;
    static int X;
    
    private static void CRT(List<String> list) {
        X = 1;
        drawing = 0;
        line = "#";
        for (String s : list) {
            String[] curr = s.split(" ");
            checkPosition();
            if (curr[0].equals("addx")) {
                X += Integer.parseInt(curr[1]);
                checkPosition();
            }
        }
    }
    
    // for CRT
    private static void checkPosition() {
        drawing++;
        if (drawing == 40) {
            System.out.println(line);
            line = "";
            drawing = 0;
        }
        line += (X - 1 <= drawing && drawing <= X + 1) ? "#" : ".";
    }
    
    private static int CPU(List<String> list) {
        int cycle = 1;
        X = 1;
        int sum = 0;
        for (String s : list) {
            String[] curr = s.split(" ");
            cycle++;
            if (cycle % 40 == 20) sum += cycle * X;
            if (curr[0].equals("addx")) {
                X += Integer.parseInt(curr[1]);
                cycle++;
                if (cycle % 40 == 20) sum += cycle * X;
            }
        }
        return sum;
    }
}
