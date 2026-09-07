# search-a-2d-matrix

## standard_binary_search.java
*Style: detailed*

# Engineering Deep-Dive: 2D Matrix Binary Search

## Summary
The solution implements a **two-stage logarithmic search** strategy to locate a target within a row-sorted and column-sorted matrix. Given the constraints typical of this problem—where the last element of row $i$ is less than the first element of row $i+1$—the matrix effectively behaves as a flattened sorted array. 

The algorithm first performs a binary search on the vertical axis (rows) to identify the specific row that could contain the target by checking the row's boundaries (index `0` and `lastColumn`). Once the candidate row is identified, it executes a secondary binary search on the horizontal axis (columns) to confirm the existence of the target value.

## Complexity Analysis

### Time Complexity: $O(\log M + \log N)$
*   **Row Search:** $O(\log M)$, where $M$ is the number of rows. This stage eliminates half of the potential rows in each iteration.
*   **Column Search:** $O(\log N)$, where $N$ is the number of columns. Once the row is isolated, we perform a standard binary search on the columns.
*   **Total:** Since the operations are sequential, the total complexity is the sum of the two, which simplifies to **$O(\log(M \cdot N))$**. This is significantly more efficient than an $O(M + N)$ scan or an $O(M \cdot N)$ exhaustive search.

### Space Complexity: $O(1)$
*   The algorithm utilizes a constant amount of extra space (pointers: `topRow`, `bottomRow`, `leftColumn`, `rightColumn`, and `middle` indices). No additional data structures are created; space usage is independent of input dimensions.

## Component Deep Dive

### 1. The Row Selection Strategy
The core heuristic relies on the property: `matrix[row][0] <= target <= matrix[row][lastColumn]`.
*   If `target > matrix[middleRow][lastColumn]`, the target *must* reside in a row below the current one, as all elements in the current and previous rows are strictly smaller than the target.
*   If `target < matrix[middleRow][0]`, the target *must* reside in a row above, as all elements in the current and subsequent rows are strictly larger.
*   If neither condition is met, the target range is captured. Crucially, the code returns `false` if this condition is met but the subsequent column search fails, preventing unnecessary traversal of other rows.

### 2. Boundary Edge Case Handling
*   **Empty Matrices:** While not explicitly guarded with a check for `matrix.length == 0`, the `lastColumn` initialization would throw an `ArrayIndexOutOfBoundsException` on an empty matrix. In a production environment, a pre-check `if (matrix == null || matrix.length == 0 || matrix[0].length == 0)` is required.
*   **Single Row/Column:** The use of `left + (right - left) / 2` correctly handles single-element boundaries, preventing overflow and ensuring termination even when indices overlap.
*   **Target Outside Bounds:** The `while` loop termination condition (`topRow <= bottomRow`) gracefully handles targets smaller than the global minimum or larger than the global maximum by eventually exhausting the row search space.

## Key Insights

*   **Mathematical Equivalence to Flattening:** This algorithm is mathematically equivalent to treating the $M \times N$ matrix as a sorted array of length $M \times N$ and performing a single binary search. The index $i$ in the virtual flat array can be mapped to `matrix[i / N][i % N]`. The current implementation effectively performs this mapping logic manually via two nested binary searches, which is arguably more readable and debuggable.
*   **Branch Prediction Efficiency:** The row-search logic is highly branch-predictor friendly. By checking the range boundaries first, we prune the search space aggressively.
*   **Avoidance of Integer Overflow:** The use of `middle = left + (right - left) / 2` is a standard, critical practice in Java. While `(left + right) / 2` works for smaller matrices, it introduces the risk of integer overflow if `left + right` exceeds `Integer.MAX_VALUE`. The provided code correctly adheres to safe arithmetic practices.
*   **Performance Nuance:** The design returns `false` immediately after the row search fails. This is a critical optimization; once the candidate row is identified, we are committed to it. If the target is not found within that specific row, it cannot possibly exist elsewhere in the matrix, thus avoiding extraneous work.

---

## standard_binary_search(One_pass).java
*Style: detailed*

# Technical Deep Dive: Virtual 1D Binary Search Matrix Traversal

## Summary
The solution employs a **Binary Search on a Virtual Linearized Space**. By leveraging the property that the matrix is sorted such that each row's start is greater than the previous row's end, the 2D matrix is treated as a continuous, monotonically increasing 1D array of size $N \times M$. This eliminates the need for expensive multi-step search algorithms (like searching rows then columns) and achieves $O(\log(NM))$ performance without allocating auxiliary memory.

---

## Complexity Analysis

### Time Complexity: $O(\log(NM))$
*   **Derivation:** The algorithm performs a standard binary search over a range of $N \times M$ elements. In each iteration, the search space is halved.
*   **Logic:** Since $N \times M$ represents the total number of elements, the number of steps required to converge to the target or exhaust the range is logarithmic relative to the total element count.

### Space Complexity: $O(1)$
*   **Derivation:** The solution utilizes a fixed number of integer variables (`left`, `right`, `middle`, `row`, `column`, `rowCount`, `columnCount`).
*   **Logic:** No data structures are created, and no recursion is used (avoiding stack overhead). The mapping from 1D to 2D is performed via constant-time arithmetic operations on the indices.

---

## Component Deep Dive

### 1. The Virtual Index Mapping
The core of the implementation is the mapping between a flat index `idx` and the matrix coordinates $(r, c)$.
*   **Row Calculation:** `row = middle / columnCount` 
    *   This leverages integer division to identify how many full rows fit into the current index.
*   **Column Calculation:** `column = middle % columnCount`
    *   The remainder represents the offset within the identified row.
*   **Constraint Handling:** This logic assumes `columnCount > 0`. A production implementation should include a guard clause for empty matrices (e.g., `if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return false;`) to avoid `ArithmeticException` (division by zero) or `ArrayIndexOutOfBoundsException`.

### 2. Binary Search Logic
*   **Range Definition:** The search space is defined as $[0, (rowCount \times columnCount) - 1]$. 
*   **Midpoint Calculation:** The expression `left + (right - left) / 2` is used instead of `(left + right) / 2` to **prevent integer overflow** that would occur if `left + right` exceeded `Integer.MAX_VALUE`.

---

## Key Insights

### Performance Optimization Nuances
*   **Memory Locality:** While the "Virtual 1D" approach is mathematically elegant, it relies on frequent `row` and `column` calculations. In modern CPU architectures, the jump between rows involves accessing memory that may not be in the immediate cache line. However, because $O(\log(NM))$ is so efficient, the impact of cache misses is negligible compared to the reduction in total comparisons.
*   **Division/Modulo Overhead:** On some low-level hardware or highly optimized JVM environments, repeated division (`/`) and modulo (`%`) can be slower than bitwise operations. If `columnCount` were guaranteed to be a power of 2, these could be replaced with `>>` and `&` operators.

### Subtle Edge Cases to Monitor
*   **Empty Matrices:** As noted, `matrix[0].length` will throw an exception on an empty array. Always validate the input dimensions before calculating `columnCount`.
*   **Large Matrices:** While the logic handles `rowCount * columnCount` well, if the product exceeds `Integer.MAX_VALUE`, the `right` pointer calculation will overflow. Given Java's `int` limit (~2 billion), this only impacts extremely large datasets (e.g., a $50,000 \times 50,000$ matrix), where `long` should be used for the `left`/`right` pointers.

### Comparison to Alternative Approaches
*   **Z-Search (Staircase Search):** A search starting from the top-right corner moving left or down runs in $O(N+M)$. The Binary Search approach ($O(\log(NM))$) is theoretically superior for large, balanced matrices, as $\log(NM)$ grows significantly slower than $N+M$.

---
