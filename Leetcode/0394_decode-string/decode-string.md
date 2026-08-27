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

## optimal_stack_solution.java
*Style: detailed*

# Technical Deep-Dive: Nested String Decoder

## Summary
The solution employs a **Two-Stack Traversal** algorithm to handle nested structures, effectively mimicking an explicit recursion stack. By maintaining state for both the "prefix" string (the buffer before a bracket) and the "multiplier" (the repeat count), the algorithm transforms a recursive expansion problem into an iterative process. It treats the input as a stream, building the result bottom-up as it encounters closing brackets (`]`), ensuring that inner-most segments are expanded before outer-most ones.

---

## Complexity Analysis

### Time Complexity: $O(S + N \cdot m)$
*   **$S$**: The total number of characters in the original input string. We iterate through the string exactly once.
*   **$N \cdot m$**: Where $N$ is the number of characters in the output string, and $m$ is the average number of operations per character. 
*   **Reasoning**: While it seems linear, the `StringBuilder.append()` operations inside the nested loop create the final string. In a deeply nested case (e.g., `2[2[a]]`), characters are copied multiple times as the stack unwinds. The time complexity is technically proportional to the total length of the expanded output string.

### Space Complexity: $O(D + M)$
*   **$D$**: The maximum nesting depth of the brackets. This governs the maximum size of the `previousStrings` and `repeatCounts` stacks.
*   **$M$**: The space required for the `StringBuilder` to store the intermediate and final strings.
*   **Reasoning**: In the worst-case scenario (e.g., `100[a]`), the stack space is negligible, but the heap allocation for the `StringBuilder` tracks the accumulated length of the decoded string.

---

## Component Deep Dive

### 1. State Management (The Two Stacks)
*   **`previousStrings` (Stack<String>)**: Stores the "context" before a bracket. When the parser encounters `[`, it captures whatever has been accumulated thus far. This acts as the return address in a recursive call.
*   **`repeatCounts` (Stack<Integer>)**: Stores the multiplier associated with the current scope.
*   **Edge Case Handling**: By resetting `currentNumber` and `currentString` immediately after pushing to the stacks upon encountering `[`, the algorithm cleanly isolates the scope for the next nested substring.

### 2. The Unwinding Mechanism (The `]` Logic)
*   When `]` is reached, the algorithm treats it as a "Reduce" operation. 
*   **Restoration**: It restores the outer scope (`previousStrings.pop()`) and merges the fully expanded inner scope into it.
*   **Performance Note**: `currentString = new StringBuilder(previousStrings.pop())` is crucial. It keeps the prefix in a mutable `StringBuilder` to allow the subsequent `append()` operations for the inner string expansion to be efficient.

### 3. Digit Accumulation
*   `currentNumber = currentNumber * 10 + (ch - '0')`
*   This handles multi-digit multipliers (e.g., `12[a]`). Because `currentNumber` is reset at `[` (the start of a bracket), it prevents cross-contamination between nested digits.

---

## Key Insights & Performance Nuances

### String Immutability and Memory Pressure
Using `StringBuilder` inside the loop is the correct approach to avoid the $O(N^2)$ penalty associated with immutable `String` concatenation in Java. However, note that `currentString.append(decodedPart)` inside a loop `repeatCount` times will perform many internal buffer reallocations if the total length is large. If the output string is massive, `StringBuilder.ensureCapacity()` could be an optimization, though difficult to calculate dynamically here.

### The "Bottom-Up" Nature
The algorithm works because the input format is strictly delimited. By resolving the inner `[...]` first, the output of the inner block effectively becomes a "normal character" block for the outer `[...]`. This reduces the problem to a simple concatenation task once the inner expansion is complete.

### Potential Edge Cases to Watch
*   **No brackets**: The code handles strings like "abc" correctly because they simply fall into the `else` block and are appended to `currentString`.
*   **Consecutive blocks**: The current implementation supports `a3[b]2[c]` because the `previousStrings` stack is empty during the "a" phase, and "a" is kept in the base `currentString` buffer. 
*   **Stack Overflow**: If the input has excessive nested brackets (e.g., thousands of deep `1[1[...]]`), one could hit `StackOverflowError` in a recursive implementation, but this **iterative** approach is only limited by the JVM heap size, making it robust for extreme nesting depths.

---
