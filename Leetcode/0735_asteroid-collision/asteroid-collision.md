# asteroid-collision

## standard_stack_solution.java
*Style: detailed*

# Technical Deep-Dive: Asteroid Collision Resolution

## Summary
The solution implements a **Monotonic Stack** pattern to simulate one-dimensional particle physics. The algorithm treats the array as a stream of objects moving on a 1D line. Collisions only occur when a right-moving asteroid (`+`) precedes a left-moving asteroid (`-`). The core logic leverages a `Deque` to maintain the "surviving" state of asteroids, effectively collapsing the state space by iteratively evaluating collision conditions at the top of the stack.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Analysis:** Although the code contains nested loops (a `for` loop containing a `while` loop), each asteroid is pushed onto the stack at most once and popped at most once. 
*   **Result:** Every element in the input array is processed a constant number of times. The final `reverseArray` pass is an additional $O(N/2)$ operation, keeping the total complexity linear relative to the number of asteroids.

### Space Complexity: $O(N)$
*   **Analysis:** In the worst-case scenario (e.g., all asteroids are moving in the same direction, or no collisions occur), the stack will store all $N$ elements. 
*   **Result:** Auxiliary space usage scales linearly with the input size.

---

## Component Deep Dive

### 1. Collision Resolution Logic
The `while` loop is the heart of the collision engine:
```java
while (!stack.isEmpty() && stack.peek() > 0 && stack.peek() < Math.abs(asteroid))
```
*   **Pre-condition:** This only triggers if the current `asteroid` is negative (moving left).
*   **Invariant:** The stack keeps track of only right-moving (`+`) asteroids that haven't exploded yet. If the `peek` value is less than the magnitude of the incoming asteroid, the `peek` asteroid is objectively "weaker" and is popped (destroyed).

### 2. State-Based Termination
After the collision loop, the code evaluates three mutually exclusive conditions:
1.  **Mutual Destruction:** `stack.peek() == Math.abs(asteroid)` — Both are destroyed. The `pop()` consumes the right-mover, and we do *not* push the current negative asteroid.
2.  **Survival:** `stack.isEmpty() || stack.peek() < 0` — The incoming asteroid survived all encounters with right-movers. It either pushed back to an empty space or encountered another left-mover (which doesn't cause a collision).
3.  **Implied Destruction:** If neither condition 1 nor 2 is met, it implies `stack.peek() > Math.abs(asteroid)`. The incoming asteroid is destroyed, and the stack remains unchanged.

### 3. Data Structure Choice
*   **`ArrayDeque`:** Chosen over `Stack<E>` because `java.util.Stack` is synchronized (thread-safe), introducing unnecessary overhead. `ArrayDeque` is a more performant implementation for stack operations in a single-threaded context.

---

## Key Insights

### The "Reverse" Trap
A subtle point is that a standard `stack.pop()` returns elements in LIFO order. Because the collision simulation is processed from left-to-right, the stack elements are naturally ordered correctly relative to their survival. However, popping them into an array results in a reversed sequence. 
*   **Optimization:** Instead of `reverseArray(result)`, one could use `Deque.toArray()` or `stack.descendingIterator()`, but the current implementation is highly readable and cache-friendly due to the sequential array fill.

### Logical Edge Cases
*   **Empty Input:** Handled gracefully; returns an empty array.
*   **Consecutive Negative Asteroids:** The condition `stack.peek() < 0` ensures that multiple left-moving asteroids (`-5, -3`) do not collide, correctly pushing them onto the stack one after another.
*   **Mixed Signatures:** The condition `stack.peek() > 0` ensures we *only* evaluate collisions between a right-moving stack top and a left-moving incoming asteroid. 

### Potential Fragility
*   **Memory Overhead:** For extremely large input arrays ($N > 10^7$), `int[] result` plus `ArrayDeque` internal array allocation may lead to `OutOfMemoryError`. In memory-constrained environments, one could pre-calculate the final stack size using an estimation heuristic or reuse the input array as a stack if the problem constraints allow destructive modification of the input.

---

## stack_simulation_solution.java
*Style: detailed*

# Engineering Reference: In-Place Asteroid Collision Resolution

## Summary
The solution implements a **linear-time stack-based simulation** to resolve asteroid collisions. By leveraging the input array itself as a stack, the algorithm achieves $O(1)$ auxiliary space complexity (excluding the output). 

The algorithmic core relies on a greedy state machine: asteroids move toward each other only when a positive-velocity asteroid is followed by a negative-velocity asteroid. The simulation resolves these interactions iteratively, effectively filtering the input stream to maintain only the "surviving" asteroids.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Proof:** Although the code contains a `while` loop nested within a `for` loop, each asteroid is pushed onto the "stack" exactly once and popped from it at most once. The `asteroid` variable is only processed through the collision logic until it is either destroyed or pushed. Thus, the total number of operations is bounded by $2N$.
*   **Amortized analysis:** Each element participates in a finite number of comparisons, ensuring linear scaling regardless of collision frequency.

### Space Complexity: $O(1)$ (Auxiliary)
*   **Reasoning:** The algorithm utilizes the input array `asteroids` as the underlying storage for the stack. The pointer `stackTop` tracks the current stack height. Since no additional data structures (like `java.util.Stack` or `ArrayList`) are instantiated, the space complexity remains constant relative to the input size. 
*   **Note:** The return value `Arrays.copyOfRange` creates a new array of size $M$ (where $M \le N$), but this is considered output space, not auxiliary space.

---

## Component Deep Dive

### 1. In-Place Stack Management
The variable `stackTop` serves as the stack pointer. By initializing it to `-1`, the code treats the existing array as a dynamic stack buffer.
*   `asteroids[++stackTop] = asteroid` performs an atomic "push" and increment.
*   `stackTop--` performs an atomic "pop."

### 2. The Collision Logic (State Machine)
The `while` loop condition `(stackTop >= 0 && asteroids[stackTop] > 0 && asteroid < 0)` acts as a guard clause for the only state that triggers a collision: a right-moving asteroid (positive) encountering a left-moving one (negative).

*   **Case 1: Stack Top Wins (`asteroids[stackTop] > abs(asteroid)`):** The incoming asteroid is annihilated. The loop breaks, and the stack remains unchanged.
*   **Case 2: Mutual Annihilation (`==`):** Both asteroids are removed. The stack is popped, and the incoming asteroid is set to `0` to signal it should not be pushed.
*   **Case 3: Incoming Wins:** The stack is popped, and the `while` loop continues to check if the new `stackTop` also collides with the (still existing) incoming asteroid.

### 3. Edge-Case Handling
*   **Empty Input:** If `asteroids` is empty, `stackTop` remains `-1`, and the method returns an empty array.
*   **Zero Velocity:** The logic inherently ignores `0` velocity asteroids. Since they are neither positive nor negative, they never enter the `while` loop collision logic, ensuring they are treated as static entities that bypass interactions.
*   **Sequential Directionality:** The logic correctly handles cases where multiple asteroids of the same direction are processed sequentially (e.g., `[-1, -2]`), as the loop condition `asteroids[stackTop] > 0` will fail immediately.

---

## Key Insights

*   **The "Destruction Signal":** The use of `asteroid = 0` as a sentinel value is a clever optimization. It cleanly communicates that the incoming asteroid has been neutralized, preventing it from being added to the stack without requiring a flag variable or complex `if/else` branching.
*   **Memory Efficiency:** By performing the operation in-place, the algorithm avoids heap allocations during the simulation phase. This is critical in high-throughput or memory-constrained systems (e.g., embedded systems or massive-scale data processing).
*   **Subtle Bug Caution:** A common mistake in this pattern is failing to account for the `asteroid` being destroyed *after* popping the stack but *before* the next comparison. The current implementation correctly maintains the state of the incoming asteroid, allowing it to "continue" its trajectory against the next item in the stack.
*   **Optimization Potential:** If the input array is strictly immutable, a `Deque` would be required, elevating space complexity to $O(N)$. The in-place mutation assumes ownership of the `asteroids` array is permitted. If input preservation is required, an explicit stack buffer must be allocated, reverting the space complexity to $O(N)$.

---
