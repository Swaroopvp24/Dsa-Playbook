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

## stack_solution(twostacks_optimal).java
*Style: detailed*

# Engineering Deep-Dive: Amortized Constant-Time Queue Implementation

## Summary
The solution implements a FIFO (First-In, First-Out) queue using two LIFO (Last-In, First-Out) stacks (`ArrayDeque`). This approach bypasses the memory overhead and shifting costs of `ArrayList` or the pointer overhead of `LinkedList` for queue operations. By leveraging a dual-stack architecture, we perform a "lazy migration" of data from an `inputStack` (collector) to an `outputStack` (consumer) only when the consumer stack is exhausted.

## Complexity Analysis

### Time Complexity
*   **`push(int x)`**: $O(1)$ constant time. We strictly push to the `inputStack`.
*   **`pop()` / `peek()`**: Amortized $O(1)$. 
    *   While an individual call might trigger an $O(n)$ migration loop, each element is pushed onto `inputStack` once, moved to `outputStack` once, and popped from `outputStack` once. Over a sequence of $m$ operations, the total cost is $O(m)$, yielding an amortized constant cost per operation.
*   **`empty()`**: $O(1)$. Simple boolean check of stack sizes.

### Space Complexity
*   **Total Space**: $O(n)$, where $n$ is the number of elements in the queue. We maintain two stacks that collectively store all inserted elements at any given time. No auxiliary storage beyond the two stacks is utilized.

## Component Deep Dive

### 1. `moveElementsIfNeeded()` (The Migration Engine)
This is the heart of the implementation. It serves as an internal "rebalancing" mechanism. By checking `outputStack.isEmpty()`, we ensure we only perform the migration cost when strictly necessary. 
*   **Edge Case**: If both stacks are empty, the method correctly does nothing, and subsequent `pop`/`peek` calls would trigger a `NoSuchElementException` (standard behavior for an empty stack).
*   **Performance Note**: By using `ArrayDeque` over `Stack`, we avoid the `Vector`-based synchronization overhead inherent in the legacy `java.util.Stack` class, resulting in faster performance in non-concurrent contexts.

### 2. State Management
*   **`inputStack`**: Handles all incoming traffic. No logic is required here.
*   **`outputStack`**: Acts as the inverted view of the input. Because it reverses the `inputStack` once, it correctly preserves the order of ingestion (LIFO + LIFO = FIFO).

### 3. Edge Case Handling
*   **Empty Queue**: If `pop()` or `peek()` is called on an empty queue, the code relies on the underlying `ArrayDeque`'s behavior (throwing `NoSuchElementException`). A more robust implementation might explicitly check `empty()` and throw a custom `QueueUnderflowException`.
*   **Alternating Operations**: The design is optimized for interleaved `push` and `pop` operations. If the application pattern is heavy on `push` then heavy on `pop`, the migration cost is minimized to once per large read cycle.

## Key Insights

### The "Amortized" Nuance
The most significant trap for junior engineers is evaluating this as $O(n)$ because of the `while` loop inside `moveElementsIfNeeded`. It is critical to recognize that the cost of moving an element is "pre-paid" by the `push` operation. Since an element can only be moved from the `inputStack` to the `outputStack` exactly once during its lifecycle, the $O(n)$ operation cannot happen frequently enough to degrade the overall performance of the data structure.

### Thread Safety Warning
This implementation is **not thread-safe**. While `ArrayDeque` is highly efficient, it does not provide any synchronization. If this `MyQueue` instance is shared across threads, external synchronization or a `ConcurrentLinkedQueue` would be required to prevent race conditions during the `moveElementsIfNeeded` check-then-act sequence.

### Structural Optimization
Because `ArrayDeque` is an array-backed circular buffer, it exhibits excellent cache locality compared to `LinkedList`. By using `ArrayDeque`, we minimize cache misses during the stack-transfer process, as the memory addresses for the stack elements are likely contiguous.

---
