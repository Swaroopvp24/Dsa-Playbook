# implement-stack-using-queues

## queue_solution(using_two_queues).java
*Style: concise*

### Stack Implementation using Two Queues

This code implements a LIFO stack using two `Queue` instances. It achieves stack behavior by ensuring the most recently pushed element is always at the front of the `primaryQueue`.

#### Key Components
*   **`primaryQueue`**: Stores elements in stack order (newest at the head).
*   **`helperQueue`**: Used as a temporary buffer during the `push` operation to reorder elements.
*   **`push(int)`**: Reorders elements so the newest entry is at the front.
*   **`pop()` / `top()`**: Standard queue operations targeting the front of `primaryQueue` (O(1)).

#### Logic Notes
*   **Time Complexity**: `push` is **O(n)** because every existing element must be moved to the helper queue. `pop` and `top` are **O(1)**.
*   **The "Swap" Trick**: Instead of copying elements back from `helperQueue` to `primaryQueue`, the implementation simply swaps the queue references, avoiding unnecessary iterations and memory overhead.
*   **Memory**: Maintains O(n) space complexity.

---
