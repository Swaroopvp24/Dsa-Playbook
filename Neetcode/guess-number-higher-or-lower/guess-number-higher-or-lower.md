# guess-number-higher-or-lower

## standard_binary_search.java
*Style: detailed*

# Engineering Deep-Dive: Binary Search Optimization for GuessGame

## Summary
The solution implements a classic **Binary Search** algorithm to identify a target integer within the discrete search space $[1, n]$. By leveraging the monotonic property of the provided `guess(int num)` API—which partitions the search space into three distinct states (less than, greater than, or equal to the target)—the algorithm achieves logarithmic search time. This is the optimal approach for searching sorted or effectively ordered numeric ranges.

## Complexity Analysis

### Time Complexity: $O(\log n)$
*   **Derivation:** The algorithm iteratively halves the search space ($n \to n/2 \to n/4 \dots$). The number of iterations $k$ required to reach a single element is defined by $n/2^k = 1$, which simplifies to $k = \log_2 n$.
*   **Constant Factor:** Each iteration performs a single constant-time `guess()` invocation and basic arithmetic operations, resulting in an efficient execution profile.

### Space Complexity: $O(1)$
*   **Derivation:** The solution utilizes a strictly iterative approach with fixed-size primitive integers (`l`, `r`, `m`). No auxiliary data structures or recursive call stacks are employed, maintaining constant memory overhead regardless of input size $n$.

## Component Deep Dive

### 1. Midpoint Calculation Strategy
```java
int m = l + (r - l) / 2;
```
*   **Technical Rationale:** Using `(l + r) / 2` is a common antipattern in Java because `l + r` can induce **integer overflow** if the sum exceeds `Integer.MAX_VALUE`. By calculating the midpoint as the base plus the half-offset, we ensure the intermediate value remains within the bounds of a 32-bit signed integer.

### 2. Search Space Initialization
*   **Initialization:** The bounds are set to `l = 0` and `r = n`. Note that if the problem constraints strictly guarantee the target exists within $[1, n]$, initializing `l = 1` is technically more precise, though the logic holds given the binary nature of the `guess` calls.
*   **Loop Invariant:** The condition `while (l <= r)` ensures that the search space is exhausted entirely. If the loop terminates without returning, the target does not exist in the specified range.

### 3. Edge-Case Handling
*   **Boundary Conditions:**
    *   **$n=1$:** The logic correctly calculates $m=1$, executes `guess(1)`, and returns successfully in one iteration.
    *   **Upper Bound ($n = \text{Integer.MAX\_VALUE}$):** The overflow-safe midpoint calculation handles the largest possible search space without errors.
*   **Invalid Range:** If `n < 0` or if the secret number is outside the $[0, n]$ range, the function returns `-1`, providing a safe fallback.

## Key Insights

### Performance Nuance: The API Cost
While the algorithm logic itself is $O(\log n)$, the performance bottleneck in production environments is frequently the `guess()` function itself (e.g., if it triggers a network request or heavy I/O). In such scenarios, the implementation remains optimal because it guarantees the minimum possible number of calls to the API to resolve the state.

### Subtle Considerations
*   **Closed vs. Open Intervals:** The use of `l <= r` combined with `r = m - 1` and `l = m + 1` correctly maintains a **closed interval** $[l, r]$. Failing to update the boundary as $m \pm 1$ would lead to an infinite loop if the `guess` condition were ever evaluated as the current `m` being either too high or too low.
*   **Integer Range Limits:** If the input $n$ were to approach `Integer.MAX_VALUE`, ensure that any future enhancements to the algorithm (such as broadening the range) do not cause the variables $l, r,$ or $m$ to wrap around into negative values, which would invalidate the binary search comparison logic.

---

## ternary_search.java
*Style: detailed*

# Deep-Dive Analysis: Ternary Search for Number Guessing

## Summary
The provided solution implements a **Ternary Search** algorithm to identify a target integer within the range $[1, n]$. While standard binary search partitions the search space into two halves, ternary search partitions the space into three equal segments using two pivots ($m_1$ and $m_2$). This approach is traditionally employed to find the extremum of unimodal functions; in this context, it functions as a search strategy that reduces the search space by approximately $2/3$ in each iteration, theoretically requiring fewer calls to the `guess()` API than a standard binary search at the cost of additional logic per iteration.

---

## Complexity Analysis

### Time Complexity: $O(\log_3 n)$
*   **Derivation:** In each iteration, the algorithm discards $2/3$ of the search space. The recurrence relation is $T(n) = T(n/3) + O(1)$, where $O(1)$ represents the constant number of `guess()` calls.
*   **Comparison:** Binary search operates in $O(\log_2 n)$. While $O(\log_3 n)$ is mathematically smaller, the constant factor overhead in this implementation (calling `guess()` up to 4 times per iteration) often results in slower real-world performance compared to a standard binary search ($O(\log_2 n)$ with 1 call per iteration).

### Space Complexity: $O(1)$
*   **Derivation:** The solution utilizes a strictly iterative approach with a fixed set of integer variables (`l`, `r`, `m1`, `m2`). No auxiliary data structures or recursion stacks are employed, maintaining constant memory overhead regardless of input size $n$.

---

## Component Deep Dive

### 1. Partitioning Strategy
The pivots are calculated as:
*   `m1 = l + (r - l) / 3`
*   `m2 = r - (r - l) / 3`

This correctly divides the range $[l, r]$ into three segments: $[l, m1-1]$, $[m1+1, m2-1]$, and $[m2+1, r]$. The use of `l + (r - l) / 3` is a defensive coding practice to prevent integer overflow, which would occur with `(l + r) / 3` if $l+r > 2^{31}-1$.

### 2. Decision Logic
The branching logic handles four distinct scenarios:
1.  **Direct Hits:** If `guess(m1) == 0` or `guess(m2) == 0`, the target is found.
2.  **Range Squeeze:** If `guess(m1) + guess(m2) == 0`, the target lies between $m_1$ and $m_2$. Note: `guess()` returns $-1$ (too high) or $1$ (too low). Thus, the sum is $0$ if and only if `guess(m1) == 1` (too low) AND `guess(m2) == -1` (too high).
3.  **Left Reduction:** `guess(m1) == -1` implies the target is in the range $[l, m1-1]$.
4.  **Right Reduction:** Otherwise, the target must be in $[m2+1, r]$.

---

## Key Insights & Critical Observations

### The "Over-Engineering" Trap
While ternary search is a valid algorithmic pattern, it is suboptimal for this specific problem. 
*   **API Cost:** Each iteration makes up to 4 calls to the `guess()` API. A standard binary search makes exactly 1 call per iteration. Even though ternary search reduces the range faster per iteration, the **cost per iteration** is significantly higher. 
*   **API Bottleneck:** In systems where the `guess()` function represents an expensive I/O operation or a network call, the increased number of calls per loop iteration significantly degrades latency.

### Potential Edge Case: Integer Overflow
The provided logic `r - (r - l) / 3` is safe from overflow. However, if the logic were restructured to `(l + 2*r) / 3`, the code would be prone to overflow if $r$ is close to `Integer.MAX_VALUE`.

### Branching Complexity
The `if-else` chain is dense. The logic `guess(m1) + guess(m2) == 0` is a clever optimization but can be harder to debug. A more readable (though not necessarily faster) approach is to explicitly handle the `guess()` results:
*   `m1` is too high: Target in $[l, m1-1]$
*   `m1` is too low and `m2` is too high: Target in $[m1+1, m2-1]$
*   `m2` is too low: Target in $[m2+1, r]$

### Subtle Bug Risk
The condition `l = m1 + 1` and `r = m2 - 1` is critical. If the range $[m1, m2]$ becomes very small (e.g., $m1 == m2$), the logic must ensure the search space doesn't collapse incorrectly. Given the `while(true)` loop and the `return` statements, the loop is guaranteed to terminate as long as a valid solution exists within the range, as each step strictly reduces the search space size.

---
