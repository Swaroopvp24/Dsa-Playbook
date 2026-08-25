# daily-temperatures

## stack_solution(NGE_type).java
*Style: detailed*

# Engineering Deep Dive: Daily Temperatures Monotonic Stack Solution

## Summary
The solution employs a **Monotonic Decreasing Stack** algorithm to solve the "Next Greater Element" variation in linear time. By iterating backward, we maintain a stack of indices whose corresponding temperatures are in strictly decreasing order. When encountering a new temperature, we prune the stack of all elements (indices) that are cooler or equal to the current temperature. The element remaining at the top of the stack is guaranteed to be the index of the nearest warmer day to the right.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Proof:** Although there is a nested `while` loop, each index is pushed onto the stack exactly once and popped at most once. Each element's lifecycle within the stack is constant time. Therefore, the amortized cost per element is $O(1)$, resulting in an overall complexity of $O(N)$ for $N$ input temperatures.

### Space Complexity: $O(N)$
*   **Proof:** In the worst-case scenario—where the input array is sorted in strictly descending order—the stack will grow to size $N$ as no elements are popped. Thus, the auxiliary space complexity is $O(N)$.

---

## Component Deep Dive

### 1. The `ArrayDeque` vs. `Stack`
*   **Design Choice:** The implementation uses `ArrayDeque` instead of the legacy `java.util.Stack`. `ArrayDeque` is not synchronized and lacks the overhead of `Vector` methods, making it significantly more performant in single-threaded contexts.
*   **Storage:** The stack stores **indices** ($i$) rather than temperatures. This is critical because the final result requires the distance (difference in indices), not the temperature value itself.

### 2. The Backward Iteration Strategy
*   Processing from `temperatures.length - 2` down to `0` allows us to leverage previously computed "future" state. Since the stack is already populated with indices of days to the right, we do not need to look ahead manually; the stack provides an immediate, optimized look-up of the next candidate for a warmer day.

### 3. Monotonic Invariant Maintenance
*   The `while` loop condition `temperatures[stack.peek()] <= temperatures[day]` is the core logic. 
    *   If the current day is warmer than or equal to the top of the stack, that stack index is "obsolete" because it can never be the *next* warmer day for any day to the left of the current index. 
    *   Popping these indices ensures the stack remains strictly monotonic decreasing.

---

## Key Insights & Performance Nuances

### Handling Edge Cases
*   **Empty/Single Input:** If `temperatures.length` is 0 or 1, the loop range `temperatures.length - 2` to `0` will not execute. The `result` array remains initialized to all zeros, which is mathematically correct (no future days exist).
*   **Monotonic Decreasing Sequences:** In cases like `[90, 80, 70]`, the `while` loop condition never triggers a pop. The stack grows, but the `result` entries remain `0` because the `!stack.isEmpty()` check following the `while` loop will fail after the initial push. This correctly handles days with no warmer future.

### Performance Nuances
*   **CPU Cache Locality:** Because we iterate backward and use an `ArrayDeque` (which is backed by a circular array), the memory access pattern for `temperatures` is relatively predictable. However, the stack operations involve jumps in memory if the stack grows large.
*   **The Equal-To Condition:** Note that the logic uses `<=` for popping. If we used `<`, we would fail to find the *nearest* warmer day if multiple identical temperatures existed, as the stack would hold onto the older index. Using `<=` ensures we discard the irrelevant "same-temperature" days that can't be the *next* warmer day.

### Subtle Potential Bugs
*   **Stack Underflow:** The code correctly checks `!stack.isEmpty()` before `stack.peek()`. Removing this check would result in a `NoSuchElementException` for cases where no warmer day exists to the right. 
*   **Index Out of Bounds:** By initializing `stack.push(temperatures.length - 1)` and starting the loop at `length - 2`, the solution elegantly avoids the need for a sentinel value or an `if` check inside the loop to handle the final index.

---
