import javafx.util.Pair;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

class Day16 {
    static Map<String, String[]> tunnels; // <valve, outgoing branches>
    static Map<String, Integer> rates;    // <valve, pressure>
    static Map<String, Map<String, Integer>> distance; // <from, <to, distance>>
    static final String START = "AA";
    static final int MINUTES = 30;
    
    public static void main(String[] args) throws FileNotFoundException {
        parseFile(new Scanner(new FileReader("input/day16_sample.txt")));
        
        bfs(); // calc shortest distance from every valve to every other valve
        System.out.println(distance);
        Set<String> open = new HashSet<>();
        for (Map.Entry<String, Integer> valve : rates.entrySet()) {
            if (valve.getValue() == 0) {
                open.add(valve.getKey());
            }
        }
        System.out.println("Part I: " + findMostPress(open, START, 0, 0)); // [, 2045]
    }
    
    private static void bfs() {
        distance = new TreeMap<>();
        for (String start : rates.keySet()) {
            Set<String> visit = new HashSet<>();
            visit.add(start);
            Queue<Pair<Integer, String>> queue = new LinkedList<>();
            queue.add(new Pair<>(0, start));
            while (!queue.isEmpty()) {
                Pair<Integer, String> valve = queue.poll();
                int dist = valve.getKey() + 1;
                String from = valve.getValue();
                for (String to : tunnels.get(from)) {
                    if (visit.contains(to)) continue;
                    visit.add(to);
                    // if (rates.get(to) > 0) { // only compute useful valves
                    Map<String, Integer> outgoing = distance.getOrDefault(start, new TreeMap<>());
                    outgoing.put(to, dist);
                    distance.put(start, outgoing);
                    // }
                    queue.add(new Pair<>(dist, to));
                }
            }
        }
    }
    
    private static int findMostPress(Set<String> open, String valve, int pressure, int minute) {
        if (minute >= MINUTES) return pressure;
        
        if (rates.get(valve) == 0) open.add(valve);
        boolean valveOpened = open.contains(valve);
        int maxPress = pressure;
        
        if (!valveOpened) {
            open.add(valve);
            for (String outgoing : tunnels.get(valve)) {
                int openValve = rates.get(valve) * (MINUTES - minute);
                int newPress = pressure + openValve;
                newPress = Math.max(newPress, findMostPress(open, outgoing, newPress, minute + 1));
                maxPress = Math.max(maxPress, newPress);
            }
            // backtrack
            open.remove(valve);
        } else {
            for (String outgoing : tunnels.get(valve)) {
                maxPress = Math.max(maxPress, findMostPress(open, outgoing, pressure, minute + 1));
            }
        }
        return maxPress;
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
