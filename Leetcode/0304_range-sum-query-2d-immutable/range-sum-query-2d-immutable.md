# range-sum-query-2d-immutable

## attempt_1_prefixsumtable.java
*Style: detailed*

# Deep-Dive Reference: 2D Prefix Sum (Summed-Area Table)

## Summary
The `NumMatrix` class implements an efficient range-sum query solution using a **Summed-Area Table (2D Prefix Sum)**. By precomputing a transformation of the input matrix, we reduce the time complexity of arbitrary rectangular region sum queries from $O(N \times M)$ per query to $O(1)$ constant time. The algorithm treats the grid as a cumulative density function, where each cell `prefix[i][j]` stores the sum of all elements in the sub-rectangle originating from `(0,0)` to `(i-1, j-1)`.

## Complexity Analysis

### Time Complexity
*   **Initialization (`NumMatrix` constructor):** $O(N \times M)$, where $N$ is the number of rows and $M$ is the number of columns. We iterate through every cell once to perform constant-time inclusion-exclusion arithmetic.
*   **Query (`sumRegion`):** $O(1)$. Regardless of the size of the region defined by `(row1, col1)` to `(row2, col2)`, the result is derived via exactly four array lookups and three arithmetic operations.

### Space Complexity
*   **Storage:** $O((N+1) \times (M+1))$. We maintain an auxiliary matrix one row and one column larger than the original to serve as a padding (sentinel) layer. This eliminates conditional checks for boundary conditions (e.g., indices `< 0`), significantly streamlining the logic.

---

## Component Deep Dive

### 1. The Precomputation Logic
The recurrence relation used to build the prefix table is:
`prefix[i][j] = prefix[i-1][j] + prefix[i][j-1] - prefix[i-1][j-1] + mat[i-1][j-1]`

*   **Logic:** We add the sum of the top rectangle (`i-1, j`) and the left rectangle (`i, j-1`). Since the area at `(i-1, j-1)` is contained within both the top and left rectangles, it is counted twice. We subtract it once to correct the overcounting (Inclusion-Exclusion Principle).

### 2. The `sumRegion` Query Strategy
To extract the sum of a specific sub-rectangle defined by `(r1, c1)` and `(r2, c2)`, we perform the inverse of the precomputation:
`Result = prefix[r2+1][c2+1] - prefix[r1][c2+1] - prefix[r2+1][c1] + prefix[r1][c1]`

*   **Sentinel Pattern:** By padding the `prefix` array with an extra row and column of zeros, the logic naturally handles cases where `row1` or `col1` is `0`. When `row1=0`, the term `prefix[r1][...]` correctly resolves to `0` without needing an `if` block.

---

## Key Insights & Performance Nuances

### 1. Integer Overflow
*   **The Risk:** In Java, `int` is a 32-bit signed primitive. If the matrix contains large values or the grid is very large, the cumulative sum can exceed $2^{31}-1$.
*   **Recommendation:** If input constraints allow for large values, the `prefix` table should be promoted to `long[][]` to prevent overflow.

### 2. Cache Locality
*   **Memory Access:** The initialization loop iterates through `i` then `j` (row-major order). This aligns with Java's internal 2D array memory representation (an array of references to 1D arrays). This ensures sequential access to memory, which is highly cache-friendly. 

### 3. Edge Case Handling
*   **Empty Matrices:** The current implementation assumes `mat` is at least $1 \times 1$. If the matrix could be empty, the constructor would need a guard clause to return early or initialize a $1 \times 1$ empty array to avoid `NullPointerException` or `ArrayIndexOutOfBoundsException` when accessing `mat[0]`.

### 4. Implementation Subtleities
*   **Padding Efficiency:** Notice the loop starts at `i=1, j=1` and indexes `mat` at `i-1, j-1`. This is a clean, robust way to handle indices. Avoid trying to optimize out the padding; the slight memory overhead is significantly cheaper than the performance degradation introduced by branch-heavy `if-else` logic inside the inner loop or the query function.

---
