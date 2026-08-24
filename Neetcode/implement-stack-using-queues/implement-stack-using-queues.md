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

## queue_solution(using_one_queue).java
*Style: concise*

### Stack Implementation using a Single Queue

This implementation realizes LIFO (stack) behavior using a single FIFO (queue) by rotating elements during the `push` operation.

#### Key Methods
*   `push(int)`: Inserts element, then rotates the queue so the most recently added element is at the head.
*   `pop()`: Removes and returns the head of the queue (which is the stack's "top").
*   `top()`: Returns the head without removing it.

#### Logic Notes
*   **Rotation Invariant:** The core logic occurs in `push` by dequeuing and re-enqueuing the first `size - 1` elements. This effectively reverses the order of the newly added element relative to existing ones, turning $O(1)$ queue insertions into $O(n)$ stack insertions.
*   **Performance:** 
    *   `push()`: $O(n)$
    *   `pop()`/`top()`: $O(1)$
    *   Space Complexity: $O(n)$

---
