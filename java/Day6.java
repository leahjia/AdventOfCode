import java.io.*;
import java.util.*;

public class Day6 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day6.txt"));
        
        while (in.hasNext()) {
            String line = in.nextLine();
            System.out.println(findMarker(line));
        }
    }
    
    private static int findMarker(String s) {
        int range = 14;
        int l = 0;
        int r = range;
        // count unique characters
        Map<Character, Integer> dict = new HashMap<>();
        
        // sliding window with fixed range
        char[] window = s.substring(l, r).toCharArray();
        for (char ch : window) {
            dict.put(ch, dict.getOrDefault(ch, 0) + 1);
        }
        while(dict.size() != range && r - 1 < s.length() - 1) {
            // remove left of the window
            char left = s.charAt(l++);
            if (dict.get(left) == 1) dict.remove(left);
            else dict.put(left, dict.get(left) - 1);
            
            // add new element from the right
            char right = s.charAt(r++);
            dict.put(right, dict.getOrDefault(right, 0) + 1);
        }
        
        return r;
    }
}
