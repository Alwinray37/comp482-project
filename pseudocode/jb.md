1.Setup
  2. Define two classes: Intersections and Edges to represent vertices and edges
  3. Create two arrayLists to store the intersections and edges given by the input
  4. Read the input into the lists, initializing the distance to each intersection as MAX_VALUE
  5. Mark the source and destination intersection, and set the distance to the source intersection to zero
6. Relax edges to find the shortest route to each intersection
  7. Iterate over all intersections, repeating the process n times
    8. Iterate through all edges
      9. If it's shorter to use the edge, do so
              10. Determine which is shorter: the current distance to the end point 
                        OR the distance to the start of the edge plus the weight of the edge 
              11. Update the distance to the end point to be the shorter of the above comparison
              12. Set the predecessor of the end to be the beginning of the edge
10. Determine whether a route was found to the destination. If so,
  11. Find the longest edge in the route
    12. Trace the path of predecessors. For each edge between an intersection and its predecessors
      13. Loop through the edges searching for an edge with these start and end points
        14. Once found, determine if the associated edge weight is higher than the current maximum
          15. If so, update maximum edge weight variable
  16. Update the distance to the destination to halve the weight of the edge with the max weight
  17. Output the updated distance to the destination
18. Otherwise output -1  
