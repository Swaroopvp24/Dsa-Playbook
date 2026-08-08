# subarray-sum-equals-k

## attempt_1_bruteforce.java
*Style: concise*

### Notes: Subarray Sum Equals K (Brute Force)

#### Purpose
Calculates the total number of continuous subarrays that sum up to exactly `k` using a nested loop approach.

#### Key Logic
*   **Outer loop (`i`)**: Defines the starting index of the subarray.
*   **Inner loop (`j`)**: Extends the subarray end index and accumulates the running sum.
*   **Conditional**: Increments `res` whenever the cumulative `sum` matches `k`.

#### Observations
*   **Complexity**: $O(n^2)$ time and $O(1)$ space.
*   **Limitation**: Inefficient for large arrays; suboptimal compared to the $O(n)$ hash map approach (Prefix Sum technique).
*   **Edge Cases**: Correctly handles negative numbers (unlike sliding window approaches which require positive-only arrays).

---
