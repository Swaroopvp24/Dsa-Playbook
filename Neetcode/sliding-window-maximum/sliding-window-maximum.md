# sliding-window-maximum

## standard_sliding_window(deque).java
*Style: detailed*

# Engineering Deep Dive: Monotonic Deque Sliding Window Maximum

## Summary
The provided implementation solves the "Sliding Window Maximum" problem using a **Monotonic Decreasing Deque**. Instead of a brute-force $O(N \cdot K)$ approach, this algorithm maintains a sliding window of indices in a `java.util.ArrayDeque` such that the corresponding values in `nums` are strictly decreasing. 

By ensuring the front of the deque always holds the index of the maximum element for the current window, we achieve optimal linear time complexity. The strategy is to eagerly discard elements that can never be the maximum (those smaller than a newer element entering the window from the right).

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Amortized Analysis**: Every element in `nums` is pushed into the `deque` exactly once and polled from the `deque` exactly once. 
*   Even though there are nested `while` loops, the total number of operations on the deque across the entire execution is proportional to $2N$. Therefore, the complexity is $O(N)$, where $N$ is the length of the input array.

### Space Complexity: $O(K)$
*   The `deque` stores at most $K$ indices at any given time (representing the current window). 
*   In the worst-case scenario (e.g., a strictly decreasing input array), the deque could hold up to $K$ elements. Thus, space complexity is $O(K)$.

## Component Deep Dive

### 1. The Monotonic Invariant
The core of the logic lies in the second `while` loop:
```java
while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[right]) {
    deque.pollLast();
}
```
This preserves the **Monotonic Decreasing Property**. If a new element `nums[right]` is greater than or equal to elements currently in the deque, those smaller elements are effectively "dead"—they can never be the maximum for any future window because `nums[right]` is both larger and "younger" (will persist longer in the window).

### 2. Window Boundary Management
The first `while` loop handles the expiration of elements:
```java
while (!deque.isEmpty() && deque.peekFirst() < left) {
    deque.pollFirst();
}
```
Since the deque stores indices, we compare the head of the deque against the `left` boundary of the sliding window. This ensures that the element at `peekFirst()` is always valid for the current `[left, right]` range.

### 3. Edge Case Handling
*   **$K=1$**: The algorithm correctly processes this, as the deque will effectively store the current element and record it immediately.
*   **$K = N$**: The loops will complete, and the final window will be captured upon the last iteration.
*   **Empty `nums` / Invalid `K`**: While not explicitly checked in the provided code, the array initialization `new int[nums.length - k + 1]` would throw a `NegativeArraySizeException` if `k > nums.length`, serving as an implicit guardrail.

## Key Insights

*   **Index-based Deque vs. Value-based**: Storing *indices* rather than *values* is critical. If we stored values, we would have no way to determine if the maximum value in the deque has "slid out" of the window boundaries.
*   **Inequality Handling (`<=`)**: Note the use of `<=`. If the input contains duplicate maximum values, the `<=` ensures that the *older* index is removed and replaced by the *newer* index. This is necessary because the older index will slide out of the window sooner.
*   **ArrayDeque Choice**: Using `java.util.ArrayDeque` is preferable to `java.util.LinkedList` here. `ArrayDeque` has a smaller memory footprint (no node object overhead) and provides better cache locality, as it is backed by a circular array.
*   **Performance Nuance**: The initialization `new int[nums.length - k + 1]` assumes `nums` is non-null. In production, adding an explicit `if (nums == null || nums.length == 0 || k <= 0)` guard is recommended to avoid `NullPointerException` or invalid index logic.

---

## dp_solution.java
*Style: detailed*

# Deep-Dive: Block-Based Max Sliding Window

## Summary
The provided solution implements the **Block Partitioning** (or Prefix/Suffix Max) technique to solve the Sliding Window Maximum problem. Unlike the standard Monotonic Queue approach (which yields $O(N)$ time with $O(K)$ space), this approach achieves $O(N)$ time complexity while maintaining $O(N)$ space but with **improved cache locality** and no complex deque management.

The algorithm divides the array into virtual blocks of size $k$. Any sliding window of size $k$ starting at index $i$ is guaranteed to span exactly two blocks or reside entirely within one. By pre-calculating the running maximum from the start of each block (`lMax`) and the end of each block (`rMax`), the maximum of any arbitrary window $[i, i+k-1]$ is simply $\max(\text{rMax}[i], \text{lMax}[i+k-1])$.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Preprocessing:** We perform two linear passes over the input array `nums` of size $N$. 
    *   The first pass fills `lMax` in $O(N)$.
    *   The second pass fills `rMax` in $O(N)$.
*   **Querying:** We perform a final pass of size $N-k+1$ to populate the `result` array.
*   **Total:** $O(N) + O(N) + O(N-K) \approx O(N)$. Each element is visited a constant number of times.

### Space Complexity: $O(N)$
*   We allocate two auxiliary arrays, `lMax` and `rMax`, each of size $N$.
*   The `result` array takes $O(N-K)$ space.
*   Total space is $O(N)$. While this is technically higher than the $O(K)$ space used by a Monotonic Deque, the memory access pattern is purely sequential, making it highly friendly to modern CPU cache hierarchies (prefetchers).

---

## Component Deep Dive

### 1. `lMax` (Prefix-Block Max)
*   **Logic:** `lMax[i]` represents $\max(nums[j \dots i])$ where $j$ is the start of the block containing $i$.
*   **Reset Condition:** `i % k == 0`. This marks the boundary where the prefix max must be re-initialized to the current element because the prefix doesn't extend across block boundaries.
*   **Critical Path:** Ensures that for any window end index, we have the running max from the left side of the block.

### 2. `rMax` (Suffix-Block Max)
*   **Logic:** `rMax[i]` represents $\max(nums[i \dots j])$ where $j$ is the end of the block containing $i$.
*   **Reset Condition:** `i % k == k - 1`. This resets the suffix max at the end of every block.
*   **Corner Case:** Handling the final block if $N$ is not perfectly divisible by $k$. Because the loop runs backwards to index 0, the last block is implicitly bounded by the array length, and the condition `i % k == k - 1` correctly anchors the suffix starting point.

### 3. Window Synthesis
*   **The Invariant:** Every window $[i, i+k-1]$ covers exactly two elements from the precomputed arrays.
    *   `rMax[i]` captures the max from $i$ to the end of the block containing $i$.
    *   `lMax[i + k - 1]` captures the max from the start of the block containing $i+k-1$ to $i+k-1$.
    *   Since $i+k-1$ is at most $k-1$ distance from $i$, these two values cover the entire range $[i, i+k-1]$ without overlapping beyond what is captured by the max operation.

---

## Key Insights

*   **Cache Efficiency:** The Monotonic Deque approach involves frequent heap/stack allocations or pointer updates and random-access behavior if implemented with a list. This approach uses two flat arrays accessed linearly, resulting in fewer cache misses and predictable memory prefetching.
*   **When to use this:** Use this approach in performance-critical environments (like C++ or low-level Java systems) where you need to avoid the overhead of object allocations associated with `Deque` implementations (e.g., `ArrayDeque` or `LinkedList`).
*   **Handling `k=1`:** The algorithm naturally handles $k=1$ without modification, as the block size is 1, and the max of the window is simply the element itself.
*   **Edge Case - $N < k$:** The `for` loop `i <= n - k` ensures that if the input array is smaller than the window size, the loop body is never entered, returning an empty array correctly.
*   **Subtle Bug:** Ensure the `rMax` loop correctly calculates `n-1` as the start. If the indices are calculated incorrectly, the boundaries between blocks will be offset, causing the "max" calculation to look into an adjacent block that is not part of the current window. The provided logic `i % k == k - 1` is robust for all $N, K$.

---
