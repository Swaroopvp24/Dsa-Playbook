# binary-search

## standard_binary_search.java
*Style: detailed*

# Technical Reference: Binary Search Implementation

## Summary
The provided implementation is a classic **Iterative Binary Search** algorithm. It operates on the principle of *Divide and Conquer* by maintaining a search interval $[l, r]$ and repeatedly halving the search space. By comparing the `target` value to the middle element `nums[m]`, the algorithm eliminates half of the remaining candidates in every iteration, provided the input array is sorted in non-decreasing order.

---

## Complexity Analysis

### Time Complexity: $O(\log n)$
*   **Derivation:** At each step, the search interval size is reduced by half ($n, n/2, n/4, \dots, 1$). The number of iterations required to reduce an array of size $n$ to a single element is defined by the inequality $n/2^k = 1$, which solves to $k = \log_2 n$.
*   **Best Case:** $O(1)$ when the middle element of the initial array is the target.

### Space Complexity: $O(1)$
*   **Derivation:** The implementation uses a fixed number of primitive integer variables (`l`, `r`, `m`) regardless of the input size $n$. This is an **in-place** algorithm that does not allocate auxiliary data structures or rely on recursion stack frames, making it memory-efficient.

---

## Component Deep Dive

### 1. Midpoint Calculation: `int m = l + (r - l) / 2;`
*   **Logic:** A naive approach like `(l + r) / 2` is susceptible to **integer overflow** when `l + r` exceeds `Integer.MAX_VALUE` ($2^{31}-1$). 
*   **Optimization:** Using `l + (r - l) / 2` maintains the mathematical equivalence to the average while ensuring the intermediate addition never exceeds the array bounds.

### 2. Search Interval Management
*   **Initialization:** `r = nums.length - 1` establishes a **closed interval** $[l, r]$. 
*   **Loop Invariant:** The condition `l <= r` is critical. It ensures that the loop continues even when `l == r` (the single-element search space), preventing premature termination before checking the final candidate.
*   **Convergence:** The adjustment logic `r = m - 1` and `l = m + 1` is strictly required. Failing to increment/decrement $m$ would lead to an infinite loop when the target is absent or at the boundaries.

### 3. Edge-Case Handling
*   **Empty Array:** If `nums.length == 0`, `r` becomes `-1`, the condition `0 <= -1` is false, and the function correctly returns `-1`.
*   **Target out of bounds:** The loop terminates naturally when `l` exceeds `r`, consistently returning `-1`.
*   **Single Element Arrays:** Handled correctly by the `l <= r` condition.

---

## Key Insights

### The "Off-by-One" Trap
The most common implementation error in binary search is the selection of the loop boundary (`l < r` vs `l <= r`) and the modification of boundaries (`r = m` vs `r = m - 1`). 
*   Because this implementation uses `r = m - 1`, we explicitly exclude `m` from the next search iteration because we have already tested `nums[m] == target`. If the logic were `r = m`, the loop would require `l < r` to avoid infinite loops.

### Cache Locality
While Binary Search is theoretically superior to Linear Search ($O(\log n)$ vs $O(n)$), it is **cache-unfriendly** for large datasets. Accessing `nums[m]` jumps across memory addresses in powers of two, leading to potential CPU cache misses. For very small arrays (typically < 32 elements), a linear scan may outperform binary search due to hardware prefetching and reduced branching.

### Duplicate Values
This specific implementation returns *any* index of the target. If the input array contains duplicate values, there is no guarantee which index will be returned. If the requirement is to find the *first* or *last* occurrence, the `if (nums[m] == target)` block must be modified to continue searching the left or right partitions, respectively.

---
