# maximum-frequency-stack

## standard_stack_solution(List+Map+Stack).java
*Style: detailed*

# Deep-Dive Technical Reference: FreqStack Implementation

## Summary
The `FreqStack` implements a priority-based stack that orders elements by frequency rather than standard LIFO (Last-In-First-Out) insertion order. The architectural approach utilizes **Frequency Bucketing**. Instead of maintaining a single global ordering structure (which would require $O(\log N)$ or $O(N)$ overhead for re-sorting), the solution decouples element tracking from frequency tracking.

By maintaining a list of stacks (`valuesByFrequency`), where `index[i]` contains all elements that have appeared at least `i` times, we transform the "most frequent" lookup into an $O(1)$ constant-time operation. This effectively treats frequency as a primary sort key and insertion time as a secondary sort key.

---

## Complexity Analysis

### Time Complexity
*   **`push(int value)`: $O(1)$**
    *   Map lookup/insertion is $O(1)$ on average.
    *   Appending to the `ArrayList` (the list of stacks) is amortized $O(1)$. 
    *   Updating `maxFrequency` is a primitive comparison, $O(1)$.
*   **`pop()`: $O(1)$**
    *   Retrieving the stack from `valuesByFrequency` via `maxFrequency` is $O(1)$.
    *   Popping from `ArrayDeque` is $O(1)$.
    *   Updating `maxFrequency` involves a potential decrement, which is $O(1)$.

### Space Complexity
*   **$O(N)$**, where $N$ is the number of elements pushed onto the stack.
    *   `frequencyMap` stores $U$ unique elements ($O(U)$).
    *   `valuesByFrequency` stores $N$ total elements across all buckets ($O(N)$). 
    *   Even though we maintain multiple stacks, each element is represented exactly once per frequency level it reaches, totaling $N$ entries across the entire list structure.

---

## Component Deep Dive

### 1. The Frequency Bucket Strategy (`List<Deque<Integer>>`)
The core innovation is the index mapping: `index = frequency`. If a value is pushed and its frequency becomes 3, it is pushed onto the stack at `valuesByFrequency.get(3)`. 
*   **Why `ArrayDeque`?** It provides $O(1)$ amortized performance for `push` and `pop` and lacks the overhead of `synchronized` methods found in the legacy `Stack` class, making it the optimal choice for non-concurrent scenarios.

### 2. Frequency Tracking (`Map<Integer, Integer>`)
This map serves as the source of truth for the *current state* of any given element. It is critical for the `pop` operation: we must know the *old* frequency to decrement it correctly. Note that we do not remove keys from this map when they reach a frequency of 0; they remain in the map with a value of 0, which is handled gracefully by `getOrDefault`.

### 3. Edge Case Handling
*   **Empty `pop()`**: The implementation assumes `pop()` will only be called when the stack is non-empty. If called on an empty structure, `valuesByFrequency.get(maxFrequency)` will throw an `IndexOutOfBoundsException`. In a production environment, an `EmptyStackException` or a guard clause would be required.
*   **Dynamic Resizing**: The `while` loop inside `push` ensures the `ArrayList` grows elastically. This handles the scenario where a new element surpasses the current `maxFrequency`, effectively "allocating" a new bucket on the fly.

---

## Key Insights & Nuances

*   **The "Double-Entry" Logic**: A common point of confusion is why an element exists in multiple buckets. If a value `x` is pushed 3 times, it exists in the stack at index 1, index 2, and index 3 simultaneously. This is intentional. When we `pop()`, we remove the instance from the `maxFrequency` bucket, decrement its count, and it remains in the stack for its *previous* frequency. This preserves the LIFO order for all elements sharing the same frequency.
*   **`maxFrequency` State Maintenance**: The `maxFrequency` variable is the "pointer" to the active bucket. By only decrementing `maxFrequency` when `valuesByFrequency.get(maxFrequency).isEmpty()`, we ensure that we always have a $O(1)$ reference to the current highest frequency.
*   **Memory Efficiency**: While the structure is $O(N)$, it is memory-intensive compared to a standard stack. Each push operation creates a stack node. If memory is a constraint, this implementation is significantly heavier than a standard `Stack<Integer>`.
*   **Thread Safety**: This implementation is **not thread-safe**. If used in a concurrent environment, the `valuesByFrequency` list and the `frequencyMap` would require external synchronization or the use of `ConcurrentHashMap` and a lock-guarded `List`.

---
