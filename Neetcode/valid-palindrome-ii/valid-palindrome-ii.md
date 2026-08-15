# valid-palindrome-ii

## attempt_1.java
*Style: concise*

### Study Notes: Valid Palindrome II (with Alphanumeric Filtering)

#### Overview
Checks if a string is a palindrome after ignoring non-alphanumeric characters and allowing for the removal of **exactly one** character to satisfy the palindrome property.

#### Key Components
*   `isAlphanumeric(char)`: Custom filter for A-Z, a-z, and 0-9.
*   `isPalindrome(String, int)`: A greedy helper that validates a string as a palindrome while skipping a specific index `i`.
*   `validPalindrome(String)`: The main entry point; uses a two-pointer approach to find the first mismatch and recursively checks if skipping either the left or right pointer results in a valid palindrome.

#### Logic Highlights
*   **Recursive Branching**: When `s[st] != s[en]`, the algorithm branches into two sub-problems: skipping the current `st` index or skipping the current `en` index. If either branch returns `true`, the original string is a valid "almost palindrome."
*   **Index Skipping**: The `isPalindrome` method explicitly excludes index `i`. This elegantly handles the "remove one character" requirement without needing string slicing/concatenation, maintaining $O(1)$ extra space complexity.
*   **Case Insensitivity**: Normalizes comparison using `Character.toUpperCase()`, ensuring uniformity across mixed-case input.
*   **Two-Pointer efficiency**: The approach remains $O(n)$ time complexity, as the mismatch branch is only triggered at most once.

---

## attempt_2.java
*Style: detailed*

# Technical Reference: Valid Palindrome II

## 1. Summary
The `validPalindrome` solution addresses the problem of determining whether a string can become a palindrome by deleting at most one character. 

The algorithmic approach utilizes a **Greedy Two-Pointer strategy**. The algorithm traverses the string from both ends moving inward. When a character mismatch occurs (`s[st] != s[en]`), the algorithm branches into two sub-problems: checking if the substring is a palindrome after removing the character at the `left` pointer OR after removing the character at the `right` pointer. If either sub-problem returns `true`, the condition is satisfied.

## 2. Complexity Analysis

### Time Complexity: $O(N)$
*   **Best Case:** $O(1)$ if the string is already a palindrome (the loop completes without triggering the branch).
*   **Worst Case:** $O(N)$. Even though there is a branching operation, we only branch **at most once**. 
    *   The primary `while` loop runs at most $N/2$ times.
    *   Upon the first mismatch, we invoke `isPalindrome` twice. Each call performs at most $O(N)$ comparisons.
    *   Since we return immediately if either sub-call succeeds, we perform effectively $O(N) + O(N) = O(N)$ total operations.

### Space Complexity: $O(1)$
*   The algorithm operates using a constant number of integer pointers (`st`, `en`).
*   No additional data structures (like recursion stacks, buffers, or HashMaps) are created. Even the `isPalindrome` helper is iterative, ensuring the memory footprint remains constant regardless of the input string length.

## 3. Component Deep Dive

### `validPalindrome` (Driver)
*   **Mechanism:** Employs two pointers starting at `0` and `s.length() - 1`. 
*   **Branching Logic:** The core insight is that if a mismatch occurs, we are forced to skip one character. Since we don't know which side (left or right) causes the disruption, we test both deletions.
*   **Early Exit:** Returns `true` immediately if the pointers cross, indicating the string is already a palindrome.

### `isPalindrome` (Validator)
*   **Mechanism:** A strictly iterative validator.
*   **Boundary Conditions:** It handles indices passed from the driver. It implicitly handles cases where the skip might result in a single-character substring (which is inherently a palindrome).
*   **Efficiency:** It does not create new substrings (which would incur $O(N)$ space and time per slice); it operates directly on the original string using indices to avoid allocation overhead.

### Edge-Case Handling
*   **Empty Strings / Single Characters:** The `while (st < en)` condition naturally handles strings of length 0 or 1, returning `true` immediately as the loop body is never entered.
*   **Two-Character Strings:** If `s = "ab"`, `st=0, en=1`. Mismatch occurs. `isPalindrome` is called for `(1, 1)` and `(0, 0)`, both returning `true`. Correct.
*   **Impossibility:** If the mismatch occurs, and both potential skips fail to result in a palindrome, the method correctly returns `false`.

## 4. Key Insights

*   **The "At Most Once" Constraint:** The logic relies on the fact that we do not need to backtrack further. Once a single character is deleted, the remaining string *must* be a perfect palindrome. If the `isPalindrome` sub-call finds another mismatch, the branch is invalid.
*   **Avoid Substring Creation:** A common anti-pattern in Java is using `s.substring()`. This would create a new object in the String Pool for every check, leading to $O(N^2)$ space complexity and significant GC pressure. By passing indices instead, we maintain $O(1)$ space.
*   **Performance Nuance:** The greedy approach is optimal because the decision to delete either the left or right character is mutually exclusive for the success condition. We do not need a DP table (which would be $O(N^2)$) because the problem constraint limits us to exactly one deletion.
*   **Potential Bug:** Ensure the `while` loop index management does not result in an `IndexOutOfBoundsException`. Because the helper function uses `st + 1` and `en - 1` only after a mismatch is detected, and the original loop ensures `st < en`, the pointers remain within the valid $[0, N-1]$ range.

---
