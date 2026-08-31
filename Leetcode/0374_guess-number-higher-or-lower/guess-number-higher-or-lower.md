# guess-number-higher-or-lower

## standard_binary_search.java
*Style: concise*

### Notes: Binary Search for Hidden Integer

**Overview**
Implements a standard binary search algorithm to identify a target integer `n` chosen by a system-provided `guess(int)` API. It narrows the search space logarithmically by comparing the midpoint against the target.

**Key Components**
* `guessNumber(int n)`: Main entry point; initializes search boundaries `[0, n]` and executes the search loop.
* `guess(int m)`: External API; returns `0` (match), `-1` (target < guess), or `1` (target > guess).

**Logic/Implementation Details**
* **Overflow Prevention:** Uses `m = l + (r - l) / 2` instead of `(l + r) / 2` to avoid integer overflow when `l` and `r` are large.
* **Search Space:** Inclusive boundaries (`l <= r`) are used to ensure the algorithm handles the case where the target is exactly at the boundary. 
* **Complexity:** Time complexity is $O(\log n)$, space complexity is $O(1)$.

---

## ternary_search.java
*Style: detailed*

# Engineering Reference: Ternary Search for Guessing Game

## Summary
The implementation utilizes a **Ternary Search** algorithm to locate a target integer within a sorted range $[1, n]$. While a standard Binary Search divides the search space into two halves, Ternary Search partitions the space into three equal segments using two midpoints ($m_1$ and $m_2$). This approach is theoretically advantageous in scenarios where the objective function is unimodal or where the cost of querying is significant, though it is often less efficient than binary search for simple point-search problems due to the increased number of API invocations per iteration.

## Complexity Analysis

### Time Complexity: $O(\log_3 n)$
*   **Derivation:** In each iteration, the search space $R$ is reduced to approximately $R/3$. The recurrence relation is $T(n) = T(n/3) + O(1)$, assuming constant time for `guess()` calls.
*   **Constant Factor Overhead:** While the logarithmic base is 3 (implying fewer iterations than binary search), the algorithm performs up to 4 `guess()` calls per iteration (checking $m_1, m_2$, and potentially their relation). 
*   **Note:** In practice, $O(\log_3 n)$ with 4 calls per iteration is typically slower than $O(\log_2 n)$ with 1 call per iteration.

### Space Complexity: $O(1)$
*   The implementation is strictly iterative and employs a fixed set of integer pointers (`l`, `r`, `m1`, `m2`). No auxiliary data structures or recursive call stacks are utilized.

## Component Deep Dive

### Partitioning Logic
The midpoints are calculated as:
*   `m1 = l + (r - l) / 3`
*   `m2 = r - (r - l) / 3`
Using `(r - l) / 3` instead of `(l + r) / 3` prevents **integer overflow**, a standard safeguard when dealing with large $n$ near `Integer.MAX_VALUE`.

### Branching Strategy
1.  **Direct Hits:** The algorithm checks `guess(m1) == 0` and `guess(m2) == 0` immediately to enable early termination.
2.  **Middle Segment:** The conditional `guess(m1) + guess(m2) == 0` catches the case where $m_1$ returns `-1` (too high) and $m_2$ returns `1` (too low). This indicates the target must lie strictly between the two midpoints.
3.  **Outer Segments:** 
    *   If `guess(m1) == -1`, the target is in the range $[l, m_1 - 1]$.
    *   Otherwise, the target is in the range $[m_2 + 1, r]$.

### Edge-Case Handling
*   **Range Convergence:** The logic handles narrow ranges (e.g., $r - l < 3$) implicitly by the nature of the division. However, one must ensure that `m1` and `m2` remain distinct to avoid unnecessary overlapping checks.
*   **Integer Limits:** The `l + (r - l) / 3` formula is robust against overflow, ensuring that even if `l + r` exceeds `Integer.MAX_VALUE`, the calculation remains within bounds.

## Key Insights

### Performance Nuance
The primary critique of this implementation is the **API Call Density**. In typical competitive programming environments (like LeetCode), the cost of the `guess()` function is the dominant factor. Performing 4 calls per loop iteration vs. 1 call (Binary Search) usually results in a net performance loss, even though the number of loop iterations is reduced.

### Potential Subtlety
*   **Convergence:** When the search space becomes very small (e.g., `r - l` is small), `m1` and `m2` might point to the same index or cross each other. While the provided logic is mathematically sound for broad ranges, in extremely tight constraints, one should verify if `l` and `r` cross correctly to avoid infinite loops.
*   **Optimality:** If the `guess()` function is expensive (e.g., involves network I/O or database access), the developer should memoize the results of `guess(m1)` and `guess(m2)` within the loop, as the current code repeatedly calls `guess()` on the same values in the `if/else if` chain.

### Recommended Optimization
To minimize `guess()` calls, cache the results:
```java
int res1 = guess(m1);
if (res1 == 0) return m1;
int res2 = guess(m2);
if (res2 == 0) return m2;
// Use res1 and res2 for subsequent logic...
```
This reduces the theoretical max calls from 4 to 2 per iteration, significantly improving real-world performance.

---
