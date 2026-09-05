# maximum-frequency-stack

## standard_stack_solution(List+Map+Stack).java
*Style: detailed*

# Technical Deep-Dive: Frequency Stack (`FreqStack`)

## Summary
The `FreqStack` is an implementation of a Last-In-First-Out (LIFO) stack that prioritizes elements based on their occurrence count. The core algorithmic technique utilizes **frequency-indexed buckets**. Instead of maintaining a single global ordering, the solution maintains an array of stacks where `index i` contains a `Deque` of all elements that have appeared at least `i` times. By tracking the `maxFrequency`, the implementation achieves $O(1)$ time complexity for both `push` and `pop` operations, effectively transforming a priority queue requirement into a direct-addressing problem.

---

## Complexity Analysis

### Time Complexity
*   **`push(int value)`**: **$O(1)$**
    *   Updating the `frequencyMap` is $O(1)$ average.
    *   Accessing/Pushing to the specific `Deque` in the `ArrayList` is $O(1)$ amortized (resizing the `ArrayList` occurs rarely and is negligible).
    *   Updating `maxFrequency` is $O(1)$.
*   **`pop()`**: **$O(1)$**
    *   Popping from the `maxFrequency` bucket is $O(1)$.
    *   Updating the `frequencyMap` is $O(1)$.
    *   Decrementing `maxFrequency` is $O(1)$ because we only check the empty state of the current stack.

### Space Complexity
*   **$O(N)$**, where $N$ is the number of elements pushed onto the stack.
    *   The `frequencyMap` stores entries for each unique element: $O(U)$ where $U \le N$.
    *   The `valuesByFrequency` structure stores each instance of each element exactly once across all deques: $O(N)$.

---

## Component Deep Dive

### 1. The Multi-Stack Structure (`List<Deque<Integer>>`)
The design decouples the concept of "insertion order" from "global frequency." 
*   **Why a List of Deques?** A traditional `PriorityQueue` would incur $O(\log N)$ overhead. By using the frequency as an index, we eliminate the need for sorting. 
*   **Zero-indexing offset:** The implementation uses index `newFrequency`. Since `ArrayList` is 0-indexed, `valuesByFrequency` index `0` remains effectively unused, which acts as a clean sentinel buffer.

### 2. Frequency Tracking (`frequencyMap`)
This map is the primary source of truth for the state of any element.
*   **Edge Case:** When an element is pushed, its frequency increases *before* it is added to the bucket. This ensures that if an element's frequency jumps from 1 to 2, it is immediately available in the "Frequency 2" bucket for potential `pop` operations.

### 3. Max Frequency Maintenance
The `maxFrequency` variable acts as a pointer to the highest occupied bucket.
*   **`push` logic:** `maxFrequency` is strictly non-decreasing.
*   **`pop` logic:** When `valuesByFrequency.get(maxFrequency).isEmpty()` becomes true, it signifies that there are no elements remaining with that frequency. The pointer decrements, effectively "garbage collecting" the empty frequency tier.

---

## Key Insights & Nuances

### 1. Implicit Sorting
The brilliance of this approach is that it maintains the **LIFO (Last-In-First-Out) property** for elements of the same frequency. Because we use a `Deque` (stack) at each frequency index, the most recently added item for a specific frequency is always retrieved first. This satisfies the requirement that "most recently pushed value is popped first" among items of equal frequency.

### 2. Space-Time Tradeoff
While this solution is $O(1)$, it is memory-intensive. It creates a `Deque` object for every potential frequency encountered. If the input stream has $N$ elements and only one unique value is pushed $N$ times, we create $N$ `ArrayDeque` instances. In a memory-constrained environment, one might replace the `List<Deque<Integer>>` with a primitive-backed array of dynamic arrays to reduce object overhead.

### 3. Potential Pitfalls
*   **Frequency Gaps:** The logic handles non-consecutive frequency jumps by expanding the `List` using the `while` loop inside `push`. This ensures robustness even if the frequency map becomes sparse.
*   **Thread Safety:** This implementation is not thread-safe. Concurrent calls to `push` and `pop` will cause race conditions on the `maxFrequency` pointer and the `frequencyMap`. If thread safety is required, external synchronization or `ConcurrentHashMap` with atomic frequency updates would be necessary.
*   **Empty `pop`:** The provided code assumes `pop()` is only called when the stack is non-empty. In a production scenario, adding a check `if (maxFrequency == 0)` and throwing a `NoSuchElementException` would be necessary to prevent `IndexOutOfBounds` exceptions.

---
