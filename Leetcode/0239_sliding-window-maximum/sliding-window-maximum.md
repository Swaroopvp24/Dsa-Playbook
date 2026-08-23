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
