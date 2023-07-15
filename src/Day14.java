import java.io.*;
import java.util.*;

public class Day14 {
    static char[][] grid;
    static int count;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day14.txt"));
        List<String> tokens = new ArrayList<>();
        while (in.hasNext()) tokens.add(in.nextLine());
        
        int row = 0;
        int col = 0;
        int leftBound = Integer.MAX_VALUE;
        List<List<int[]>> input = new ArrayList<>();
        
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
        for (List<int[]> seg : input) {
            for (int[] pair : seg) {
                System.out.println(Arrays.toString(pair));
            }
        }
        
        int start = 500 - leftBound;
        
        col -= leftBound;
        getGrid(input, row, col, start);
        
        // part 1
        count = 0;
        sandFall(row, col, start);
        print(grid);
        System.out.println("Count: " + count); // ~799
        
        // part 2
//        getGrid(input, row + 2, col + 10, leftBound);
//        Arrays.fill(grid[row + 2], '#');
//        count = 0;
//        sandFall(row + 2, col + 10, leftBound);
//        print(grid);
//        System.out.println(count);
    }
    
    private static void getGrid(List<List<int[]>> input, int row, int col, int start) {
        grid = new char[row + 1][col + 1];
        for (char[] r : grid) {
            Arrays.fill(r, '.');
        }
        grid[0][start] = '+';
        
        for (List<int[]> path : input) {
            for (int i = 0; i < path.size() - 1; i++) {
                int[] a = path.get(i);
                int[] b = path.get(i + 1);
                for (int r = Math.min(a[1], b[1]); r <= Math.max(a[1], b[1]); r++) {
                    for (int c = Math.min(a[0], b[0]); c <= Math.max(a[0], b[0]); c++) {
                        grid[r][c] = '#';
                    }
                }
            }
        }
    }
    
    private static void sandFall(int row, int col, int start) {
        while (true) {
            int r = 0;
            int c = start;
            
            // straight down || down-left || down-right
            while (grid[r + 1][c] == '.' || grid[r + 1][c - 1] == '.' || grid[r + 1][c + 1] == '.') {
                if (grid[r + 1][c] != '.') {
                    c += grid[r + 1][c - 1] == '.' ? -1 : 1;
                }
                r++;
                if (r >= row || c <= 0 || c > col) {
                    break;
                }
            }
            
            // left/right edge || empty floor
            if (c == 0 || c == col || r == row) {
                break;
            } else {
                count++;
            }
            grid[r][c] = 'O';
        }
    }
    
    private static void print(char[][] grid) {
        int i = 0;
        for (char[] chars : grid) {
            System.out.println(i++ + " " + Arrays.toString(chars));
        }
    }
}
