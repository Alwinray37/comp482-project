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
    print dist[t]
