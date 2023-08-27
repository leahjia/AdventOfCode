import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.regex.*;

class Day16 {
    static Map<String, String[]> tunnels;              // <valve, outgoing branches>
    static Map<String, Integer> rates;                 // <valve, pressure>
    static Map<String, Map<String, Integer>> distance; // <from, <to, distance>>
    static final String SRC = "AA";
    static final int MINUTES = 30;
    
    public static void main(String[] args) throws FileNotFoundException {
        parseFile(new Scanner(new FileReader("input/day16_sample.txt")));
        
        bfs(); // calculate shortest distances from every valve to every other valve
        System.out.println("<from, <to, distance>>: " + distance);
        Set<String> open = new HashSet<>();
        // set useless valves to be open by default
        for (Map.Entry<String, Integer> valve : rates.entrySet()) {
            if (valve.getValue() == 0) {
                open.add(valve.getKey());
            }
        }
        long startTime = System.currentTimeMillis();
        System.out.println("Part I: " + findMostPress(open, SRC, 0, 0)); // [, 2045]
        System.out.println("Execution time: " + (System.currentTimeMillis() - startTime) + "ms");
    }
    
    private static void bfs() {
        distance = new TreeMap<>();
        for (String start : rates.keySet()) {
            // skip useless valves except the initial starting point
            if (rates.get(start) == 0 && !start.equals(SRC)) continue;
            Set<String> visit = new HashSet<>();
            visit.add(start);
            
            Queue<Pair<Integer, String>> queue = new LinkedList<>();
            queue.add(new Pair<>(0, start));
            
            while (!queue.isEmpty()) {
                Pair<Integer, String> valve = queue.poll();
                int dist = valve.getKey() + 1;
                String from = valve.getValue();
                
                for (String to : tunnels.get(from)) {
                    if (!visit.contains(to)) {
                        visit.add(to);
                        if (rates.get(to) > 0) { // only compute useful valves
                            Map<String, Integer> outgoing = distance.getOrDefault(start, new TreeMap<>());
                            outgoing.put(to, dist);
                            distance.put(start, outgoing);
                        }
                        queue.add(new Pair<>(dist, to));
                    }
                }
            }
        }
    }
    
    Map<String, Map<String, String>> cache;
    
    private static int findMostPress(Set<String> open, String valve, int pressure, int minute) {
        if (minute >= MINUTES) return minute > MINUTES ? 0 : pressure;
        int max = pressure;
        for (String outgoing : distance.get(valve).keySet()) {
            if (open.contains(outgoing)) continue;
            
            minute += distance.get(valve).get(outgoing); // plus time to get there
            
            open.add(outgoing);
            int newPress = rates.get(outgoing) * (MINUTES - minute - 1);
            int openValve = findMostPress(open, valve, pressure + newPress, minute + 1);
            max = Math.max(max, openValve);
            open.remove(outgoing);
            
            int notOpen = findMostPress(open, valve, pressure, minute);
            max = Math.max(max, notOpen);
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
