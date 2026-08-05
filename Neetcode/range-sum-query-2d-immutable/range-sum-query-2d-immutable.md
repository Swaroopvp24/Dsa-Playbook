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

## attempt_2_math.java
*Style: detailed*

# Engineering Reference: 2D Prefix Sum (Summed-Area Table)

## Summary
The `NumMatrix` class implements a **2D Prefix Sum** (also known as a **Summed-Area Table**). This technique pre-calculates the sum of all elements within a rectangular region from the origin `(0,0)` to any point `(i, j)`. 

By storing these pre-calculated values in a secondary matrix of size `(M+1) x (N+1)`, the solution transforms a sub-matrix sum query from an $O(M \times N)$ operation into an $O(1)$ constant-time lookup. This is the optimal approach for scenarios involving a static matrix with high-frequency range queries.

---

## Complexity Analysis

### Time Complexity
*   **Initialization (`NumMatrix` constructor):** $O(M \times N)$, where $M$ is the number of rows and $N$ is the number of columns. We must visit each element exactly once to compute the cumulative sum.
*   **Query (`sumRegion`):** $O(1)$. Regardless of the size of the queried sub-matrix, the result is derived via exactly four array accesses and three arithmetic operations.

### Space Complexity
*   **Space:** $O(M \times N)$. The `prefix` matrix mirrors the dimensions of the input matrix (plus a padding row/column). This is the auxiliary space required to achieve $O(1)$ query time.

---

## Component Deep Dive

### 1. The `prefix` Matrix Padding
The implementation uses a `(mat.length + 1) x (mat[0].length + 1)` matrix. This **1-based padding** is a critical design pattern that eliminates boundary checks. By initializing the first row and column to `0` (which Java does implicitly), we handle the base cases (where the region starts at index `0`) without explicit `if` statements or conditional branches, keeping the inner loop tight and branch-prediction friendly.

### 2. Pre-calculation Logic
The recurrence relation used during construction is the Principle of Inclusion-Exclusion:
```java
prefix[i][j] = prefix[i - 1][j] + prefix[i][j - 1] - prefix[i - 1][j - 1] + mat[i - 1][j - 1];
```
*   `prefix[i-1][j] + prefix[i][j-1]`: Adds the regions above and to the left.
*   `- prefix[i-1][j-1]`: Subtracts the overlapping region that was added twice.
*   `+ mat[i-1][j-1]`: Adds the current cell value.

### 3. Query Logic (Inclusion-Exclusion)
The `sumRegion` method reverses the construction logic to isolate the target rectangle:
```java
return prefix[row2][col2] - prefix[row1 - 1][col2] - prefix[row2][col1 - 1] + prefix[row1 - 1][col1 - 1];
```
The adjustment `row1++`, `col1++`, etc., maps the 0-indexed input coordinates to the 1-indexed internal `prefix` table, shifting the boundary calculations safely into the padded region.

---

## Key Insights

### Performance Optimization
*   **Memory Locality:** The loops are ordered `i` then `j` (row-major order). Since Java stores 2D arrays as arrays-of-arrays (row-major), this ensures optimal cache hit rates during pre-calculation. 
*   **Branch Elimination:** The use of the `+1` padding row/column effectively removes conditional logic inside the `sumRegion` method. This is highly beneficial for CPU instruction pipelining.

### Subtle Considerations
*   **Integer Overflow:** The current implementation uses `int`. If the matrix contains large integers, the sum could exceed `Integer.MAX_VALUE`. In a production environment with potentially large values, `long[][]` should be preferred for the `prefix` table to prevent overflow.
*   **Mutability:** The implementation is not thread-safe. If the input `mat` is modified after instantiation, the `prefix` table will become stale. If the input is expected to be mutable, a deep copy should be taken in the constructor, or a synchronization mechanism must be introduced.
*   **Input Validation:** The code assumes a non-empty, rectangular matrix (no jagged arrays). In a defensive programming context, adding a check for `mat == null || mat.length == 0` is recommended to prevent `ArrayIndexOutOfBoundsException`.

---
