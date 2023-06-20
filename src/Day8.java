import java.io.*;
import java.util.*;

public class Day8 {
    static final int[][] direct = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    static int row;
    static int col;
    static List<List<Integer>> grid;
    static int[][] matrix;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day8.txt"));
        
        grid = new ArrayList<>();
        while (in.hasNext()) {
            char[] line = in.nextLine().toCharArray();
            List<Integer> list = new ArrayList<>();
            for (char ch : line) list.add(Character.getNumericValue(ch));
            grid.add(list);
        }
        row = grid.size();
        col = grid.get(0).size();
        
        matrix = new int[row][col];
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                matrix[r][c] = grid.get(r).get(c);
            }
        }
        
        System.out.println("Part I: " + visible());
        System.out.println("Part II: " + scenicScore());
    }
    
    private static int scenicScore() {
        int max = 0;
        for (int r = 1; r < row - 1; r++) {
            for (int c = 1; c < col - 1; c++) {
                int scoreInDirection = 1;
                for (int[] d : direct) {
                    scoreInDirection *= scoreInDirection(r, c, d[0], d[1]);
                }
                max = Math.max(max, scoreInDirection);
            }
        }
        return max;
    }
    
    private static int scoreInDirection(int r, int c, int dr, int dc) {
        int og = matrix[r][c];
        int res = 0;
        while ((r += dr) >= 0 && (c += dc) >= 0 && r < row && c < col) {
            res++;
            if (matrix[r][c] >= og) break;
        }
        return res; // 595080
    }
    
    private static int visible() {
        // max height so far in all directions
        int[][] top = new int[row][col];
        int[][] bottom = new int[row][col];
        int[][] left = new int[row][col];
        int[][] right = new int[row][col];
        
        // filling the edges
        top[0] = bottom[0] = left[0] = right[0] = matrix[0];
        top[row - 1] = bottom[row - 1] = left[row - 1] = right[row - 1] = matrix[row - 1];
        for (int r = 0; r < row; r++) {
            top[r][0] = bottom[r][0] = left[r][0] = right[r][0] = matrix[r][0];
            int c = col - 1;
            top[r][c] = bottom[r][c] = left[r][c] = right[r][c] = matrix[r][c];
        }
        
        for (int r = 1; r < row - 1; r++) {
            for (int c = 1; c < col - 1; c++) {
                top[r][c] = Math.max(matrix[r][c], top[r - 1][c]);
                left[r][c] = Math.max(matrix[r][c], left[r][c - 1]);
                int lastRow = row - r - 1;
                int lastCol = col - c - 1;
                bottom[lastRow][c] = Math.max(matrix[lastRow][c], bottom[lastRow + 1][c]);
                right[r][lastCol] = Math.max(matrix[r][lastCol], right[r][lastCol + 1]);
            }
        }
        
        int visible = (row + col) * 2 - 4;
        for (int r = 1; r < row - 1; r++) {
            for (int c = 1; c < col - 1; c++) {
                int curr = grid.get(r).get(c);
                if (curr > top[r - 1][c] || curr > left[r][c - 1] ||
                        curr > bottom[r + 1][c] || curr > right[r][c + 1]) {
                    visible++;
                }
            }
        }
        return visible; // ~1543
    }
}
