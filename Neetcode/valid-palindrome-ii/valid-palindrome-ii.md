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
