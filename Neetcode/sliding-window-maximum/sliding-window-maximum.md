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
