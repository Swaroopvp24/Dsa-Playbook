# longest-repeating-character-replacement

## standard_two_pointer.java
*Style: detailed*

# Technical Reference: Longest Repeating Character Replacement

## Summary
The provided solution utilizes a **sliding window** technique combined with an exhaustive search over the unique character set of the input string. The algorithm treats the problem as a constraint-satisfaction task: for every distinct character `c` present in the string, it calculates the longest substring where `c` can serve as the "majority" character, filling all other positions using the available `k` budget.

## Complexity Analysis

### Time Complexity: $O(26 \cdot N) \approx O(N)$
*   **Outer Loop:** Iterates over the `charSet`, which has a maximum size of 26 (assuming English uppercase alphabet).
*   **Inner Logic:** A sliding window approach is employed for each character. The `r` pointer traverses the string once ($N$ iterations), and the `l` pointer also traverses the string at most once per unique character.
*   **Total:** Since the number of unique characters is bounded by a constant (alphabet size $\Sigma = 26$), the complexity simplifies to linear time relative to string length $N$.

### Space Complexity: $O(1)$ (or $O(\Sigma)$)
*   **Auxiliary Space:** The `HashSet` stores at most $\Sigma$ characters. 
*   **Variables:** The solution uses a fixed number of integer primitives (`res`, `count`, `l`, `r`). 
*   **Result:** The space is effectively constant relative to the input size $N$, dominated only by the character set size.

---

## Component Deep Dive

### 1. The Sliding Window Mechanism
For a fixed target character `c`, the constraint is:
`Window Length - Count(c) <= k`
Where `(r - l + 1)` is the current window size and `count` is the frequency of `c` in that window. 
*   **Expansion:** The `r` pointer expands the window. If the character at `r` is `c`, the `count` increments.
*   **Contraction:** When the constraint is violated (`(r - l + 1) - count > k`), the `l` pointer increments until the window is valid again. This ensures that for every `r`, the window `[l, r]` represents the longest valid substring ending at `r` for that specific character.

### 2. Edge-Case Handling
*   **k = 0:** The logic correctly defaults to finding the longest substring of identical characters, as the `while` loop condition `(r - l + 1) - count > 0` forces a contraction as soon as a non-`c` character is encountered.
*   **k >= string length:** The condition `(r - l + 1) - count > k` will never be true, allowing the window to expand to the full length of the string.
*   **Empty String:** The loop over `charSet` will not execute, returning `0` correctly.

---

## Key Insights & Performance Nuances

### Algorithmic Efficiency
While this implementation is $O(26 \cdot N)$, it is **sub-optimal** compared to the canonical $O(N)$ approach. The standard implementation maintains a single window and tracks the frequency of the *most frequent character currently in the window* via a frequency array (`int[26]`). 

**Why this version is less efficient:**
1.  **Multiple Passes:** By iterating through each unique character, this code performs redundant traversals of the string.
2.  **Memory Access:** It builds a `HashSet`, which involves overhead compared to a primitive array.

### Subtle Optimization (The "Global Max" Strategy)
The primary weakness of the current implementation is that it re-scans the string 26 times. To optimize to a single-pass $O(N)$ algorithm:
*   Instead of iterating over `charSet`, use a single frequency array `int[26]`.
*   Maintain a `maxFreq` variable representing the frequency of the most abundant character *seen so far* in any window.
*   The valid condition becomes: `(r - l + 1) - maxFreq <= k`.
*   *Note:* You do not need to shrink the window size if a new character does not improve the `maxFreq`, as we are only interested in finding a length *greater* than the current `res`.

### Potential Improvements
*   **Replace `HashSet`:** Using `boolean[26]` or `int[26]` to track character existence would be significantly more memory-efficient and avoid boxing overhead.
*   **Refactor to single-pass:** The logic can be collapsed into a single `while` loop, reducing the constant factor of the runtime by roughly 26x.

---
