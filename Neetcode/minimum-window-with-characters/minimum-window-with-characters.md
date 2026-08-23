# minimum-window-with-characters

## standard_sliding_window.java
*Style: detailed*

# Technical Deep-Dive: Sliding Window Minimum Window Substring

## Summary
The solution implements an **optimal sliding window algorithm** (Two-Pointer technique) to solve the Minimum Window Substring problem. The core objective is to identify the smallest contiguous sub-segment of string `s` that contains all characters present in string `t` (including duplicates). 

Instead of a brute-force $O(N^2)$ approach, this implementation utilizes a frequency map and a state-tracking mechanism (`formedCharacters`) to maintain a "valid" window. By dynamically expanding the right boundary and contracting the left boundary, the algorithm performs a single linear pass over the input.

---

## Complexity Analysis

### Time Complexity: $O(S + T)$
*   **Preprocessing:** $O(T)$ to traverse string `t` and populate the `requiredCount` frequency array.
*   **Window Traversal:** Each character of `s` is visited at most twice (once by the `right` pointer, once by the `left` pointer).
*   **Operations:** Inside the loop, all operations (array access, integer comparisons) are $O(1)$. 
*   Thus, $O(2N + M)$ simplifies to **$O(S + T)$**, where $S$ is the length of string `s` and $T$ is the length of string `t`.

### Space Complexity: $O(1)$ (effectively)
*   The frequency maps (`requiredCount`, `windowCount`) are fixed-size arrays of length 128 (ASCII/Extended ASCII range).
*   Regardless of input size, these arrays occupy a constant $128 \times 4 \text{ bytes} = 512 \text{ bytes}$ per map.
*   Therefore, space is **$O(\Sigma)$**, where $\Sigma$ is the alphabet size (128). In asymptotic terms, this is constant $O(1)$.

---

## Component Deep Dive

### 1. Frequency Tracking (`requiredCount` vs. `windowCount`)
*   The use of fixed-size integer arrays instead of `HashMap<Character, Integer>` is a critical optimization. It eliminates object instantiation overhead and hash collisions, providing a massive boost in constant-factor performance.
*   `requiredCharacters` tracks the *number of unique* characters needed, not the sum of frequencies. This allows the algorithm to check `formedCharacters == requiredCharacters` as a boolean flag for validity.

### 2. State-Driven Contraction (`formedCharacters`)
*   **Expansion:** As `right` increments, `windowCount` is incremented. When `windowCount[ch]` precisely matches `requiredCount[ch]`, it indicates we have satisfied the requirement for that character, incrementing `formedCharacters`.
*   **Contraction:** Once the window is valid, the `while` loop aggressively contracts `left`. It only decreases `formedCharacters` when a character's frequency falls strictly *below* the required threshold. This "lazy" check avoids unnecessary logical evaluations.

### 3. Edge Case Handling
*   **Length Constraints:** `t.length() > s.length()` returns `""` immediately, pruning the search space before any logic execution.
*   **Empty `t`:** Handled as an early exit.
*   **No valid window:** The `minWindowLength` is initialized to `Integer.MAX_VALUE`. If no window is found, the ternary operator ensures an empty string is returned rather than an index out-of-bounds error.

---

## Key Insights

### Performance Nuances
*   **The "Exact Match" Logic:** By using `windowCount[currentChar] == requiredCount[currentChar]`, we avoid incrementing `formedCharacters` unnecessarily when we have "extra" instances of a required character in the current window. This ensures that the validity state transitions only when a character requirement is strictly satisfied.
*   **Pointer Invariants:** The `left` pointer only ever moves forward. Because we check `minWindowLength` *before* incrementing `left`, we guarantee the smallest possible window starting at any valid `left` position.

### Subtle Considerations
*   **Character Set:** The code assumes ASCII (length 128). If the input included Unicode characters (e.g., emojis), the `int[128]` array would cause an `ArrayIndexOutOfBoundsException`. A more robust production-grade version might use a `Map<Character, Integer>` if the character set is unknown or significantly larger than standard ASCII.
*   **String Allocation:** The final call to `s.substring()` creates a new string object. In Java, this is technically $O(K)$ where $K$ is the length of the window. Since this happens only once at the end, it does not impact the overall Big O complexity, but it is worth noting if memory pressure is a significant concern for large strings.

---
