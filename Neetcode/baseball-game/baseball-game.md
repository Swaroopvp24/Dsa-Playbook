# baseball-game

## standard_stack_solution.java
*Style: detailed*

# Technical Deep-Dive: Baseball Game Scoring (Stack-Based Evaluation)

## Summary
The solution implements a stateful evaluation engine using a **Stack (LIFO)** data structure to process a stream of score-modifying operations. The algorithm treats the score sequence as a dependency chain: "plus" operations require access to the immediate history, "double" operations require the last state, and "cancel" operations represent an undo mechanism. By utilizing `java.util.ArrayDeque`, the solution achieves $O(1)$ amortized insertion and deletion, maintaining linear performance relative to the input size.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Processing:** We iterate through the `operations` array exactly once. Each operation (`+`, `D`, `C`, or `Integer`) involves a constant number of stack operations (`push`, `pop`, `peek`), all of which are $O(1)$.
*   **Summation:** After processing, we drain the stack. Since the stack size $S \le N$, this takes $O(N)$ time.
*   **Total:** $O(N) + O(N) = O(N)$, where $N$ is the length of the operations array.

### Space Complexity: $O(N)$
*   **Stack Storage:** In the worst-case scenario (where all operations are valid integers), the stack will grow to size $N$. 
*   **Auxiliary Space:** We use a constant amount of extra space for pointers and intermediate arithmetic, resulting in an overall $O(N)$ space requirement.

## Component Deep Dive

### 1. Data Structure Choice: `ArrayDeque` vs. `Stack`
The implementation correctly favors `ArrayDeque` over the legacy `java.util.Stack`.
*   **Performance:** `java.util.Stack` is synchronized (thread-safe), imposing unnecessary overhead. `ArrayDeque` is non-synchronized and faster in single-threaded contexts.
*   **Memory Efficiency:** `ArrayDeque` is backed by a resizable array, which provides better cache locality compared to the linked-node structure of a `LinkedList` or the synchronized overhead of `Stack`.

### 2. Logic Branching
*   **`+` Operation (State Retrieval):** To compute the sum of the last two scores without destroying the stack permanently, the algorithm pops the top element ($n_1$), peeks the next ($n_2$), and then pushes $n_1$ back before pushing the sum. This maintains the stack's integrity for subsequent operations.
*   **`D` Operation:** Simple peek-then-push. Note that `peek()` is used here as we do not need to remove the top element.
*   **`C` Operation:** This serves as a "pop" operation, effectively removing the last valid entry.
*   **Integer Parsing:** Uses `Integer.parseInt(s)`, which assumes the input contract guarantees valid integer strings for non-operator inputs.

## Key Insights

### 1. The "Peek-Pop-Push" Sequence
In the `+` operation, the sequence:
```java
int n1 = stack.pop();
int n2 = stack.peek();
stack.push(n1); // Restore state
stack.push(n1 + n2);
```
is a idiomatic way to handle "look-behind" dependencies in a LIFO structure. Without the `push(n1)`, the state required for the next operation would be mutated incorrectly.

### 2. Edge-Case Resilience
*   **Empty Stack:** The code assumes the input follows valid baseball rules (i.e., we never call `pop` or `peek` on an empty stack). In a production environment, one should wrap `peek()`/`pop()` in a check for `stack.isEmpty()` or use a `try-catch` block if the input is untrusted.
*   **Integer Overflow:** While the problem constraints usually fit within a 32-bit integer, if the game sequence is extremely long or scores are high, `n1 + n2` or `2 * n` could trigger an overflow. In a high-scale system, using `long` for the stack elements would be the safer architectural choice.

### 3. Performance Nuance: Summation
The current summation logic `while (!stack.isEmpty())` is destructive. If the requirement ever evolved to support queries mid-process, this approach would require a refactor to a streaming summation variable (e.g., maintaining a `runningSum` variable that is updated on every push/pop). As written, the solution is optimized for a single-pass "calculate at the end" requirement.

---

## standard_stack_solution2.java
*Style: detailed*

# Technical Deep Dive: Baseball Game Score Calculator

## Summary
The solution employs a **Stack-based evaluation strategy** to process a sequence of operations that mutate a record of scores. By utilizing an `ArrayDeque` as a LIFO (Last-In-First-Out) structure, the algorithm maintains a temporal history of valid scores. This allows for $O(1)$ access to the most recent elements, which are required for calculating dependent operations (summation and doubling). The running sum is maintained incrementally, avoiding the need for a final linear pass to aggregate the scores.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Analysis:** We perform a single iteration over the `operations` array of size $N$. 
*   **Operations:** Inside the loop, `push`, `pop`, `peek`, and `Integer.parseInt` are all $O(1)$ operations (given that integer string parsing is bounded by the number of digits in a 32-bit integer, which is a constant $K \le 10$). Consequently, the total time complexity is linear relative to the input size.

### Space Complexity: $O(N)$
*   **Analysis:** In the worst-case scenario (e.g., a sequence consisting entirely of integer inputs), the `stack` will store $N$ elements. 
*   **Footprint:** While `ArrayDeque` is more memory-efficient than `java.util.Stack` (as it is not synchronized and avoids the overhead of `Vector` inheritance), the heap allocation remains proportional to the number of operations.

---

## Component Deep Dive

### 1. Data Structure Choice: `ArrayDeque`
The use of `ArrayDeque<Integer>` over `Stack<Integer>` is a standard best practice in Java. `java.util.Stack` is legacy, thread-safe (synchronized), and incurs significant performance overhead. `ArrayDeque` provides a faster, non-thread-safe implementation backed by a resizable array, which is ideal for this single-threaded context.

### 2. State Management Logic
The algorithm maintains two distinct states:
*   **The Stack:** Tracks the valid chronological history of scores.
*   **The Accumulator (`res`):** A running total that eliminates the $O(N)$ overhead of summing the stack at the conclusion of the process.

### 3. Handling Operation Branches
*   **`+` (Summation):** This is the most complex operation. To avoid destroying the top of the stack, the code performs a `pop()` followed by a `peek()` to retrieve the two most recent values. It then pushes the popped value back (preserving the state) before pushing the sum.
*   **`D` (Double):** Relies on `peek()` to retrieve the last value without removing it. This is a subtle point: if `pop()` were used instead, the history would be corrupted.
*   **`C` (Cancellation):** Effectively "undos" the last operation by updating `res` before the element is discarded from the stack.

---

## Key Insights & Performance Nuances

### 1. Integer Parsing Overhead
`Integer.parseInt(s)` is called twice in the `else` block. While not critical here, in a performance-sensitive environment with massive input arrays, storing the result of `Integer.parseInt(s)` in a variable before use would be a cleaner and slightly more optimized approach.

### 2. The "Pop/Push" Gymnastics
The logic for `+` (`stack.pop()`, `stack.peek()`, `stack.push(n1)`) is a manual implementation of a "peek-two-elements-sum" pattern. This is required because `java.util.Deque` does not provide an `access-by-index` method that ignores the top element (e.g., `get(1)` is possible but less idiomatic).

### 3. Edge Cases & Safety
*   **Empty Operations:** If the input array is empty, the loop terminates immediately, returning `0`. Correct.
*   **Invalid States:** The problem constraints imply that "C", "D", and "+" are always called when the stack has sufficient elements. If this were a production system with unvalidated input, a `NoSuchElementException` would be thrown by `pop()` or `peek()` on an empty stack. A production-ready version should include an explicit check (e.g., `if (!stack.isEmpty())`).

### 4. Integer Overflow
The solution uses `int` for `res`. If the sum of scores exceeds $2^{31}-1$, the result will overflow. Depending on the constraints (typically defined in the problem description), a `long` for the `res` variable would be a safer defensive programming choice if inputs are potentially large.

---
