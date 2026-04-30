import java.util.*;
import java.io.*;

// using dijktras algo
public class RaySolution { // update this to "Solution" before submitting

    // node representing each vertex
    static class Node {
        int intersection; // value
        int travelTime; // weight

        Node(int intersection, int travelTime) {
            this.intersection = intersection;
            this.travelTime = travelTime;
        }
    }

    // dijktras algo
    public int dijkstra(List<List<Node>> roadNetwork, int n, int s, int t) {
        // this method will run dijkstras algorithm
        // n = number of intersections
        // s = starting intersection
        // t = destination intersection
        // roadNetwork = adjacency list of the directed road network
        // Returns minimum travel time from s to t
        System.out.println("\n=== DIJKSTRA'S ALGORITHM START ===");
        System.out.println("Starting intersection: " + s);
        System.out.println("Destination intersection: " + t);
        System.out.println("Number of intersections: " + n);

        // initializing the distance array
        int[] distances = new int[n + 1];

        // setting all distances to infinity
        for (int i = 0; i <= n; i++) {
            distances[i] = Integer.MAX_VALUE;
        }

        // distance from source to itself is 0
        distances[s] = 0;

        // logging checks
        System.out.println("\nInitialized distances array:");
        for (int i = 1; i <= n; i++) {
            if (distances[i] == Integer.MAX_VALUE) {
                System.out.println("  distances[" + i + "] = INFINITY");
            } else {
                System.out.println("  distances[" + i + "] = " + distances[i]);
            }
        }

        System.out.println("\nRoad network (adjacency list):");
        for (int i = 1; i <= n; i++) {
            System.out.print("  Intersection " + i + " -> ");
            if (roadNetwork.get(i).isEmpty()) {
                System.out.println("(no outgoing roads)");
            } else {
                for (Node neighbor : roadNetwork.get(i)) {
                    System.out.print("[" + neighbor.intersection + " (time: " + neighbor.travelTime + ")] ");
                }
                System.out.println();
            }
        }

        // creating a priority queue min-heap
        PriorityQueue<Node> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(a.travelTime, b.travelTime)
        );

        // add the starting node with dist 0
        minHeap.offer(new Node(s, 0));

        // main loop, process interections
        int iteration = 0;

        while (!minHeap.isEmpty()) { // while min-heap is not empty
            iteration++;
            System.out.println("\n--- Iteration " + iteration + " ---");

            // extracting the intersection with MINIMUM travel time from queue
            Node current = minHeap.poll();
            int currentIntersection = current.intersection;
            int currentDistance = current.travelTime;

            // log
            System.out.println("Processing intersection: " + currentIntersection);
            System.out.println("  Current shortest distance from " + s + " to " + currentIntersection + ": " + currentDistance);

            // skip if we already found a better path
            // This happens when the same intersection is in the queue multiple times
            if (currentDistance > distances[currentIntersection]) {
                System.out.println("SKIP: We already found a better path to " + currentIntersection + " (distance: " + distances[currentIntersection] + ")");
                continue;
            }

            System.out.println("  Checking outgoing roads from intersection " + currentIntersection + ":");

            List<Node> neighbors = roadNetwork.get(currentIntersection);
            if (neighbors.isEmpty()) {
                System.out.println("    (no outgoing roads)");
            } else {
                for (Node neighbor : neighbors) {
                    int nextIntersection = neighbor.intersection;
                    int roadTime = neighbor.travelTime;

                    // log
                    System.out.println("    -> Road to intersection " + nextIntersection + " (travel time: " + roadTime + ")");

                    // calculating the toatl travel time if we go through current intersetion
                    int newDistance = distances[currentIntersection] + roadTime;

                    // log
                    System.out.println("      Total time via " + currentIntersection + ": " + distances[currentIntersection] + " + " + roadTime + " = " + newDistance);
                    System.out.println("      Current best to " + nextIntersection + ": " +
                                    (distances[nextIntersection] == Integer.MAX_VALUE ? "INFINITY" : distances[nextIntersection]));

                    // relaxation, if we found a shorter paath, update
                    if (newDistance < distances[nextIntersection]) {
                        distances[nextIntersection] = newDistance;
                        minHeap.offer(new Node(nextIntersection, newDistance));
                    }
                }
            }
        }

        System.out.println("\n=== LOOP FINISHED ===");
        System.out.println("Final distances array:");
        for (int i = 1; i <= n; i++) {
            System.out.println("  distances[" + i + "] = " +
                            (distances[i] == Integer.MAX_VALUE ? "INFINITY (unreachable)" : distances[i]));
        }

        // return the result
        System.out.println("Looking for shortest distance to intersection " + t);
        if (distances[t] == Integer.MAX_VALUE) {
            System.out.println("Result: " + t + " is UNREACHABLE from " + s);
            System.out.println("Returning: -1");
            return -1;
        } else {
            System.out.println("Result: Shortest travel time from " + s + " to " + t + " is: " + distances[t]);
            System.out.println("Returning: " + distances[t]);
            return distances[t];
        }
    }

    // main func
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken()); // number of intersections/vertices
        int m = Integer.parseInt(st.nextToken()); // number of one way roads
        int s = Integer.parseInt(st.nextToken()); // start node
        int t = Integer.parseInt(st.nextToken()); // end node

        // building the adjecency list for the road network
        List<List<Node>> roadNetwork = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            roadNetwork.add(new ArrayList<>());
        }

        // read m roads and add to the roadNetwork
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken()); // from intersection u
            int v = Integer.parseInt(st.nextToken()); // to intersection v
            int w = Integer.parseInt(st.nextToken()); // with travel time w
            roadNetwork.get(u).add(new Node(v, w));
        }

        RaySolution sol = new RaySolution();
        int result = sol.dijkstra(roadNetwork, n, s, t);
        out.println(result);

        br.close();
        out.close();
    }
}
