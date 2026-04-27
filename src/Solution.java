import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        int n, m, s, t; //input variables: nodes count, roads count, start index, end index
        Scanner sc = new Scanner(System.in);
        String line1 = sc.nextLine();
        Scanner sc1 = new Scanner(line1);

        n = sc1.nextInt();
        m = sc1.nextInt();
        s = sc1.nextInt() - 1; //adjusting to zero index
        t = sc1.nextInt() - 1; //adjusting to zero index

        ArrayList<Intersection> nodes = new ArrayList<>(n); //holds nodes
        ArrayList<Edge> roads = new ArrayList<Edge>(m); //holds roads

        //populate array of nodes
        for (int i = 0; i < n; i++) {
            nodes.add(new Intersection()); //default to MAX_VALUE distance
        }

        //set start and ending intersections
        nodes.get(s).set_s(true);
        nodes.get(t).set_t(true);

        //populate array of roads by reading next input lines
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Scanner sc2 = new Scanner(line);
            int u = sc2.nextInt() - 1; //adjust to zero index
            int v = sc2.nextInt() - 1; //adjust to zero index
            int w = sc2.nextInt();
            roads.add(new Edge(u, v, w));
        }

        // relax edges
        for (Intersection i : nodes) { //repeat n times
            for (Edge e : roads) { //repeat m times
                int u = e.start;
                int v = e.end;
                int w = e.travel_time;
                if (nodes.get(u).distance != Integer.MAX_VALUE
                        && nodes.get(u).distance + w < nodes.get(v).distance) { //decide if it costs less to use this edge to reach v
                    nodes.get(v).distance = nodes.get(u).distance + w; //if so, set v's distance to use the edge
                    nodes.get(v).predecessor = u; //and record the path
                }
            }
        }

        //find maximum-cost edge in path, then update overall cost to halve it
        if (nodes.get(t).distance != Integer.MAX_VALUE){ //if we reached t
            int index = t, prev, max_distance = 0;
            do {
                prev = nodes.get(index).predecessor; //working back from t, find previous node
                for (Edge e : roads) { //find cost of edge from previous to current index
                    if (e.start == prev) {
                        if (e.end == index) {
                            if (e.travel_time > max_distance) {
                                max_distance = e.travel_time; //determine max edge length in path to t
                            }
                        }
                    }
                }
                index = prev;
            } while (index != s);
            
            //adjust distance to t by subtracting half of max distance
            nodes.get(t).distance -= max_distance / 2;
            if(max_distance % 2 != 0){
                nodes.get(t).distance --; //adjust so that the floor of the dividend is counted as cost of edge
            }

            System.out.println(nodes.get(t).distance); //display the min distance after applying prio pass
        }
        else {
            System.out.println("-1"); //if no path is found
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
