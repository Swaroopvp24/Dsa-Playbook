# binary-search

## standard_binary_search.java
*Style: detailed*

# Deep-Dive Technical Reference: Binary Search Implementation

## Summary
The provided implementation is a classic **Iterative Binary Search** algorithm. It operates on the principle of *Divide and Conquer*, specifically designed for searching a sorted array by repeatedly halving the search space. Unlike recursive implementations, this iterative approach eliminates function call overhead and prevents potential `StackOverflowError` exceptions in systems with constrained stack memory, making it the preferred choice for production-grade library code.

## Complexity Analysis

### Time Complexity: $O(\log n)$
*   **Derivation:** At each iteration of the `while` loop, the algorithm narrows the search range by a factor of 2. For an input size $n$, the number of operations follows the recurrence $T(n) = T(n/2) + O(1)$. Solving this via the Master Theorem (or simple iterative reduction) yields $\log_2(n)$ steps.
*   **Best Case:** $O(1)$, occurring when the middle element of the initial array is the target.

### Space Complexity: $O(1)$
*   **Derivation:** The algorithm utilizes a constant amount of auxiliary space. It maintains only three integer variables (`l`, `r`, and `m`), regardless of the input size $n$. There is no additional memory allocation or recursion stack growth.

---

## Component Deep Dive

### 1. Midpoint Calculation (`l + (r - l) / 2`)
*   **The Nuance:** The common `(l + r) / 2` approach is vulnerable to **integer overflow** when `l + r` exceeds `Integer.MAX_VALUE` (2,147,483,647). By utilizing `l + (r - l) / 2`, we maintain the midpoint within the bounds of the existing pointers, ensuring robustness for large arrays that approach the maximum integer limit.

### 2. Search Bounds (`l <= r`)
*   **The Logic:** The condition `l <= r` is critical. Using `l < r` would cause the algorithm to terminate prematurely, failing to check the final element if the target is located at the absolute boundary of the array. The inclusive nature of the pointers (`r = nums.length - 1`) necessitates the inclusive loop condition to ensure a comprehensive scan.

### 3. Pointer Adjustment
*   **Logic:** Since the array is sorted, the algorithm uses the transitive property of equality/inequality to eliminate half the search space:
    *   `r = m - 1`: Target must reside in the lower partition.
    *   `l = m + 1`: Target must reside in the upper partition.

---

## Key Insights

### 1. Stability and Edge Cases
*   **Empty Arrays:** If `nums` is empty, `r` becomes `-1`. The condition `l <= r` (0 <= -1) evaluates to false immediately, returning `-1` correctly. No additional null/empty checks are required for logical safety.
*   **Duplicates:** In its current form, this function returns the *first* index it encounters that matches the target. It does not guarantee finding the *leftmost* or *rightmost* index in arrays with duplicate values (e.g., in `[2, 2, 2]`, it returns the middle `2`).

### 2. Cache Locality
Because this algorithm performs a binary jump pattern, it does not demonstrate ideal **Spatial Locality** compared to linear scans. In extremely large, memory-resident datasets, the jumps can trigger cache misses, though the $O(\log n)$ reduction far outweighs this performance penalty compared to an $O(n)$ linear scan.

### 3. Optimization Nuance
While current compilers optimize the midpoint division into a bitwise shift (`(r - l) >> 1`), writing `l + ((r - l) >>> 1)` (unsigned right shift) is a common pattern in high-performance Java code to avoid sign-bit interference if negative indices were somehow introduced, though it is not strictly necessary for standard array indexing.

---
