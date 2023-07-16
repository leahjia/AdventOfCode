import java.io.*;
import java.util.*;

public class Day12 {
    static int[][] direct = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    static int[] S;
    static int[] E;
    static int row;
    static int col;
    static char[][] grid;
    static Map<String, List<int[]>> map;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day12.txt"));
        
        List<char[]> allLines = new ArrayList<>();
        while (in.hasNext()) {
            allLines.add(in.nextLine().toCharArray());
        }
        
        row = allLines.size();
        col = allLines.get(0).length;
        S = new int[0];
        E = new int[0];
        grid = new char[row][col];
        List<int[]> allA = new ArrayList<>();
        
        // convert to matrix; mark start & end
        for (int r = 0; r < row; r++) {
            char[] line = allLines.get(r);
            grid[r] = line;
            for (int c = 0; c < col; c++) {
                char ch = line[c];
                if (ch == 'S') {
                    S = new int[]{r, c};
                    grid[r][c] = 'a';
                    allA.add(new int[]{r, c});
                } else if (ch == 'E') {
                    E = new int[]{r, c};
                    grid[r][c] = 'z';
                } else if (ch == 'a') {
                    allA.add(new int[]{r, c});
                }
            }
        }
        
        // build a spanning tree
        map = new HashMap<>();
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                char prev = grid[r][c];
                String key = r + "," + c;
                List<int[]> edgeTo = map.getOrDefault(key, new ArrayList<>());
                // going 4 directions
                for (int[] d : direct) {
                    int R = r + d[0];
                    int C = c + d[1];
                    if (R >= 0 && C >= 0 && R != row && C != col && grid[R][C] - prev <= 1) {
                        edgeTo.add(new int[]{R, C});
                        map.put(key, edgeTo);
                    }
                }
            }
        }
        
        // part I
        System.out.println("From S: " + getSteps(S));
        
        // part II
        int min = row * col;
        for (int[] a : allA) {
            min = Math.min(min, getSteps(a));
        }
        System.out.println("From any a's: " + min);
    }
    
    private static int getSteps(int[] S) {
        int min = row * col;
        // [r, c, dist], ranked by distance at index 2
        Queue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        pq.offer(new int[]{S[0], S[1], 0});
        
        // need a visit set to break out the loop early
        Set<String> visit = new HashSet<>();
        
        // Dijkstra's alg
        while (!pq.isEmpty() && visit.size() != row * col) {
            int[] curr = pq.poll();
            int r = curr[0];
            int c = curr[1];
            int dist = curr[2];
            
            if (r == E[0] && c == E[1]) {
                min = Math.min(min, dist);
                continue;
            }
            
            String key = r + "," + c;
            if (map.containsKey(key)) {
                for (int[] next : map.get(key)) {
                    if (!visit.contains(key)) {
                        pq.offer(new int[]{next[0], next[1], dist + 1});
                    }
                }
            }
            visit.add(key);
        }
        return min;
    }
    
    // didn't work - runtime issue (bfs + backtracking)
    private static void bfs(int r, int c, char prev, boolean[][] visit, int steps) {
        if (r < 0 || c < 0 || r == row || c == col || visit[r][c] || grid[r][c] - prev > 1) return;
        if (r == E[0] && c == E[1]) {
            // min = Math.min(min, steps);
            return;
        }
        System.out.println(steps + ": " + grid[r][c]);
        steps++;
        for (int[] d : direct) {
            visit[r][c] = true;
            bfs(r + d[0], c + d[1], grid[r][c], visit, steps);
            visit[r][c] = false;
        }
    }
}
