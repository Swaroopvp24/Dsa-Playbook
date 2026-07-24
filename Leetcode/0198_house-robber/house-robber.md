# house-robber

## recusrion+memoization.java
*Style: detailed*

## Deep-Dive Technical Reference: House Robber Solution

### 1. Summary
The provided solution implements a **Top-Down Dynamic Programming (Memoization)** approach to solve the classic "House Robber" problem. The core algorithmic principle is the application of the **Principle of Optimality**: the maximum value obtainable for a set of $n$ houses is defined as the maximum of two mutually exclusive choices:
1. **Robbing house $i$**: Adds `nums[i]` to the optimal solution for $i-2$.
2. **Skipping house $i$**: Inherits the optimal solution for $i-1$.

The algorithm recursively decomposes the state space, caching results in a state array (`dp`) to transform an exponential tree-based recursion into a linear-time lookup operation.

---

### 2. Complexity Analysis

*   **Time Complexity: $O(n)$**
    *   There are $n$ distinct states (indices 0 to $n-1$). Each state is computed exactly once due to memoization. Subsequent calls for a computed state return in $O(1)$ time. 
*   **Space Complexity: $O(n)$**
    *   **Memoization Array:** $O(n)$ space is required to store the results of subproblems.
    *   **Call Stack:** The recursion depth is $O(n)$ in the worst case (e.g., when the stack grows linearly without branching early). 
    *   *Note:* This could be optimized to $O(1)$ space using an iterative bottom-up approach with two variables to track the previous two states, but this recursive implementation remains $O(n)$.

---

### 3. Component Deep Dive

#### `solve(int i, int[] nums)`
*   **State Definition:** `dp[i]` stores the maximum amount of money stolen from houses `0` to `i`.
*   **Base Cases:**
    *   `i < 0`: Returns 0; handles the boundary where the "rob $i-2$" choice exceeds the array bounds.
    *   `i == 0`: Returns `nums[0]`; serves as the anchor point for the recursion.
*   **Transition Logic:** 
    *   The state transition is defined as: $f(i) = \max(nums[i] + f(i-2), f(i-1))$.
    *   The decision is local (binary choice) but produces a global optimum by relying on the optimal sub-solutions stored in the cache.

#### `rob(int[] nums)`
*   **Initialization:** The `dp` array is initialized with `-1`. This is a critical sentinel value, as a house could potentially hold `0` value, and initializing to `0` would lead to re-computation of subproblems, degrading performance to $O(2^n)$.
*   **Constraint Handling:** The algorithm assumes $n \ge 1$ based on typical problem constraints. If `nums` is empty, `dp` initialization and the recursive call would need additional guard clauses to prevent `ArrayIndexOutOfBoundsException`.

---

### 4. Key Insights & Engineering Nuances

*   **Recursion Depth Limits:** While $O(n)$ is efficient, for extremely large arrays, this implementation will hit the JVM `StackOverflowError`. In production scenarios where $N > 10^5$, an **iterative (bottom-up)** approach is preferred to keep the logic on the heap and avoid stack exhaustion.
*   **Memoization vs. Tabulation:** The current approach is lazy-loading (computes only what is necessary). While valid, if the entire `dp` table is always filled, tabulation (iterative loop) is technically faster due to reduced overhead from recursive method frames and better cache locality.
*   **Optimization Opportunity:** Observe that we only ever access `i-1` and `i-2`. We do not need the entire `dp` array. We could replace the array with two integer variables (`prev1`, `prev2`) to track the previous two states, reducing auxiliary space complexity from **$O(n)$ to $O(1)$**.
*   **Sentinel Value Sensitivity:** Using `-1` as a cache miss indicator is robust here because house values are typically non-negative ($nums[i] \ge 0$). If the problem allowed negative values (e.g., "robbery costs"), `-1` would be a valid calculation result, and one would need a separate `boolean[] visited` array to track state computation.

---
