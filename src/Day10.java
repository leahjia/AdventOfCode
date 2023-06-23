import java.io.*;
import java.util.*;

public class Day10 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day10_sample.txt"));
        List<String> commands = new ArrayList<>();
        while (in.hasNext()) commands.add(in.nextLine());
        System.out.println("part 1: " + CPU(commands)); // 16880
        CRT(commands);
//        System.out.println("part 2: " + CRT(commands));
    }
    
    private static void CRT(List<String> list) {
        int X = 1;
        int drawing = 0;
        String line = "#";
        for (String s : list) {
//            System.out.println("begin executing " + s);
//            System.out.println("CRT draws pixel in position " + drawing);
//            System.out.println("Current CRT row: " + line);
            String[] curr = s.split(" ");
            drawing++;
            line += (X - 1 <= drawing && drawing <= X + 1) ? "#" : ".";
            if (drawing == 40) {
                System.out.println(line);
                line = "#";
                drawing = 0;
                X = 1;
            }
//            System.out.println();
//            System.out.println("CRT draws pixel in position " + drawing);
//            System.out.println("Current CRT row: " + line);
            if (curr[0].equals("addx")) {
                X += Integer.parseInt(curr[1]);
                drawing++;
                line += (X - 1 <= drawing && drawing <= X + 1) ? "#" : ".";
//                System.out.println("End of cycle " + drawing + ": finish executing " + s + " (Register X is now " + X + ")");
//                System.out.print("Sprite position: ");
//                for (int i = 0; i < 40; i++) {
//                    System.out.print(i < X - 1||i > X + 1?".":"#");
//                }
//                System.out.println();
                if (drawing == 40) {
                    System.out.println(line);
                    line = "#";
                    drawing = 0;
                    X = 1;
                }
            }
//            System.out.println();
        }
    }
    
    private static int CPU(List<String> list) {
        int cycle = 1;
        int X = 1;
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
