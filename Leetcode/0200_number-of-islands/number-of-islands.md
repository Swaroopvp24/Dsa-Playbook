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
