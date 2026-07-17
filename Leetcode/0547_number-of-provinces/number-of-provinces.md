# number-of-provinces

## attempt_1.java
*Style: concise*

### Study Notes: Number of Provinces (Connected Components)

#### Overview
This code calculates the number of connected components in an undirected graph represented by an adjacency matrix. It uses Depth First Search (DFS) to traverse and mark all nodes within each isolated subgraph.

#### Key Components
*   **`findCircleNum(int[][] isConnected)`**: Iterates through all nodes; triggers a DFS traversal for every unvisited node, incrementing a counter for each new component found.
*   **`dfs(int node, int[][] adj, boolean[] visited)`**: Recursively visits all reachable neighbors of a given node in the adjacency matrix and marks them as visited.

#### Implementation Notes
*   **Logic**: The `ct` (counter) only increments when the main loop encounters a node that hasn't been reached by a previous DFS call, effectively counting the number of independent "provinces."
*   **Time Complexity**: $O(n^2)$, where $n$ is the number of nodes, due to scanning the full adjacency matrix row for every node.
*   **Space Complexity**: $O(n)$ for the `visited` array and the recursion stack.

---
