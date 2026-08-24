# implement-queue-using-stacks

## stack_solution(brute_force).java
*Style: concise*

### Study Notes: Queue implementation using Stacks

**Summary**
This class implements a FIFO (First-In, First-Out) queue using two LIFO (Last-In, First-Out) stacks. It maintains the queue order by temporarily migrating elements to a helper stack during `pop` and `peek` operations to access the oldest element.

**Key Components**
*   `inputStack`: Stores the primary data.
*   `helperStack`: Acts as a temporary buffer to expose the bottom element of `inputStack`.
*   `push(int)`: O(1) operation; adds elements directly to the `inputStack`.
*   `pop()`/`peek()`: O(n) operation; moves $n-1$ elements to `helperStack`, accesses the target element, and moves elements back to restore order.

**Logic Notes**
*   **Performance Trade-off**: This specific implementation favors $O(1)$ push at the cost of $O(n)$ pop/peek. 
*   **Restoration Pattern**: The logic relies on a "dump-and-restore" cycle. Because the `inputStack` is restored every time, the state remains consistent across interleaved `push` and `pop` calls.
*   **Optimization Opportunity**: This could be optimized to $O(1)$ amortized time by moving elements only when `helperStack` is empty and keeping them there, rather than restoring the `inputStack` after every operation.

---
