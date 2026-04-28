input: n (intersection), 
       m (roads), 
       s (start intersection), 
       t (destination intersection)

create graph using adjacency list
each road: u -> v with travel time w

distance table:

create dist[n][2]
//each interseciton has 2 values:
//set all dist to inf, since we know any best paths yet
dist[i][0] = inf //without using priority pass
dist[i][1] = inf //after using priority pass

dist[s][0] = 0 //time=0, priority pass not used

create priority queue (to always process smallest travel time first)
//first state: at s, time: 0, pass not used
inset (0,s,0) 

//main loop
while pq is not empty: //explore all possible paths
//intersecion u, travel time:time, used: 0 or 1
(time, u, used) = remove smallest from pq 

if time > dist[u][used]:
continue

for each rode u -> v with travel time w:
//no priority pass
newTime = time + w;

if newTime < dist[v][used]:
dist[v][used] = newTime
insert (newTime, v, used) into pq

//use priority pass
if used == 0;
//reduce travel time of this road
discountedTime = time + (w/2)

//update dist[v][1]
if discountedTime < dist[v][1]
dist[v][1] = discountedTime
insert (discountedTime, v, 1) into pq

answer = min(dist[t][0], dist[t][1])

if (answer is inf){ 
	print -1
} else {
	print answer
}



test cases:
Case1

4 4 1 4
1 2 100
2 4 1 
1 3 50
3 4 50


2 routes:
1 -> 2 -> 4  (100 + 1 = 101)

1 -> 3 -> 4 (50 + 50 = 100)

pass:
(100/2)=50 
=> 50 + 1 = 51

Case2:

3 2 1 3
1 2 10
2 3 10

only path:
1 -> 2 -> 3 (10 + 10 = 20)
pass:

(10/2)= 5 
=> 5 + 10 = 20


Case3:

4 2 1 4
1 2 5 
3 4 5

1 -> 2
3 -> 4

no path 
-1

Case4:
1 0 1 1

no travel needed
0


