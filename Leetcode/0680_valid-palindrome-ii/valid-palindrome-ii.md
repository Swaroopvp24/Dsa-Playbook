# valid-palindrome-ii

## optimal_two_pointer.java
*Style: detailed*

# Engineering Deep Dive: Valid Palindrome II

## 1. Summary
The `validPalindrome` solution implements a **greedy two-pointer approach** to determine if a string can become a palindrome by deleting at most one character. 

The algorithmic strategy utilizes a "fail-fast" mechanism:
1. Traverse the string from both ends moving inward.
2. If a mismatch occurs, the string is not currently a palindrome. 
3. At the point of first mismatch, the algorithm branches into two sub-problems: check if the remaining substring is a palindrome after deleting either the character at the `st` (left) pointer or the `en` (right) pointer.
4. If either sub-problem returns `true`, the original constraint is satisfied.

## 2. Complexity Analysis

### Time Complexity: $O(N)$
*   **Best Case:** $O(1)$ (if the first or last characters don't match or the string is already a palindrome).
*   **Worst Case:** $O(N)$. 
    *   The primary traversal of the string takes $O(N)$.
    *   Upon encountering the first mismatch, we invoke the `isPalindrome` helper function. This helper scans the remaining substring exactly once.
    *   Since the problem only allows for *at most one* deletion, we never perform nested or recursive branches beyond the initial mismatch. The total work remains linear.

### Space Complexity: $O(1)$
*   The implementation uses a constant amount of extra space.
*   The approach is iterative and avoids auxiliary data structures (no stacks, queues, or substring copies). It operates strictly on the existing character indices of the input string.

## 3. Component Deep Dive

### `validPalindrome(String s)`
*   **Logic:** Acts as the primary orchestrator. It maintains two pointers, `st` and `en`, converging toward the center.
*   **Termination:** If the loop completes without a mismatch, the string is already a perfect palindrome (`return true`).
*   **Branching:** The expression `isPalindrome(s, st + 1, en) || isPalindrome(s, st, en - 1)` is the core logic. It lazily evaluates: if `isPalindrome` returns `true` for the first option, the second is never evaluated (short-circuiting).

### `isPalindrome(String s, int st, int en)`
*   **Logic:** A standard two-pointer validator.
*   **Edge Case Handling:**
    *   **Empty strings/Single characters:** The loop `st < en` handles this implicitly; the condition will be false, and it will return `true`, which is mathematically correct.
    *   **Indices:** The function is pure and relies on the caller to provide valid bounds within the string length to avoid `StringIndexOutOfBoundsException`.

## 4. Key Insights

### Greedy vs. Dynamic Programming
While many "edit distance" problems require Dynamic Programming ($O(N^2)$), the constraint "at most one deletion" makes this specific variant solvable in $O(N)$. The greedy approach works because we only care about the *first* discrepancy. If we encounter a mismatch at indices $i$ and $j$, we have no choice but to remove either $s[i]$ or $s[j]$. There is no "future" state where deleting a character elsewhere could resolve this specific mismatch.

### Performance Nuance: No Substring Copies
A common pitfall in Java is using `s.substring()`. If the implementation used `s.substring(...)`, the space complexity would escalate to $O(N)$ due to new string allocations in the heap. By passing indices (`st`, `en`) to the helper function, we maintain $O(1)$ space, which is critical for memory-constrained environments or large input strings.

### Potential Edge Cases
*   **Unicode/UTF-16:** `charAt()` in Java retrieves a 16-bit `char`. If the string contains Unicode characters represented by surrogate pairs (e.g., certain emojis), this implementation will fail. For production-grade code handling international text, `s.codePointAt()` would be required.
*   **Null Inputs:** The current code does not check for `null`. A production guard clause (`if (s == null) return false;`) should be added to prevent `NullPointerException`.

---
