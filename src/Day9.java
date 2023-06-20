import java.io.*;
import java.util.*;

public class Day9 {
    static Map<Character, int[]> directions = new HashMap<>();
    static Map<Integer, Set<Integer>> visit = new HashMap<>();
    static int[] T = new int[]{0, 0};
    static int[] H = new int[]{0, 0};
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day9.txt"));
        
        directions.put('R', new int[]{1, 0});
        directions.put('L', new int[]{-1, 0});
        directions.put('U', new int[]{0, 1});
        directions.put('D', new int[]{0, -1});
        
        visit.put(T[0], new HashSet<>(T[1]));
        
        while (in.hasNext()) {
            char direct = in.next().charAt(0);
            int times = in.nextInt();
            move(direct, times);
        }
        
        int count = 0;
        for (Set<Integer> set : visit.values()) {
            count += set.size();
        }
        System.out.println("Part 1: " + count); // 6026
    }
    
    private static void move(char direct, int times) {
        int[] step = directions.get(direct);
        while (times > 0) {
            H[0] += step[0];
            H[1] += step[1];
            if (Math.abs(H[0] - T[0]) == 2) T = new int[]{H[0] - step[0], H[1]};
            if (Math.abs(H[1] - T[1]) == 2) T = new int[]{H[0], H[1] - step[1]};
            Set<Integer> newSet = visit.getOrDefault(T[0], new HashSet<>());
            newSet.add(T[1]);
            visit.put(T[0], newSet);
            times--;
        }
    }
}
