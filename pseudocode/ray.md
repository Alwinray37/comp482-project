# Rays Pseudocode

## Problem
You are given a directed road network with n intersections and m one-way roads. Each road u → v has travel time w. A driver starts at intersection s and wants to reach intersection t. The driver owns one Priority Pass, which may be used on at most one road during the trip. If the pass is used on a road with travel time w, that road takes floor (w / 2) time instead of w. Compute the minimum possible travel time from s to t. The pass may be used on one road or not used at all. If t is unreachable from s, print -1.

Input Format

• First line: n m s t • Next m lines: u v w • Road u → v is directed and has travel time w

Constraints

• 1 ≤ n ≤ 2 × 10^5 • 1 ≤ m ≤ 3 × 10^5 • 1 ≤ w ≤ 10^9 • 1 ≤ s, t ≤ n • Expected solution: O((n + m) log n) or O(m log n)

Output Format

Print a single integer: the minimum travel time from s to t after using the Priority Pass on at most one road. If no route exists, print -1.