# pascals-triangle

## attempt_1.java
*Style: concise*

### Pascal's Triangle Generator
This code generates Pascal's Triangle up to `numRows` using a recursive approach. Each row is constructed based on the sum of adjacent elements from the previous row.

#### Key Methods
*   `generate(int numRows)`: Initializes the result list and initiates the recursive building process starting at index 0.
*   `fun(List<List<Integer>> list, int i, int n)`: Recursive helper that builds the $i$-th row and appends it to the master list until $i = n$.

#### Non-Obvious Logic
*   **Boundary Conditions:** Rows always start and end with `1`. Internal elements are derived via `list.get(i-1).get(j-1) + list.get(i-1).get(j)`, effectively performing a rolling sum of the previous row.
*   **Recursion Flow:** The base case `i >= n` terminates the recursion. Because the function appends to the list *before* the recursive call (`fun(list, i + 1, n)`), it ensures rows are built in the correct order ($0$ to $n-1$).

---

## attempt_1.java
*Style: detailed*

## Deep-Dive Technical Reference: Pascal's Triangle Generation

### 1. Summary
The provided implementation employs an **iterative dynamic programming** approach to generate Pascal's Triangle. The algorithm leverages the mathematical property that each interior element $C(n, k)$ is the sum of the two elements directly above it in the preceding row: $C(n, k) = C(n-1, k-1) + C(n-1, k)$. By memoizing previously computed rows in a `List<List<Integer>>`, the solution avoids redundant calculations and recursive stack overhead, effectively building the triangle layer-by-layer.

### 2. Complexity Analysis

*   **Time Complexity:** $O(N^2)$, where $N$ is `numRows`.
    *   The algorithm utilizes nested loops. The outer loop runs $N$ times, and the inner loop runs $i$ times for each $i \in [1, N]$. The total number of operations follows the arithmetic series $\sum_{i=1}^{N} i = \frac{N(N+1)}{2}$. Thus, the complexity is $O(N^2)$.
*   **Space Complexity:** $O(N^2)$.
    *   The space required is proportional to the number of elements in the triangle. Since row $i$ contains $i$ elements, the total number of integers stored is $\frac{N(N+1)}{2}$. As $N$ grows, this dominates the memory footprint, resulting in $O(N^2)$ space. 
    *   *Note:* The implementation does not utilize auxiliary space beyond the output structure.

### 3. Component Deep Dive

*   **Boundary Handling (`j == 1 || j == i`):**
    The code explicitly handles the edges of the triangle. Since the first and last elements of every row in Pascal's Triangle are identity values (1), the algorithm bypasses the addition logic for these indices. This effectively acts as the base case for the DP recurrence.
*   **Recurrence Logic (`result.get(i - 2).get(j - 2) + result.get(i - 2).get(j - 1)`):**
    *   The `i` index is 1-based, and `result` is 0-indexed. Therefore, `result.get(i - 2)` retrieves the *previous* row.
    *   The indices `j-2` and `j-1` correctly map to the two parents in the row above.
    *   *Edge Case Note:* If `i=1`, the logic for interior elements is never triggered because the `if` condition catches `j=1` and `j=i` (which are the same for the first row). The logic is safe against `IndexOutOfBoundsException` for `numRows >= 1`.
*   **Data Structure Choice:**
    *   `ArrayList<List<Integer>>` is appropriate here as we have frequent random access to the previous row for summation. The constant-time $O(1)$ lookup for `ArrayList.get()` is critical to maintaining the $O(N^2)$ time complexity.

### 4. Key Insights

*   **Memory Efficiency:** While the implementation is $O(N^2)$, it is worth noting that for large $N$, this approach is limited by the heap size. If only the *last* row were required, space could be optimized to $O(N)$ by maintaining only the current and previous state arrays.
*   **Integer Overflow:** The current implementation uses `Integer`. Pascal's Triangle values grow combinatorially (specifically at the rate of $\binom{n}{k}$). For `numRows > 33`, the values will exceed `Integer.MAX_VALUE` ($2^{31}-1$), leading to arithmetic overflow. If the requirement expanded beyond $N=33$, a `Long` or `BigInteger` implementation would be mandatory.
*   **Pre-allocation:** The `ArrayList` objects are initialized without an initial capacity. In a high-performance, tight-memory environment, providing an initial capacity for `ArrayList` (e.g., `new ArrayList<>(i)`) would reduce the number of array copies triggered by resizing, leading to a minor constant-time performance improvement.
*   **Loop Indices:** The use of 1-based indexing for the loops makes the logic intuitively map to mathematical notation, but it necessitates careful offsetting (`i-2`, `j-2`) when accessing the 0-indexed `List`. This is the most common site for "off-by-one" errors during maintenance.

---
