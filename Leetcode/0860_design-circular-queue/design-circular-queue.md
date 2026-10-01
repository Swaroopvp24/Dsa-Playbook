# design-circular-queue

## linkedlist_solution(single linked list).java
*Style: detailed*

# Deep-Dive Reference: Linked-List Based Circular Queue

## Summary
The provided `MyCircularQueue` implementation utilizes a **Singly Linked List** to maintain a First-In-First-Out (FIFO) data structure with a fixed capacity. Unlike array-based circular queues that use modular arithmetic (index wrapping), this implementation achieves the "circular" logical behavior by maintaining explicit `front` and `rear` pointers. 

While the implementation does not physically form a circular loop in memory (i.e., `rear.next` does not point back to `front`), it mimics circular queue behavior by strictly enforcing a `capacity` constraint and managing node lifecycle via standard linked list pointer manipulation.

---

## Complexity Analysis

| Operation | Time Complexity | Space Complexity |
| :--- | :--- | :--- |
| `enQueue` | O(1) | O(1) |
| `deQueue` | O(1) | O(1) |
| `Front`/`Rear` | O(1) | O(1) |
| `isFull`/`isEmpty` | O(1) | O(1) |

### Rationale
*   **Time Complexity:** All operations perform a constant number of pointer assignments and integer comparisons. No loops or recursion are utilized, ensuring strict constant-time performance regardless of the state of the queue.
*   **Space Complexity:** The space complexity is O(k), where `k` is the `capacity`. This is dominated by the allocation of `ListNode` objects. No additional auxiliary data structures (like buffers) are used, making the space overhead strictly proportional to the number of stored elements.

---

## Component Deep Dive

### 1. State Management
The class relies on two primary state variables:
*   `size`: An integer tracker that serves as the "source of truth" for queue occupancy. This avoids O(n) traversals to determine fullness.
*   `capacity`: An immutable final field that defines the upper bound.

### 2. `enQueue(int value)`
*   **Mechanism:** Creates a new `ListNode`. If the queue is empty, both `front` and `rear` reference the new node. Otherwise, it updates `rear.next` to the new node and then updates the `rear` pointer.
*   **Edge Case Handling:** It explicitly checks `isFull()` before allocation, preventing memory overflow beyond the logical capacity.

### 3. `deQueue()`
*   **Mechanism:** Directly advances the `front` pointer to `front.next`. 
*   **Critical Path:** The logic includes a conditional check: `if (size == 0) { rear = null; }`. This is essential because if the last remaining element is dequeued, the `front` becomes `null`, but the `rear` would still be pointing at the orphaned node if not manually cleared, leading to potential memory leaks or state inconsistency.

### 4. `Front()` / `Rear()`
*   **Mechanism:** These are accessor methods that return the values pointed to by `front` and `rear`. 
*   **Safety:** They return `-1` if `isEmpty()` is true, adhering to common interface specifications for such structures.

---

## Key Insights & Performance Nuances

### Pointer Integrity
The implementation relies on manual garbage collection triggers. By nullifying `rear` when `size == 0`, we ensure that there are no remaining references to the dequeued `ListNode` objects, allowing the JVM to reclaim the memory promptly.

### Potential Limitations
*   **Memory Fragmentation:** Because this implementation uses `ListNode` objects, each enqueue operation triggers a new heap allocation. In a high-throughput environment, this may lead to frequent GC activity compared to an array-based circular queue, which utilizes contiguous memory and is generally more cache-friendly.
*   **Lack of "True" Circularity:** This is technically a **bounded linked queue**. A true circular queue typically uses a static array to recycle memory. This implementation creates and destroys nodes, which is less efficient if the queue experiences a high churn rate of insertions and deletions.

### Subtle Considerations
*   **Null Safety:** The code is robust against `NullPointerException` because the `isEmpty()` checks prevent dereferencing `front` or `rear` when they are null.
*   **Concurrency:** This class is **not thread-safe**. If this were to be used in a multithreaded context, the `enQueue` and `deQueue` operations would require `synchronized` blocks or an `AtomicReference` approach to protect the `size` variable and the `front`/`rear` pointers from race conditions.

---
