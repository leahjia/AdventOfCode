import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class Day15 {
    
    static int left = Integer.MAX_VALUE;
    static int right = Integer.MIN_VALUE;
    static List<int[][]> pairs;
    static Map<Integer, Set<Integer>> map;
    static final int BOUND = 4000000;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day15.txt"));
        pairs = new ArrayList<>();
        int upper = Integer.MIN_VALUE;
        int lower = Integer.MAX_VALUE;
        Pattern pat = Pattern.compile("x=(-?\\d+),\\s*y=(-?\\d+)");
        
        while (in.hasNext()) {
            Matcher mat = pat.matcher(in.nextLine());
            
            mat.find();
            int[] sensor = new int[]{Integer.parseInt(mat.group(1)), Integer.parseInt(mat.group(2))};
            mat.find();
            int[] beacon = new int[]{Integer.parseInt(mat.group(1)), Integer.parseInt(mat.group(2))};
            pairs.add(new int[][]{sensor, beacon});

//            lower = Math.min(lower, Math.min(sensor[1], beacon[1]));
//            upper = Math.max(upper, Math.max(sensor[1], beacon[1]));
            int dist = getDist(sensor, beacon);
            lower = Math.min(lower, sensor[1] - dist + 1);
            upper = Math.max(upper, sensor[1] + dist + 1);
            left = Math.min(left, sensor[0] - dist + 1);
            right = Math.max(right, sensor[0] + dist);
        }
        
        map = new HashMap<>();
//        System.out.println("Count: " + getRange(pairs, 2000000)); // 5809294
        
        long startTime = System.currentTimeMillis();
        findPoints(4000000);
        System.out.println("Execution time: " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("Point? " + map);
    }
    
    private static void findPoints(int bound) {
        // narrow down candidate list
//        List<int[][]> clone = pairs.stream().map(array -> Arrays.stream(array).map(int[]::clone).toArray(int[][]::new)).collect(Collectors.toList());

//        for (int i = clone.size() - 1; i >= 0; i++) {
//            Sensor a = new Sensor(clone.get(i));
//            for (int j = clone.size() - 1; j >= 0; j++) {
//                Sensor b = new Sensor(clone.get(j));
//                if (getDist(a.pt, b.pt) * 2 <= a.range) {
//                    pairs.remove(j);
//                }
//                if (getDist(a.pt, b.pt) * 2 <= b.range) {
//                    pairs.remove(i);
//                }
//            }
//        }
        for (int[][] pair : pairs) {
            int[] sensor = pair[0];
            int[] beacon = pair[1];
            int dist = getDist(sensor, beacon);
            int row = sensor[1];
            int y = -1;
            
            // search through the parameter
            int len = sensor[0] + dist + 1;
            for (int x = sensor[0] - dist - 1; x <= len; x++) {
                y += x < len / 2 ? 1 : -1;
                if (0 <= x && x <= bound) {
                    Set<Integer> set = map.getOrDefault(x, new HashSet<>());
                    if (0 <= row - y && row - y <= bound) set.add(row - y);
                    if (0 <= row + y && row + y <= bound) set.add(row + y);
                    if (!set.isEmpty()) map.put(x, set);
                }
            }
            checkPoints();
            if (map.size() == 1) {
                System.out.println("returning");
                return;
            }
        }
    }
    
    private static void checkPoints() {
        for (int x : map.keySet()) {
            Set<Integer> set = map.get(x);
            set.removeIf(y -> isInRange(x, y));
            map.put(x, set);
        }
        map.entrySet().removeIf(e -> e.getValue() != null && (e.getValue()).isEmpty());
    }
    
    private static boolean isInRange(int x, int y) {
        int[] point = new int[]{x, y};
        for (int[][] pair : pairs) {
            int[] sensor = pair[0];
            int[] beacon = pair[1];
            if (getDist(point, sensor) <= getDist(sensor, beacon)) {
                return true;
            }
        }
        return false;
    }
    
    private static int getRange(List<int[][]> pairs, int row) {
        char[] chars = new char[right - left];
        Arrays.fill(chars, '.');
        
        // applying each pair to that row
        for (int[][] pair : pairs) {
            int[] sensor = pair[0];
            int[] beacon = pair[1];
            int dist = getDist(sensor, beacon);
            
            // label sensor and beacon
            if (sensor[1] == row) chars[sensor[0] - left] = 'S';
            if (beacon[1] == row) chars[beacon[0] - left] = 'B';
            
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
        return count;
    }
    
    private static int getDist(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }
}
