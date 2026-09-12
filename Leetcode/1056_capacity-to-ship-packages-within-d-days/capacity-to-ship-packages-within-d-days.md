# capacity-to-ship-packages-within-d-days

## standard_binary_search.java
*Style: detailed*

# Technical Reference: Capacity-Constrained Optimization (Ship Within Days)

## Summary
The solution employs a **Binary Search on the Answer** space to identify the minimum ship capacity required to meet a temporal deadline. Since the function mapping `capacity -> days_required` is monotonically non-increasing (as capacity increases, the days required to ship remain constant or decrease), the problem is reduced to finding the "leftmost" value in the search space $[max(weights), \sum weights]$ that satisfies the condition `days_required <= days`.

## Complexity Analysis

### Time Complexity: $O(N \cdot \log(\sum W))$
*   **Search Space:** The binary search operates on the range defined by the heaviest package ($max(W)$) and the sum of all packages ($\sum W$). The number of iterations is $\log(\text{range}) \approx \log(\sum W)$.
*   **Validation:** In each iteration, `calculateRequiredDays` performs a linear scan of the `weights` array, taking $O(N)$ time.
*   **Total:** $O(N \cdot \log(\sum W))$, where $N$ is the number of packages and $W$ is the set of package weights.

### Space Complexity: $O(1)$
*   The algorithm operates strictly in-place. We only maintain a few integer pointers (`left`, `right`, `capacity`) and accumulators (`currentLoad`, `totalWeight`), regardless of the input size.

---

## Component Deep Dive

### 1. `calculateRequiredDays(int[] weights, int capacity)`
*   **Mechanism:** A greedy approach is used. Since the sequence of shipments is fixed by the input array order, we fill the current "ship" until adding the next weight exceeds the `capacity`.
*   **Boundary Handling:** 
    *   If a single weight exceeds `capacity`, the function will effectively "reset" the `currentLoad`, but the logic relies on the caller ensuring `capacity >= maxWeight`.
    *   `requiredDays` is initialized to 1 because even if the array is empty or partial, at least one day is consumed by the final batch of goods.

### 2. Binary Search Logic
*   **Search Range:**
    *   `left = max(weights)`: The absolute lower bound. If the capacity is less than the heaviest package, it is physically impossible to ship that package.
    *   `right = sum(weights)`: The absolute upper bound. This represents shipping everything in a single day.
*   **Convergence:** The implementation uses the condition `while (left < right)` with `right = capacity` and `left = capacity + 1`. This is the standard pattern for finding the **lower bound** (the first element that satisfies the predicate `requiredDays <= days`).

---

## Key Insights

### Greedy Sub-optimality
While the ship-loading logic is greedy, it is **optimal** for this specific problem because the constraint is the total weight per day, not the number of items. Because we cannot reorder the packages, the greedy approach correctly produces the minimum number of days for any given capacity.

### Avoiding Overflow
*   The `right` bound is `totalWeight`. If the array contains many large integers, `totalWeight` could theoretically overflow a 32-bit `int`. While this solution uses `int` per the prompt, in a production environment with larger payloads, `long` should be used for `totalWeight` and `right` to prevent overflow during summation.

### Subtle Performance Nuance
The `capacity` calculation `left + (right - left) / 2` is used instead of `(left + right) / 2`. This is a classic safeguard to prevent integer overflow when `left` and `right` are sufficiently large, as `left + right` might exceed `Integer.MAX_VALUE`.

### Edge Cases to Consider
1.  **`days == weights.length`**: The binary search will correctly converge to the `maxWeight`.
2.  **`days == 1`**: The binary search will correctly converge to the `sum(weights)`.
3.  **Single element array**: The search range collapses to `left == right`, returning the weight of the only package, which is correct.

---
