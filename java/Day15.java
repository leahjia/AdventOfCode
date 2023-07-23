import java.io.*;
import java.util.*;
import java.util.regex.*;

class Day15 {
    
    static int left = Integer.MAX_VALUE;
    static int right = Integer.MIN_VALUE;
    static List<Point[]> pairs;
    static final int BOUND = 4000000;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day15.txt"));
        pairs = new ArrayList<>();
        parser(in);
        
        // part 1 - where points cannot exist
        System.out.println("Count: Expected 5809294, Received " + getRange(pairs, 2000000)); // 5809294
        
        // part 2
        long startTime = System.currentTimeMillis();
        Point pt = findPoints();
        System.out.println("Part II: Expected (2673432, 3308112), Received (" + (pt.x + ", " + pt.y) + ")"); // 2673432, 3308112 -> 10693731308112
        System.out.println("Execution time: " + (System.currentTimeMillis() - startTime) + "ms");
    }
    
    // go through the parameter of rach point
    private static Point findPoints() {
        for (Point[] pair : pairs) {
            Point sensor = pair[0];
            Point beacon = pair[1];
            int dist = getDist(sensor, beacon);
            int row = sensor.y;
            int y = -1;
            
            // search around the parameter
            int rightBound = sensor.x + dist + 1;
            int leftBound = sensor.x - dist - 1;
            int width = rightBound - leftBound;
            for (int x = leftBound; x <= rightBound; x++) {
                // y increase on first half, decrease second half
                y += x <= width / 2 + leftBound ? 1 : -1; // this is what messed me up
                if (0 <= x && x <= BOUND) {
                    if (0 <= row - y && row - y <= BOUND) {
                        Point point = new Point(x, row - y);
                        if (notInRange(point)) return point;
                    }
                    if (0 <= row + y && row + y <= BOUND) {
                        Point point = new Point(x, row + y);
                        if (notInRange(point)) return point;
                    }
                }
            }
        }
        return new Point(0, 0);
    }
    
    private static boolean notInRange(Point point) {
        for (Point[] pair : pairs) {
            Point sensor = pair[0];
            Point beacon = pair[1];
            if (getDist(point, sensor) <= getDist(sensor, beacon)) {
                return false;
            }
        }
        return true;
    }
    
    // for part 1
    private static int getRange(List<Point[]> pairs, int row) {
        char[] chars = new char[right - left];
        Arrays.fill(chars, '.');
        
        // applying each pair to that row
        for (Point[] pair : pairs) {
            Point sensor = pair[0];
            Point beacon = pair[1];
            int dist = getDist(sensor, beacon);
            
            // label sensor and beacon
            if (sensor.y == row) chars[sensor.x - left] = 'S';
            if (beacon.y == row) chars[beacon.x - left] = 'B';
            
            // if row is in range (sensor.y +- dist), count #
            if (sensor.y - dist <= row && row <= sensor.y + dist) {
                // run from left to right of that row
                for (int i = left; i < right; i++) {
                    Point point = new Point(i, row);
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
    
    // parsing input file into a list of sensor-beacon pairs
    private static void parser(Scanner in) {
        Pattern pattern = Pattern.compile("x=(-?\\d+), y=(-?\\d+).*x=(-?\\d+), y=(-?\\d+)");
        
        while (in.hasNext()) {
            Matcher matcher = pattern.matcher(in.nextLine());
            matcher.find();
            Point sensor = new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            Point beacon = new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
            pairs.add(new Point[]{sensor, beacon});
            
            int dist = getDist(sensor, beacon);
            left = Math.min(left, sensor.x - dist + 1);
            right = Math.max(right, sensor.x + dist);
        }
    }
    
    private static int getDist(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
    
    static class Point {
        int x;
        int y;
        
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
