import java.io.*;
import java.util.*;

public class Day13 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day13_sample.txt"));
        List<String[]> pairs = new ArrayList<>();
        while (in.hasNext()) {
            String left = in.nextLine();
            String right = in.nextLine();
            pairs.add(new String[]{left, right});
            if (in.hasNext()) in.nextLine();
        }
        int ct = 0;
        for (int i = 0; i < pairs.size(); i++) {
            if (inOrder(pairs.get(i))) {
                ct += i + 1;
            }
        }
        System.out.println("Count: " + ct);
    }
    
    private static boolean inOrder(String[] pair) {
        // convert both to List
        List<String> left = toList(pair[0]);
        List<String> right = toList(pair[1]);
        
        // while there are elements left in both, compare
        int iter = Math.min(left.size(), right.size());
        for (int i = 0; i < iter; i++) {
            // both are integers
            String l = left.get(i);
            String r = right.get(i);
            if (!l.startsWith("[") && !r.startsWith("[")) {
                int a = Integer.parseInt(l);
                int b = Integer.parseInt(r);
                if (a == b) continue;
                return a < b;
            } else if (l.equals("")) {
                return true;
            } else if (r.equals("")) {
                return false;
            } else if (!l.startsWith("[") && r.startsWith("[")) {
                return inOrder(new String[]{"[" + l + "]", r});
            } else if (l.startsWith("[") && !r.startsWith("[")) {
                return inOrder(new String[]{l, "[" + r + "]"});
            } else {
                return inOrder(new String[]{l, r});
            }
        }
        return left.size() <= right.size();
    }
    
    private static List<String> toList(String s) {
        List<String> res = new ArrayList<>();
        if (s.equals("[]")) return res;
        s = s.substring(1, s.length() - 1);
        String[] list = s.split(",");
        for (int i = 0; i < list.length; i++) {
            String str = list[i];
            
            // integer or list with one integer
            if (!str.startsWith("[") || str.endsWith("]")) {
                res.add(str);
                continue;
            }
            
            Stack<String> stack = new Stack<>();
            stack.push(str);
            while (!stack.isEmpty()) {
                i++;
                for (char ch : list[i].toCharArray()) {
                    if (ch == '[') {
                        stack.push(ch + "");
                    } else if (ch == ']') {
                        String temp1 = stack.pop() + ch;
                        if (stack.isEmpty()) {
                            str = temp1;
                        } else {
                            String temp = stack.pop();
                            if (!temp.endsWith("[")) {
                                temp += ",";
                            }
                            temp += temp1;
                            stack.push(temp);
                        }
                    } else {
                        // integer
                        String temp = stack.pop();
                        // if there's already some number in it
                        if (!temp.endsWith("[")) {
                            temp += ",";
                        }
                        stack.push(temp + ch);
                    }
                }
            }
            res.add(str);
        }
        return res;
    }
    
    static class packet {
        boolean isList;
        String val;
        
        private packet(String val) {
            this.val = val;
            this.isList = val.startsWith("[");
        }
    }
}