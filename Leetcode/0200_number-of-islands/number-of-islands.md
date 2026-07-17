# number-of-islands

## attempt_1.java
*Style: concise*

### Study Notes: Number of Islands (DFS)

#### Overview
This code calculates the number of connected components of '1's (islands) in a 2D grid using Depth First Search. It iterates through the grid and initiates a recursive traversal whenever an unvisited land cell is encountered.

#### Key Functions
*   **`numIslands(char[][] grid)`**: The entry point; iterates through every cell in the grid and triggers a DFS traversal when a new, unvisited land cell is found to count unique islands.
*   **`dfs(row, col, visited, grid)`**: A recursive helper that traverses all reachable land cells (up, down, left, right) from the current position and marks them as visited to prevent double-counting.

#### Key Logic
*   **State Management**: Uses a separate `boolean[][] visited` matrix to track state. Alternatively, one could modify the input `grid` in-place (changing '1' to '0') to save $O(M \times N)$ auxiliary space.
*   **Boundary Checking**: Explicitly checks array bounds (`row > 0`, `row < grid.length - 1`, etc.) before accessing adjacent cells to avoid `ArrayIndexOutOfBoundsException`.
*   **Connectivity**: The four-way movement logic ensures all contiguous '1's are identified as a single island during a single DFS recursion stack.

---

## attempt_2.java
*Style: concise*

### Notes: Number of Islands (DFS)

#### Overview
This code calculates the number of connected components ("islands") in a 2D grid of '1's (land) and '0's (water). It uses a standard Depth-First Search (DFS) approach to traverse and mark all nodes belonging to the same island.

#### Key Components
*   **`numIslands(char[][] grid)`**: Iterates through every cell in the grid. Triggers a `dfs` call only when an unvisited land cell ('1') is encountered, incrementing the island counter.
*   **`dfs(int row, int col, ...)`**: Recursively marks the current cell as visited and traverses adjacent cells (up, down, left, right) that are also land and unvisited.

#### Logic Highlights
*   **Traversal Pattern**: Uses direction arrays (`dr`, `dc`) to cleanly iterate through the 4-connectivity neighbors, avoiding redundant `if` or `switch` statements.
*   **Space Complexity**: Uses an auxiliary `boolean[][]` array to track state. 
    *   *Note:* Space can be optimized by mutating the input `grid` (replacing '1' with '0') if modification of the input is permissible.
*   **Base Case**: The recursive structure relies on the boundary and "unvisited/is-land" checks within the loop to implicitly handle recursion termination.

---

## attempt_3.java
*Style: concise*

### Number of Islands (DFS Approach)

This code calculates the number of distinct islands in a 2D grid of '1's (land) and '0's (water). It uses Depth-First Search to traverse and "sink" (mark as '0') all connected land pieces once they are discovered.

#### Key Components
*   `numIslands(char[][] grid)`: Iterates through every cell in the grid; initiates a DFS whenever an unvisited land cell ('1') is encountered and increments the island count.
*   `dfs(int row, int col, char[][] grid)`: Recursively visits all adjacent land cells (up, down, left, right) and flips them to '0' to mark them as processed.

#### Logic Notes
*   **In-place Mutation:** The code modifies the input `grid` directly to track visited cells, saving $O(N \times M)$ space by eliminating the need for a separate `boolean[][] visited` array.
*   **Space Complexity:** $O(N \times M)$ worst-case stack space due to recursion if the grid is entirely land.
*   **Connectivity:** Only horizontal and vertical connections are considered; diagonal connections do not join land masses.

---
