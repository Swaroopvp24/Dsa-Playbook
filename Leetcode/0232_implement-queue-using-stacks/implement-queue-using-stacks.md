# implement-queue-using-stacks

## stack_solution(brute_force).java
*Style: concise*

### Notes: Queue Implementation using Stacks

**Overview**
This class implements a FIFO (First-In, First-Out) queue using two LIFO (Last-In, First-Out) stacks. It maintains queue semantics by transferring elements between stacks to expose the bottom-most element of `stack1`.

**Key Components**
*   `stack1`: Primary storage for all incoming elements.
*   `stack2`: Auxiliary buffer used to temporarily hold elements during `pop()` and `peek()` operations.
*   `push(int x)`: O(1) operation; adds elements directly to `stack1`.
*   `pop()` / `peek()`: O(n) operations; transfers all but the last element to `stack2` to access the "front" of the queue, then restores `stack1`.

**Logic Notes**
*   **Performance Trade-off:** This specific implementation favors `push` efficiency at the cost of `pop`/`peek` performance.
*   **Restoration Pattern:** Because the state of `stack1` must be preserved for future operations, `stack2` is always drained back into `stack1` immediately after the target element is accessed.
*   **Constraint:** This approach causes significant overhead compared to a two-stack queue implementation where elements are moved only when `stack2` is empty (amortized O(1)).

---
