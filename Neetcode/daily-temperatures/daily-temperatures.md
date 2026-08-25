# daily-temperatures

## stack_solution(NGE_type).java
*Style: detailed*

# Engineering Deep Dive: Daily Temperatures Solution

## Summary
The solution employs a **Monotonic Decreasing Stack** algorithm to solve the "Next Greater Element" problem in linear time. Rather than using a naive $O(n^2)$ nested loop approach, the algorithm maintains a stack of indices representing temperatures in decreasing order. By iterating backward, we ensure that the stack always contains potential "future" candidates for the next warmer day. When the current temperature is higher than the temperature at the index stored at `stack.peek()`, that stack element is popped because it can no longer be the "next warmer day" for any preceding elements.

## Complexity Analysis

*   **Time Complexity: $O(n)$**
    *   Although there is a `while` loop nested inside the `for` loop, each index is pushed onto the stack exactly once and popped at most once throughout the entire execution. Thus, the total number of operations scales linearly with the input array size $n$.
*   **Space Complexity: $O(n)$**
    *   In the worst-case scenario (e.g., a strictly decreasing temperature array like `[90, 80, 70, 60]`), the stack will store all $n$ indices. The output array also requires $O(n)$ space.

## Component Deep Dive

### 1. The Monotonic Stack Strategy
By iterating from right-to-left (`temperatures.length - 2` down to `0`), the algorithm effectively builds a history of the "right-side" landscape.
*   **The Invariant:** The stack maintains indices such that `temperatures[stack[i]]` is strictly decreasing. 
*   **The Pruning Mechanism:** The `while` loop `temperatures[stack.peek()] <= temperatures[day]` is the critical filtering step. It discards indices that are "shadowed" by the current temperature, as the current temperature is both warmer and closer, rendering the previous smaller values irrelevant for all future (leftward) indices.

### 2. Data Structure Choice: `ArrayDeque`
The code uses `java.util.ArrayDeque` as a `Deque` interface implementation.
*   **Efficiency:** Unlike `java.util.Stack`, which is synchronized and legacy-based (Vector-backed), `ArrayDeque` is an unsynchronized, array-based implementation. It provides $O(1)$ amortized push/pop operations without the overhead of thread-safety locks or the performance penalties associated with `Stack`.

### 3. Edge-Case Handling
*   **Single Element:** If `temperatures.length` is 1, the loop `for (int day = temperatures.length - 2; ...)` will not execute. The `result` array remains initialized with `0`, which is the correct output (no future days exist).
*   **Monotonic Decreasing Input:** If the array is `[70, 60, 50]`, the `while` loop condition is never met. The stack fills with indices, but the `!stack.isEmpty()` check will only ever find the previous day on the stack, and `result` stays `0` because no higher temperature is ever found.
*   **Empty Array:** While not explicitly guarded by an `if` statement, `new int[0]` handles empty input gracefully, returning an empty array without runtime exceptions.

## Key Insights

*   **Index Storage vs. Value Storage:** Storing the *index* on the stack is superior to storing the *value* because the problem requires calculating the distance (`stack.peek() - day`). Storing indices grants access to both the value (`temperatures[index]`) and the position.
*   **The "Pop" Logic:** The `pop()` operation represents the algorithm "forgetting" temperatures that are no longer useful. Because a larger (or equal) temperature appeared more recently, those smaller, older values will never be the "next warmer" day for any element to the left of the current index.
*   **Performance Nuance:** The `Deque` acts as a sliding window of candidates. Because we move from right to left, `stack.peek()` always points to the *closest* warmer temperature to the right. 
*   **Potential Optimization:** For extremely large datasets where the stack depth might trigger memory constraints, one could theoretically use a primitive-backed stack (like an `int[]` with a pointer `top`) to avoid the autoboxing of `Integer` objects associated with `Deque<Integer>`, though the performance gain is marginal compared to the $O(n)$ algorithmic complexity.

---
