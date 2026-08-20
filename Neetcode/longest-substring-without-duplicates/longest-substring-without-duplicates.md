# longest-substring-without-duplicates

## standard_sliding_window.java
*Style: detailed*

# Technical Reference: Longest Substring Without Repeating Characters

## 1. Summary
The provided solution implements a **Sliding Window** technique to identify the longest substring containing unique characters. By maintaining a dynamic window `[l, r]` and a `HashSet` representing the current window's state, the algorithm achieves linear time complexity. The window expands by advancing the right pointer `r` and contracts from the left pointer `l` only when a duplicate character is encountered, ensuring the window always contains a valid set of unique characters.

## 2. Complexity Analysis

### Time Complexity: $O(n)$
*   **Derivation:** Although there is a nested `while` loop, the right pointer `r` traverses the string exactly once ($n$ iterations). The left pointer `l` also traverses the string at most once. Each character is added to and removed from the `HashSet` exactly once.
*   **Amortized Analysis:** Since each index is visited by `l` and `r` at most once, the total number of operations is $2n$, resulting in $O(n)$.

### Space Complexity: $O(min(m, n))$
*   **Derivation:** The space complexity is dictated by the `HashSet`. In the worst case, the set stores every character in the string ($O(n)$). However, if the alphabet size $m$ (e.g., 256 for extended ASCII) is smaller than $n$, the space is capped at $O(m)$.

## 3. Component Deep Dive

### The Sliding Window Mechanism
*   **`r` (Right Pointer):** The iterator that expands the search frontier. For every iteration, it blindly adds the current character `s.charAt(r)` to the set.
*   **`l` (Left Pointer):** The contraction mechanism. It resolves conflicts. When a character exists in the `seen` set, the algorithm effectively "slides" the start of the window forward until the duplicate is evicted.

### HashSet Lifecycle
*   The `seen` set functions as a **lookup table** for $O(1)$ containment checks. 
*   **Conflict Resolution:** The `while` loop `seen.remove(s.charAt(l++))` is critical. It ensures that the window shrinks just enough to eliminate the duplicate, keeping the window state consistent.

### Edge Case Handling
*   **Empty String:** `s.length() == 0` returns 0 correctly.
*   **Single Character:** Returns 1, as the loop executes once and `r - l + 1` evaluates to 1.
*   **All Unique:** `r` simply increments to the end, `seen` grows to length $n$, and `count` captures the maximum window size.
*   **All Same:** The `while` loop triggers on every step, keeping the window size at 1.

## 4. Key Insights & Optimization Nuances

### Hash Set vs. Array/Map Optimization
*   **Current State:** The `HashSet<Character>` approach has overhead due to auto-boxing (`char` to `Character`) and internal bucket management.
*   **Optimization Strategy:** For production-grade performance in Java, replace `HashSet` with an integer array `int[128]` (or `256` for ASCII). 
    *   Store the **last seen index** of each character instead of presence. 
    *   This allows the left pointer `l` to jump directly to `lastSeen[char] + 1` instead of incrementing one by one, reducing the number of `remove` operations.

### The "r - l + 1" vs "seen.size()"
*   The current implementation uses `r - l + 1` for the calculation. This is logically equivalent to `seen.size()` provided the internal state of the `HashSet` and the pointers `l` and `r` are perfectly synchronized. Using `r - l + 1` is generally preferred as it relies on simple integer arithmetic rather than a method call on a collection object.

### Potential Bug: Integer Overflow
*   While impossible in this specific implementation (due to string length limitations in Java's heap), always be wary of index-based arithmetic if dealing with custom string buffers or large-scale data processing where indices could exceed `Integer.MAX_VALUE`.

---
