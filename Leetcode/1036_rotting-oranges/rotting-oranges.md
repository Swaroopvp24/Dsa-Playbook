# rotting-oranges

## attempt_1.java
*Style: detailed*

# Engineering Deep-Dive: Multi-Source Breadth-First Search for Grid Propagation

## Summary
The solution implements a **Multi-Source Breadth-First Search (BFS)** to simulate the diffusion of a "rotting" state across a grid. By treating all initially rotten oranges (state `2`) as starting nodes in a graph, the algorithm expands layer-by-layer. This approach is optimal for finding the shortest path/time in an unweighted grid where edges represent unit time (1 minute) transitions between adjacent cells.

## Complexity Analysis

### Time Complexity: $O(N \times M)$
*   **Grid Initialization:** We perform a single pass over the grid of dimensions $N \times M$ to populate the queue and count fresh oranges.
*   **BFS Traversal:** Each cell is added to the queue at most once. For each cell, we inspect its 4 neighbors. Since each cell is processed constant times, the complexity scales linearly with the total number of cells.
*   **Total:** $O(N \times M)$.

### Space Complexity: $O(N \times M)$
*   **Queue Storage:** In the worst-case scenario (e.g., a grid full of rotten oranges), the `ArrayDeque` stores $O(N \times M)$ coordinates.
*   **In-place Mutation:** The algorithm modifies the input `grid` array to track state (`2` for rotten), avoiding the need for a auxiliary `visited` matrix. Thus, space is dominated strictly by the BFS queue.

---

## Component Deep Dive

### 1. Multi-Source Initialization
Unlike standard BFS starting from a single node, this implementation initializes the queue with *every* rotten orange simultaneously. This is the critical design choice that ensures we calculate the time from the *nearest* source, inherently satisfying the requirement for the shortest time to rot all oranges.

### 2. State Propagation Logic
*   **Fresh Count Tracking:** The `fresh` variable acts as a gatekeeper. By decrementing this counter exactly when a node transitions from `1` to `2`, we avoid an expensive $O(N \times M)$ scan at the end of the algorithm.
*   **In-place State Mutation:** Setting `grid[nr][nc] = 2` serves dual purposes: 
    1. It effectively acts as a `visited` flag to prevent cycles or redundant queue insertions.
    2. It updates the grid state for potential future iterations within the same BFS level.

### 3. Layer-Based Termination
*   The `size = queue.size()` loop is the "Level-Order" mechanism. It processes all nodes at the current distance `d` before incrementing `min`.
*   **Edge Case:** The `if (!queue.isEmpty()) min++` check ensures we do not increment the timer if the last level of rotting occurs exactly when no further fresh oranges are reachable.

---

## Key Insights & Performance Nuances

### 1. The "Fresh" Remaining Check
The final return `fresh == 0 ? min : -1` is the canonical way to detect if the graph is disconnected. If `fresh > 0` after the queue exhausts, it implies that the remaining fresh oranges were geographically isolated from the rotten sources (e.g., trapped by empty cells/walls).

### 2. Space Optimization
If the input grid size is large and memory is constrained, consider packing two 16-bit integers (row/col) into a single 32-bit `int` before insertion into the `ArrayDeque`. This avoids the allocation overhead of the `int[]` object per coordinate, which can cause significant GC pressure in extremely large grids.

### 3. Edge-Case Handling
*   **Empty Grid/No Fresh Oranges:** The code correctly returns `0`. The `fresh` variable initializes to 0, the loop never triggers the `min++` condition, and the ternary operator evaluates correctly.
*   **No Rotten Oranges:** If `fresh > 0` and the queue is empty, the logic returns `-1` (unless `fresh` is also `0`), correctly identifying that rot cannot propagate if no initial source exists.

### 4. Subtle Pitfalls
*   **Input Modification:** This solution modifies the input array. In a production system or a multi-threaded context, ensure this is permissible. If the state must be preserved, a `boolean[][] visited` matrix is required, increasing space complexity to $O(N \times M)$.
*   **Queue Choice:** The use of `ArrayDeque` is strictly superior to `LinkedList` in Java for BFS, as `LinkedList` incurs higher overhead due to node object allocation for each entry.

---

## attempt_1_modified.java
*Style: concise*

### Notes: Rotting Oranges (BFS)

**Overview**
Calculates the minimum time required for all fresh oranges to rot using a multi-source Breadth-First Search (BFS). Each minute represents one layer of expansion from all currently rotten oranges.

**Key Components**
*   **`Queue<int[]> queue`**: Stores coordinates of rotten oranges to process level-by-level.
*   **`fresh` counter**: Tracks the count of fresh oranges; allows for $O(1)$ verification of completion at the end.
*   **`dr`, `dc` arrays**: Standard directional vectors for grid traversal (up, down, left, right).

**Logic to Remember**
*   **Level-order BFS**: The `for (int i = 0; i < size; i++)` loop is critical—it ensures we increment the `min` timer only after all oranges at the current "minute" have finished infecting their neighbors.
*   **State Mutation**: The input `grid` is updated in-place (changing `1` to `2`) to act as a "visited" set, saving space.
*   **Corner Cases**:
    *   No fresh oranges initially $\rightarrow$ returns `0`.
    *   Fresh oranges exist but are unreachable (no initial rotten ones) $\rightarrow$ returns `-1`.
    *   After BFS, if `fresh > 0`, some oranges were trapped $\rightarrow$ returns `-1`.

---
