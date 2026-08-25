# evaluate-reverse-polish-notation

## standard_stack_solution.java
*Style: detailed*

# Engineering Deep-Dive: Reverse Polish Notation (RPN) Evaluator

## Summary
The solution implements an **RPN Evaluator** utilizing a **Post-fix notation processing algorithm**. Unlike Infix notation (standard human-readable math), RPN places operators after their operands, eliminating the need for parentheses or operator precedence rules. The algorithm leverages a **Stack data structure** to maintain the state of evaluated sub-expressions, ensuring that operands are consumed in a Last-In-First-Out (LIFO) manner that naturally aligns with the post-fix evaluation structure.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Derivation:** The algorithm iterates through the input array of $N$ tokens exactly once.
*   **Operations:** For each token, stack operations (`push`, `pop`) and arithmetic operations in `calculate` are performed in $O(1)$ constant time. String inclusion checks (`"+/*-".contains(token)`) are constant time since the operator set is fixed in size. Thus, total time is strictly linear.

### Space Complexity: $O(N)$
*   **Derivation:** In the worst-case scenario (e.g., an expression consisting entirely of operands such as `["1", "2", "3", "4", ...]`), the stack will store every operand provided in the input array.
*   **Bound:** The space consumed by the `ArrayDeque` grows linearly with the number of operands, resulting in $O(N)$ auxiliary space complexity.

---

## Component Deep Dive

### 1. `Deque<Integer>` (The Evaluation Engine)
We utilize `java.util.ArrayDeque` over `java.util.Stack`. `ArrayDeque` is preferred in modern Java as it is not synchronized (no overhead of legacy locking) and provides better cache locality by using a contiguous array under the hood.

### 2. Operand Consumption Logic
The critical state transition occurs when an operator is encountered:
1.  **Popping Order:** The logic `int rightOperand = stack.pop();` followed by `int leftOperand = stack.pop();` is fundamental. Because a stack is LIFO, the first item popped is the *most recently added*, which represents the right-hand operand in non-commutative operations like division and subtraction. Reversing this order will result in incorrect evaluation (e.g., calculating `1/2` instead of `2/1`).

### 3. `calculate` Method (Switch Expression)
While this is a helper method, the use of a `switch` block is highly efficient for discrete branches.
*   **Type Safety:** The solution assumes valid RPN input. In a production environment, one would need to add defensive checks for `ArithmeticException` (division by zero) and `EmptyStackException` (malformed input).

---

## Key Insights & Optimization Nuances

*   **Operator Detection Efficiency:** The use of `"+/*-".contains(token)` is a clean way to check for membership. However, note that `contains` on a String is $O(M)$ where $M$ is the length of the string. While $M=4$ here, in high-frequency systems, checking `if (token.length() == 1 && "+-*/".indexOf(token.charAt(0)) != -1)` is technically more performant as it avoids heap allocation of the substring/pattern and utilizes index lookup.
*   **Integer Parsing Cost:** `Integer.parseInt(token)` is the most expensive part of the loop. If performance requirements are extremely stringent (e.g., real-time signal processing), manual character-by-character parsing into an integer is faster as it avoids intermediate object creation and regex-based validation inside `parseInt`.
*   **Edge Case: Single Token:** The logic gracefully handles expressions like `["42"]` by pushing the token to the stack and returning it immediately, correctly adhering to the mathematical definition of a single-term expression.
*   **Potential Bug - Division Truncation:** This implementation relies on standard integer division (`/`). Note that in Java, `/` truncates toward zero. This is standard for RPN implementations (e.g., LeetCode/JVM specs), but ensure the upstream requirements do not demand `Math.floor()` behavior for negative results.

---
