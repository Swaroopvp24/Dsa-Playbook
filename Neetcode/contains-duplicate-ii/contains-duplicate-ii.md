# contains-duplicate-ii

## standard_sliding_window.java
*Style: detailed*

# Technical Reference: Sliding Window Duplicate Detection

## Summary
The solution implements a **fixed-size sliding window** algorithm to solve the "nearby duplicate" problem. By maintaining a `HashSet` containing only elements within the index range $[i-k, i]$, the algorithm transforms the problem from an $O(n \cdot k)$ brute-force comparison into an $O(n)$ search. The approach uses a two-pointer technique where the `right` pointer expands the window and the `left` pointer contracts it once the window size exceeds $k$, ensuring the constraint $|i - j| \le k$ is strictly satisfied.

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Expansion:** The `right` pointer iterates through the array exactly once, performing $n$ iterations.
*   **Contraction:** Although there is a `while` loop for the `left` pointer, each element is added to the `HashSet` once and removed at most once throughout the entire lifecycle of the algorithm.
*   **Hash Operations:** `HashSet.add()`, `remove()`, and `contains()` operate in $O(1)$ amortized time.
*   **Total:** $O(n)$ where $n$ is the length of `nums`.

### Space Complexity: $O(\min(n, k))$
*   The `HashSet` stores at most $k$ elements at any given time because the `while` loop ensures the window size never exceeds $k$. If $k \ge n$, the set will contain at most $n$ elements.
*   The constant overhead of the `HashSet` (load factor, bucket array) is proportional to the number of elements stored.

## Component Deep Dive

### 1. The Sliding Window Mechanism
*   **The Constraint:** The condition `right - left > k` is critical. It evaluates the distance between indices. If the distance exceeds $k$, the element at `nums[left]` is no longer "nearby," necessitating its removal to maintain the invariant.
*   **The Order of Operations:** The `while` loop for contraction precedes the `contains` check. This ensures that the set strictly represents indices in the range $[right-k, right-1]$.

### 2. Edge Case Handling
*   **Small `k`:** If $k=0$, the condition `right - left > 0` triggers immediately. The `window` remains empty, and the function correctly returns `false` (as a duplicate cannot exist at the same index).
*   **Small `nums`:** If the array length is less than 2, the `for` loop executes, the `contains` check fails, and the function returns `false`, which is correct.
*   **Large `k`:** If $k \ge nums.length$, the `while` loop never executes. The `HashSet` essentially grows to the size of the array, acting as a standard duplicate detector.

## Key Insights

### Performance Optimization Nuances
*   **HashSet vs. HashMap:** While some variations of this problem use a `HashMap<Integer, Integer>` to store the *last seen index* of an element, using a `HashSet` with a sliding window is often more memory-efficient when $k$ is small. A `HashMap` would store all elements, whereas this `HashSet` approach self-prunes.
*   **Amortized Cost:** The efficiency relies heavily on the `HashSet` hash function. In environments with malicious inputs (e.g., hash collisions), the $O(1)$ performance could degrade to $O(k)$ for operations, leading to $O(n \cdot k)$ overall time.

### Common Pitfalls/Refinements
*   **Window Size Limit:** The `while` condition `right - left > k` is mathematically equivalent to keeping the window size at exactly $k+1$ elements before the next `right` addition.
*   **Memory Efficiency:** If $k$ is known to be significantly smaller than $n$, the memory footprint is strictly controlled. However, if $k$ is very large, the `HashSet` will experience frequent resizing. Pre-initializing the `HashSet` with a capacity of `Math.min(nums.length, k + 1)` could avoid rehashing overhead in memory-constrained environments.
*   **Floating Point/Overflow:** There is no risk of integer overflow here as `right` and `left` are within array bounds, but developers should verify that `k` is a non-negative integer. If $k < 0$, the logic would break immediately.

---
