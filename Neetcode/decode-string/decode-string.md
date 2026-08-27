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

## optimal_stack_solution.java
*Style: detailed*

# Technical Deep-Dive: String Decoding Algorithm

## Summary
The solution employs a **dual-stack parsing strategy** to process nested string encodings of the form `k[encoded_string]`. By decoupling the repetition frequency (`k`) from the string context, the algorithm effectively treats the problem as a depth-first traversal of a nested structure. It uses an iterative approach to simulate an explicit recursion stack, avoiding potential `StackOverflowError` overhead while maintaining the state necessary to reconstruct deep, nested concatenations upon encountering the `]` delimiter.

---

## Complexity Analysis

### Time Complexity: $O(S + N \cdot m)$
*   **$S$**: The length of the input string.
*   **$N$**: The number of nested groupings.
*   **$m$**: The average repetition count.
*   **Why**: We iterate through the input string once ($O(S)$). However, the string construction within the `]` block involves repeated appending. In the worst-case (deeply nested strings), the total number of characters produced can be exponential relative to the input length. The cost is bounded by the total length of the final decoded string.

### Space Complexity: $O(D + M)$
*   **$D$**: Maximum nesting depth of brackets.
*   **$M$**: Total characters stored in the stacks during intermediate processing.
*   **Why**: The `previousStrings` stack grows proportionally to the depth of nested brackets. The `currentString` `StringBuilder` holds intermediate segments. In a worst-case scenario (e.g., `1[1[1[a]]]`), the stack size is proportional to the nesting level.

---

## Component Deep Dive

### 1. The Dual-Stack State Management
*   **`previousStrings` (Stack<String>)**: Captures the "prefix" context. When entering a new bracketed scope, the work-in-progress string must be preserved because the current scope is a child of the outer scope.
*   **`repeatCounts` (Stack<Integer>)**: Manages the multiplier for the imminent inner string. By pushing `currentNumber` at the `[` boundary, we maintain the scope-specific frequency.

### 2. The Finite State Machine (Implicit)
The logic transitions through four explicit states based on character type:
*   **Digit**: Accumulates numeric value. Note the `currentNumber * 10 + (ch - '0')` logic; this handles multi-digit numbers (e.g., `12[a]`) correctly, which a simple `char` conversion would miss.
*   **`[`**: Performs a state push. The current progress is "frozen" into the stack, and the builder is reset to begin a new scope.
*   **`]`**: Performs the "collapse" operation. This is where the heavy lifting occurs:
    1.  The inner scope is finalized.
    2.  The parent scope is popped from `previousStrings`.
    3.  The repeated multiplication occurs via `StringBuilder.append()` in a loop.
*   **Characters**: Standard character ingestion into the active scope.

### 3. Edge-Case Handling
*   **Multi-digit numbers**: The algorithm correctly handles `k >= 10` by shifting the magnitude of `currentNumber` before adding the next digit.
*   **Nested structures**: Because it uses a stack, `3[a2[c]]` correctly processes `c` into `cc`, then combines with `a` to get `acc`, and finally triples it to `accaccacc`.
*   **No nesting**: A string like `abc` functions correctly as the stack remains empty and the character appends directly to the primary builder.

---

## Key Insights

### Performance Nuance: StringBuilder Efficiency
While using `StringBuilder` is more efficient than `String` concatenation, the `for` loop `currentString.append(decodedPart)` can be a hotspot. 
*   **Optimization Opportunity**: For very large repeat counts, calling `append` in a loop may cause frequent `StringBuilder` resizing (internal buffer doubling). If performance constraints are extreme, one could pre-calculate the required capacity and use a `char[]` buffer or a more sophisticated string-building primitive.

### The "Reset" Trap
Observe the order of operations in the `[` case:
1.  Push `currentString` to stack.
2.  Reset `currentString = new StringBuilder()`.
If the order were reversed, the current context would be lost or overwritten before being saved. The explicit separation of the *builder* from the *stack* is the crucial pattern that prevents state pollution across scopes.

### Subtle Potential Bug
If the input format is invalid (e.g., mismatched brackets), the current code will likely throw an `EmptyStackException` on a `pop()` or leave the result in an incomplete state. In a production environment, adding a `peek()` or `isEmpty()` check before `pop()` is highly recommended to provide descriptive parsing errors.

---
