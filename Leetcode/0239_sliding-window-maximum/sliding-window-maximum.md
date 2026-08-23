# sliding-window-maximum

## standard_sliding_window(deque).java
*Style: detailed*

# Technical Deep-Dive: Monotonic Queue Sliding Window Maximum

## Summary
The solution employs a **Monotonic Decreasing Queue** to solve the sliding window maximum problem in linear time. Rather than re-scanning the window (which would be $O(n \cdot k)$), the algorithm maintains a double-ended queue (`Deque`) that stores indices of elements in the `nums` array. 

The invariant maintained is that the indices in the deque correspond to values that are strictly decreasing. Consequently, the front of the deque (`peekFirst()`) always points to the index of the maximum element within the current window $[left, right]$.

---

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Amortized Analysis:** Although there are nested `while` loops, each index from the input array `nums` is pushed into the `Deque` exactly once and popped from the `Deque` at most once. 
*   Because each element undergoes a maximum of two operations (enqueue and dequeue), the total number of operations scales linearly with the size of the input array, regardless of the window size $k$.

### Space Complexity: $O(k)$
*   In the worst-case scenario (e.g., a strictly decreasing input array), the deque will store at most $k$ indices at any given time. The output array requires $O(n - k + 1)$ space, but auxiliary space complexity is dominated by the deque size $k$.

---

## Component Deep Dive

### 1. The Monotonic Deque Invariant
The core mechanism is the `while` loop that removes elements from the back of the deque:
```java
while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[right]) {
    deque.pollLast();
}
```
*   **Logic:** If the current element `nums[right]` is greater than or equal to the elements represented by indices currently in the deque, those older, smaller elements can never be the maximum again. Removing them keeps the deque "clean" and ensures the search space is always optimal.

### 2. Window Boundary Enforcement
```java
while (!deque.isEmpty() && deque.peekFirst() < left) {
    deque.pollFirst();
}
```
*   **Logic:** This ensures the "Sliding" aspect of the window. Since indices are monotonic, the oldest index is always at the front. If the front index is less than `left`, it has fallen outside the current window $[left, right]$ and must be discarded.

### 3. Result Recording
*   The `if (right - left + 1 >= k)` check ensures that we do not start populating the result array until the first complete window of size $k$ is processed.
*   By incrementing `left` only after `right >= k - 1`, we effectively emulate a sliding window of constant size.

---

## Key Insights & Performance Nuances

*   **Index-Based Storage:** Storing indices instead of values is crucial. It allows us to perform the "stale index" check (`peekFirst() < left`) in $O(1)$. If we only stored values, we would have no way of knowing if the maximum value in the deque actually belongs to the current window or a previous, expired one.
*   **Comparison Strategy:** Using `<=` in `nums[deque.peekLast()] <= nums[right]` is intentional. If we used `<`, we would keep redundant equal values. Removing equal values ensures the deque length remains strictly limited by the range of unique values in the current window, optimizing memory usage.
*   **Edge Cases:**
    *   **$k=1$:** The logic naturally reduces to returning the original array, as the deque will always contain exactly one index.
    *   **Descending Array:** The deque will essentially become a FIFO queue of size 1 (the current element), as every new element is smaller than the previous.
    *   **Ascending Array:** The deque will act as a stack, popping all previous elements and storing only the current (largest) element.
*   **Implementation Note:** `java.util.ArrayDeque` is preferred over `java.util.LinkedList` for this implementation because `ArrayDeque` has lower memory overhead and better cache locality, as it is backed by a circular array rather than individual node objects.

---

## dp_solution.java
*Style: detailed*

# Engineering Deep-Dive: Block-Based Sliding Window Maximum

## Summary
The provided solution addresses the Sliding Window Maximum problem using a **block-based prefix/suffix decomposition** technique. Unlike the classic monotonic queue approach ($O(N)$ with a `Deque`), which manages window state dynamically, this approach treats the array as a sequence of fixed-size blocks of length $k$. 

By precomputing the maximums of prefixes within blocks (`lMax`) and suffixes within blocks (`rMax`), any arbitrary sliding window of size $k$—which necessarily spans at most two adjacent blocks—can be solved in $O(1)$ by comparing the precomputed suffix of the left block and the prefix of the right block.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Preprocessing:** We iterate through the array twice: once to build `lMax` and once to build `rMax`. Each pass is $O(N)$.
*   **Querying:** A final pass over $N-k+1$ elements computes the result by accessing precomputed indices.
*   Total operations $\approx 3N$, simplifying to **$O(N)$**.

### Space Complexity: $O(N)$
*   We allocate two auxiliary arrays of size $N$ (`lMax` and `rMax`) and one result array of size $N-k+1$.
*   Total space complexity is **$O(N)$**. This is slightly less memory-efficient than the $O(K)$ space required by the monotonic deque approach, but it offers better cache locality due to sequential array access patterns.

---

## Component Deep Dive

### 1. The Block Decomposition Strategy
The array is conceptually partitioned into segments of size $k$.
*   **`lMax[i]`**: Captures the running maximum from the start of the current $k$-sized block up to index $i$. If `i % k == 0`, a new block begins, resetting the accumulation.
*   **`rMax[i]`**: Captures the running maximum from index $i$ to the end of the current $k$-sized block. The reset condition `i % k == k - 1` identifies the boundary of the previous block during the right-to-left traversal.

### 2. Window Intersection Logic
For any window starting at index $i$ and ending at $j = i + k - 1$:
*   The window covers a portion of block $B_m$ and potentially the start of block $B_{m+1}$.
*   `rMax[i]` provides the maximum of the elements in the window that reside within the current block.
*   `lMax[i + k - 1]` provides the maximum of the elements in the window that reside in the next block.
*   **Result:** `max(rMax[i], lMax[i + k - 1])` effectively covers the entire window range $[i, i+k-1]$.

### 3. Edge Case Handling
*   **$k=1$**: The logic naturally collapses as `rMax[i] == lMax[i] == nums[i]`, resulting in the original array.
*   **$k=n$**: The loop for `result` runs exactly once for $i=0$. `lMax[n-1]` and `rMax[0]` will correctly hold the global maximum.
*   **Array size not divisible by $k$**: The logic remains robust because the modular arithmetic `i % k` implicitly handles the final "partial" block at the end of the array.

---

## Key Insights

### Performance Nuance: Cache Friendliness
While a `Deque`-based solution is often cited as the "optimal" $O(N)$ approach, this block-based implementation is often faster in practice on modern hardware. Because it iterates linearly through memory without the overhead of dynamic object allocation (nodes in a linked deque) or random pointer jumping, it is **cache-friendly** and exhibits high spatial locality.

### Comparison to Monotonic Deque
*   **Deque Approach:** Maintains indices of elements in decreasing order. Best when $k$ is small relative to $N$ or the stream is infinite.
*   **Block Approach:** Superior in static array scenarios where memory overhead is acceptable, as it avoids the `LinkedList`/`ArrayDeque` management overhead. 

### Subtle Bug Traps
*   **Index Out of Bounds:** Note that `lMax` and `rMax` rely on indices `i-1` and `i+1` respectively. The loop boundaries (`i=1` to `n-1` and `n-2` down to `0`) are critical. 
*   **Off-by-one errors:** The condition `i % k == k - 1` for `rMax` is the most common point of failure. It correctly identifies the *last* element of a block. If this were off by one, the logic would conflate values from different blocks, leading to incorrect maximums.

### Optimization Potential
If space were constrained, `rMax` could be computed on-the-fly or stored in a smaller buffer, but the $O(N)$ trade-off is standard for clarity and maintenance in professional codebases.

---
