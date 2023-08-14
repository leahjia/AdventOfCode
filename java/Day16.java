import java.io.*;
import java.util.*;
import java.util.regex.*;

class Day16 {
    static Map<String, String[]> tunnels;
    static Map<String, Integer> rates;
    static final int MINUTES = 30;
    
    public static void main(String[] args) throws FileNotFoundException {
        parseFile(new Scanner(new FileReader("input/day16_sample.txt")));
        Set<String> open = new HashSet<>();
        Map<String, Set<String>> visit = new HashMap<>();
        System.out.println("Part I: " + findMostPress(visit, open, "AA", 0, 0)); // [, 2045]
    }
    
    private static int findMostPress(Map<String, Set<String>> visit, Set<String> open, String currValve, int pressure, int minutesPassed) {
        if (minutesPassed == MINUTES) return pressure;
        if (!open.contains(currValve) && rates.get(currValve) > 0) {
            open.add(currValve);
            System.out.println("opening valve " + currValve + " after minute " + (minutesPassed + 1) + " , adding " + (rates.get(currValve) * (MINUTES - minutesPassed)));
            System.out.println(open);
            int newPress = pressure + rates.get(currValve) * (MINUTES - minutesPassed - 1);
            int newMax = findMostPress(visit, open, currValve, newPress, minutesPassed + 2);
            pressure = Math.max(pressure, newMax);
        }
        // expect: DD -> BB -> JJ -> HH -> EE -> CC
        for (String nextValve : tunnels.get(currValve)) {
            if (visit.containsKey(currValve) && visit.get(currValve).contains(nextValve)) {
                continue;
            }
            Set<String> set = visit.getOrDefault(currValve, new HashSet<>());
            set.add(nextValve);
            visit.put(currValve, set);
            int newMax = findMostPress(visit, open, nextValve, pressure, minutesPassed + 1);
            pressure = Math.max(pressure, newMax);
        }
        return pressure;
    }
    
    private static void parseFile(Scanner in) {
        tunnels = new HashMap<>();
        rates = new HashMap<>();
        Pattern pat = Pattern.compile("Valve ([A-Z]+) has flow rate=(\\d+); tunnels? leads? to valves? (.*)");
        while (in.hasNext()) {
            String line = in.nextLine();
            Matcher mat = pat.matcher(line);
            if (mat.find()) {
                String valve = mat.group(1);
                rates.put(valve, Integer.parseInt(mat.group(2)));
                tunnels.put(valve, mat.group(3).split(", "));
            }
        }
    }
}
