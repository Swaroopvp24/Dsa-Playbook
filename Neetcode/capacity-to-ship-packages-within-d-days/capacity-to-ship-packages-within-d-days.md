# capacity-to-ship-packages-within-d-days

## standard_binary_search.java
*Style: detailed*

# Engineering Deep Dive: Capacity Optimization (Binary Search on Answer)

## Summary
The solution employs a **Binary Search on the Answer space** to solve the "Capacity to Ship Packages within $D$ Days" problem. Instead of performing a brute-force search on potential capacities, we recognize that the function `calculateRequiredDays(capacity)` is **monotonically non-increasing**—as capacity increases, the required days stay the same or decrease. This allows us to treat the possible capacity range $[max(weights), \sum weights]$ as a sorted search space and converge on the global minimum capacity in logarithmic time.

## Complexity Analysis

### Time Complexity: $O(N \log(W - M))$
*   **$N$**: Number of elements in `weights`.
*   **$W$**: Total weight of all packages ($\sum weights$).
*   **$M$**: Maximum weight of a single package ($\max(weights)$).
*   **Breakdown**: 
    *   The binary search space is defined by $[M, W]$. The number of iterations is $\log_2(W - M)$.
    *   Inside each iteration, `calculateRequiredDays` iterates through the `weights` array exactly once, resulting in $O(N)$ operations.
    *   Total complexity: **$O(N \log(W - M))$**. Given typical constraints (e.g., $N=5 \cdot 10^4$), this is highly efficient compared to a linear scan.

### Space Complexity: $O(1)$
*   The algorithm uses a constant amount of extra space for pointers (`left`, `right`, `mid`) and accumulators (`totalWeight`, `currentLoad`). No auxiliary data structures are allocated, making this space-optimal.

## Component Deep Dive

### 1. `calculateRequiredDays(int[] weights, int capacity)`
*   **Logic**: This is a greedy simulator. It treats the problem as a "Bin Packing" variation where the bin size is fixed.
*   **State Management**: It maintains `currentLoad` to track the filling status of the current day. The counter `requiredDays` initializes to `1` because even if the ship is empty, at least one day is implied if `weights` is non-empty.
*   **Greedy Choice Property**: Because the order of packages is fixed, the greedy approach of filling the ship until the next package exceeds `capacity` is optimal for a given capacity.

### 2. Search Space Initialization
*   **Lower Bound (`left`)**: Set to `max(weights)`. This is the absolute physical constraint; if capacity is less than the largest item, that item can never be shipped.
*   **Upper Bound (`right`)**: Set to `sum(weights)`. This is the "best-case scenario" where all items fit in a single day. 

### 3. Binary Search Strategy
*   **Invariant**: The target minimum capacity always resides in the range `[left, right]`.
*   **Convergence**: We use the `left < right` template. When `requiredDays <= days`, we know the current `capacity` might be the answer, so we include it in the search space (`right = capacity`). When `requiredDays > days`, we must discard the current `capacity` (`left = capacity + 1`). This ensures the pointer `left` settles on the smallest viable value.

## Key Insights & Nuances

*   **Integer Overflow**: In the `calculateRequiredDays` method, if the sum of weights were to exceed `Integer.MAX_VALUE` (e.g., in a system with massive weight constraints), `currentLoad + weight` could overflow. While not an issue in standard LeetCode constraints, a production implementation should use `long` for `totalWeight` and `currentLoad` to ensure robustness.
*   **Branch Prediction**: The `if (currentLoad + weight <= capacity)` check inside the loop is highly predictable if the distribution of weights is uniform relative to the capacity, but becomes volatile near the target capacity.
*   **The "One-Day" Case**: The logic naturally handles cases where `days` is very large (capacity converges to `max(weights)`) or very small (capacity converges to `sum(weights)`).
*   **Optimization Pitfall**: Do not attempt to calculate `requiredDays` using division or modulo. Because the order of packages is immutable, you must respect the contiguous nature of the input array. The greedy simulation is the only correct way to validate a capacity.

---
