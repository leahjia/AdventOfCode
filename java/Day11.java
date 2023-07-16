import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Day11 {
    static Map<Integer, Monkey> map;
    static int MOD = 1;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day11.txt"));
        map = new TreeMap<>();
        int number = 0;
        while (in.hasNext()) {
            in.nextLine();
            String[] itemsStr = in.nextLine().split(": ")[1].split(", ");
            Queue<BigInteger> items = new LinkedList<>();
            for (String s : itemsStr) {
                items.offer(new BigInteger(s));
            }
            
            // [operator, variable]
            String[] operation = in.nextLine().split("= old ")[1].split(" ");
            int test = getLastDigit(in.nextLine());
            MOD *= test;
            int[] next = {getLastDigit(in.nextLine()), getLastDigit(in.nextLine())};
            
            Monkey monkey = new Monkey(number, items, operation, test, next, 0);
            map.put(number, monkey);
            if (in.hasNext()) in.nextLine();
            number++;
        }
        for (int i = 0; i < 10000; i++) {
            inspect();
        }
        System.out.println("Most active: " + twoMostActive());
        // 99852, 25935263541
    }
    
    // ranking activity
    private static long twoMostActive() {
        Queue<Long> pq = new PriorityQueue<>();
        for (Monkey monkey : map.values()) {
            pq.add((long) monkey.inspected);
            if (pq.size() > 2) pq.poll();
        }
        return pq.poll() * pq.poll();
    }
    
    // apply inspections
    private static void inspect() {
        for (Monkey monkey : map.values()) {
            while (!monkey.items.isEmpty()) {
                BigInteger item = operate(monkey.items.poll(), monkey.operation).mod(BigInteger.valueOf(MOD));
                BigInteger test = BigInteger.valueOf(monkey.test);
                int nextMonkey = monkey.next[item.mod(test).equals(BigInteger.valueOf(0)) ? 0 : 1];
                map.get(nextMonkey).items.offer(item);
                monkey.inspected++;
            }
        }
    }
    
    // apply operations
    private static BigInteger operate(BigInteger item, String[] operation) {
        BigInteger var = operation[1].equals("old") ? item : new BigInteger(operation[1]);
        if (operation[0].equals("*")) {
            return item.multiply(var);
        } else {
            return item.add(var);
        }
    }
    
    private static int getLastDigit(String line) {
        String[] str = line.split(" ");
        return Integer.parseInt(str[str.length - 1]);
    }
    
    // Monkey object with all the info
    static class Monkey {
        int number;
        Queue<BigInteger> items;
        String[] operation;
        int test;
        int[] next;
        int inspected;
        
        private Monkey(int number, Queue<BigInteger> items, String[] operation, int test, int[] next, int inspected) {
            this.number = number;
            this.items = items;
            this.operation = operation;
            this.test = test;
            this.next = next;
            this.inspected = inspected;
        }
    }
}
