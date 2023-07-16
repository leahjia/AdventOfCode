import java.io.*;
import java.util.*;

public class Day13 {
    static int index;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day13.txt"));
        List<String[]> pairs = new ArrayList<>();
        while (in.hasNext()) {
            String left = in.nextLine();
            String right = in.nextLine();
            pairs.add(new String[]{left, right});
            if (in.hasNext()) in.nextLine();
        }
        
        // part 1
        int ct = 0;
        for (int i = 0; i < pairs.size(); i++) {
            if (inOrder(pairs.get(i))) {
                ct += i + 1;
            }
        }
        System.out.println("Count: " + ct);
        
        // part 2 sorting
        // pq to sort - didn't work
        /*
        Queue<String> res = new PriorityQueue<>((a, b) -> a.equals(b) ? 0 : (inOrder(new String[]{a, b}) ? -1 : 1));
        for (String[] pair : pairs) {
            res.add(pair[0]);
            res.add(pair[1]);
        }
         */
        List<String> res = new ArrayList<>();
        for (String[] pair : pairs) {
            insert(res, pair[0]);
            insert(res, pair[1]);
        }
        insert(res, "[[2]]");
        int decoder = index + 1;
        insert(res, "[[6]]");
        decoder *= (index + 1);
        System.out.println("Decoder: " + decoder); // ~23049
    }
    
    private static void insert(List<String> res, String s) {
        int i = 0;
        int len = res.size();
        while (i < len && inOrder(new String[]{res.get(i), s})) {
            i++;
        }
        index = i;
        if (i == len) {
            res.add(s);
        } else {
            String cur = res.get(i);
            res.set(i, s);
            i++;
            while (i < len) {
                String temp = res.get(i);
                res.set(i, cur);
                cur = temp;
                i++;
            }
            res.add(cur);
        }
    }
    
    private static boolean inOrder(String[] pair) {
        // convert both to List
        List<String> left = toList(pair[0]);
        List<String> right = toList(pair[1]);
        
        // while there are elements left in both, compare
        int iter = Math.min(left.size(), right.size());
        for (int i = 0; i < iter; i++) {
            String l = left.get(i);
            String r = right.get(i);
            
            if (!l.startsWith("[") && !r.startsWith("[")) {
                // case 1 - both are integers
                int a = Integer.parseInt(l);
                int b = Integer.parseInt(r);
                if (a == b) continue;
                return a < b;
            } else if (l.equals("") || r.equals("")) {
                // case 2 - one of them is empty
                return l.equals("");
            } else if (!l.startsWith("[")) {
                // case 3 - one is a value, another is a list
                l = "[" + l + "]";
            } else if (!r.startsWith("[")) {
                r = "[" + r + "]";
            }
            if (l.equals(r)) continue;
            
            // case 4 - nested -> recursion
            return inOrder(new String[]{l, r});
        }
        return left.size() <= right.size();
    }
    
    private static List<String> toList(String s) {
        List<String> res = new ArrayList<>();
        if (s.equals("[]")) return res;
        
        // remove brackets
        String[] list = s.substring(1, s.length() - 1).split(",");
        
        // iter through each token
        for (int i = 0; i < list.length; i++) {
            String curr = list[i];
            
            // single integer
            if (!curr.startsWith("[")) {
                res.add(curr);
                continue;
            }
            
            int open = 0;
            for (char ch : curr.toCharArray()) {
                if (ch == '[') {
                    open++;
                } else if (ch == ']') {
                    open--;
                }
            }
            String output = curr + ",";
            while (open != 0) {
                i++;
                for (char ch : list[i].toCharArray()) {
                    if (ch == '[') {
                        open++;
                    } else if (ch == ']') {
                        open--;
                    }
                    output += ch;
                }
                output += ",";
            }
            res.add(output.substring(0, output.length() - 1));
        }
        return res;
    }
}