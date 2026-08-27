# decode-string

## standard_stack_solution.java
*Style: detailed*

# Technical Reference: Recursive String Decoding via Stack-Based Iteration

## Summary
The solution employs a **Manual Stack-Based Parsing** approach to handle nested pattern decoding ($k[string]$). By leveraging an `ArrayDeque` as a LIFO buffer, the algorithm flattens the nested structures from the inside out. When a closing bracket `]` is encountered, the algorithm pops the preceding elements until the matching `[` is found, parses the multi-digit integer multiplier, computes the repeated segment, and pushes the resulting expansion back onto the stack. This effectively transforms a recursive definition into an iterative process.

## Complexity Analysis

### Time Complexity: $O(S \cdot N)$
Where $S$ is the total length of the decoded output and $N$ is the depth of the nested structures.
*   **Expansion:** Each character in the original string is pushed and popped from the stack at least once. 
*   **String Construction:** The `StringBuilder.insert(0, ...)` operation is $O(K)$ where $K$ is the length of the string segment being built. In the worst-case scenario of highly nested structures, repeatedly re-inserting at the head of a `StringBuilder` or the stack results in a quadratic cost relative to the length of the decoded substring.
*   **Parsing:** Multi-digit integer parsing is $O(D)$ where $D$ is the number of digits. Since $D$ is bounded by the magnitude of the repeat factor, it is effectively $O(1)$ in practice.

### Space Complexity: $O(S)$
*   **Auxiliary Space:** The `Deque` stores all characters of the encoded string, and eventually all characters of the expanded decoded string.
*   **Resultant Storage:** The `decodedPart` and `repeatedPart` builders temporarily store segments of the final string. In the worst case, the stack holds the entirety of the fully decoded string before the final concatenation.

---

## Component Deep Dive

### 1. The LIFO Stack Strategy
The choice of `Deque<String>` serves as a state machine. Unlike a standard `char` stack, storing `String` objects allows the logic to handle multi-digit numbers (e.g., `"12"`) and multi-character substrings as single entities, simplifying the popping process during bracket resolution.

### 2. Inner-Bracket Resolution (`while (!stack.peek().equals("["))`)
This loop acts as the delimiter for the current nesting level. By popping until `[` is reached, the algorithm isolates the "base" string.
*   **Performance Note:** `StringBuilder.insert(0, ...)` is an $O(n)$ operation for each character/string added because it triggers an array copy of the underlying `char[]`. For very large inputs, this creates a performance bottleneck.

### 3. Multi-Digit Multiplier Parsing
The implementation handles `k` values $> 9$ by checking `Character.isDigit` on the top of the stack. This logic correctly gathers digits in reverse order (since they were pushed sequentially) and reconstructs them into an integer.

---

## Key Insights & Performance Nuances

### The "Insert" Bottleneck
The current code relies heavily on `StringBuilder.insert(0, ...)`. 
*   **The Problem:** `insert(0, ...)` causes an $O(N)$ shift for every character appended. 
*   **The Optimization:** It is mathematically more efficient to `append()` the characters in reverse order and then call `sb.reverse()` once per block, or better yet, maintain the stack in a way that avoids head-insertion entirely.

### Sub-optimal Concatenation
The final construction phase (`result.insert(0, stack.pop())`) repeats the same $O(N^2)$ behavior found in the inner loops. For a string of length $L$, this loop will take $O(L^2)$ time. 
*   *Senior Engineer Recommendation:* Use an auxiliary `Deque` to store popped elements and iterate in reverse, or append to a `StringBuilder` and perform a single `.reverse().toString()` at the end.

### Potential Edge Cases
*   **Deep Nesting:** While the stack prevents stack overflow errors associated with recursion, the heap can be exhausted if the repeat count $k$ is extremely large (e.g., `10000[a]`).
*   **Leading/Trailing non-bracketed characters:** The algorithm handles these gracefully because the final loop drains the stack, concatenating any segments that were not subject to bracket multiplication.
*   **Empty Strings:** If `s` is `""`, the loop is skipped, and an empty string is returned, which is correct.

### Subtle Bugs/Behavior
*   **Memory Overhead:** Storing `String` objects for every character in `stack.push(String.valueOf(currentChar))` creates significant memory overhead due to object headers in the JVM. For high-throughput scenarios, using a `char[]` or a `Deque<Character>` for raw input and a `StringBuilder` for temporary segments would significantly reduce GC pressure.

---
