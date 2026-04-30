import java.util.*;

public class RaySolution {

    static class Node {
        int intersection;
        int travelTime;

        Node(int intersection, int travelTime) {
            this.intersection = intersection;
            this.travelTime = travelTime;
        }
    }

    public int dijkstra(List<List<Node>> roadNetwork, int n, int s, int t) {
        int[] distances = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        distances[s] = 0;

        PriorityQueue<Node> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(a.travelTime, b.travelTime)
        );
        minHeap.offer(new Node(s, 0));

        while (!minHeap.isEmpty()) {
            Node current = minHeap.poll();
            int currentIntersection = current.intersection;
            int currentDistance = current.travelTime;

            if (currentDistance > distances[currentIntersection]) {
                continue;
            }

            for (Node neighbor : roadNetwork.get(currentIntersection)) {
                int nextIntersection = neighbor.intersection;
                int roadTime = neighbor.travelTime;
                int newDistance = distances[currentIntersection] + roadTime;

                if (newDistance < distances[nextIntersection]) {
                    distances[nextIntersection] = newDistance;
                    minHeap.offer(new Node(nextIntersection, newDistance));
                }
            }
        }

        if (distances[t] == Integer.MAX_VALUE) {
            return -1;
        } else {
            return distances[t];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();
        int s = sc.nextInt();
        int t = sc.nextInt();

        List<List<Node>> roadNetwork = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            roadNetwork.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            int w = sc.nextInt();
            roadNetwork.get(u).add(new Node(v, w));
        }

        RaySolution sol = new RaySolution();
        int result = sol.dijkstra(roadNetwork, n, s, t);
        System.out.println(result);
        sc.close();
    }
}
