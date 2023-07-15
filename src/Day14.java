import java.io.*;
import java.util.*;

public class Day14 {
    static char[][] grid;
    static int count;
//    static int leftBound;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day14.txt"));
        List<List<int[]>> input = new ArrayList<>();
        int row = 0;
        int col = 0;
        int leftBound = Integer.MAX_VALUE;
        while (in.hasNext()) {
            String[] path = in.nextLine().split(" -> ");
            List<int[]> segments = new ArrayList<>();
            for (String str : path) {
                String[] pair = str.split(",");
                int[] xy = new int[]{Integer.parseInt(pair[0]), Integer.parseInt(pair[1])};
                col = Math.max(col, xy[0]);
                leftBound = Math.min(leftBound, xy[0]);
                row = Math.max(row, xy[1]);
                segments.add(xy);
            }
            input.add(segments);
        }
        
        col -= leftBound;
        getGrid(input, row, col, leftBound);
        
        // part 1
        count = 0;
        sandFall(row, col, leftBound);
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
    
    private static void getGrid(List<List<int[]>> input, int row, int col, int leftBound) {
        grid = new char[row + 1][col + 1];
        for (char[] r : grid) {
            Arrays.fill(r, '.');
        }
        grid[0][500 - leftBound] = '+';
        
        for (List<int[]> path : input) {
            for (int i = 0; i < path.size() - 1; i++) {
                int[] a = path.get(i);
                int[] b = path.get(i + 1);
                for (int r = Math.min(a[1], b[1]); r <= Math.max(a[1], b[1]); r++) {
                    for (int c = Math.min(a[0] - leftBound, b[0] - leftBound); c <= Math.max(a[0] - leftBound, b[0] - leftBound); c++) {
                        grid[r][c] = '#';
                    }
                }
            }
        }
    }
    
    private static void sandFall(int row, int col, int leftBound) {
        while (true) {
            int r = 0;
            int c = 500 - leftBound;
            
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
