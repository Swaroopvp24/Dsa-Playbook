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
