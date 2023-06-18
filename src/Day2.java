import java.io.*;

public class Day2 {
    static final int[] shape = {1, 2, 3};
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("input/day2.txt"));
        // part1(in);
        part2(in);
    }
    
    private static void part2(BufferedReader in) throws IOException {
        int res = 0;
        while (in.ready()) {
            char[] line = in.readLine().toCharArray();
            int op = line[0];
            int outcome = line[2];
            if (outcome == 'X') {
                if (op == 'A') res += shape[2]; // me = scissor
                else if (op == 'B') res += shape[0]; // me = rock
                else res += shape[1]; // me = paper
            } else if (outcome == 'Y') {
                res += 3 + shape[op - 'A'];
            } else {
                res += 6;
                if (op == 'A') res += shape[1]; // me = paper
                else if (op == 'B') res += shape[2]; // me = sci
                else res += shape[0]; // me = rock
            }
        }
        System.out.println(res); // 12767
    }
    
    private static void part1(BufferedReader in) throws IOException {
        int res = 0;
        while (in.ready()) {
            char[] line = in.readLine().toCharArray();
            int op = line[0];
            int me = line[2] - ('X' - 'A');
            res += shape[me - 'A'];
            
            if (op == me) res += 3;
            else if (op == 'A' && me == 'B' ||
                    op == 'B' && me == 'C' ||
                    op == 'C' && me == 'A') res += 6;
        }
        System.out.println(res);
    }
}
