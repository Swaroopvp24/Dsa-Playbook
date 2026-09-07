# search-2d-matrix

## standard_binary_search.java
*Style: detailed*

# Technical Deep Dive: Hierarchical Binary Search on Sorted Matrix

## Summary
The provided solution addresses the search problem in a sorted $M \times N$ matrix by treating the matrix as a flattened, ordered sequence without the overhead of physical flattening. It employs a **two-tier binary search strategy**:
1. **Vertical Search:** Locates the candidate row by evaluating the target against the bounds of each row (`matrix[i][0]` and `matrix[i][lastColumn]`).
2. **Horizontal Search:** Performs a standard 1D binary search within the identified candidate row.

This approach exploits the matrix properties—where row $i$ is sorted and the first element of row $i+1$ is greater than the last element of row $i$—to achieve logarithmic time complexity.

---

## Complexity Analysis

### Time Complexity: $O(\log M + \log N)$
*   **Vertical Search:** We perform a binary search over $M$ rows. Each comparison is $O(1)$, resulting in $O(\log M)$.
*   **Horizontal Search:** Once the row is identified, we perform a binary search over $N$ columns. This is $O(\log N)$.
*   **Total:** The operations are sequential, not nested. Therefore, the complexity is $O(\log M + \log N)$, which is mathematically equivalent to $O(\log(MN))$.

### Space Complexity: $O(1)$
*   The algorithm utilizes a constant amount of auxiliary space for pointer variables (`topRow`, `bottomRow`, `middleRow`, `leftColumn`, `rightColumn`). No additional data structures are allocated, making this an in-place solution.

---

## Component Deep Dive

### 1. Vertical Row Pruning
The primary logic relies on the boundary constraints of each row. 
*   **Targeting logic:** If `target > matrix[middleRow][lastColumn]`, the search space must shift to higher indices (`topRow = middleRow + 1`). Conversely, if `target < matrix[middleRow][0]`, the target resides in a lower row index (`bottomRow = middleRow - 1`).
*   **Implicit Range:** If neither condition is met, we have mathematically proven that if the target exists in the matrix, it *must* reside in the current `middleRow`, as the target satisfies `matrix[middleRow][0] <= target <= matrix[middleRow][lastColumn]`.

### 2. Horizontal Search (Narrowing)
Once the row is isolated, the inner `while` loop behaves as a standard 1D binary search.
*   **Edge Case Handling:** By returning `false` immediately after the inner binary search loop terminates (if the target isn't found), the code handles cases where the target falls within a row's numerical range but is not present in that row. This is a critical exit condition that prevents redundant searches in rows that do not exist or are already excluded.

---

## Key Insights

### Performance Optimization Nuance
While this algorithm is $O(\log(MN))$, it is technically possible to perform the search using a single binary search by treating the $M \times N$ matrix as a virtual 1D array of length $L = M \times N$. 
*   **Index Conversion:** An index $i$ in a virtual array maps to `matrix[i / N][i % N]`.
*   **Comparison:** This removes the branching logic between row and column searches. 
*   **Staff Note:** The provided solution's two-tier approach is often more cache-friendly in certain environments if the matrix is accessed row-major, as it minimizes random access patterns by localizing the search to a single row once identified.

### Subtle Bugs & Corner Cases
*   **Empty Matrix:** The code assumes `matrix[0]` exists. If `matrix` is empty or `matrix[0]` is empty, `lastColumn` will throw an `ArrayIndexOutOfBoundsException`. In a production-grade system, an input validation check for `matrix == null || matrix.length == 0` is required.
*   **Overflow:** `middleRow = topRow + (bottomRow - topRow) / 2` is used instead of `(topRow + bottomRow) / 2`. This is a best practice to prevent integer overflow when dealing with extremely large matrices where the indices might exceed `Integer.MAX_VALUE / 2`.
*   **Strict Monotonicity:** This implementation assumes the standard "Sorted Matrix" definition (each row is sorted, and rows are sorted relative to each other). If the rows were sorted individually but *not* relative to each other, this algorithm would fail, requiring a different approach (like the "Search from top-right corner" technique which is $O(M+N)$).

---
