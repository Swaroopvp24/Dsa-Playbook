# range-sum-query-2d-immutable

## attempt_1_bruteforce.java
*Style: concise*

### Study Notes: NumMatrix (Brute Force Approach)

**Purpose**
Provides a utility to calculate the sum of elements within a defined sub-rectangle of a 2D matrix. This implementation uses a naive brute-force approach with $O(M \times N)$ time complexity per query.

**Key Components**
*   `NumMatrix(int[][] matrix)`: Stores a reference to the input grid.
*   `sumRegion(int r1, int c1, int r2, int c2)`: Iterates through the specified sub-grid range and returns the accumulated sum.

**Notes**
*   **Performance:** This implementation is inefficient for frequent queries or large matrices. 
*   **Optimization Path:** For production scenarios involving many calls, this should be refactored to use a **2D Prefix Sum (Summed-Area Table)**. Pre-calculating a `dp[row+1][col+1]` table would allow for $O(1)$ query time at the cost of $O(M \times N)$ extra space.
*   **Edge Cases:** The current implementation assumes valid input coordinates (`r1 <= r2`, `c1 <= c2`); it lacks bounds checking or validation.

---
