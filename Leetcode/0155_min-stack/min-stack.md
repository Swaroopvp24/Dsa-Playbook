# min-stack

## stack_solution(twostacks).java
*Style: concise*

### MinStack Notes

**Overview**
Implements a stack that supports `push`, `pop`, `top`, and `getMin` operations in $O(1)$ time. Uses an auxiliary stack (`minStack`) to track the minimum element relative to the current state of the main stack.

**Key Components**
*   `stack`: Stores all pushed elements.
*   `minStack`: Stores values in non-increasing order; the top element always represents the current minimum.
*   `push(int)`: Adds to `stack`; pushes to `minStack` if the new value is $\le$ current min.
*   `pop()`: Removes from `stack`; pops `minStack` only if the removed value matches the current min.

**Logic to Remember**
*   **Handling Duplicates:** The condition `value <= minStack.peek()` in `push` is critical. Using `<=` instead of `<` ensures that if the same minimum value is pushed multiple times, it is correctly tracked and removed during corresponding pops.
*   **State Sync:** The `minStack` acts as a chronological filter of minimum values, ensuring $O(1)$ access for `getMin()` without requiring a full scan.

---

## stack_solution(optimal).java
*Style: detailed*

# Technical Reference: Space-Optimized MinStack

## 1. Summary
The `MinStack` implements a constant-time retrieval stack by utilizing a **differential encoding strategy**. Instead of storing raw values (which would require an auxiliary stack to track minimums, doubling memory usage), this implementation stores the difference between the incoming value and the current global minimum.

By encoding the state transitions within the stack values themselves, the algorithm maintains the current minimum as a standalone variable (`currentMin`), effectively compressing the state of the stack into a single `Deque<Long>`.

## 2. Complexity Analysis

*   **Time Complexity:**
    *   `push()`, `pop()`, `top()`, `getMin()`: **$O(1)$**
    *   All operations perform a fixed number of arithmetic operations and stack mutations. There are no loops or recursive calls.
*   **Space Complexity:** **$O(N)$**
    *   $N$ is the number of elements in the stack. 
    *   **Note on memory efficiency:** Unlike a traditional two-stack approach (one for data, one for minimums), this implementation achieves a lower constant factor in space usage. By using `long` to store differences, it safely handles `Integer.MIN_VALUE` and `Integer.MAX_VALUE` without overflow during subtraction.

## 3. Component Deep Dive

### Differential Encoding Mechanism
The core logic revolves around the `difference` variable.
*   **When `value >= currentMin`:** The stored `difference` is $\ge 0$.
*   **When `value < currentMin`:** The stored `difference` is $< 0$. This negative value acts as a "breadcrumb" or signal that the global minimum was updated upon this specific push.

### `push(int value)`
1.  If the stack is empty, initialize `currentMin` to the first value and push `0L` (a dummy difference).
2.  Otherwise, calculate `difference = value - currentMin`.
3.  If `value < currentMin`, update `currentMin = value`. This ensures that the global minimum always tracks the smallest value seen *up to that point*.

### `pop()`
This is the most critical operation for state restoration. 
*   If the popped `difference` is negative, we know this element was the one that established the current `currentMin`.
*   We restore the *previous* minimum by reversing the operation: `currentMin = currentMin - difference`.
*   *Edge Case:* If `difference >= 0`, the popped value did not affect the minimum, so `currentMin` remains unchanged.

### `top()`
*   If `difference < 0`, the value at the top of the stack is the `currentMin`.
*   If `difference >= 0`, the actual value is reconstructed via `currentMin + difference`.

## 4. Key Insights & Nuances

### Arithmetic Overflow Protection
Using `long` is not merely for type safety; it is a **functional requirement**. 
*   If `value` is `Integer.MIN_VALUE` and `currentMin` is `Integer.MAX_VALUE`, `value - currentMin` will underflow a 32-bit `int`.
*   By casting to `long`, we ensure that the difference fits within the 64-bit bounds, allowing for correct reconstruction of the original integer regardless of the magnitude of the operands.

### The "Negative Difference" Logic
The logic hinges on the invariant that `currentMin` is updated **after** the difference is calculated but **before** (or during) the push. This creates a predictable state where:
*   A negative value in the stack is a proxy for: *"I was the minimum at the time of insertion, and you need to subtract me from the current global minimum to revert to the previous one."*

### Potential Pitfalls
*   **Non-atomic updates:** If `currentMin` were updated incorrectly, the entire stack chain would become corrupted because the stack depends on the validity of `currentMin` for every subsequent `top()` and `pop()` call.
*   **Empty Stack Access:** The current implementation assumes `pop()` and `top()` are not called on an empty stack. In a production environment, you should add an `if (stack.isEmpty())` check to throw an `EmptyStackException` to follow standard Java collection idioms.

---
