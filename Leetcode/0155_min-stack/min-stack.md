# min-stack

## stack_solution(twostacks).java
*Style: concise*

### MinStack Notes

**Overview**
Implements a stack that supports `push`, `pop`, `top`, and `getMin` operations in $O(1)$ time. Uses an auxiliary stack (`minStack`) to track the minimum element relative to the current state of the main stack.

**Key Components**
*   `stack`: Stores all pushed elements.
*   `minStack`: Stores values in non-increasing order; the top element always represents the current minimum.
*   `push(int)`: Adds to `stack`; pushes to `minStack` if the new value is $\le$ current min.
*   `pop()`: Removes from `stack`; pops `minStack` only if the removed value matches the current min.

**Logic to Remember**
*   **Handling Duplicates:** The condition `value <= minStack.peek()` in `push` is critical. Using `<=` instead of `<` ensures that if the same minimum value is pushed multiple times, it is correctly tracked and removed during corresponding pops.
*   **State Sync:** The `minStack` acts as a chronological filter of minimum values, ensuring $O(1)$ access for `getMin()` without requiring a full scan.

---
