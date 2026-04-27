import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        int n, m, s, t;
        Scanner sc = new Scanner(System.in);
        String line1 = sc.nextLine();
        Scanner sc1 = new Scanner(line1);

        n = sc1.nextInt();
        m = sc1.nextInt();
        s = sc1.nextInt() - 1;
        t = sc1.nextInt() - 1;

        ArrayList<Intersection> nodes = new ArrayList<>(n);
        ArrayList<Edge> roads = new ArrayList<Edge>(m);

        //populate array of intersections
        for (int i = 0; i < n; i++) {
            nodes.add(new Intersection());
        }

        //set start and ending intersections
        nodes.get(s).set_s(true);
        nodes.get(t).set_t(true);

        //populate array of roads
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Scanner sc2 = new Scanner(line);
            int u = sc2.nextInt() - 1;
            int v = sc2.nextInt() - 1;
            int w = sc2.nextInt();
            roads.add(new Edge(u, v, w));
        }

        for (Intersection i : nodes) {
            for (Edge e : roads) {
                int u = e.start;
                int v = e.end;
                int w = e.travel_time;
                // relax edges
                if (nodes.get(u).distance != Integer.MAX_VALUE
                        && nodes.get(u).distance + w < nodes.get(v).distance) {
                    nodes.get(v).distance = nodes.get(u).distance + w;
                    nodes.get(v).predecessor = u;
                }
            }
        }
        if (nodes.get(t).distance != Integer.MAX_VALUE){
            int index = t, prev, max_distance = 0;
            do {
                prev = nodes.get(index).predecessor;
                for (Edge e : roads) {
                    if (e.start == prev) {
                        if (e.end == index) {
                            if (e.travel_time > max_distance) {
                                max_distance = e.travel_time;
                            }
                        }
                    }
                }
                index = prev;
            } while (index != s);

            int count = 0;
            for (Edge e : roads) {
                if (e.travel_time == max_distance && count == 0) {
                    nodes.get(t).distance -= e.travel_time / 2;
                    if(e.travel_time % 2 != 0){
                        nodes.get(t).distance --;
                    }
                    count++;
                }
            }

            System.out.println(nodes.get(t).distance);
        }
        else {
            System.out.println("-1");
        }
    }
}
class Intersection {
    int distance, predecessor;
    boolean s, t;
    Intersection() {
        distance = Integer.MAX_VALUE;
    }
    void set_s(boolean start){
        s = start;
        if(s){
            distance = 0;
        }
    }
    void set_t(boolean end){
        t = end;
    }
}
class Edge {
    int start, end, travel_time;
    boolean isMax;
    Edge(int start, int end, int travel_time) {
        this.start = start;
        this.end = end;
        this.travel_time = travel_time;
    }
}
