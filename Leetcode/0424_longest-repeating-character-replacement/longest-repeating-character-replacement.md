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

## standard_two_pointer_modified.java
*Style: concise*

## Quick Summary  
Finds the longest substring of `s` that can be turned into a single repeated character by changing at most `k` characters (LeetCode 424 – *Longest Repeating Character Replacement*).

---

## Key Components  

| Element | Purpose |
|---------|---------|
| **`characterReplacement(String s, int k)`** | Main method; returns the maximal length. |
| `boolean[] present` | Flags which uppercase letters actually appear in `s` to skip unnecessary target scans. |
| Outer loop `for (char targetCharacter = 'A' … 'Z')` | Treats each existing letter as the candidate “majority” character for the window. |
| Sliding‑window variables `left`, `right`, `targetCharacterCount` | Maintains a window where the number of non‑target chars ≤ `k`. |
| Condition `while ((right‑left+1) - targetCharacterCount > k)` | Shrinks the window when the allowed replacements are exceeded. |
| `maxLength = Math.max(maxLength, right‑left+1)` | Updates the best window size found for the current target. |

---

## Non‑Obvious Logic  

- **Pre‑filtering with `present`** – Scans the string once to know which letters occur; the outer loop skips letters that never appear, saving up to 26 unnecessary O(N) passes.  
- **Window invariant**: `windowSize - targetCharacterCount` equals the count of characters that would need to be replaced to make the whole window the `targetCharacter`. The `while` loop enforces this count ≤ `k`.  
- **Re‑using `targetCharacterCount`** – When `left` moves forward, the count is decremented only if the leaving character matches the target, keeping the invariant O(1) per step.  

Overall complexity: **O(26 · N) ≈ O(N)** time, **O(1)** extra space (aside from the fixed 26‑element boolean array).

---

## optimal_two_pointer.java
*Style: detailed*

# Technical Deep-Dive: Longest Repeating Character Replacement

## Summary
The provided solution employs the **Sliding Window** technique with a dynamic frequency map to identify the longest substring that can be transformed into a string of identical characters by performing at most `k` replacements. 

The core algorithmic insight is that for any window of length `(r - l + 1)`, if the count of the most frequent character within that window is `maxf`, the number of characters requiring replacement is `(r - l + 1) - maxf`. The algorithm maintains a valid window where `(window_size - maxf) <= k` and expands `r` greedily, only shrinking `l` when the constraint is violated.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Expansion:** The right pointer `r` traverses the string exactly once ($O(N)$).
*   **Contraction:** Although there is a `while` loop, the left pointer `l` also traverses the string at most once. Each character is added to the hash map once and removed from the hash map at most once.
*   **Map Operations:** With a character set size restricted to the English alphabet (26), map operations (`get`/`put`) are effectively $O(1)$.
*   **Total:** $O(N)$.

### Space Complexity: $O(1)$
*   The `HashMap` stores at most 26 unique keys (for uppercase English letters). Since the alphabet size is constant regardless of input string length $N$, the space used is constant $O(26) = O(1)$.

---

## Component Deep Dive

### 1. The `maxf` Optimization
One might be tempted to re-scan the map to find the new `maxf` after decrementing a count during contraction. However, `maxf` does not need to be perfectly accurate for every `l`. 
*   **Why?** If we shrink the window, the window size decreases. `maxf` only needs to represent the historical maximum frequency found in *any* valid window seen so far. If a new `maxf` is smaller than the previous one, it implies the current window cannot possibly exceed the maximum length we have already recorded. Thus, we only update `maxf` when we find a character with a frequency *higher* than the existing `maxf`.

### 2. The Sliding Window Constraint
The condition `(r - l + 1) - maxf > k` effectively checks: "Are the number of characters that *aren't* the majority character greater than our budget `k`?" If true, the window is invalid, and we must increment `l` until the constraint is met.

### 3. Edge-Case Handling
*   **$k=0$:** The logic correctly defaults to finding the longest contiguous substring of identical characters.
*   **Empty string:** The loop condition `r < s.length()` handles this gracefully, returning `0`.
*   **All characters different:** `maxf` stays at 1, the window length stays at $k+1$, correctly returning $k+1$ (assuming $k < N$).

---

## Key Insights & Nuances

### Why `maxf` doesn't need to be recalculated
A common point of confusion is why we don't decrement `maxf` when `l` moves. 
*   If the current window size is `W` and `W - maxf <= k`, then `res = W`. 
*   If we move to a state where we increment `r`, we only care if we can find a window *larger* than `res`. 
*   Because `maxf` only increases when `r` finds a better candidate, and `res` is only updated when the window is valid, the variable `maxf` acts as a "high-water mark." Even if the local `maxf` for a smaller window is lower, it doesn't invalidate our ability to track the longest window found thus far.

### Performance Nuance: Data Structures
While the code uses `HashMap<Character, Integer>`, in performance-critical production systems where the input is guaranteed to be ASCII/English letters, replacing `HashMap` with an `int[26]` array is highly recommended. 
*   **Benefits:** This avoids the overhead of object hashing, autoboxing (`int` to `Integer`), and pointer chasing, potentially offering a 5x–10x speedup in Java due to cache locality and the removal of object allocation.

### Potential "Trap"
Be aware that this solution returns the maximum length of the *transformed* string. If the problem were modified to require the actual transformed string (reconstructing the result), the complexity would shift significantly, as one would need to track the dominant character and the indices of the modifications within the sliding window.

---
