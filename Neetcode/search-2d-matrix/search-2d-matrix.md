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

## standard_binary_search(One_pass).java
*Style: detailed*

# Engineering Deep-Dive: Virtualized Binary Search on 2D Matrices

## Summary
The solution implements an efficient search algorithm on a row-wise and column-wise sorted $m \times n$ matrix. By treating the matrix as a flattened, monotonically increasing 1D array of length $N = m \times n$, the algorithm avoids redundant traversals. It achieves $O(\log(mn))$ time complexity by performing a binary search on the index space $[0, mn-1]$, mapping indices to $(r, c)$ coordinates on-the-fly using integer division and modulo arithmetic. This approach preserves $O(1)$ auxiliary space while minimizing the constant factors associated with pointer arithmetic.

---

## Complexity Analysis

### Time Complexity: $O(\log(mn))$
The search space is defined by the total number of elements in the matrix ($mn$). In every iteration of the `while` loop, the search range is reduced by half. Thus, the number of operations follows the recurrence $T(n) = T(n/2) + O(1)$, which resolves to logarithmic time relative to the total number of elements.

### Space Complexity: $O(1)$
The algorithm operates in-place. It only stores a fixed number of primitive integers (`left`, `right`, `middle`, `row`, `column`), regardless of the input matrix size. No auxiliary data structures (like flattened arrays or recursion stacks) are allocated, ensuring constant space usage.

---

## Component Deep Dive

### 1. Index Mapping (Virtual Flattening)
The crux of the algorithm is the coordinate transformation logic:
*   **Row index:** `middle / columnCount`
*   **Column index:** `middle % columnCount`

This mapping works because the matrix is globally sorted; each row starts with a value strictly greater than the last element of the previous row. This preserves the transitive property required for binary search across the entire domain.

### 2. Binary Search Boundary Logic
The search range is initialized as:
*   `left = 0`
*   `right = (rowCount * columnCount) - 1`

The condition `while (left <= right)` is critical to handle edge cases, such as matrices with a single element. By using `left + (right - left) / 2` to calculate the `middle`, we prevent potential integer overflow that occurs with `(left + right) / 2` when dealing with very large matrices where `left + right` might exceed `Integer.MAX_VALUE`.

### 3. Edge-Case Handling
*   **Empty Matrices:** The provided code assumes `matrix[0]` exists. In a production environment, one should add a guard clause `if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return false;`.
*   **Single Element Matrix:** The logic correctly handles `1x1` matrices: `left=0, right=0`. The loop executes once, checks the single element, and terminates.
*   **Non-existent Target:** The search space naturally exhausts (`left > right`), correctly returning `false` after the loop.

---

## Key Insights

### Performance Optimization
*   **Memory Locality:** While this approach is theoretically optimal, it can exhibit sub-optimal cache performance compared to a block-based search on extremely large matrices. However, for standard application usage, the lack of object allocation and the minimal instruction count make this the standard, high-performance approach.
*   **Integer Arithmetic:** The `middle / columnCount` and `middle % columnCount` operations are relatively expensive compared to bitwise shifts. In performance-critical environments where the `columnCount` is a power of 2, these could be replaced with bitwise `>>` and `&` operations for minor gains.

### Subtle Caveats
*   **Sorting Assumption:** This algorithm strictly requires that the first element of each row is greater than the last element of the previous row. If the matrix is sorted only within rows and columns independently (but not globally), this algorithm will fail. In that case, the optimal approach shifts to a "Search from Top-Right" strategy, which runs in $O(m + n)$.
*   **Integer Overflow:** While `left + (right - left) / 2` mitigates overflow, the total number of elements `rowCount * columnCount` must fit within a 32-bit signed integer. For massive datasets exceeding $2^{31}-1$ elements, the indexing logic must be upgraded to `long`.

---
