import java.util.*;

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

        RaySolution sol = new RaySolution();
        int result = sol.dijkstra(roadNetwork, n, s, t);
        System.out.println(result);
        sc.close();
    }
}
