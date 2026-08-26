# asteroid-collision

## standard_stack_solution.java
*Style: detailed*

# Technical Deep Dive: Asteroid Collision Resolution

## Summary
The solution employs a **Monotonic Stack** strategy to resolve collisions in linear time. The algorithm treats the problem as a state-machine where incoming asteroids interact with the existing "stable" sequence. By maintaining a stack of asteroids that are either moving left (negative) or have already safely passed potential collision points, we ensure that we only process each asteroid through a constant number of operations.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Analysis:** Although there is a nested `while` loop within the `for` loop, each asteroid is pushed onto the stack exactly once and popped at most once. The `reverseArray` operation is a simple $O(K)$ pass where $K \le N$. Therefore, the amortized cost per asteroid is constant, leading to a total time complexity of $O(N)$.

### Space Complexity: $O(N)$
*   **Analysis:** In the worst-case scenario (e.g., all asteroids are moving in the same direction or no collisions occur), the stack will store all $N$ elements. The auxiliary space for the result array also scales linearly with the input size.

## Component Deep Dive

### 1. Collision Logic (`asteroidCollision`)
The algorithm processes the array linearly. The core logic hinges on the state of the stack:
*   **Non-colliding Push:** If the incoming asteroid is positive (`> 0`), it is pushed immediately, as it cannot collide with anything currently in the stack (which would only contain left-moving asteroids or previous right-moving asteroids).
*   **Destructive Resolution:** When a negative asteroid enters, the `while` loop clears the stack of all smaller right-moving asteroids (`stack.peek() < Math.abs(asteroid)`). 
*   **Neutralization:** The `stack.peek() == Math.abs(asteroid)` condition handles the "mutual destruction" edge case, where both asteroids are removed and the negative one does not enter the stack.
*   **Survival:** The `else if` block handles the survival of the negative asteroid: it survives if the stack is empty (nothing to collide with) or if the current stack top is also moving left (`peek() < 0`), preventing further collisions.

### 2. Array Reversal (`reverseArray`)
Because the `ArrayDeque` acts as a Last-In-First-Out (LIFO) structure, popping elements results in a reversed sequence of the surviving asteroids. The `reverseArray` method implements an **in-place two-pointer swap**, which is the most space-efficient way to restore the original spatial order.

### 3. Edge-Case Handling
*   **Empty Input:** Handled gracefully; the loop simply never executes, returning an empty array.
*   **Same-Direction Sequences:** `[1, 2, 3]` or `[-3, -2, -1]` never trigger the collision `while` loop, demonstrating the algorithm's robustness against non-colliding sets.
*   **Total Annihilation:** If the stack is emptied during a collision and the negative asteroid also equals the top element, it is correctly dropped without being pushed.

## Key Insights

*   **The "Peek" Trap:** Note the use of `stack.peek() > 0`. This is critical. A collision *only* occurs if the existing asteroid is moving right (`> 0`) and the new one is moving left (`< 0`). If the stack contains a negative asteroid, no further negative asteroids can ever collide with it, regardless of their magnitude. 
*   **Performance Optimization:** Using `ArrayDeque` is preferable to `java.util.Stack` because it is not synchronized, reducing overhead and improving cache locality during stack operations. 
*   **Subtle Bug Warning:** When resolving collisions, ensure `Math.abs(asteroid)` is calculated early. Comparing a negative incoming asteroid directly to a positive stack element without the absolute value will lead to incorrect logical branching.
*   **Memory Efficiency:** The current approach creates a new array of size `stack.size()`. While technically $O(N)$, this is unavoidable as the size of the final array is unknown at the start of the process. An alternative would be to use a fixed-size array and a pointer to simulate the stack to avoid `Deque` object overhead, though this would provide negligible performance gains in standard JVM environments.

---
