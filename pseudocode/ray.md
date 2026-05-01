# Rays Pseudocode

## Problem
You are given a directed road network with n intersections and m one-way roads. Each road u -> v has travel time w. A driver starts at intersection s and wants to reach intersection t. The driver owns one Priority Pass, which may be used on at most one road during the trip. If the pass is used on a road with travel time w, that road takes floor (w / 2) time instead of w. Compute the minimum possible travel time from s to t. The pass may be used on one road or not used at all. If t is unreachable from s, print -1.

Input Format

- First line: n m s t 
- Next m lines: u v w 
- Road u -> v is directed and has travel time w

Constraints

- 1 ≤ n ≤ 2 × 10^5 
- 1 ≤ m ≤ 3 × 10^5 
- 1 ≤ w ≤ 10^9 • 1 ≤ s, t ≤ n 
- Expected solution: O((n + m) log n) or O(m log n)

Output Format

Print a single integer: the minimum travel time from s to t after using the Priority Pass on at most one road. If no route exists, print -1.

## Approach
- using modified Dijkstra's Algo that tracks two versions of every intersection/path
  - Without Priority Pass (PP)
  - With Priority Pass 

Initialization:
- set distance[s][0] = 0 -> starting node with cost 0
- every other node has MAX_VALUE to represent infinity
- add starting state to priority queue 

Main Loop:
- process every reachable state until the heap is empty 
- pull the state with lowest travel time
- stale check -> if current node's travelTime is greater than distance saved, then continue. this means we went through this already
- For each neighbor:
  - go to neighbor without using PP, update node total path distance 
  - go to neighbor with using PP, update node total path distance

Return the answer: minimum(distances[target node][0], distances[target node][1])

## Algorithm (Pseudocode)

PSEUDOCODE:

```bash 
CLASSES:
    Node(intersection, travelTime)           // represents a directed road/edge
    State(intersection, travelTime, ppUsed)  // represents a position + pass status in the heap


DIJKSTRA(roadNetwork, n, s, t):

    // log input variables (debug)
    print n, s, t
    print adjacency list for each intersection 1..n

    // 2D distance table
    // distances[i][0] = best cost to reach intersection i WITHOUT having used PP
    // distances[i][1] = best cost to reach intersection i AFTER having used PP
    distances[0..n][0..1] = INFINITY
    distances[s][0] = 0

    minHeap = priority queue ordered by travelTime ascending
    minHeap.push(State(intersection=s, travelTime=0, ppUsed=false))

    iteration = 0

    while minHeap is not empty:
        iteration++
        current = minHeap.pop()                        // extract lowest cost state
        stateIndex = current.ppUsed ? 1 : 0

        // stale check: skip if a cheaper path for this (intersection, ppUsed) was already processed
        if current.travelTime > distances[current.intersection][stateIndex]:
            continue

        for each neighbor of current.intersection:
            roadTime = neighbor.travelTime

            // Option 1: travel this road WITHOUT using PP
            nextStateIndex = current.ppUsed ? 1 : 0   // pass status unchanged
            costWithoutPP = current.travelTime + roadTime
            if costWithoutPP < distances[neighbor.intersection][nextStateIndex]:
                distances[neighbor.intersection][nextStateIndex] = costWithoutPP
                minHeap.push(State(neighbor.intersection, costWithoutPP, current.ppUsed))

            // Option 2: use PP on this road (only available if PP not yet used)
            if not current.ppUsed:
                discountedCost = roadTime / 2          // floor division
                costWithPP = current.travelTime + discountedCost
                if costWithPP < distances[neighbor.intersection][1]:
                    distances[neighbor.intersection][1] = costWithPP
                    minHeap.push(State(neighbor.intersection, costWithPP, ppUsed=true))

    resultNoPP   = distances[t][0]
    resultWithPP = distances[t][1]

    // log final distances (debug)
    print resultNoPP, resultWithPP

    best = min(resultNoPP, resultWithPP)
    return best == INFINITY ? -1 : best


MAIN:
    read n, m, s, t

    roadNetwork = adjacency list of size n+1, all empty (1-indexed)
    for each of m roads:
        read u, v, w
        roadNetwork[u].add(Node(intersection=v, travelTime=w))

    result = DIJKSTRA(roadNetwork, n, s, t)
    print result
```

<!-- Actual Code -->
```java
import java.util.*;
import java.io.*;

// using dijktras algo
public class Solution { // update this to "Solution" before submitting

    // node representing each vertex
    static class Node {
        int intersection; // value
        int travelTime; // weight

        Node(int intersection, int travelTime) {
            this.intersection = intersection;
            this.travelTime = travelTime;
        }
    }

    // state to track Priority Pass (PP) has been used
    static class State {
        int intersection; // current intersection
        int travelTime; // total travel time to reach this intersection
        boolean ppUsed; // whether PP has been used

        State(int intersection, int travelTime, boolean ppUsed) {
            this.intersection = intersection;
            this.travelTime = travelTime;
            this.ppUsed = ppUsed;
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

        // confirm input variables
        System.err.println("Input Variables:");
        System.err.println("  n (intersections) = " + n);
        System.err.println("  s (start) = " + s);
        System.err.println("  t (end) = " + t);

        // confirm adjacency list
        System.err.println("[Adjacency List]");
        for (int i = 1; i <= n; i++) {
            System.err.print("  " + i + " -> ");
            if (roadNetwork.get(i).isEmpty()) {
                System.err.println("(none)");
            } else {
                for (Node neighbor : roadNetwork.get(i)) {
                    System.err.print(neighbor.intersection + "(w:" + neighbor.travelTime + ") ");
                }
                System.err.println();
            }
        }

        // initializing the distance array
        // distances[intersection][passUsed]
        // distances[i][0] = best cost to reach i WITHOUT using pass
        // distances[i][1] = best cost to reach i AFTER using pass
        int[][] distances = new int[n + 1][2];

        // setting all distances to infinity
        for (int i = 0; i <= n; i++) {
            distances[i][0] = Integer.MAX_VALUE;
            distances[i][1] = Integer.MAX_VALUE;
        }

        // distance from source to itself is 0
        distances[s][0] = 0;

        // creating a priority queue min-heap
        // the queue will store states, and we want to prioritize states with lower travel time
        PriorityQueue<State> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(a.travelTime, b.travelTime)
        );

        // add the starting node with dist 0
        minHeap.offer(new State(s, 0, false));

        // main loop, process interections
        int iteration = 0;

        while (!minHeap.isEmpty()) { // while min-heap is not empty
            iteration++;

            // extracting the intersection with minimum travel time from queue
            State current = minHeap.poll();
            int currentIntersection = current.intersection;
            int currentDistance = current.travelTime;
            boolean currentPPUsed = current.ppUsed; 

            // determining which distance array to check based on whether PP has been used
            int stateIndex = currentPPUsed ? 1 : 0;

            // skip if we already found a better path
            // This happens when the same intersection is in the queue multiple times
            if (currentDistance > distances[currentIntersection][stateIndex]) {
                // System.err.println("[ITER " + iteration + "] SKIP intersection " + currentIntersection + " (stale cost " + currentDistance + ", best is " + distances[currentIntersection][stateIndex] + ")");
                continue;
            }

            // exploring all neghibors of the current intersection
            List<Node> neighbors = roadNetwork.get(currentIntersection);
            for (Node neighbor : neighbors) {
                int nextIntersection = neighbor.intersection;
                int roadTime = neighbor.travelTime;

                // calculating the toatl travel time if we go through current intersetion
                int costWithoutPP = currentDistance + roadTime;
                int nextStateIndex = currentPPUsed ? 1 : 0;
                
                if(costWithoutPP < distances[nextIntersection][nextStateIndex]) {
                    // if this path is better than the best known path to nextIntersection without using PP
                    distances[nextIntersection][nextStateIndex] = costWithoutPP;
                    minHeap.offer(new State(nextIntersection, costWithoutPP, currentPPUsed));
                }

                // take this road with useing PP if we haven't used it yet
                if (!currentPPUsed) {
                    int discountedCost = (roadTime / 2); // using PP on this road
                    int costWithPP = currentDistance + discountedCost;

                    if(costWithPP < distances[nextIntersection][1]) {
                        // if this path is better than the best known path to nextIntersection with using PP
                        distances[nextIntersection][1] = costWithPP;
                        minHeap.offer(new State(nextIntersection, costWithPP, true));
                    }
                }
            }
        }

        // result 
        int resultWithNoPP = distances[t][0];
        int resultWithPP = distances[t][1];

        // confirm final result
        System.err.println("  dist[" + t + "][0] (no pass) = " + (resultWithNoPP == Integer.MAX_VALUE ? "UNREACHABLE" : resultWithNoPP));
        System.err.println("  dist[" + t + "][1] (with pass) = " + (resultWithPP == Integer.MAX_VALUE ? "UNREACHABLE" : resultWithPP));
        
        // return the result
        return Math.min(resultWithNoPP, resultWithPP) == Integer.MAX_VALUE ? -1 : Math.min(resultWithNoPP, resultWithPP);
    }

    // main function
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt(); // number of intersections/vertices
        int m = sc.nextInt(); // number of one way roads
        int s = sc.nextInt(); // start node
        int t = sc.nextInt(); // end node

        // building the adjecency list for the road network
        List<List<Node>> roadNetwork = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            roadNetwork.add(new ArrayList<>());
        }

        // read m roads and add to the roadNetwork
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt(); // from intersection u
            int v = sc.nextInt(); // to intersection v
            int w = sc.nextInt(); // with travel time w
            roadNetwork.get(u).add(new Node(v, w));
        }

        Solution sol = new Solution();
        int result = sol.dijkstra(roadNetwork, n, s, t);
        System.out.println(result);
        sc.close();
    }
}

```

## Edge Cases

```bash 
4 4 1 4 
1 2 100 
2 4 1 
1 3 50 
3 4 50

```

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
