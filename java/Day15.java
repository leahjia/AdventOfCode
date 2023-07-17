import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Day15 {
    
    static int left = Integer.MAX_VALUE;
    static int right = Integer.MIN_VALUE;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day15.txt"));
        List<int[][]> pairs = new ArrayList<>();
        int lower = Integer.MAX_VALUE;
        int upper = Integer.MIN_VALUE;
        
        while (in.hasNext()) {
            Pattern pat = Pattern.compile("x=(-?\\d+),\\s*y=(-?\\d+)");
            Matcher mat = pat.matcher(in.nextLine());
            
            mat.find();
            int[] sensor = new int[]{Integer.parseInt(mat.group(1)), Integer.parseInt(mat.group(2))};
            mat.find();
            int[] beacon = new int[]{Integer.parseInt(mat.group(1)), Integer.parseInt(mat.group(2))};
            pairs.add(new int[][]{sensor, beacon});
            
            lower = Math.min(lower, Math.min(sensor[1], beacon[1]));
            upper = Math.max(upper, Math.max(sensor[1], beacon[1]));
            int dist = getDist(sensor, beacon);
            left = Math.min(left, sensor[0] - dist);
            right = Math.max(right, sensor[0] + dist);
        }

//        for (int y = lower; y <= upper; y++) {
//        }
        long startTime = System.currentTimeMillis();
        int[] range1 = getRange(pairs, 2000000); // 5809294
        System.out.println("Execution time: " + (System.currentTimeMillis() - startTime) + "ms");
    }
    
    private static int[] getRange(List<int[][]> pairs, int row) {
        int[] range = new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE};
        char[] chars = new char[right - left];
        Arrays.fill(chars, '.');
        
        // applying each pair to that row
        for (int[][] pair : pairs) {
            int[] sensor = pair[0];
            int[] beacon = pair[1];
            int dist = getDist(sensor, beacon);
            
            // label sensor and beacon
            if (sensor[1] == row) {
                chars[sensor[0] - left] = 'S';
            }
            if (beacon[1] == row) {
                chars[beacon[0] - left] = 'B';
            }
            
            // if row is in range (sensor.y +- dist), count #
            if (sensor[1] - dist <= row && row <= sensor[1] + dist) {
                // run from left to right of that row
                for (int i = left; i < right; i++) {
                    int[] point = new int[]{i, row};
                    if (chars[i - left] == '.' && getDist(point, sensor) <= dist) {
                        chars[i - left] = '#';
                    }
                }
            }
        }
        
        int count = 0;
        for (char ch : chars) {
            count += ch == '#' ? 1 : 0;
        }
        System.out.println("count: " + count);
        
        return range;
    }
    
    private static int getDist(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }
}
