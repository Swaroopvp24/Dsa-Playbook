# is-palindrome

## two_pointer.java
*Style: detailed*

# Technical Deep-Dive: Palindrome Validation

## Summary
The solution implements an **in-place two-pointer algorithm** to validate palindromic strings while ignoring non-alphanumeric characters. By maintaining two pointers starting at opposite ends of the string and converging toward the center, the algorithm avoids the $O(n)$ space overhead typically associated with "cleaning" the string (e.g., removing punctuation and converting to lowercase via regex or string concatenation). It evaluates the palindrome property in a single pass while performing on-the-fly character normalization.

---

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Derivation:** Every character in the input string `s` is visited at most once by either the `st` (start) or `en` (end) pointer. The nested `while` loops for skipping non-alphanumeric characters increment/decrement the pointers, but the total number of operations across all loops remains bounded by $N$ (where $N$ is the string length).
*   **Constant Factors:** The character-level checks (`isAlphanumeric` and `toUpperCase`) are $O(1)$ operations, ensuring the linear complexity holds.

### Space Complexity: $O(1)$
*   **Derivation:** The solution uses a fixed number of integer variables (`st`, `en`) regardless of input size. 
*   **Memory Efficiency:** Unlike a naive approach that creates a new filtered `String` or `StringBuilder` (which would be $O(n)$ space), this implementation performs all operations in the existing memory heap allocated to the input `String`.

---

## Component Deep Dive

### 1. `isAlphanumeric(char ch)`
*   **Logic:** Uses ASCII range comparisons (`A-Z`, `a-z`, `0-9`).
*   **Optimization:** Direct primitive comparison is significantly faster than regex-based checks (e.g., `s.matches("[a-zA-Z0-9]")`) because it avoids the overhead of regex engine compilation and pattern matching.

### 2. Convergence Logic (The Two-Pointer Pattern)
*   **The Inner `while` loops:** These act as "skip-ahead" mechanisms. The condition `st < en` inside these inner loops is critical. Without it, if the string contained only non-alphanumeric characters, the pointers could drift past each other, leading to an `IndexOutOfBoundsException` when accessing `charAt()`.
*   **Character Normalization:** `Character.toUpperCase()` is used to achieve case-insensitivity. Note that `Character.toLowerCase()` would be equally valid here; the choice is arbitrary as long as it is consistent.

### 3. Edge-Case Handling
*   **Empty String or Single Character:** The `while (st < en)` loop correctly handles both cases immediately returning `true`, which is the mathematically correct definition for a palindrome.
*   **Non-Alphanumeric Strings:** If a string consists purely of punctuation (e.g., `",.:"`), the inner loops will exhaust the pointers, the outer loop will never execute the comparison block, and the method will return `true` (an empty set of characters is considered a palindrome).

---

## Key Insights

### 1. The "Off-by-One" Danger
A common pitfall in two-pointer palindrome logic is failing to increment/decrement the pointers *after* the character comparison inside the main `while` loop. If `st++` and `en--` are omitted, the loop will enter an infinite state, re-comparing the same non-matching characters indefinitely.

### 2. Unicode and Locale Sensitivity
The current implementation relies on basic ASCII ranges for `isAlphanumeric`.
*   **Warning:** This code does not support international characters (e.g., `é` or `π`). If the requirements include full Unicode support, `isAlphanumeric` should be refactored to use `Character.isLetterOrDigit(char)`.

### 3. Performance Nuance: `charAt()` vs `toCharArray()`
*   Calling `s.charAt(i)` repeatedly inside a loop is efficient for `String` objects in Java. 
*   **Optimization Opportunity:** In scenarios where the string is extremely long and the system is under high memory pressure, converting to a `char[]` once via `s.toCharArray()` can provide a minor performance boost by avoiding the range-checking overhead inherent in `String.charAt()`. However, this introduces $O(n)$ space complexity. For most high-performance service requirements, the provided approach is the optimal balance of speed and memory footprint.

---
