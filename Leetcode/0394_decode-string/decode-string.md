# decode-string

## standard_stack_solution.java
*Style: detailed*

# Technical Deep-Dive: Nested String Decoder

## Summary
The solution employs a **Stack-based parsing strategy** to handle nested recursive structures. By treating the string as an expression with formal delimiters (`[` and `]`), the algorithm processes the input in a single pass (O(N)). When a closing bracket is encountered, the algorithm shifts from "collection mode" to "reduction mode," popping the stack to resolve the innermost scope (multiplier and content) before pushing the result back onto the stack for outer-scope evaluation.

## Complexity Analysis

### Time Complexity: $O(S)$
*   **$S$ is the total length of the decompressed string.**
*   While iterating through the input string of length $N$, each character is pushed and popped from the stack exactly once. However, the `String.repeat(count)` operation and the subsequent `StringBuilder` manipulations are proportional to the size of the *resulting* string.
*   **Nuance:** Because nesting can cause exponential expansion (e.g., `3[a2[b]]`), the time complexity is bounded by the final output size, not just the input string length.

### Space Complexity: $O(M + S_{max})$
*   **Stack Storage:** The stack stores segments of the string. In the worst-case nested scenario, the stack holds $O(M)$ elements where $M$ is the depth of nesting.
*   **Intermediate Strings:** The space required to store the `repeatedStr` and the final result corresponds to the decoded length $S$.
*   **System Overhead:** `ArrayDeque` provides $O(1)$ amortized operations for push/pop, but internal array resizing may occur.

## Component Deep Dive

### 1. The Reduction Logic (`c == ']'`)
The critical logic triggers upon encountering the closing bracket. 
*   **Inner Content Extraction:** The algorithm pops until it hits `[` (the scope delimiter). Note the use of `substr.insert(0, ...)` which is technically $O(K)$ where $K$ is the current length of `substr`. Using `append` followed by `reverse()` would technically be more performant than repetitive `insert(0, ...)` calls.
*   **Multiplier Extraction:** Once the scope is clear, the algorithm continues to pop to extract multi-digit integers (e.g., `12[a]`). 

### 2. State Management via `ArrayDeque`
*   The `stack` holds both operators (delimiters), operands (integers), and partially decoded strings.
*   By pushing `String.valueOf(c)` for every character, the stack maintains the exact sequential order of the input, enabling a "bottom-up" evaluation of nested segments.

### 3. Edge Case Handling
*   **Multi-digit numbers:** The logic `while (... Character.isDigit(...))` correctly handles integers larger than 9 by popping individual characters and prepending them to reconstruct the full integer value.
*   **Nested structures:** Because the stack is LIFO, the innermost brackets are always processed first, effectively mimicking recursion without the overhead of the call stack.
*   **Empty inputs:** The `while (!stack.isEmpty())` loop ensures that even if the input is a simple unbracketed string, the stack contents are flushed into the result buffer correctly.

## Key Insights & Optimization Nuances

*   **The `insert(0, ...)` Bottleneck:** 
    *   In the provided implementation, `substr.insert(0, stack.pop())` is an $O(L^2)$ operation (where $L$ is the length of the string being reconstructed) because `insert(0)` requires shifting all elements in the `StringBuilder` internal buffer.
    *   **Optimization:** A more performant approach would be to collect characters in a standard list, and then reverse the list once, or use a `Deque` to store characters and append them to a `StringBuilder` in the correct order.

*   **Memory Pressure:**
    *   The repeated usage of `String.repeat()` and `new StringBuilder()` creates significant transient object allocations. In highly nested/repetitive cases, this may trigger frequent Garbage Collection cycles.

*   **Implicit Order of Operations:**
    *   The order of operations is rigid: 1) Pop content until `[`, 2) Pop `[`, 3) Pop digits. This assumes the input format is strictly valid (e.g., `k[...]`). If the input contains malformed brackets (e.g., `[a]3`), the `stack.peek().charAt(0)` call or `Integer.parseInt` will throw a `NumberFormatException` or `NullPointerException`. A production-grade version should include explicit validation of the input grammar.

*   **Performance Insight:**
    *   The `stack.peek().equals("[")` check is robust, but string comparisons in Java have a small overhead. Given the constrained alphabet, a character-based approach using a `Stack<Object>` or two separate stacks (one for counts, one for strings) is often faster as it avoids wrapping characters into `String` objects entirely.

---
