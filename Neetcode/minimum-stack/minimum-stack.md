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
