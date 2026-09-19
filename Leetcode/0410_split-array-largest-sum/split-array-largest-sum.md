# split-array-largest-sum

## binary_search_on_answer(nlogn).java
*Style: detailed*

# Technical Reference: Optimal Array Splitting (Min-Max Problem)

## 1. Summary
The problem asks to partition an array into $k$ contiguous subarrays such that the largest sum among these subarrays is minimized. This is a classic **Min-Max** optimization problem. 

The solution employs a **Binary Search on the Answer space**. Instead of attempting to construct the partition directly (which would be computationally prohibitive), we define a monotonic predicate: *"Is it possible to partition the array into $k$ or fewer subarrays such that no subarray sum exceeds $X$?"* By binary searching for the minimal $X$, we reduce the complexity from a potential exponential partition search to $O(N \log(\sum \text{nums}))$.

---

## 2. Complexity Analysis

### Time Complexity: $O(N \cdot \log(S - M))$
*   **$N$**: Number of elements in `nums`.
*   **$M$**: The largest single element in the array (`max(nums)`).
*   **$S$**: The sum of all elements in the array (`sum(nums)`).
*   **Reasoning**: The binary search range $[M, S]$ spans a total of $S - M$ values. Each iteration of the binary search performs a linear scan of the array in the `countSubarrays` helper function ($O(N)$). Thus, the binary search performs $\log(S - M)$ iterations, each costing $O(N)$.

### Space Complexity: $O(1)$
*   **Reasoning**: The solution operates in-place using only primitive integer accumulators (`currentSum`, `subarrayCount`, `left`, `right`). No auxiliary data structures are allocated that scale with input size.

---

## 3. Component Deep Dive

### `countSubarrays(int[] nums, int maxAllowedSum)`
*   **Logic**: This is a **Greedy Partitioning** algorithm. By iterating once and greedily adding elements to the current subarray until the `maxAllowedSum` is violated, we ensure the minimal number of subarrays for a given limit.
*   **Edge Case - Individual Element Overflow**: The logic implicitly assumes `maxAllowedSum >= max(nums)`. If `maxAllowedSum` were smaller than a single element, the greedy approach would fail to partition. However, the binary search range ensures `left` starts at `max(nums)`, effectively preventing this state.

### `splitArray(int[] nums, int k)`
*   **Binary Search Range**: 
    *   **Lower Bound (`left`)**: The largest single element. Any limit lower than this makes it impossible to include that element in any subarray.
    *   **Upper Bound (`right`)**: The sum of all elements. This represents the partition where $k=1$ (the entire array).
*   **Binary Search Invariant**:
    *   If `countSubarrays(mid) > k`: The `mid` is too tight; we must increase the limit to reduce the number of subarrays.
    *   If `countSubarrays(mid) <= k`: The `mid` is a valid candidate for the result. We record it implicitly by moving `right = mid - 1` to search for a tighter, more optimal limit.

---

## 4. Key Insights

### Monotonicity of the Predicate
The algorithm relies on the property that if a capacity $X$ is valid (can be partitioned into $\le k$ arrays), then any capacity $Y > X$ is also valid. This monotonicity is the fundamental requirement for Binary Search on the answer space.

### Why `requiredSubarrays <= k`?
In the binary search, we don't look for exactly $k$ subarrays. We look for $\le k$.
*   **Reasoning**: If a maximum sum $X$ allows us to split the array into $m$ subarrays where $m < k$, we can always further split those $m$ subarrays (until we reach exactly $k$) without increasing the maximum subarray sum. Therefore, any $X$ that allows $\le k$ splits is a feasible solution.

### Subtle Bugs & Performance Nuances
1.  **Integer Overflow**: In languages with fixed-width integers, `left + right` can overflow. The implementation correctly uses `mid = left + (right - left) / 2` to mitigate this. Similarly, if `totalSum` were to exceed `Integer.MAX_VALUE`, one would need to use `long` for the search boundaries.
2.  **Greedy Correctness**: The greedy strategy works here because we are dealing with contiguous subarrays. If the order could be changed (e.g., bin packing), this problem would become NP-Hard. Since the order is fixed, the "local optimal" (greedily filling until the limit) is also the "global optimal" for a specific `maxAllowedSum`.
3.  **Search Termination**: The loop condition `left <= right` with `right = mid - 1` and `left = mid + 1` correctly terminates at the smallest valid `left` that satisfies the condition.

---
