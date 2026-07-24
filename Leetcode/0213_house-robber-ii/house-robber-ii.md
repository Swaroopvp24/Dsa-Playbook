# house-robber-ii

## attempt_1.java
*Style: detailed*

# Technical Reference: House Robber II (Circular Dependency)

## 1. Summary
The `House Robber II` problem introduces a circular constraint: the first and last houses are adjacent. This transforms the standard 1D dynamic programming approach into a constrained optimization problem.

The algorithmic approach utilizes **Space-Optimized Dynamic Programming** with a **Divide and Conquer partitioning strategy**. By splitting the circular array into two overlapping linear sub-problems—one excluding the last element `[0, n-2]` and one excluding the first `[1, n-1]`—we reduce the circular dependency to two independent linear segments. The global maximum is simply the optimal result of these two independent ranges.

## 2. Complexity Analysis

*   **Time Complexity: $O(N)$**
    *   The `solve` function iterates linearly through the range defined by `st` and `en`.
    *   We invoke `solve` twice, covering the array segments `[0, n-2]` and `[1, n-1]`.
    *   Since $2 \times N$ operations scale linearly, the total time complexity remains $O(N)$.
*   **Space Complexity: $O(1)$**
    *   We use constant space for pointers (`prev1`, `prev2`, `cur`) rather than an $O(N)$ DP table (array).
    *   The recursion stack is not used, as we utilize an iterative bottom-up approach.

## 3. Component Deep Dive

### `solve(int[] nums, int st, int en)`
This function implements the standard "House Robber" linear recurrence: 
$dp[i] = \max(dp[i-2] + nums[i], dp[i-1])$

*   **State Space Compression:** Instead of maintaining a full `dp[]` array, we only track the two previous optimal states (`prev1` and `prev2`). This is a classic optimization for DP problems where the transition function only depends on a fixed look-back window.
*   **Edge Case Handling:**
    *   `st == en`: Returns `nums[st]` immediately, handling the base case of a single-house range.
    *   **Initialization:** `prev1` is initialized as `Math.max(nums[st], nums[st+1])`. This establishes the correct induction base for the loop starting at `st + 2`.

### `rob(int[] nums)`
This is the entry point that manages the structural constraint.
*   **Zero/Single House Constraints:** The function explicitly handles $N=0$ and $N=1$ cases. Without these, `solve` would trigger `ArrayIndexOutOfBoundsException` or incorrect logic when processing ranges shorter than two elements.
*   **The Circular Partition:** By selecting `(0, n-2)` and `(1, n-1)`, we ensure the algorithm never attempts to rob the first and last house simultaneously. Any circular combination that would have included both is strictly forbidden by the partitioning logic.

## 4. Key Insights

*   **State Transition Nuance:** The recurrence `Math.max(prev2 + nums[i], prev1)` is critical. It captures the choice between "adding the current house plus the maximum from two houses ago" versus "skipping the current house and keeping the maximum from the previous house." 
*   **Optimization Pitfall:** A common mistake is to attempt to solve this by modifying the input array (e.g., setting elements to zero). That approach fails if negative numbers were introduced (though not applicable in standard House Robber) or if the state needs to be reused. The partitioning approach used here is cleaner and safer.
*   **Subtle Bug Warning:** Be extremely careful with index bounds in the `solve` call. Passing `nums.length - 1` for the first range would incorrectly allow robbing both the first and last house, violating the circular constraint. The current implementation correctly uses `nums.length - 2` as the inclusive end index for the first partition.
*   **Performance Nuance:** Because this uses primitive variables (`int`) and avoids object allocation within the loop, it performs at the JVM's peak efficiency. The hot path of the loop is highly optimized by the JIT compiler due to the lack of branching inside the arithmetic logic.

---
