# implement-stack-using-queues

## queue_solution(using_two_queues).java
*Style: concise*

### Study Notes: Queue-based Stack Implementation

**Overview**
This code implements a LIFO (Last-In, First-Out) stack using two `Queue` objects. By reordering elements during the `push` operation, it ensures that the most recently added element is always at the head of the `primaryQueue`.

**Key Components**
*   **`primaryQueue`**: Holds the stack elements in LIFO order (the front of the queue is the top of the stack).
*   **`helperQueue`**: Used as a temporary buffer to reorder elements during `push`.
*   **`push(int)`**: Enqueues the new element first, then transfers all existing elements from the `primaryQueue` to the back of the new element.
*   **`pop()` / `top()`**: Direct operations on the `primaryQueue` (O(1) time complexity).

**Non-Obvious Logic**
*   **Queue Swap**: Instead of manually moving elements back and forth after the `push`, the code swaps references (`primaryQueue` = `helperQueue`). This keeps the `primaryQueue` consistently ordered for O(1) access.
*   **Performance Trade-off**: The `push` operation is O(n) because it forces a full migration of elements, but this optimizes `pop` and `top` to O(1). This is a "push-heavy" implementation.

---
