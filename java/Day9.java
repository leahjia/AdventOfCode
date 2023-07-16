import java.io.*;
import java.util.*;

public class Day9 {
    static Map<Character, int[]> directions;
    static Map<Integer, Set<Integer>> visit;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day9.txt"));
        directions = new HashMap<>();
        directions.put('R', new int[]{1, 0});
        directions.put('L', new int[]{-1, 0});
        directions.put('U', new int[]{0, 1});
        directions.put('D', new int[]{0, -1});
        
        List<int[]> steps = new ArrayList<>();
        while (in.hasNext()) {
            char direct = in.next().charAt(0);
            int times = in.nextInt();
            for (int i = 1; i <= times; i++) {
                steps.add(directions.get(direct));
            }
        }
        
        System.out.println("Part I: " + nTails(steps, 1)); // part 1: 6026
        System.out.println("Part II: " + nTails(steps, 9)); // part 2: 2273
    }
    
    private static int nTails(List<int[]> steps, int tail) {
        for (int i = 1; i <= tail; i++) {
            steps = move(steps);
        }
        
        int count = 0;
        for (Set<Integer> set : visit.values()) {
            count += set.size();
        }
        return count;
    }
    
    private static List<int[]> move(List<int[]> steps) {
        int[] T = new int[]{0, 0};
        int[] H = new int[]{0, 0};
        
        visit = new HashMap<>();
        Set<Integer> newSet = new HashSet<>();
        newSet.add(T[1]);
        visit.put(T[0], newSet);
        
        List<int[]> res = new ArrayList<>();
        for (int[] step : steps) {
            int dx = step[0];
            int dy = step[1];
            int xDiff = H[0] - T[0];
            int yDiff = H[1] - T[1];
            // need to record HOW it got there, not just jump to new position
            if (Math.abs(xDiff + dx) == 2 || Math.abs(yDiff + dy) == 2) {
                // from adjacent to anywhere further -> use the same direction
                if (xDiff == 0 || yDiff == 0) { // THIS!!!
                    res.add(new int[]{dx, dy});
                    T[0] += dx;
                    T[1] += dy;
                } else if (xDiff + dx == 0) {
                    // from diagonal to same x-axis
                    res.add(new int[]{0, yDiff});
                    T[1] = H[1];
                } else if (yDiff + dy == 0) {
                    // from diagonal to same y-axis
                    res.add(new int[]{xDiff, 0});
                    T[0] = H[0];
                } else {
                    // from diagonal to anywhere further -> take its place
                    res.add(new int[]{xDiff, yDiff});
                    T = new int[]{H[0], H[1]}; // need a deep copy
                }
                
                newSet = visit.getOrDefault(T[0], new HashSet<>());
                newSet.add(T[1]);
                visit.put(T[0], newSet);
            }
            H[0] += dx;
            H[1] += dy;
        }
        return res;
    }
}
