# simplify-path

## standard_stack_solution.java
*Style: detailed*

# Deep-Dive Reference: Canonical Path Simplification

## 1. Summary
The `simplifyPath` implementation utilizes a **Stack-based state machine** to resolve Unix-style path navigation. The algorithm iterates through the path string, tokenizing directory segments delimited by `/`. By treating the path as a sequence of operations (push current, pop parent, or ignore), it reconstructs the canonical path by maintaining an ordered stack of valid directory names. This approach effectively handles redundant separators (`//`), self-references (`.`), and upward navigation (`..`) in linear time.

---

## 2. Complexity Analysis

### Time Complexity: $O(N)$
*   **Traversal:** The code performs a single pass over the input string of length $N$.
*   **Operations:** Inside the loop, `currentDirectory.append()` is an amortized $O(1)$ operation. The `processDirectory` method performs stack operations (`push`/`pop`) and string comparisons, all of which are $O(K)$ where $K$ is the length of the directory name. 
*   **Total:** Since each character is processed at most twice (once for appending and once for tokenizing), the complexity is strictly linear, $O(N)$.

### Space Complexity: $O(N)$
*   **Stack:** In the worst-case scenario (e.g., `/a/b/c/d/...`), the stack stores all characters of the path.
*   **StringBuilder:** The `currentDirectory` buffer stores the current token, which can approach $O(N)$ in a pathological case (e.g., a path with no slashes).
*   **Total:** $O(N)$ auxiliary space is required to store the components of the path.

---

## 3. Component Deep Dive

### `processDirectory` Function
This is the core logic handler. It maps input tokens to filesystem mutations:
*   **`..` (Parent Directory):** Triggers `stack.pop()`. The check `!stack.isEmpty()` is critical; it ensures that navigating above the root (`/../`) resolves to `/` rather than throwing an exception.
*   **`.` (Current Directory):** Explicitly ignored.
*   **Empty strings:** Occur when multiple slashes are encountered consecutively (e.g., `//`). The code ignores these to ensure the canonical path doesn't contain empty segments.
*   **Named Directories:** Valid directory names are pushed onto the `Deque`.

### Data Structure Selection: `ArrayDeque`
*   The implementation uses `ArrayDeque` instead of `Stack`. This is a best-practice decision in Java; `Stack` is a legacy synchronized class, whereas `ArrayDeque` provides a faster, non-synchronized stack implementation with better cache locality.

### String Reconstruction
*   The use of `stack.reversed()` (a feature in newer Java versions) is an elegant way to traverse the stack in FIFO order for concatenation.
*   **Edge Case:** If the stack is empty (e.g., input was `/../`), the code correctly returns `/`, as `String.join` on an empty collection returns an empty string, which is then prefixed by `/`.

---

## 4. Key Insights & Nuances

### 1. The Post-Loop Flush
A common oversight in tokenization algorithms is failing to process the final token if the string does not end with a delimiter. The inclusion of `processDirectory(stack, currentDirectory)` after the `for` loop ensures that the final directory in the path is captured, addressing cases like `/a/b`.

### 2. Performance Nuance: `StringBuilder.setLength(0)`
Rather than reallocating a new `StringBuilder` object for every directory segment, the code reuses the same buffer and clears it with `.setLength(0)`. This significantly reduces garbage collection (GC) pressure, especially for long paths, by keeping the underlying char array allocation stable.

### 3. Edge-Case Scenarios
*   **Redundant Slashes (`/a///b`):** The `processDirectory` logic effectively handles this by checking `!directoryName.isEmpty()`. When `//` occurs, the intermediate empty string is ignored.
*   **Root-level `..`:** By checking `!stack.isEmpty()` before calling `pop()`, the implementation correctly implements the Unix requirement that `cd ..` from root stays at root.

### 4. Potential Improvements/Bugs
*   **String Creation:** The code calls `directory.toString()` every time `processDirectory` is invoked. While correct, it creates a new `String` object for every directory found. For extremely high-throughput systems, this could be optimized by comparing the `StringBuilder` content directly with constant buffers if performance benchmarks indicate GC pressure.

---
