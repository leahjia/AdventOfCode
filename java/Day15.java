import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Day15 {
    
    static int left = Integer.MAX_VALUE;
    static int right = Integer.MIN_VALUE;
    static Set<Point> set;
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
        set = new TreeSet<>(new PointComparator());
        findPoints();
        System.out.println(set.size());
        for (Point p : set) {
            System.out.println(p.x + ", " + p.y);
        }
        System.out.println("Execution time: " + (System.currentTimeMillis() - startTime) + "ms");
    }
    
    private static void findPoints() {
        // narrow down candidate list
        for (Point[] pair : pairs) {
            Point sensor = pair[0];
            Point beacon = pair[1];
            int dist = getDist(sensor, beacon);
            int row = sensor.y;
            int y = -1;
            
            // search around the parameter
            int width = sensor.x + dist + 1;
            for (int x = sensor.x - dist - 1; x <= width; x++) {
                // y increase on first half, decrease second half
                y += x < width / 2 ? 1 : -1;
                if (0 <= x && x <= BOUND) {
                    if (0 <= row - y && row - y <= BOUND) set.add(new Point(x, row - y));
                    if (0 <= row + y && row + y <= BOUND) set.add(new Point(x, row + y));
                }
            }
            checkPoints();
        }
    }
    
    private static void checkPoints() {
        Set<Point> remove = new TreeSet<>(new PointComparator());
        for (Point pt : set) {
            if (isInRange(pt.x, pt.y)) {
                remove.add(pt);
            }
        }
        set.removeAll(remove);
    }
    
    private static boolean isInRange(int x, int y) {
        Point point = new Point(x, y);
        for (Point[] pair : pairs) {
            Point sensor = pair[0];
            Point beacon = pair[1];
            if (getDist(point, sensor) <= getDist(sensor, beacon)) {
                return true;
            }
        }
        return false;
    }
    
    private static Point part2(List<Point[]> pairs, int row) {
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

//        System.out.print(row + ": ");
//        System.out.println(chars);
        
        for (int i = 0; i <= BOUND; i++) {
            char ch = chars[i];
            if (ch == '.') return new Point(i, row);
        }
        return null;
    }
    
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
    
    // parsing input file
    private static void parser(Scanner in) {
        Pattern pattern = Pattern.compile("x=(-?\\d+),\\s*y=(-?\\d+)");
        while (in.hasNext()) {
            Matcher matcher = pattern.matcher(in.nextLine());
            matcher.find();
            Point sensor = new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            matcher.find();
            Point beacon = new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
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
    
    static class PointComparator implements Comparator<Point> {
        @Override
        public int compare(Point a, Point b) {
            return a.x == b.x ? Integer.compare(a.y, b.y) : Integer.compare(a.x, b.x);
        }
    }
}
