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

## queue_solution(using_one_queue).java
*Style: detailed*

# Engineering Deep-Dive: Single-Queue LIFO Implementation

This document analyzes the implementation of a Last-In-First-Out (LIFO) stack using a single First-In-First-Out (FIFO) queue.

---

## 1. Summary
The solution implements a `Stack` interface using a `Queue` by leveraging **queue rotation**. Since a queue is inherently FIFO, the strategy is to force the newly inserted element to the front of the queue. Upon `push(x)`, we insert the element into the queue and then perform $N-1$ rotate operations (dequeuing and re-enqueuing), effectively reversing the order of the existing elements relative to the new element. This creates the LIFO property required for stack behavior at the expense of `push` performance.

---

## 2. Complexity Analysis

| Operation | Time Complexity | Space Complexity |
| :--- | :--- | :--- |
| `push(x)` | $O(N)$ | $O(1)$ |
| `pop()` | $O(1)$ | $O(1)$ |
| `top()` | $O(1)$ | $O(1)$ |
| `empty()` | $O(1)$ | $O(1)$ |

### Breakdown
*   **`push(x)` ($O(N)$):** For every push, we execute a loop that runs `size - 1` times. Each iteration involves a `poll()` and an `offer()`, both of which are $O(1)$ operations in a `LinkedList` implementation of a `Queue`. Thus, the operation is strictly linear relative to the number of elements current stored.
*   **`pop()` / `top()` ($O(1)$):** Because we maintain the invariant that the "top" of the stack is always at the head of the queue, retrieving or removing this element requires only a single `peek` or `poll` operation, which is constant time.
*   **Space Complexity:** $O(N)$, where $N$ is the number of elements in the stack. We store exactly one node per element in the underlying `LinkedList`.

---

## 3. Component Deep Dive

### `push(int value)` - The Rotation Logic
This is the heart of the implementation.
*   **The Invariant:** The head of the queue must always represent the top of the stack.
*   **Mechanism:** 
    1. `offer(value)`: The new element is placed at the tail.
    2. `for` loop: We rotate the first `size - 1` elements. 
    *   *Example:* Queue is `[2, 1]`. `push(3)` is called. 
    *   Queue becomes `[2, 1, 3]`. 
    *   Loop runs `size - 1` (2) times:
        *   `poll()` 2, `offer()` 2 -> `[1, 3, 2]`
        *   `poll()` 1, `offer()` 1 -> `[3, 2, 1]`
*   **Edge Case:** If the queue is empty, `size - 1` is 0, the loop does not execute, and the element is correctly placed at the head.

### Underlying Data Structure
*   **`LinkedList`:** Used here as the queue implementation. It provides $O(1)$ insertion and deletion from both ends. Note that `LinkedList` has higher memory overhead than an `ArrayDeque` due to node object allocation (pointers for `prev`, `next`, and the `item`). In high-performance scenarios, `ArrayDeque` is generally preferred for cache locality and reduced memory footprint.

---

## 4. Key Insights

### Performance Trade-offs
*   **Push-Heavy vs. Pop-Heavy:** This design optimizes for $O(1)$ stack access (`pop`/`top`). If the workload consists primarily of `push` operations, this design is sub-optimal compared to a native stack implementation or a two-queue approach (where you could shift the $O(N)$ cost to either push or pop depending on requirements).

### Thread Safety
*   **Concurrency:** This class is **not thread-safe**. If accessed by multiple threads, the `push` operation (which performs multiple operations on the queue) would require synchronization or the use of a `ConcurrentLinkedQueue`. 

### Subtle Nuances
*   **Queue Invariants:** The reliance on `stackQueue.size()` inside the loop is safe because `offer` and `poll` operations within the loop do not change the total number of elements in the structure; they only change the internal ordering.
*   **Memory Fragmentation:** Because `java.util.LinkedList` allocates a new `Node` object for every `push`, this implementation will exert more pressure on the Garbage Collector (GC) compared to an array-backed stack (like `java.util.Stack` or `ArrayDeque`). If memory efficiency is a strict requirement, consider using a circular buffer (array) approach.

---
