# minimum-window-substring

## standard_sliding_window.java
*Style: detailed*

# Technical Reference: Minimum Window Substring

## Summary
The solution implements a **Sliding Window** technique with a two-pointer approach ($l$ and $r$) to identify the smallest contiguous subarray in $S$ that contains all characters (including duplicates) of $T$. 

The algorithm maintains a frequency map of characters in $T$ (`need`) and a dynamic frequency map of the current window (`have`). By utilizing an integer `formed` to track how many unique characters in $T$ have satisfied their frequency requirements in the current window, the algorithm achieves $O(N)$ time complexity, effectively transforming a search problem into a linear scan.

---

## Complexity Analysis

### Time Complexity: $O(|S| + |T|)$
- **Preprocessing:** Initializing the `need` map takes $O(|T|)$.
- **Sliding Window:** The right pointer ($r$) iterates through $S$ exactly once ($|S|$ steps). The left pointer ($l$) also iterates through $S$ at most once. Each character is visited by the pointers a constant number of times.
- **Operations:** Map lookups/updates are $O(1)$ due to the fixed-size array (ASCII 128) approach.

### Space Complexity: $O(1)$
- **Auxiliary Space:** The arrays `need` and `have` are size 128 (constant space), independent of input string size $N$ or $M$. Even if expanded to accommodate extended ASCII/Unicode, it remains $O(K)$ where $K$ is the alphabet size.

---

## Component Deep Dive

### 1. State Tracking (`need`, `have`, `req`, `formed`)
- `need`: Stores the required frequency of each character in $T$.
- `have`: Tracks the frequency of characters currently within the window $[l, r]$.
- `req`: The total number of *unique* characters in $T$ that must satisfy the frequency condition. 
- `formed`: A counter that increments only when `have[c] == need[c]`. This is critical: we only count a character as "satisfied" when the window frequency hits the target, preventing over-counting issues.

### 2. Shrinking Logic (`while (req == formed)`)
When `formed == req`, the window is valid. We perform two operations:
1. **Update Minimum:** Compare current length $(r - l + 1)$ with the global minimum.
2. **Minimize Window:** Increment $l$ to shrink the window.
    - If removing `s.charAt(l)` causes the frequency of that character to fall below the `need` threshold, `formed` is decremented. This immediately breaks the `while` loop, signaling that the window is no longer valid and shifting back to the expansion phase.

### 3. Edge Case Handling
- **T > S:** Returns `""` immediately to save cycles.
- **No valid window:** The `len` variable remains `Integer.MAX_VALUE`. The post-loop check `if (len == Integer.MAX_VALUE)` ensures we do not attempt to return a substring from invalid indices.

---

## Key Insights

### The `have[c] == need[c]` nuance
The logic `if (need[c] == have[c]) formed++` is the most subtle part of this implementation.
- By checking strictly for equality, we ensure that the `formed` counter only tracks characters that have met their specific frequency requirement. 
- If a character appears more times in the window than in $T$, `have[c]` will exceed `need[c]`, and `formed` will not increment again. This prevents redundant state updates and logically separates "contains at least" from "contains exactly."

### Performance Optimization
- **Array over HashMap:** Using `int[128]` instead of `Map<Character, Integer>` provides significant speedups. Java's `HashMap` introduces boxing/unboxing overhead and hash collisions, whereas array indexing translates to a simple memory offset calculation (`base_address + index * 4`).
- **Early Termination:** By checking `t.length() > s.length()` at the start, we handle empty strings or impossible constraints in $O(1)$ before performing $O(M)$ allocations.

### Subtle Risks
- **Character Encoding:** This implementation assumes ASCII characters (128 indices). If the input contains Unicode characters, the `int[128]` will trigger an `ArrayIndexOutOfBoundsException`. In a production environment, this should be replaced with `new int[256]` (extended ASCII) or a `Map` if the character set is arbitrary Unicode.

---
