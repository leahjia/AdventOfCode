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
        System.out.println("Part I: " + findMostPress(new HashMap<>(), open, "AA", 0, 0)); // [886, 2045]
    }
    
    private static int findMostPress(Map<String, Set<String>> visit, Set<String> open, String currValve, int pressure, int minute) {
        if (minute >= MINUTES) return pressure;
        int rate = rates.get(currValve);
        int temp = pressure;
        
        if (!open.contains(currValve) && rate > 0) {
            // open valve
            open.add(currValve);
            minute++; // plus 1 for opening
            int minutesLeft = MINUTES - minute;
            int gain = rate * minutesLeft;
            temp += gain;
            System.out.println("opening valve " + currValve + " after minute " + minute + ", adding " + (rate + " * " + minutesLeft) + " = " + gain + ", curr press: " + temp + "\n" + open);
            pressure = Math.max(pressure, Math.max(temp, dfs(visit, open, currValve, temp, minute)));
            // not open
            open.remove(currValve);
            minute--;
        }
        pressure = Math.max(pressure, Math.max(temp, dfs(visit, open, currValve, pressure, minute)));
        return pressure;
    }
    
    private static int dfs(Map<String, Set<String>> visit, Set<String> open, String currValve, int currPress, int minute) {
        if (minute >= MINUTES) return currPress;
        int max = currPress;
        System.out.println(currValve + " going out to " + Arrays.toString(tunnels.get(currValve)) + " at min " + minute);
        // expect: DD -> BB -> JJ -> HH -> EE -> CC
        for (String nextValve : tunnels.get(currValve)) {
            if (visit.containsKey(currValve) && visit.get(currValve).contains(nextValve)) continue;
            System.out.println(currValve + " going out to " + nextValve);
            Set<String> set = visit.getOrDefault(currValve, new HashSet<>());
            set.add(nextValve);
            visit.put(currValve, set);
            max = Math.max(max, findMostPress(visit, open, nextValve, currPress, minute + 1));
        }
        return max;
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