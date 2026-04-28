# Rays Pseudocode

## Problem
You are given a directed road network with n intersections and m one-way roads. Each road u → v has travel time w. A driver starts at intersection s and wants to reach intersection t. The driver owns one Priority Pass, which may be used on at most one road during the trip. If the pass is used on a road with travel time w, that road takes floor (w / 2) time instead of w. Compute the minimum possible travel time from s to t. The pass may be used on one road or not used at all. If t is unreachable from s, print -1.

Input Format

• First line: n m s t • Next m lines: u v w • Road u → v is directed and has travel time w

Constraints

• 1 ≤ n ≤ 2 × 10^5 • 1 ≤ m ≤ 3 × 10^5 • 1 ≤ w ≤ 10^9 • 1 ≤ s, t ≤ n • Expected solution: O((n + m) log n) or O(m log n)

Output Format

Print a single integer: the minimum travel time from s to t after using the Priority Pass on at most one road. If no route exists, print -1.

## Algorithm (Pseudocode)

We model each intersection with two states: pass not used (layer 0) and pass used (layer 1).
Run Dijkstra on the 2-layer graph of size 2*n.

PSEUDOCODE:

1. Read n, m, s, t
2. Create adjacency list: adj[u] = list of (v, w) for each directed edge u->v with weight w

3. Let INF = large (e.g., 10^18)
4. Create dist[2][n+1] and initialize all to INF
5. dist[0][s] = 0   // start at s with pass unused

6. Priority queue PQ of tuples (distance, node, used) ordered by distance
7. push (0, s, 0) into PQ

8. while PQ not empty:
	 d, u, used = PQ.pop()
	 if d > dist[used][u]:
		 continue
	 for each (v, w) in adj[u]:
		 // 1) travel without using pass
		 if dist[used][v] > d + w:
			 dist[used][v] = d + w
			 PQ.push(dist[used][v], v, used)
		 // 2) if pass not yet used, consider using it on this road
		 if used == 0:
			 newd = d + floor(w / 2)
			 if dist[1][v] > newd:
				 dist[1][v] = newd
				 PQ.push(newd, v, 1)

9. answer = min(dist[0][t], dist[1][t])
10. if answer == INF: print -1 else print answer

Notes:
- Use 64-bit integers for distances.
- floor(w/2) is integer division: w / 2.
- Time complexity: O((n + m) log n) using binary heap.