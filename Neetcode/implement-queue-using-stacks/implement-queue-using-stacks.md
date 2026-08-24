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

## stack_solution(twostacks_optimal).java
*Style: detailed*

# Technical Reference: Amortized Constant-Time Queue Implementation

## 1. Summary
This implementation realizes the First-In-First-Out (FIFO) constraint of a queue using two Last-In-First-Out (LIFO) stacks (`ArrayDeque`). The fundamental algorithmic technique is **Amortized Analysis**. By maintaining an `inputStack` for incoming elements and an `outputStack` for outgoing elements, we delay the reversal of the order (required to turn LIFO into FIFO) until the exact moment an extraction operation (`pop` or `peek`) is invoked on an empty `outputStack`.

## 2. Complexity Analysis

### Time Complexity
*   **`push(int x)`**: $O(1)$. This is a simple push operation onto the `inputStack`.
*   **`pop()` / `peek()`**: Amortized $O(1)$. While a single call might trigger a $O(N)$ transfer of all elements from `inputStack` to `outputStack`, each element is pushed onto `inputStack` once, popped from `inputStack` once, pushed onto `outputStack` once, and popped from `outputStack` once. Over a sequence of $M$ operations, the total work is proportional to $O(M)$.
*   **`empty()`**: $O(1)$. Straightforward boolean conjunction of two collection states.

### Space Complexity
*   **$O(N)$**, where $N$ is the number of elements currently in the queue. We store every element exactly once across the two internal stacks at any given time.

## 3. Component Deep Dive

### Data Structure Selection
The implementation utilizes `java.util.ArrayDeque` instead of `java.util.Stack`. 
*   **Reasoning**: `java.util.Stack` is a legacy class that extends `Vector`, making it synchronized and subject to significant performance overhead due to lock acquisition. `ArrayDeque` is a non-synchronized, resizeable-array implementation, providing superior cache locality and lower constant-time overhead for stack operations.

### Lazy Migration Pattern (`moveElementsIfNeeded`)
The core of the performance model is the lazy migration logic.
*   **Trigger**: The migration of elements from `inputStack` to `outputStack` is performed **only** when `outputStack` is empty. 
*   **Ordering Invariant**: Because each stack reversal flips the order of elements, moving all $N$ elements from `inputStack` to `outputStack` results in the oldest element residing at the top of the `outputStack`, correctly mimicking the queue's head.

### Edge-Case Handling
*   **Empty State**: The `empty()` method correctly evaluates `inputStack.isEmpty() && outputStack.isEmpty()`. This is robust because it accounts for scenarios where elements might exist in either stack.
*   **Underflow**: Note that the provided implementation assumes the caller will not call `pop()` or `peek()` on an empty queue. In a production environment, explicit `NoSuchElementException` handling should be added to the `pop()` and `peek()` methods if the state is empty.

## 4. Key Insights

### The "Amortized" Reality
It is a common mistake to misinterpret the `pop()` complexity as $O(N)$. While the worst-case for a single `pop()` is $O(N)$ (the transfer phase), this only happens when the `outputStack` is exhausted. Once migrated, the next $N$ `pop()` operations are purely $O(1)$. This "deferred work" pattern is a classic example of the **Accounting Method** in algorithm analysis.

### Potential Bottlenecks
*   **Memory Locality**: While `ArrayDeque` is cache-friendly, excessive migration between stacks can cause CPU cache misses if the stacks are large. However, for most memory-resident queue use cases, this is negligible compared to the overhead of linked-node structures (like `LinkedList`), which would involve frequent pointer chasing and heap allocations.
*   **Concurrency**: This class is **not thread-safe**. If utilized in a multi-threaded context, external synchronization or a `ConcurrentLinkedQueue` would be required, as concurrent access to `inputStack` and `outputStack` would lead to race conditions in the `moveElementsIfNeeded` check-and-act sequence.

---
