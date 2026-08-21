# permutation-string

## standard_sliding_window.java
*Style: concise*

### Study Notes: Permutation in String

#### Overview
This solution determines if `s2` contains a permutation of `s1` by using a **sliding window** of fixed size (`s1.length()`) and maintaining frequency counts of characters in both strings. It achieves $O(n)$ time complexity by updating the match status incrementally rather than re-comparing the full frequency array.

#### Key Logic
*   **`targetFreq` / `windowFreq`**: Two integer arrays of size 26 tracking character counts.
*   **`matchingChars`**: An integer state representing how many of the 26 character slots currently have identical counts between the window and `s1`. If this reaches 26, the current window is a permutation of `s1`.

#### Non-Obvious Logic
*   **Incremental Update**: Instead of checking `Arrays.equals()` in every loop iteration, we update `matchingChars` only when a frequency change pushes a count into or out of parity with `targetFreq`.
    *   **Entering Window**: If `count++` makes it equal to target, `matchingChars++`. If `count` becomes `target + 1`, it was previously a match but is now broken, so `matchingChars--`.
    *   **Leaving Window**: If `count--` makes it equal to target, `matchingChars++`. If `count` becomes `target - 1`, the previous match is destroyed, so `matchingChars--`.
*   **Edge Case**: The final `matchingChars == 26` check is required outside the loop to catch a permutation occurring at the very end of `s2`.

---

## sliding_window-myversion.java
*Style: detailed*

# Technical Reference: Sliding Window Permutation Check

## Summary
The solution employs a **fixed-size sliding window** strategy combined with **frequency counting** to determine if any permutation of string `s1` exists as a substring within `s2`. 

Instead of generating permutations (which is $O(n!)$), the algorithm treats the problem as a frequency equivalence check. By maintaining a sliding window of length `len(s1)` over `s2` and updating the character frequency counts incrementally, we transform the problem into a constant-time comparison between two character distribution vectors (size 26).

---

## Complexity Analysis

### Time Complexity: $O(L_1 + (L_2 - L_1) \cdot \Sigma)$
*   **$L_1$**: Length of `s1`.
*   **$L_2$**: Length of `s2`.
*   **$\Sigma$**: Size of the alphabet (constant 26).
*   **Breakdown**: Initializing `targetFreq` takes $O(L_1)$. The sliding window traverses `s2` exactly once ($O(L_2 - L_1)$). Within each step of the iteration, `Arrays.equals` performs a linear scan over the size-26 array. Since $\Sigma$ is constant, this is effectively $O(L_2)$.

### Space Complexity: $O(\Sigma)$
*   **Breakdown**: We utilize two fixed-size integer arrays of length 26 to store frequency counts. This is $O(1)$ auxiliary space, as it remains constant regardless of input string length.

---

## Component Deep Dive

### 1. Frequency Distribution Vectors
The algorithm uses `int[26]` arrays as histograms. This is the optimal data structure for alphabet-constrained problems as it provides $O(1)$ lookups and updates, avoiding the overhead of hash maps (avoiding hash collisions and object allocation).

### 2. The Sliding Mechanism
The implementation uses a `right` pointer for expansion and an implicit `left` pointer update.
*   **Synchronization**: The loop logic `while (right - left >= windowSize)` ensures that the window size strictly adheres to `s1.length()`. 
*   **Optimization Note**: The current implementation updates the window by removing `s2.charAt(left)` *after* the window exceeds size, then adding `s2.charAt(right)`. While functional, this can be slightly optimized by shifting the `left` pointer index *before* the check to make the window update more atomic.

### 3. Edge-Case Handling
*   **Length Mismatch**: The `if (windowSize > s2.length())` guard clause correctly handles cases where a substring permutation is mathematically impossible.
*   **Alphabet Boundary**: The code assumes `s2.charAt(i) - 'a'` will always fall within $[0, 25]$. This assumes input strings are restricted to lowercase English letters. If non-ASCII or mixed-case characters were introduced, the index math would result in an `ArrayIndexOutOfBoundsException`.

---

## Key Insights

### Performance Optimization: Avoiding `Arrays.equals`
While `Arrays.equals` is $O(\Sigma)$, it is called $L_2 - L_1$ times. We can optimize this by tracking a `matchCount` variable representing the number of characters whose frequencies are currently identical between the two arrays:
1.  Maintain a `diff` variable or `matchCount`.
2.  Update the count only when an increment/decrement makes a frequency identical to the target.
3.  This reduces the inner comparison from $O(\Sigma)$ to $O(1)$, though the overall complexity class remains the same.

### Subtle Bug: The Logic Flow
In the provided code, the loop structure is:
```java
while (right - left >= windowSize) { remove; left++; }
add;
check;
```
This is logically sound, but note that it performs the `Arrays.equals` check *after* every single insertion once the window is full. If $L_2 \gg L_1$, this is highly efficient, but the overhead of checking 26 integers is non-trivial if done millions of times. 

### Why this is superior to alternatives:
*   **Sorting approach**: Sorting each window would be $O(L_2 \cdot L_1 \log L_1)$, which is significantly slower.
*   **Hashing approach**: While a rolling hash (like Rabin-Karp) could be used, it introduces the risk of hash collisions, requiring secondary checks, and is more complex to implement correctly than simple frequency counters.

---
