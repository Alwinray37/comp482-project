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
        while (!minHeap.isEmpty()) { // while min-heap is not empty
            // extracting the intersection/node with minimum travel time from queue
            State current = minHeap.poll();
            int currentIntersection = current.intersection;
            int currentDistance = current.travelTime;
            boolean currentPPUsed = current.ppUsed; 

            // var check
            System.err.println("\nCurrent Intersection/node: " + currentIntersection + " Current Distance: " + currentDistance + " PP Used: " + currentPPUsed);

            // determining which distance array to check based on whether PP has been used
            int stateIndex = currentPPUsed ? 1 : 0;

            // skip if we already found a better path
            // This happens when the same intersection is in the queue multiple times
            if (currentDistance > distances[currentIntersection][stateIndex]) {
                continue;
            }

            // exploring all neghibors nodes of the current intersection/state
            List<Node> neighbors = roadNetwork.get(currentIntersection);
            // debug log
            System.err.println("  Exploring neighbors of intersection " + currentIntersection);

            // for each neighber in neighbors of current intersection
            for (Node neighbor : neighbors) {
                int neighborIntersection = neighbor.intersection;
                int neighborTravelTime = neighbor.travelTime;

                // current neighbor node path cost without using PP on this road
                int costWithoutPP = currentDistance + neighborTravelTime;

                // determine which state index to check for the next intersection based on whether PP has been used in the current path
                int nextStateIndex = currentPPUsed ? 1 : 0;

                // var check
                System.err.println("  Neighbor: " + neighborIntersection + " Neighbor Travel Time: " + neighborTravelTime + " Cost without PP: " + costWithoutPP);
                
                // if current path cost less than saved state 
                if(costWithoutPP < distances[neighborIntersection][nextStateIndex]) {
                    // if this path is better than the best known path to neighborIntersection without using PP
                    distances[neighborIntersection][nextStateIndex] = costWithoutPP;
                    minHeap.offer(new State(neighborIntersection, costWithoutPP, currentPPUsed));
                }

                // take this road with useing PP if we haven't used it yet
                if (!currentPPUsed) {
                    int discountedCost = (neighborTravelTime / 2); // using PP on this road
                    int costWithPP = currentDistance + discountedCost;

                    if(costWithPP < distances[neighborIntersection][1]) {
                        // if this path is better than the best known path to neighborIntersection with using PP
                        // update the distance for neighborIntersection with using PP
                        distances[neighborIntersection][1] = costWithPP;
                        minHeap.offer(new State(neighborIntersection, costWithPP, true));
                    }
                }
            }

            // debug log to show updated distances after processing current intersection
            System.err.println("  Updated distances:");
            for (int i = 1; i <= n; i++) {
                System.err.println("    dist[" + i + "][0] (no pass) = " + (distances[i][0] == Integer.MAX_VALUE ? "UNREACHABLE" : distances[i][0]) + " | dist[" + i + "][1] (with pass) = " + (distances[i][1] == Integer.MAX_VALUE ? "UNREACHABLE" : distances[i][1]));
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
        
        // update this to "Solution" before submitting
        RaySolution sol = new RaySolution();
        int result = sol.dijkstra(roadNetwork, n, s, t);
        System.out.println(result);
        sc.close();
    }
}
