# pascals-triangle

## attempt_1.java
*Style: detailed*

# Engineering Reference: Pascal’s Triangle Implementation

## Summary
The implementation utilizes a **dynamic programming** approach to construct Pascal’s Triangle iteratively. Since each coefficient $C(n, k)$ in the triangle is the sum of the two elements directly above it ($C(n-1, k-1) + C(n-1, k)$), the algorithm maintains a state of previously computed rows to derive subsequent values in $O(1)$ time per element. By leveraging the property that the boundaries of each row are always $1$, we reduce the problem to local summation within the grid.

---

## Complexity Analysis

### Time Complexity: $O(n^2)$
*   **Derivation:** The total number of elements in Pascal’s Triangle with $n$ rows is given by the sum of the first $n$ integers: $\sum_{i=1}^{n} i = \frac{n(n+1)}{2}$.
*   Each element is computed exactly once via a constant-time addition operation. Therefore, the complexity is directly proportional to the total number of elements, yielding $O(n^2)$.

### Space Complexity: $O(n^2)$
*   **Derivation:** The algorithm stores all rows in a `List<List<Integer>>`. The total number of integers stored is $\frac{n(n+1)}{2}$.
*   While the auxiliary space required for the *logic* is $O(1)$, the space required to store the *output structure* is $O(n^2)$. 
*   *Note:* If the requirement were only to return the $n$-th row, space could be optimized to $O(n)$. Given the requirement to return the full triangle, $O(n^2)$ is the theoretical lower bound.

---

## Component Deep Dive

### 1. State Access Pattern
The logic relies on `result.get(i - 2).get(j - 2) + result.get(i - 2).get(j - 1)`.
*   **`i-2`**: Refers to the previous row index (0-indexed). Since `i` starts at 1, `i-2` safely accesses the row computed in the previous iteration.
*   **`j-2` and `j-1`**: These indices correspond to the parents of the current cell. Because Pascal's Triangle is shifted, `j-2` fetches the "left-upper" parent and `j-1` fetches the "right-upper" parent.

### 2. Boundary Condition Handling
The code uses an explicit check: `if (j == 1 || j == i)`.
*   This serves as the base case for the recurrence relation $C(n, 0) = C(n, n) = 1$. 
*   This approach avoids an `IndexOutOfBoundsException` that would occur if the code attempted to access `result.get(i-2)` at indices outside the range of the previous row's length.

### 3. Data Structure Choices
*   `ArrayList`: Used for both rows and the master list. 
    *   *Trade-off:* `ArrayList` provides $O(1)$ access time, which is critical here. However, repeated `add()` operations may trigger internal array resizing. Since the total number of elements is known ($\frac{n(n+1)}{2}$), pre-initializing the `ArrayList` capacity could offer a minor performance gain, though it is usually negligible for standard input ranges.

---

## Key Insights & Nuances

### 1. Indexing Offset
The implementation uses 1-based indexing for the loop (`i` from 1 to `numRows`), which simplifies the boundary logic (`j == 1` or `j == i`). However, this requires careful management when mapping to the 0-indexed `ArrayList` stored in `result`. The code handles this effectively with `i-2`, effectively shifting the 1-based logic into the 0-based memory access required by the Java Collection Framework.

### 2. Integer Overflow
*   **Risk:** Pascal's Triangle values grow exponentially ($2^{n-1}$ for the sum of the $n$-th row). 
*   **Constraint:** This implementation uses `Integer`. If `numRows > 30`, the values will exceed `Integer.MAX_VALUE` (2,147,483,647), leading to silent integer overflow. In production scenarios involving larger `n`, `Long` or `BigInteger` should be used.

### 3. Performance Optimization
*   **Memory Locality:** While `ArrayList<ArrayList<Integer>>` is idiomatic Java, it results in multiple object allocations and pointer indirection (the list of lists points to objects, which point to arrays of `Integer` objects). For extreme performance, a flattened 1D primitive array `int[]` or a `int[][]` would significantly improve cache locality and reduce memory overhead by avoiding auto-boxing. 
*   **Allocation:** Pre-sizing the `ArrayList` (`new ArrayList<>(i)`) inside the loop would prevent the internal array from needing to resize as the row grows.

---
