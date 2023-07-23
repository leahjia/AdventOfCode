import sun.management.Sensor;

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
        // System.out.println("Count: " + getRange(pairs, 2000000)); // 5809294
        
        // part 2
        long startTime = System.currentTimeMillis();
        Point pt = findPoints();
        // print all possible points
        System.out.println("Part II: " + pt.x + ", " + pt.y); // 2673432, 3308112
        System.out.println("Execution time: " + (System.currentTimeMillis() - startTime) + "ms");
    }
    
    // go through the parameter of rach point
    private static Point findPoints() {
        for (Point[] pair : pairs) {
//        Point[] pair = new Point[]{new Point(17, 20), new Point(21, 22)};
//        Point[] pair = new Point[]{new Point(16, 7), new Point(15, 3)};
            Point sensor = pair[0];
            Point beacon = pair[1];
            int dist = getDist(sensor, beacon);
            int row = sensor.y;
            int y = -1;
            
            // search around the parameter
            int rightBound = sensor.x + dist + 1;
            int leftBound = sensor.x - dist - 1;
            int width = rightBound - leftBound;
            //System.out.println("Checking from " + leftBound + " to " + rightBound + " at row " + row);
            for (int x = leftBound; x <= rightBound; x++) {
                // y increase on first half, decrease second half
                y += x <= width / 2 + leftBound ? 1 : -1;
                if (0 <= x && x <= BOUND) {
                    if (0 <= row - y && row - y <= BOUND) {
                        Point point = new Point(x, row - y);
                        //System.out.println(point.x + ", " + point.y);
                        if (notInRange(point)) return point;
                    }
                    if (0 <= row + y && row + y <= BOUND) {
                        Point point = new Point(x, row + y);
                        //System.out.println(point.x + ", " + point.y);
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
    
    private static void printCoverage(int row) {
        char[] chars = new char[BOUND + 1];
        Arrays.fill(chars, '.');
        
        // applying each pair to that row
        for (Point[] pair : pairs) {
            Point sensor = pair[0];
            Point beacon = pair[1];
            int dist = getDist(sensor, beacon);
            
            // label sensor and beacon
            if (sensor.y == row && 0 <= sensor.x && sensor.x <= BOUND) chars[sensor.x] = 'S';
            if (beacon.y == row && 0 <= beacon.x && beacon.x <= BOUND) chars[beacon.x] = 'B';
            
            // if row is in range (sensor.y +- dist), count #
            if (sensor.y - dist <= row && row <= sensor.y + dist) {
                // run from left to right of that row
                for (int i = 0; i <= BOUND; i++) {
                    Point point = new Point(i, row);
                    if (chars[i] == '.' && getDist(point, sensor) <= dist) {
                        chars[i] = '#';
                    }
                }
            }
        }
//        System.out.println(row + ": " + chars);
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
    
    // parsing input file into pairs of sensor-beacon
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
        
        boolean equals(Point other) {
            return this.x == other.x && this.y == other.y;
        }
    }
    
    // need this comparator to make TreeSet work properly
    static class PointComparator implements Comparator<Point> {
        @Override
        public int compare(Point a, Point b) {
            return a.y == b.y ? Integer.compare(a.x, b.x) : Integer.compare(a.y, b.y);
        }
    }
}
