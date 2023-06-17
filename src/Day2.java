import java.io.*;

public class Day2 {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("input/day2.txt"));
        int[] shape = new int[]{1, 2, 3};
        
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
        System.out.println(res); // 11666
    }
}
