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
