# Rays Pseudocode

## Problem
You are given a directed road network with n intersections and m one-way roads. Each road u -> v has travel time w. A driver starts at intersection s and wants to reach intersection t. The driver owns one Priority Pass, which may be used on at most one road during the trip. If the pass is used on a road with travel time w, that road takes floor (w / 2) time instead of w. Compute the minimum possible travel time from s to t. The pass may be used on one road or not used at all. If t is unreachable from s, print -1.

Input Format

• First line: n m s t • Next m lines: u v w • Road u -> v is directed and has travel time w

Constraints

• 1 ≤ n ≤ 2 × 10^5 • 1 ≤ m ≤ 3 × 10^5 • 1 ≤ w ≤ 10^9 • 1 ≤ s, t ≤ n • Expected solution: O((n + m) log n) or O(m log n)

Output Format

Print a single integer: the minimum travel time from s to t after using the Priority Pass on at most one road. If no route exists, print -1.

## Algorithm (Pseudocode)
PSEUDOCODE:
// Initialization
read n, m
read s, t
adj = list of lists, size n+1
for i from 1 to m:
    read u, v, w
    adj[u].append( (v, w) )   // directed edge u -> v

INF = very large number
dist[1..n] = INF
dist[s] = 0

pq = empty min-heap
push (0, s) into pq

// Main loop
while pq is not empty:
    (d, u) = pop from pq      // node with smallest distance so far

    if d > dist[u]:
        continue              // stale entry, skip

    // Relax all outgoing edges from u
    for each (v, w) in adj[u]:    // only u -> v, no reverse edge unless given
        newDist = d + w
        if newDist < dist[v]:
            dist[v] = newDist
            push (newDist, v) into pq

// Result for single-source single-target
if dist[t] == INF:
    print -1      // unreachable
else:
    print dist[t]\

<!-- Actual Code -->
```java
import java.io.*; 
import java.util.*;

// using dijktras algo 
public class Solution {
    // node representing each vertex
    static class Node {
        int intersection; // value
        int travelTime; // weight

        Node(int intersection, int travelTime){
            this.intersection = intersection;
            this.travelTime = travelTime;
        }
    }
    // dijktras algo
    public int dijkstra(List<List<Node>> roadNetwork, int n, int s, int t){
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
        int[] distances = new int[n+1];

        // setting all distances to infinity 
        for(int i = 0; i <=n; i++){
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

        while(!minHeap.isEmpty()){ // while min-heap is not empty
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
                for ( Node neighbor : neighbors){
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
                    if(newDistance < distances[nextIntersection]){
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
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // might need to change this ti STDIN

        int n = sc.nextInt(); // number of intersections/vertices
        int m = sc.nextInt(); // number of one way roads 
        int s = sc.nextInt(); // start node
        int t = sc.nextInt(); // end node

        // building the adjecency list for the road network
        List<List<Node>> roadNetwork = new ArrayList<>(); 
        for (int i = 0; i <= n; i++){
            roadNetwork.add(new ArrayList<>());
        }
        // read m roads and add to the roadNetwork
        for (int i = 0; i < m; i++){
            int u = sc.nextInt();      // from intersection u
            int v = sc.nextInt();      // to intersection v
            int w = sc.nextInt();      // with travel time w
            roadNetwork.get(u).add(new Node(v, w));
        }

        Solution sol = new Solution();
        int result = sol.dijkstra(roadNetwork, n, s, t);
        System.out.println(result);
    }
}
```

## Edge Cases

### Start is already the destination

Input:
```text
1 0 1 1
```

Expected output:
```text
0
```

Reason: No roads need to be taken, so the minimum travel time is 0.

### Destination is unreachable

Input:
```text
4 2 1 4
1 2 5
3 4 5
```

Expected output:
```text
-1
```

Reason: There is no directed path from 1 to 4.

### Priority Pass on an odd-weight road

Input:
```text
3 2 1 3
1 2 9
2 3 10
```

Expected output:
```text
14
```

Reason: Using the pass on weight 9 makes that road cost floor(9 / 2) = 4, so the route costs 4 + 10 = 14.

### Better route only after using the pass

Input:
```text
4 4 1 4
1 2 1
2 4 100
1 3 60
3 4 60
```

Expected output:
```text
51
```

Reason: The normal shortest path is 1 -> 2 -> 4 with cost 101, and using the pass on the 100 edge gives 1 + 50 = 51.

### Pass is optional

Input:
```text
2 1 1 2
1 2 1
```

Expected output:
```text
0
```

Reason: Using the pass on the only edge gives floor(1 / 2) = 0, which is better than not using it.

### Large weights need long integers

Input:
```text
3 2 1 3
1 2 1000000000
2 3 1000000000
```

Expected output:
```text
1500000000
```

Reason: The full route costs 2000000000, and the pass reduces one edge by 500000000.
