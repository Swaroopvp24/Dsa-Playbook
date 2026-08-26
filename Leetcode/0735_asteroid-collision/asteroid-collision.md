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
