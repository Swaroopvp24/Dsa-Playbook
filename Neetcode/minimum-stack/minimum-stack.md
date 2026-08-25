# minimum-stack

## stack_solution(twostacks).java
*Style: concise*

### MinStack Study Notes

**Functionality**
A stack implementation that tracks the minimum element in $O(1)$ time by maintaining a secondary auxiliary stack that stores minimums in non-increasing order.

**Key Components**
*   `stack`: Stores all pushed elements.
*   `minStack`: Stores values such that the top is always the minimum of the primary `stack`.
*   `push(int)`: Updates `minStack` if the new value is $\le$ the current minimum.
*   `pop()`: Synchronizes `minStack` removal only if the popped value matches the current minimum.

**Key Logic**
*   **Duplicate Handling:** Using `<=` in the `push` condition is critical; it ensures that if the same minimum value is pushed multiple times, each occurrence is tracked and correctly popped.
*   **Space/Time Trade-off:** Sacrifices $O(N)$ extra space to achieve $O(1)$ performance for `getMin()`.

---

## stack_solution(optimal).java
*Style: detailed*

# Technical Deep-Dive: Mathematical Encoding Min-Stack

## Summary
The provided `MinStack` implementation employs a **Difference-Encoding** strategy to achieve constant-time $O(1)$ retrieval of both the stack top and the global minimum, while maintaining a space-efficient memory footprint. 

Unlike traditional approaches that use an auxiliary stack to track minimums (doubling space requirements), this implementation stores the mathematical deviation of a pushed element from the *running minimum*. By storing the delta rather than the absolute value, the algorithm can "reverse engineer" the previous minimum during a `pop` operation if the current element was the minimum, effectively embedding the state transition history directly into the stack.

## Complexity Analysis

### Time Complexity: $O(1)$
*   **`push`**: Arithmetic operations and stack operations are constant time.
*   **`pop`**: Single `pop` and conditional check are constant time.
*   **`top` & `getMin`**: Direct return of `currentMin` or computed reconstruction are constant time.

### Space Complexity: $O(N)$
*   **Analysis**: We store exactly one `Long` per element in the `ArrayDeque`. 
*   **Nuance**: Using `Long` is necessary to prevent integer overflow during the calculation `value - currentMin`. If `value` is `Integer.MIN_VALUE` and `currentMin` is `Integer.MAX_VALUE`, the difference exceeds the range of a 32-bit `int`.

## Component Deep Dive

### 1. Mathematical Encoding Logic
The state is managed using the variable `currentMin` and the delta stored in the stack:
*   **`push(val)`**: 
    *   If `val < currentMin`, we push `val - currentMin` (which will be **negative**). We then update `currentMin = val`.
    *   If `val >= currentMin`, we push `val - currentMin` (which will be **zero or positive**).
*   **`pop()`**:
    *   If the popped `difference` is negative, it signals that the element being removed was the `currentMin` at the time of insertion. To revert to the previous minimum, we calculate: `prevMin = currentMin - difference`.

### 2. Edge Case Handling
*   **Integer Overflow**: The use of `Long` in the `Deque<Long>` is critical. By casting the input `int` to `long` before subtraction, we avoid overflow scenarios that would corrupt the `currentMin` pointer.
*   **Empty Stack**: Handled by the initial check in `push`. The first element inserted effectively initializes the `currentMin`.
*   **Negative Values**: The logic handles negative input values seamlessly because the difference encoding relies on the relative delta, not the absolute magnitude of the input.

## Key Insights

### The "Difference" Trick
The most subtle aspect of this implementation is that the `stack` does not contain the actual values pushed. It contains a reconstruction key. 
*   If `top` is called and `difference < 0`, we know the actual value is `currentMin`.
*   If `top` is called and `difference >= 0`, we know the actual value is `currentMin + difference`.

### Memory Efficiency vs. Clarity
*   **Advantage**: This implementation is significantly more memory-efficient than the standard "Two-Stack" approach (where one stack stores values and another stores minimums), especially in scenarios where the minimum changes infrequently.
*   **Trade-off**: The logic is less intuitive than a two-stack system. Maintenance requires developers to understand the overflow-protection mechanism (`long` usage) and the mathematical reconstruction.

### Potential Pitfalls
1.  **Strict Overflow Limits**: While `long` protects against `int` overflow, if the input domain itself utilized the full `Long` range, this implementation would fail. Given the input is `int`, the `long` approach is perfectly safe.
2.  **Type Safety**: The conversion between `long` and `int` via explicit casting in `top()` and `getMin()` assumes the stack only ever contains values that originated as `int`. Ensure that the caller does not attempt to push values outside the `Integer` range, as the `(int)` cast will truncate high-order bits silently.

---
