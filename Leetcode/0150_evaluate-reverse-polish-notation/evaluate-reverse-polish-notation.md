# evaluate-reverse-polish-notation

## standard_stack_solution.java
*Style: detailed*

# Engineering Deep Dive: Reverse Polish Notation (RPN) Evaluator

## Summary
The solution implements a **Stack-based Expression Evaluator** for arithmetic expressions in Reverse Polish Notation (Postfix notation). By design, RPN eliminates the need for parentheses or operator precedence rules. The algorithm processes tokens linearly: operands are pushed onto an `ArrayDeque` acting as a LIFO stack, and operators trigger the consumption of the two most recent operands. This transforms a functional postfix string into a single scalar result in a single pass.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Derivation:** The algorithm performs a single pass over the input array of $N$ tokens. Each token is either parsed into an integer (constant time, bounded by string length) or processed via a stack operation (`push`/`pop`). Since stack operations are $O(1)$, the total time complexity is strictly linear $O(N)$.

### Space Complexity: $O(N)$
*   **Derivation:** In the worst-case scenario (e.g., an expression where all tokens are operands, such as `1 2 3 4 5`), the stack will grow to size $N$. While the steady-state for a balanced RPN expression is much smaller, the auxiliary space allocated for the stack remains proportional to the number of operands provided, leading to $O(N)$ worst-case space complexity.

---

## Component Deep Dive

### 1. Stack Selection: `ArrayDeque`
*   **Mechanism:** The code utilizes `java.util.ArrayDeque` instead of `java.util.Stack`. 
*   **Rationale:** `java.util.Stack` is a legacy collection that extends `Vector`, inheriting unnecessary `synchronized` overhead, which imposes a performance penalty due to internal monitor locking. `ArrayDeque` provides a lock-free, resizable array implementation that is significantly more performant for stack-based operations.

### 2. Operator Dispatcher (`calculate` function)
*   **Logic:** The helper method acts as a functional map. 
*   **Edge Case Handling:**
    *   **Integer Division:** Note that Java’s integer division (`/`) truncates toward zero. This is the expected behavior for RPN problems, but it differs from floor division for negative results.
    *   **Operator Integrity:** The code relies on `"+/*-".contains(token)`. While succinct, this has a subtle vulnerability: if an unexpected string is passed that contains these characters (e.g., a multi-character token or invalid input), the logic may misinterpret it. In a production environment, an explicit `switch` on the operator or a `Set<String>` lookup is more robust.

### 3. Operand Parsing
*   **Mechanism:** `Integer.parseInt(token)` is invoked only for non-operator tokens. This implicitly assumes that all tokens that are not operators are valid integer representations.

---

## Key Insights & Performance Nuances

### 1. Operand Order Sensitivity
*   **Crucial Detail:** Because the stack is LIFO, the first `pop()` operation retrieves the *right* operand, and the second `pop()` retrieves the *left* operand. For commutative operations ($+$, $*$), this is irrelevant, but for non-commutative operations ($-$, $/$), failing to maintain this `(left, right)` order will result in logically inverted outcomes (e.g., calculating $2/8$ instead of $8/2$).

### 2. Memory Locality
*   `ArrayDeque` is backed by a circular array. Unlike `LinkedList` (which is often incorrectly used for stacks), `ArrayDeque` offers superior cache locality because its elements are stored in contiguous memory blocks. This minimizes CPU cache misses during heavy iteration.

### 3. Potential Failure Points
*   **Stack Underflow:** The code assumes the input `tokens` array represents a mathematically valid RPN expression. If the input is malformed (e.g., an operator is provided with insufficient operands in the stack), `stack.pop()` will throw a `NoSuchElementException`. 
*   **Integer Overflow:** The problem uses `int`. If the cumulative result of multiplications or additions exceeds `Integer.MAX_VALUE` or is less than `Integer.MIN_VALUE`, silent integer overflow will occur. Depending on the environment, a `long` transition or `Math.addExact` might be required for production-grade arithmetic safety.

### 4. Optimization Path
If memory constraints were extreme and the operator set limited, one could replace the string comparisons (`"+/*-".contains`) with a hash-based lookup or a single character comparison (`token.charAt(0)` if length is 1) to reduce the overhead of string object creation and `contains()` method scanning.

---
