import java.io.*;
import java.util.*;

public class Day4 {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("input/day4.txt"));
        System.out.println(overlap(in));
    }
    
    private static int overlap(BufferedReader in) throws IOException {
        int res = 0;
        while (in.ready()) {
            String[] line = in.readLine().split("[,-]");
            int[][] pair = {{Integer.parseInt(line[0]), Integer.parseInt(line[1])},
                    {Integer.parseInt(line[2]), Integer.parseInt(line[3])}};
            // int[] line = Arrays.stream(in.readLine().split("[,-]")).mapToInt(Integer::parseInt).toArray();
          
            // start in ASC, end in DESC
            Arrays.sort(pair, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);
            
            // part 1
            // if (pair[0][1] >= pair[1][1]) res++;
            
            // part 2
            if (pair[0][1] >= pair[1][0]) res++;
        }
        return res; // 524, 798
    }
}
