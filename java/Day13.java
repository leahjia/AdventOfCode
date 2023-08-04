import java.io.*;
import java.util.*;

public class Day13 {
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
            if (inOrder(pairs.get(i))) ct += i + 1;
        }
        System.out.println("Part I - Count: " + ct);
        
        // part 2 sorting
        // approach 1 - pq to sort
        long startTime = System.currentTimeMillis();
        Queue<String> pq = new PriorityQueue<>((a, b) -> a.equals(b) ? 0 : (inOrder(new String[]{a, b}) ? -1 : 1));
        pq.offer("[[2]]");
        pq.offer("[[6]]");
        for (String[] pair : pairs) {
            pq.offer(pair[0]);
            pq.offer(pair[1]);
        }
        int prod = 1;
        int index = 1;
        while (!pq.isEmpty()) {
            String curr = pq.poll();
            if (curr.equals("[[2]]")) prod *= index;
            if (curr.equals("[[6]]")) prod *= index;
            index++;
        }
        System.out.println("Part II - Decoder: " + prod);
        System.out.println("pq execution time: " + (System.currentTimeMillis() - startTime) + "ms");
        
        
        // approach 2 - dumb sort
        startTime = System.currentTimeMillis();
        List<String> res = new ArrayList<>();
        for (String[] pair : pairs) {
            insert(res, pair[0]);
            insert(res, pair[1]);
        }
        int decoder = (insert(res, "[[2]]") + 1) * (insert(res, "[[6]]") + 1);
        System.out.println("Part II - Decoder: " + decoder); // ~23049
        System.out.println("dumb execution time: " + (System.currentTimeMillis() - startTime) + "ms");
    }
    
    private static int insert(List<String> res, String s) {
        int i = 0;
        int len = res.size();
        while (i < len && inOrder(new String[]{res.get(i), s})) i++;
        int index = i;
        if (i == len) res.add(s);
        else {
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
        return index;
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
                if (ch == '[') open++;
                else if (ch == ']') open--;
            }
            StringBuilder output = new StringBuilder(curr + ",");
            while (open != 0) {
                i++;
                for (char ch : list[i].toCharArray()) {
                    if (ch == '[') open++;
                    else if (ch == ']') open--;
                    output.append(ch);
                }
                output.append(",");
            }
            res.add(output.substring(0, output.length() - 1));
        }
        return res;
    }
}