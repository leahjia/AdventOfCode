import java.io.*;
import java.util.*;

public class Day3 {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("input/day3.txt"));
        // part1(in);
        System.out.println(part2(in));
    }
    
    private static int part2(BufferedReader in) throws IOException {
        int res = 0;
        while (in.ready()) {
            Set<Character> common = new HashSet<>();
            char[] group1 = in.readLine().toCharArray();
            Set<Character> curr = new HashSet<>();
            for (char ch : group1) curr.add(ch);
            
            char[] group2 = in.readLine().toCharArray();
            for (char ch : group2) {
                if (curr.contains(ch)) common.add(ch);
            }
            
            char[] group3 = in.readLine().toCharArray();
            for (char ch : group3) {
                if (common.contains(ch)) {
                    if (ch >= 'a') res += ch - 'a' + 1;
                    else res += ch - 'A' + 27;
                    break;
                }
            }
        }
        return res;
    }
    
    private static void part1(BufferedReader in) throws IOException {
        int res = 0;
        while (in.ready()) {
            char[] items = in.readLine().toCharArray();
            int len = items.length;
            int mid = len / 2;
            Set<Character> set = new HashSet<>();
            for (char ch : Arrays.copyOfRange(items, 0, mid)) set.add(ch);
            for (char ch : Arrays.copyOfRange(items, mid, len)) {
                if (set.contains(ch)) {
                    if (ch >= 'a') res += ch - 'a' + 1;
                    else res += ch - 'A' + 27;
                    break;
                }
            }
        }
        System.out.println(res);
    }
}