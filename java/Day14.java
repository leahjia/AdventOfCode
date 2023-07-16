import java.io.*;
import java.util.*;

public class Day14 {
    static char[][] grid;
    static List<List<int[]>> input;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day14.txt"));
        List<String> tokens = new ArrayList<>();
        while (in.hasNext()) tokens.add(in.nextLine());
        
        int row = 0;
        int col = 0;
        int leftBound = Integer.MAX_VALUE;
        input = new ArrayList<>();
        
        for (String line : tokens) {
            String[] path = line.split(" -> ");
            List<int[]> segments = new ArrayList<>();
            for (String str : path) {
                int[] pair = new int[]{Integer.parseInt(str.split(",")[0]), Integer.parseInt(str.split(",")[1])};
                row = Math.max(row, pair[1]);
                col = Math.max(col, pair[0]);
                leftBound = Math.min(leftBound, pair[0]);
                segments.add(pair);
            }
            input.add(segments);
        }
        
        for (List<int[]> seg : input) {
            for (int[] pair : seg) {
                pair[0] -= leftBound;
            }
        }
        col -= leftBound;
        
        int start = 500 - leftBound;
        
        // part 1 - no floor
        getGrid(row, col, start, 0);
        System.out.println("Part I count: " + getSand(start)); // ~799
        
        // part 2 - add floor 2 levels down
        int shift = 150;
        getGrid(row + 2, col, start + shift, shift);
        Arrays.fill(grid[row + 2], '#');
        System.out.println("Part II count: " + getSand(start + shift)); // ~29076
    }
    
    private static void getGrid(int row, int col, int src, int shift) {
        grid = new char[row + 1][col + shift * 3 + 1];
        for (char[] r : grid) {
            Arrays.fill(r, '.');
        }
        grid[0][src] = '+';
        
        // label all rocks
        for (List<int[]> path : input) {
            for (int i = 0; i < path.size() - 1; i++) {
                int[] a = path.get(i);
                int[] b = path.get(i + 1);
                for (int r = Math.min(a[1], b[1]); r <= Math.max(a[1], b[1]); r++) {
                    for (int c = Math.min(a[0], b[0]) + shift; c <= Math.max(a[0], b[0]) + shift; c++) {
                        grid[r][c] = '#';
                    }
                }
            }
        }
    }
    
    private static int getSand(int start) {
        int row = grid.length;
        int col = grid[0].length;
        int count = 0;
        while (true) {
            int r = 0;
            int c = start;
            
            // straight down || down-left || down-right
            while (grid[r + 1][c] == '.' || grid[r + 1][c - 1] == '.' || grid[r + 1][c + 1] == '.') {
                if (grid[r + 1][c] != '.') {
                    // go left/right if straight down is blocked
                    c += grid[r + 1][c - 1] == '.' ? -1 : 1;
                }
                r++;
                
                // hits empty floor || left/right edge
                if (r == row - 1 || c == 0 || c == col - 1) {
                    return count;
                }
            }
            
            grid[r][c] = 'O';
            count++;
            
            // if source is finally blocked
            if (grid[0][start] == 'O') {
                return count;
            }
        }
    }
}
