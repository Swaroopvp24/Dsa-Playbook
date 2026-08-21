# longest-repeating-substring-with-replacement

## standard_two_pointer.java
*Style: detailed*

# Deep-Dive: Longest Repeating Character Replacement

## Summary
The provided solution utilizes a **Sliding Window** technique combined with an exhaustive search over the character set. Instead of attempting to track all character frequencies simultaneously (as in the optimal $O(N)$ solution), this approach iterates through every unique character present in the string, treating each as the potential "target" character that will populate the final repeating sequence.

For each target character, the algorithm maintains a window $[left, right]$ where the number of non-target characters (calculated as `(windowSize - targetCharacterCount)`) does not exceed $k$. If the budget $k$ is exceeded, the window is contracted from the left.

---

## Complexity Analysis

### Time Complexity: $O(A \cdot N)$
*   **$N$**: The length of the string `s`.
*   **$A$**: The size of the alphabet (number of unique characters).
*   **Why**: We perform an outer loop over the unique characters ($A$). Inside, we execute a standard sliding window over the entire string ($N$). Since the sliding window pointer operations (`right` and `left`) are amortized $O(1)$ per iteration, the total complexity is $O(A \cdot N)$. If the alphabet size is constant (e.g., 26 for English uppercase), this effectively functions as $O(N)$.

### Space Complexity: $O(A)$
*   **Why**: The `HashSet` stores up to $A$ unique characters. The space consumed by the sliding window pointers and counters is $O(1)$. Thus, the space is dominated by the storage of unique characters.

---

## Component Deep Dive

### 1. Sliding Window Logic
The core mechanism is the constraint: `(right - left + 1) - targetCharacterCount <= k`.
*   `(right - left + 1)` represents the current window size.
*   `targetCharacterCount` represents the frequency of the current "target" character within the window.
*   The difference is the number of characters that *must* be changed to match the `targetCharacter`.

### 2. Contraction Strategy
When the condition `(windowSize - targetCharacterCount) > k` is triggered, the `while` loop forces the window to shrink from the `left`. This is crucial because it ensures the window is always the largest valid sequence for the current `targetCharacter`. Importantly, the window size only ever stays the same or grows; it never shrinks when moving `right`, which maintains the "longest" property.

### 3. Edge-Case Handling
*   **$k=0$**: The code correctly defaults to finding the longest substring of identical contiguous characters.
*   **$k \ge |s|$**: The code will correctly return the full length of the string, as the condition will never be violated.
*   **Empty String**: The loops will not execute, returning `maxLength = 0`, which is the correct behavior.

---

## Key Insights

### Performance Nuance: Sub-optimal vs. Optimal
While this solution is $O(A \cdot N)$, it is less efficient than the single-pass $O(N)$ approach.
*   **The Single-Pass Optimization:** In a single-pass approach, you maintain a frequency map of *all* characters in the window and track the `maxFrequency` (the frequency of the most common character seen so far). The condition becomes `(windowSize - maxFrequency) <= k`.
*   **Why the provided code is still valid:** The provided solution is conceptually easier to reason about because it transforms the problem into a "Filter" exercise: "If I *must* use this character as the result, what is the best I can do?"

### Potential Bottlenecks
*   **Repeated Scanning:** For strings with very large alphabets (e.g., Unicode), $O(A \cdot N)$ could become significantly slower than the single-pass approach.
*   **String Access:** `s.charAt(right)` is called frequently. In performance-critical Java applications, converting the string to a `char[]` (`s.toCharArray()`) before entering the loops would avoid the overhead of method calls to `charAt()` and potential bounds checking.

### Subtle Logic Observation
The current implementation resets `targetCharacterCount` inside the `targetCharacter` loop but not `left`. This is correct because the `left` pointer must be reset for every new character iteration. If the developer attempted to maintain state across characters, the logic would break, highlighting that the algorithm treats each character "target" as an independent problem instance.

---
