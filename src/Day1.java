import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("input/day1.txt"));
        System.out.println(findMax(in));
    }
    
    
    
    // day 1
    private static int findMax(BufferedReader in) throws IOException {
        Queue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
        int curr = 0;
        while (in.ready()) {
            String line = in.readLine();
            if (line.equals("")) {
                pq.offer(curr);
                curr = 0;
            } else {
                curr += Integer.parseInt(line);
            }
        }
        int res = 0;
        int size = pq.size();
        for (int i = 0; i < 3 && i < size; i++) res += pq.poll();
        return res;
    }
    
}